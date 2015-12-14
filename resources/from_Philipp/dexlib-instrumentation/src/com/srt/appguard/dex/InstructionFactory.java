package com.srt.appguard.dex;

import java.util.LinkedList;
import java.util.List;

import org.jf.dexlib.DexFile;
import org.jf.dexlib.FieldIdItem;
import org.jf.dexlib.MethodIdItem;
import org.jf.dexlib.ProtoIdItem;
import org.jf.dexlib.StringIdItem;
import org.jf.dexlib.TypeIdItem;
import org.jf.dexlib.TypeListItem;
import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.Opcode;
import org.jf.dexlib.Code.Format.Instruction10t;
import org.jf.dexlib.Code.Format.Instruction10x;
import org.jf.dexlib.Code.Format.Instruction11n;
import org.jf.dexlib.Code.Format.Instruction11x;
import org.jf.dexlib.Code.Format.Instruction21c;
import org.jf.dexlib.Code.Format.Instruction21s;
import org.jf.dexlib.Code.Format.Instruction22c;
import org.jf.dexlib.Code.Format.Instruction23x;
import org.jf.dexlib.Code.Format.Instruction32x;
import org.jf.dexlib.Code.Format.Instruction35c;
import org.jf.dexlib.Code.Format.Instruction3rc;

import com.srt.appguard.util.TypeUtils;


public class InstructionFactory {

	protected DexFile dex;
	
	public InstructionFactory(final DexFile dex) {
		this.dex = dex;
	}
	
	/* Const Instructions */
	public Instruction constByte(final Integer register, final byte value) {
		return new Instruction11n(Opcode.CONST_4, register.byteValue(), value);
	}

	public Instruction constShort(final Integer register, final short value) {
		return new Instruction21s(Opcode.CONST_16, register.shortValue(), value);
	}

	public Instruction constString(final Integer register, final String value) {
		final StringIdItem string = StringIdItem.internStringIdItem(dex, value);
		return constString(register, string);
	}

	public Instruction constString(final Integer register, final StringIdItem string) {
		return new Instruction21c(Opcode.CONST_STRING, register.shortValue(), string);
	}

	public Instruction constClass(final Integer register, final Class<?> clazz) {
		return constClass(register, TypeUtils.getTypeDescriptor(clazz));
	}

	public Instruction constClass(final Integer register, final String typeDescriptor) {
		final TypeIdItem type = TypeIdItem.internTypeIdItem(dex, typeDescriptor);
		return new Instruction21c(Opcode.CONST_CLASS, register.shortValue(), type);
	}


	/* New Instructions */
	public Instruction newInstance(final Integer register, final Class<?> clazz) {
		return newInstance(register, TypeUtils.getTypeDescriptor(clazz));
	}

	public Instruction newInstance(final Integer register, final String typeDescriptor) {
		final TypeIdItem type = TypeIdItem.internTypeIdItem(dex, typeDescriptor);
		return new Instruction21c(Opcode.NEW_INSTANCE, register.shortValue(), type);
	}

	
	public Instruction newArray(final Integer register, final Integer sizeRegister, final Class<?> type) {
		return newArray(register, sizeRegister, TypeUtils.getTypeDescriptor(type));
	}

	public Instruction newArray(final Integer register, final Integer sizeRegister, final String typeDescriptor) {
		final TypeIdItem type = TypeIdItem.internTypeIdItem(dex, typeDescriptor);
		return new Instruction22c(Opcode.NEW_ARRAY, register.byteValue(), sizeRegister.byteValue(), type);
	}
	
	
	public Instruction filledNewArray(final Class<?> clazz, final Integer ... registers) {
		return filledNewArray(TypeUtils.getTypeDescriptor(clazz), registers);
	}

	public Instruction filledNewArray(final String typeDescriptor, final Integer ... registers) {
		final byte[] args = new byte[] { 0, 0, 0, 0, 0 };
		for (int i = 0; i < registers.length; ++i) {
			args[i] = registers[i].byteValue();
		}

		final TypeIdItem type = TypeIdItem.internTypeIdItem(dex, typeDescriptor);
		return new Instruction35c(Opcode.FILLED_NEW_ARRAY, registers.length, args[0], args[1], args[2], args[3], args[4], type);
	}

	public Instruction filledNewArrayRange(final Class<?> clazz, final Integer startRegister, final Integer registerCount) {
		return filledNewArrayRange(TypeUtils.getTypeDescriptor(clazz), startRegister, registerCount);
	}

	public Instruction filledNewArrayRange(final String typeDescriptor, final Integer startRegister, final Integer registerCount) {
		final TypeIdItem type = TypeIdItem.internTypeIdItem(dex, typeDescriptor);
		return new Instruction3rc(Opcode.FILLED_NEW_ARRAY_RANGE, registerCount.shortValue(), startRegister, type);
	}

	
	/* Array Instructions */
	public Instruction arrayPutObject(final Integer arrayRegister, final Integer indexRegister, final Integer valueRegister) {
		return new Instruction23x(Opcode.APUT_OBJECT, valueRegister.shortValue(), arrayRegister.shortValue(), indexRegister.shortValue());
	}
	

	/* Static Instructions */
	// TODO cleanup
	public Instruction staticGet(final Integer register, final FieldIdItem field) {
		final Opcode opcode;
		final TypeIdItem type = field.getFieldType();

		// determine the correct opcode from the field type
		if (TypeUtils.isWide(type)) {
			opcode = Opcode.SGET_WIDE;
		} else if (TypeUtils.isPrimitive(type)) {
			final char descriptor = type.getTypeDescriptor().charAt(0);
			switch (descriptor) {
				case 'Z':
					opcode = Opcode.SGET_BOOLEAN;
					break;
				case 'B':
					opcode = Opcode.SGET_BYTE;
					break;
				case 'C':
					opcode = Opcode.SGET_CHAR;
					break;
				case 'S':
					opcode = Opcode.SGET_SHORT;
					break;
				default:
					opcode = Opcode.SGET;
			}
		} else {
			opcode = Opcode.SGET_OBJECT;
		}

		return new Instruction21c(opcode, register.shortValue(), field);
	}

	
	/* Instance Instructions */
	// TODO cleanup
	public Instruction instanceGet(final Integer targetRegister, final Integer instanceRegister, final FieldIdItem field) {
		final Opcode opcode;
		final TypeIdItem type = field.getFieldType();

		// determine the correct opcode from the field type
		if (TypeUtils.isWide(type)) {
			opcode = Opcode.IGET_WIDE;
		} else if (TypeUtils.isPrimitive(type)) {
			final char descriptor = type.getTypeDescriptor().charAt(0);
			switch (descriptor) {
				case 'Z':
					opcode = Opcode.IGET_BOOLEAN;
					break;
				case 'B':
					opcode = Opcode.IGET_BYTE;
					break;
				case 'C':
					opcode = Opcode.IGET_CHAR;
					break;
				case 'S':
					opcode = Opcode.IGET_SHORT;
					break;
				default:
					opcode = Opcode.IGET;
			}
		} else {
			opcode = Opcode.IGET_OBJECT;
		}

		return new Instruction22c(opcode, targetRegister.byteValue(), instanceRegister.byteValue(), field);
	}


	/* Invoke Instructions */
	public Instruction invoke(final Opcode opcode, final MethodIdItem method, final Integer ... registers) {
		final byte[] args = new byte[] { 0, 0, 0, 0, 0 };
		for (int i = 0; i < registers.length; ++i) {
			args[i] = registers[i].byteValue();
		}

		return new Instruction35c(opcode, registers.length, args[0], args[1], args[2], args[3], args[4], method);
	}

	public Instruction invokeRange(final Opcode opcode, final MethodIdItem method, final Integer startRegister, final Integer registerCount) {
		return new Instruction3rc(opcode, registerCount.shortValue(), startRegister, method);
	}


	/* Move Instructions */
	public Instruction move(final Integer registerDest, final Integer registerSrc) {
		return new Instruction32x(Opcode.MOVE_16, registerDest, registerSrc);
	}

	public Instruction moveWide(final Integer registerDest, final Integer registerSrc) {
		return new Instruction32x(Opcode.MOVE_WIDE_16, registerDest, registerSrc);
	}
	
	public Instruction moveObject(final Integer registerDest, final Integer registerSrc) {
		return new Instruction32x(Opcode.MOVE_OBJECT_16, registerDest, registerSrc);
	}

	public Instruction moveResult(final Integer register) {
		return new Instruction11x(Opcode.MOVE_RESULT, register.shortValue());
	}

	public Instruction moveResultWide(final Integer register) {
		return new Instruction11x(Opcode.MOVE_RESULT_WIDE, register.shortValue());
	}

	public Instruction moveResultObject(final Integer register) {
		return new Instruction11x(Opcode.MOVE_RESULT_OBJECT, register.shortValue());
	}

	public Instruction moveException(final Integer register) {
		return new Instruction11x(Opcode.MOVE_EXCEPTION, register.shortValue());
	}

	
	/* Control-flow Instructions */
	public Instruction gotoBranch(final Integer branchOffset) {
		return new Instruction10t(Opcode.GOTO, branchOffset);
	}
	

	/* Return instructions */
	public Instruction returnVoid() {
		return new Instruction10x(Opcode.RETURN_VOID);
	}

	public Instruction returnObject(final Integer register) {
		return new Instruction11x(Opcode.RETURN_OBJECT, register.shortValue());
	}


	/* Misc Instructions */
	public Instruction checkCast(final Integer register, final Class<?> clazz) {
		return checkCast(register, TypeUtils.getTypeDescriptor(clazz));
	}

	public Instruction checkCast(final Integer register, final String typeDescriptor) {
		final TypeIdItem type = TypeIdItem.internTypeIdItem(dex, typeDescriptor);
		return new Instruction21c(Opcode.CHECK_CAST, register.shortValue(), type);
	}
	

	public Instruction nop() {
		return new Instruction10x(Opcode.NOP);
	}


	/* Lookup Helper */
	// TODO cleanup: remove string variants?
	public FieldIdItem lookupField(final Class<?> clazz, final String name, final Class<?> type) {
		return lookupField(TypeUtils.getTypeDescriptor(clazz), name, TypeUtils.getTypeDescriptor(type));
	}

	public FieldIdItem lookupField(final String classDescriptor, final String name, final String typeDescriptor) {
		final TypeIdItem classType = TypeIdItem.internTypeIdItem(dex, classDescriptor);
		final TypeIdItem fieldType = TypeIdItem.internTypeIdItem(dex, typeDescriptor);
		final StringIdItem fieldName = StringIdItem.internStringIdItem(dex, name);

		return FieldIdItem.internFieldIdItem(dex, classType, fieldType, fieldName);
	}
	
	public MethodIdItem lookupMethod(final Class<?> clazz, final String name, final Class<?> returnType, final Class<?> ... params) {
		TypeListItem typeList = null;
		if (params.length > 0) {
			final List<TypeIdItem> paramTypes = new LinkedList<TypeIdItem>();
			for (final Class<?> type : params) {
				paramTypes.add(TypeIdItem.internTypeIdItem(dex, TypeUtils.getTypeDescriptor(type)));
			}

			typeList = TypeListItem.internTypeListItem(dex, paramTypes);
		}
		final TypeIdItem retType = TypeIdItem.internTypeIdItem(dex, TypeUtils.getTypeDescriptor(returnType));
		final ProtoIdItem prototype = ProtoIdItem.internProtoIdItem(dex, retType, typeList);

		return lookupMethod(clazz, name, prototype);
	}

	public MethodIdItem lookupMethod(final Class<?> clazz, final String name, final ProtoIdItem prototype) {
		final TypeIdItem classType = TypeIdItem.internTypeIdItem(dex, TypeUtils.getTypeDescriptor(clazz));
		final StringIdItem methodName = StringIdItem.internStringIdItem(dex, name);

		return MethodIdItem.internMethodIdItem(dex, classType, prototype, methodName);
	}

	public TypeIdItem lookupType(final Class<?> clazz) {
		return TypeIdItem.internTypeIdItem(dex, TypeUtils.getTypeDescriptor(clazz));
	}
}
