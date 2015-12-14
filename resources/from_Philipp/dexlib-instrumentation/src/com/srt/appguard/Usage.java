package com.srt.appguard;

import java.io.IOException;
import java.io.PrintStream;

import org.jf.dexlib.CodeItem;
import org.jf.dexlib.DexFile;
import org.jf.dexlib.FieldIdItem;
import org.jf.dexlib.MethodIdItem;
import org.jf.dexlib.Code.Opcode;

import com.srt.appguard.dex.InstructionFactory;
import com.srt.appguard.util.CodeUtil;

public class Usage {

	public static void main(String[] args) throws IOException {
		// read the target dex/apk
		final DexFile dex = new DexFile("somefile");
		
		// TODO do some meaningful search for a method here
		final CodeItem codeItem = dex.CodeItemsSection.getItems().get(0);
		
		// create instrumentation helpers
		final InstructionFactory isf = new InstructionFactory(dex);
		final CodeUtil code = new CodeUtil(codeItem);

		// perform the actual instrumentation
		// insert System.out.println("Hello World!") at the beginning of the method
		int instrIdx = 0;

		// load the "out"-field from the System.class into regOut
		final int regOut = code.newLocal();
		final FieldIdItem fieldOut = isf.lookupField(System.class, "out", PrintStream.class);
		code.insertInstruction(instrIdx++, isf.staticGet(regOut, fieldOut));

		// load the string "Hello World" into regMsg
		final int regMsg = code.newLocal();
		code.insertInstruction(instrIdx++, isf.constString(regMsg, "Hello World!"));

		// invoke println()
		final MethodIdItem methodPrintln = isf.lookupMethod(PrintStream.class, "println", Void.class, String.class);
		code.insertInstruction(instrIdx++, isf.invoke(Opcode.INVOKE_VIRTUAL, methodPrintln, regOut, regMsg));
		
		// don't forget: finalize the code item when you're done
		code.fix();
		
		// write the dex file
		// dex.writeTo(...)
	}
}
