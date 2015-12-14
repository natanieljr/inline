package com.srt.appguard.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jf.dexlib.CodeItem;
import org.jf.dexlib.CodeItem.EncodedCatchHandler;
import org.jf.dexlib.CodeItem.EncodedTypeAddrPair;
import org.jf.dexlib.CodeItem.TryItem;
import org.jf.dexlib.DexFile;
import org.jf.dexlib.TypeIdItem;
import org.jf.dexlib.TypeListItem;
import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Util.AccessFlags;

import com.srt.appguard.dex.InstructionFactory;


public class CodeUtil {

	protected final DexFile dex;
	protected final CodeItem code;

	protected final boolean isStatic;

	protected int registerCount;
	protected int parameterCount;
	protected int newLocals = 0;
	
	protected Map<Integer, Integer> addresses = new HashMap<Integer, Integer>();
	
	public CodeUtil(final CodeItem code) {
		this.dex = code.getDexFile();
		this.code = code;

		this.registerCount = code.getRegisterCount();
		this.parameterCount = code.getParent().method.getPrototype().getParameterRegisterCount();

		// check if the method this code belongs to is static (static flag set)
		this.isStatic = (code.getParent().accessFlags & AccessFlags.STATIC.getValue()) != 0;
		if (!this.isStatic) {
			++parameterCount;
		}
		
		calculateAddresses();
	}

	protected void calculateAddresses() {
		addresses.clear();

		final Instruction[] instructions = code.getInstructions();
		int currentAddress = 0;
		for (int i = 0; i < instructions.length; ++i) {
			addresses.put(i, currentAddress);
			currentAddress += instructions[i].getSize(currentAddress);
		}
	}
	
	public int newLocal() {
		// We reserve the first new registers for the method parameters of
		// this code item. This way, we don't have to update all instructions
		// referencing the parameter registers as we move them up to fit the
		// new locals into the frame.
		// The new locals start after these reserved registers.
		return registerCount + newLocals++;
	}

	public void insertInstruction(final int instructionIndex, final Instruction instruction) {
		insertInstruction(instructionIndex, instruction, false);
	}

	public void insertInstruction(final int instructionIndex, final Instruction instruction, boolean keepLabel) {
		code.insertInstructionAtIndex(instructionIndex, instruction, keepLabel);

		// TODO optimize
		calculateAddresses();
	}

	public void replaceInstruction(final int instructionIndex, final Instruction instruction) {
		code.replaceInstructionAtIndex(instructionIndex, instruction);

		// TODO optimize
		calculateAddresses();
	}

	public void appendInstruction(final Instruction instruction) {
		code.appendInstruction(instruction);

		// TODO optimize
		calculateAddresses();
	}

	public void addTry(final int startIndex, final int tryLength, final EncodedCatchHandler handler) {
		final int startAddress = addresses.get(startIndex);
		final int endAddress = addresses.get(startIndex + tryLength);

		// bytecode doesn't support nested try/catch blocks
		// thus, we check if the new try block is being created within an existing try block and
		// split the surrounding try block if we find one
		final TryItem[] tries = code.getTries();
		if (tries != null) {
			for (int i = 0; i < tries.length; ++i) {
				final TryItem tri = tries[i];
				final int triStart = tri.getStartCodeAddress();
				final int triEnd = triStart + tri.getTryLength();
	
				if (triStart <= startAddress && triEnd >= endAddress) {
					// the new try block is completely contained within another try block
					// adjust the length of the surrounding try block to the start of the new try block
					// if the try block would be reduced to length 0, remove it and adjust index
					if (startAddress == triStart) {
						code.removeTry(tri);
						i--;
					} else {
						tri.setTryLength(startAddress - triStart);
					}

					// insert our try block, copying the outer catch handlers
					final int numHandlers = tri.encodedCatchHandler.handlers.length;
					final int numNewHandlers = handler.handlers.length;
					final int numTotalHandlers = numHandlers + numNewHandlers;
					final EncodedTypeAddrPair[] newHandlers = new EncodedTypeAddrPair[numTotalHandlers];
					for (int j = 0; j < numNewHandlers; ++j) {
						newHandlers[j] = handler.handlers[j];
					}
					for (int j = 0; j < numHandlers; ++j) {
						final EncodedTypeAddrPair h = tri.encodedCatchHandler.handlers[j];
						newHandlers[numNewHandlers + j] = new EncodedTypeAddrPair(h.exceptionType, h.getHandlerAddress());
					}
					
					final EncodedCatchHandler newHandler = new EncodedCatchHandler(
							newHandlers,
							tri.encodedCatchHandler.getCatchAllHandlerAddress()
					);
					code.addHandler(newHandler);

					final TryItem newTri = new TryItem(startAddress, endAddress - startAddress, newHandler);
					code.addTry(newTri, i + 1);

					// insert a new try block to cover the rest, if necessary
					if (triEnd > endAddress) {
						final TryItem restTri = new TryItem(endAddress, triEnd - endAddress, tri.encodedCatchHandler);
						code.addTry(restTri, i + 2);
					}

					// there can at most one surrounding try, so we're done
					return;
				} else if (triEnd < startAddress) {
					// check if we are at the correct position in the list, if so insert our try/catch block
					if (i == tries.length - 1 || tries[i + 1].getStartCodeAddress() > endAddress) {
						code.addHandler(handler);
						code.addTry(new TryItem(startAddress, endAddress - startAddress, handler), i + 1);
						return;
					}
				} else if (i == 0 && triStart > endAddress) {
					code.addHandler(handler);
					code.addTry(new TryItem(startAddress, endAddress - startAddress, handler), i);
					return;
				}
			}
		} else {
			// no try/catch blocks exist yet, insert ours
			code.addHandler(handler);
			code.addTry(new TryItem(startAddress, endAddress - startAddress, handler), 0);
		}
	}

	public EncodedCatchHandler createCatchHandler(final TypeIdItem exceptionType, final int handlerInstructionIndex) {
		// TODO add support to create more than one catch clause
		final EncodedTypeAddrPair[] handlers = new EncodedTypeAddrPair[] {
				new EncodedTypeAddrPair(exceptionType, addresses.get(handlerInstructionIndex))
		};

		return new EncodedCatchHandler(handlers, -1);
	}

	public int getInstructionCount() {
		return code.getInstructions().length;
	}
	
	public int getInstructionsLength() {
		return code.getInstructionsLength();
	}

	public Integer getAddress(final Integer index) {
		return addresses.get(index);
	}

	public void fix() {
		fixParameterRegisters();
		fixOutWords();

		code.fixInstructions(true, true);
	}
	
	protected void fixParameterRegisters() {
		if (newLocals == 0) {
			return;
		}

		if (parameterCount == 0) {
			updateRegisterCount(registerCount + newLocals);
			return;
		}

		final InstructionFactory isf = new InstructionFactory(dex);
		
		int index = 0;
		int paramOffset = registerCount - parameterCount;
		int newParamOffset = registerCount + newLocals;

		// move the instance parameter
		if (!isStatic) {
			insertInstruction(index++, isf.moveObject(paramOffset, newParamOffset));
			++paramOffset;
			++newParamOffset;
		}

		// move the method arguments into their reserved registers
		// take care of the type
		final TypeListItem params = code.getParent().method.getPrototype().getParameters();
		if (params != null) {
			final List<TypeIdItem> types = params.getTypes();
			final int paramCount = types.size();
			for (int i = 0; i < paramCount; ++i) {
				final TypeIdItem type = types.get(i);
				
				if (TypeUtils.isWide(type)) {
					insertInstruction(index++, isf.moveWide(paramOffset + i, newParamOffset + i));
					++paramOffset;
					++newParamOffset;
				} else if (TypeUtils.isPrimitive(type)) {
					insertInstruction(index++, isf.move(paramOffset + i, newParamOffset + i));
				} else {
					insertInstruction(index++, isf.moveObject(paramOffset + i, newParamOffset + i));
				}
			}
		}

		updateRegisterCount(registerCount + parameterCount + newLocals);
	}
	
	// TODO check if this is correct
	protected void fixOutWords() {
		int outWords = code.getOutWords();
		for (final Instruction instruction : code.getInstructions()) {
			if (instruction instanceof org.jf.dexlib.Code.InvokeInstruction) {
				final int regCount = ((org.jf.dexlib.Code.InvokeInstruction) instruction).getRegCount();
				if (regCount > outWords) {
					outWords = regCount;
				}
			}
		}

		code.setOutWords(outWords);
	}

	protected void updateRegisterCount(final int registerCount) {
		code.setRegisterCount(registerCount);
		this.registerCount = registerCount;
	}
}
