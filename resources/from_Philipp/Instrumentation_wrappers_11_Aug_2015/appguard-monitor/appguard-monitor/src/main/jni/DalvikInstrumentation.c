#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>

#include "dalvik/dalvik.h"
#include "utils/log.h"
#include "dex.h"

/* AutoBoxing */
static const char types[] = {
	'B', // byte
	'C', // char
	'D', // double
	'F', // float
	'I', // integer
	'J', // long
	'S', // short
	'Z'  // boolean
};

static const char* classes[] = {
	"Ljava/lang/Byte;",
	"Ljava/lang/Character;",
	"Ljava/lang/Double;",
	"Ljava/lang/Float;",
	"Ljava/lang/Integer;",
	"Ljava/lang/Long;",
	"Ljava/lang/Short;",
	"Ljava/lang/Boolean;"
};

static const char* methods[] = {
	"byteValue",
	"charValue",
	"doubleValue",
	"floatValue",
	"intValue",
	"longValue",
	"shortValue",
	"booleanValue"
};

static u4 boxingMethods[] = {
	0, 0, 0, 0, 0, 0, 0, 0
};

static u4 unboxingMethods[] = {
	0, 0, 0, 0, 0, 0, 0, 0
};

bool isVoidType(const char descriptor) {
	return descriptor == 'V';
}

bool isObjectType(const char descriptor) {
	return descriptor == 'L';
}

bool isWideType(const char descriptor) {
	return descriptor == 'D' || descriptor == 'J';
}

u4 getBoxTypeIdx(const char descriptor) {
	for (int i = 0; i < 8; i++) {
		if (types[i] == descriptor) {
			return i;
		}
	}

	LOGW("BoxTypeIdx not found for %c", descriptor);
	return -1;
}

u4 findBoxMethodIdx(DexFile* pDexFile, u4 boxTypeIdx, const char type) {
	const char* class = classes[boxTypeIdx];

	u4 classIdx = dexFindTypeIdx(pDexFile, class);
	const char shorty[] = {
		'L',
		type,
		0x0
	};

	return dexFindMethodIdx(pDexFile, classIdx, "valueOf", shorty);
}

u4 findUnboxMethodIdx(DexFile* pDexFile, u4 boxTypeIdx, const char type) {
	const char* class = classes[boxTypeIdx];
	const char* method = methods[boxTypeIdx];

	u4 classIdx = dexFindTypeIdx(pDexFile, class);
	const char shorty[] = {
		type,
		0x0
	};

	return dexFindMethodIdx(pDexFile, classIdx, method, shorty);
}

u4 getBoxingMethodIdx(DexFile* pDexFile, const char srcType, const char dstType) {
	// check if we are boxing or unboxing
	if (isObjectType(srcType)) {
		// unboxing:
		// check if we have the method in the cache
		u4 boxTypeIdx = getBoxTypeIdx(dstType);
		u4 unboxingMethod = unboxingMethods[boxTypeIdx];
		//if (unboxingMethod != 0) {
		//	return unboxingMethod;
		//}

		// cache miss, find the method and cache it
		unboxingMethod = findUnboxMethodIdx(pDexFile, boxTypeIdx, dstType);
		unboxingMethods[boxTypeIdx] = unboxingMethod;

		return unboxingMethod;
	} else {
		// boxing:
		// check if we have the method in the cache
		u4 boxTypeIdx = getBoxTypeIdx(srcType);
		u4 boxingMethod = boxingMethods[boxTypeIdx];
		//if (boxingMethod != 0) {
		//	return boxingMethod;
		//}

		// cache miss, find the method and cache it
		boxingMethod = findBoxMethodIdx(pDexFile, boxTypeIdx, srcType);
		boxingMethods[boxTypeIdx] = boxingMethod;

		return boxingMethod;
	}
}


/* Main Instrumentation */

// taken from: dalvik/libdex/SysUtil.c
/*
 * Change the access rights on one or more pages to read-only or read-write.
 *
 * Returns 0 on success.
 */
int sysChangeMapAccess(void* addr, size_t length, int wantReadWrite)
{
	/*
	 * Align "addr" to a page boundary and adjust "length" appropriately.
	 * (The address must be page-aligned, the length doesn't need to be,
	 * but we do need to ensure we cover the same range.)
	 */
	u1* alignAddr = (u1*) ((int) addr & ~(SYSTEM_PAGE_SIZE-1));
	size_t alignLength = length + ((u1*) addr - alignAddr);

	//LOGI("%p/%zd --> %p/%zd\n", addr, length, alignAddr, alignLength);
	int prot = wantReadWrite ? (PROT_READ|PROT_WRITE) : (PROT_READ);
	if (mprotect(alignAddr, alignLength, prot) != 0) {
		int err = errno;
		LOGD("mprotect (%p,%zd,%d) failed: %s\n", alignAddr, alignLength, prot, strerror(errno));
		return (errno != 0) ? errno : -1;
	}

	/* for "fake" mapping, no need to do anything */
	return 0;
}


// cache the methodIdx of the Method.invoke() method
static u4 methodIdxInvoke = 0;

int generateTryCatchBlock(DexFile* pDexFile, u1* pBuffer, u4 tryLength, u4 handlerAddress) {
	int offset = 0;

	// TryItem
	// startCodeAddress (uint) (start of the method -> 0 ... or maybe 3?)
	pBuffer[offset++] = 0x03;
	pBuffer[offset++] = 0x0;
	pBuffer[offset++] = 0x0;
	pBuffer[offset++] = 0x0;

	// tryLength (ushort)
	pBuffer[offset++] = tryLength & 0x00ff;
	pBuffer[offset++] = tryLength & 0xff00;

	// handlerOffset (ushort) (we only have one -> 1)
	pBuffer[offset++] = 0x01;
	pBuffer[offset++] = 0x0;

	// EncodedCatchHandler count (unsignedLeb128) (just one -> 1)
	dexWriteUnsignedLeb128(pBuffer, &offset, 0x01);

	// EncodedCatchHandler
	// handlerCount (signedLeb128) (just one -> 1)
	dexWriteSignedLeb128(pBuffer, &offset, 0x01);

	// EncodedTypeAddrPair
	// exceptionType (unsignedLeb128) (typeIdx of InvocationTargetException)
	int typeIdxException = dexFindTypeIdx(pDexFile, "Ljava/lang/reflect/InvocationTargetException;");
	if (typeIdxException < 0) {
		return -1;
	}
	dexWriteUnsignedLeb128(pBuffer, &offset, typeIdxException);

	// handlerAddress (unsignedLeb128) (insns index of the catch handler)
	dexWriteUnsignedLeb128(pBuffer, &offset, handlerAddress);

	return offset;
}


DexCode* createTrampolineMethod(Method* pMethod, uintptr_t guardsPtr, u4 guardsIdx) {
	DexFile* pDexFile = pMethod->clazz->pDvmDex->pDexFile;

	// Named registers for readability
	u4 v0 = 0;
	u4 v1 = 1;
	u4 v2 = 2;

	// We use the original number of method arguments
	u2 localsSize = 3;
	u2 outsSize = 3;
	u2 triesSize = 1;
	u2 insSize = pMethod->insSize;
	u2 registersSize = localsSize + insSize;

	// Points to the first argument register
	u4 vArgs = localsSize;

	// Generate the instructions of the trampoline method
	// TODO insns buffer size
	u2 insns[128];
	u4 offset = 0;

	// << START OF BYTECODE GENERATION >>

	// Prepare the arguments for the call to Method.invoke().

	// v0: Size of the argument array for guard method.
	// This includes the _this argument for virtual methods.
	// We determine the number of arguments by taking the strlen of the shorty,
	// which includes the return type and is thus one character longer than the
	// number of arguments.
	u4 numArgs = strlen(pMethod->shorty);
	if (dexIsStaticMethod(pMethod)) {
		numArgs--;
	}
	dexInstrConst(insns, &offset, v0, numArgs);

	// v1: The argument array (Object[]) passed to Method.invoke()
	int typeIdxObject = dexFindTypeIdx(pDexFile, "[Ljava/lang/Object;");
	if (typeIdxObject < 0) {
		return NULL;
	}
	dexInstrNewArray(insns, &offset, v1, v0, typeIdxObject);

	// Fill the argument array. This is the usual nasty autoboxing stuff.
	// We use v0 as the index register for the aput instructions.
	// For virtual methods, the first argument is the _this object.
	int argIdx = 0;
	if (!dexIsStaticMethod(pMethod)) {
		dexInstrConst(insns, &offset, v0, argIdx);
		dexInstrAputObject(insns, &offset, vArgs, v1, v0);

		// increment the vArgs pointer to point to the next argument register
		vArgs++;

		// fix numArgs to reflect the number of arguments of the original method
		numArgs--;

		// increment argIdx to pointer to the next array element
		argIdx++;
	}

	// Parse the shorty and add conversion instructions if needed.
	for (int i = 0; i < numArgs; i++) {
		const char type = pMethod->shorty[i + 1];

		// check if we need to box
		if (!isObjectType(type)) {
			// invoke the boxing method and move the result into the original register vArgs
			int methodIdxBoxing = getBoxingMethodIdx(pDexFile, type, 'L');
			if (methodIdxBoxing < 0) {
				return NULL;
			}

			if (isWideType(type)) {
				dexInstrInvokeStatic(insns, &offset, methodIdxBoxing, 2, vArgs, vArgs+1, 0, 0, 0);
				dexInstrMoveResultObject(insns, &offset, vArgs);
			} else {
				dexInstrInvokeStatic(insns, &offset, methodIdxBoxing, 1, vArgs, 0, 0, 0, 0);
				dexInstrMoveResultObject(insns, &offset, vArgs);
			}
		}

		// we have an object type now, put it into the array
		dexInstrConst(insns, &offset, v0, argIdx);
		dexInstrAputObject(insns, &offset, vArgs, v1, v0);

		// increment vArgs & argIdx
		vArgs += isWideType(type) ? 2 : 1;
		argIdx++;
	}


	// v0: The hard-coded pointer to the guards array (evil!)
	// let's pray Google never implements a copying GC...
	dexInstrConst(insns, &offset, v0, guardsPtr);

	// v2: The index into the guards array
	dexInstrConst(insns, &offset, v2, guardsIdx);

	// Get the guard method object from the guards array and store it in v0
	dexInstrAgetObject(insns, &offset, v0, v0, v2);

	// v2: null
	dexInstrConst(insns, &offset, v2, 0x0);

	// Find the methodIdx of Method.invoke()
	//if (methodIdxInvoke == 0) {
		int typeIdxMethod = dexFindTypeIdx(pDexFile, "Ljava/lang/reflect/Method;");
		methodIdxInvoke = dexFindMethodIdx(pDexFile, typeIdxMethod, "invoke", "LLL");
		if (methodIdxInvoke < 0) {
			return NULL;
		}
	//}

	// Invoke Method.invoke()
	dexInstrInvokeVirtual(insns, &offset, methodIdxInvoke, 3, v0, v2, v1, 0, 0);

	// If the method returns void, we are done
	const char returnType = pMethod->shorty[0];
	if (isVoidType(returnType)) {
		// method returns void
		dexInstrReturnVoid(insns, &offset);
	} else {
		// Move result into v0
		dexInstrMoveResultObject(insns, &offset, v0);

		// Convert result to correct return type and return it
		if (isObjectType(returnType)) {
			// lookup the type descriptor for the return type
			const char* descriptor = dexGetReturnTypeDescriptor(pDexFile, pMethod);

			// method returns an object, no conversion, only casting
			u4 typeIdxReturnType = dexFindTypeIdx(pDexFile, descriptor);
			if (typeIdxReturnType < 0) {
				return NULL;
			}
			dexInstrCheckCast(insns, &offset, v0, typeIdxReturnType);

			// return result from v0
			dexInstrReturnObject(insns, &offset, v0);
		} else {
			// method returns a primitive type, unbox the result
			// invoke the boxing method, move the result into v0 and return it
			u4 methodIdxBoxing = getBoxingMethodIdx(pDexFile, 'L', returnType);
			if (methodIdxBoxing < 0) {
				return NULL;
			}
			dexInstrInvokeVirtual(insns, &offset, methodIdxBoxing, 1, v0, 0, 0, 0, 0);

			if (isWideType(returnType)) {
				dexInstrMoveResultWide(insns, &offset, v0);
				dexInstrReturnWide(insns, &offset, v0);
			} else {
				dexInstrMoveResult(insns, &offset, v0);
				dexInstrReturn(insns, &offset, v0);
			}
		}
	}

	// after the return, we add the catch handler that unwraps any InvocationTargetExceptions
	// determine the size of the try/catch block here (all insns except the catch handler)
	// this is also the start address of the catch handler
	u4 tryLength = offset;

	// first, store the just-caught exception in v0
	dexInstrMoveException(insns, &offset, v0);

	// get the wrapped exception from the InvocationTargetException object by calling .getCause/.getTargetException()
	// TODO improve
	int typeIdxException = dexFindTypeIdx(pDexFile, "Ljava/lang/reflect/InvocationTargetException;");
	int methodIdxGetCause = dexFindMethodIdx(pDexFile, typeIdxException, "getCause", "L");
	if (methodIdxGetCause < 0) {
		methodIdxGetCause = dexFindMethodIdx(pDexFile, typeIdxException, "getTargetException", "L");
		if (methodIdxGetCause < 0) {
			typeIdxException = dexFindTypeIdx(pDexFile, "Ljava/lang/Exception;");
			methodIdxGetCause = dexFindMethodIdx(pDexFile, typeIdxException, "getCause", "L");
			if (methodIdxGetCause < 0) {
				return NULL;
			}
		}
	}
	dexInstrInvokeVirtual(insns, &offset, methodIdxGetCause, 1, v0, 0, 0, 0, 0);

	// store the result in v0
	dexInstrMoveResultObject(insns, &offset, v0);

	// throw the wrapped exception
	dexInstrThrow(insns, &offset, v0);

	// << END OF BYTECODE GENERATION >>


	// we're done generating instructions, we know their size now
	u4 insnsSize = offset;

	// add padding if necessary
	if (offset % 2 != 0) {
		insns[offset++] = 0x0;
	}
	u4 insnsBufferSize = offset;

	// generate the try catch block for unwrapping InvocationTargetExceptions
	// TODO buffer size
	u1 tryBuffer[64];
	int tryBufferSize = generateTryCatchBlock(pDexFile, tryBuffer, tryLength, tryLength);
	if (tryBufferSize < 0) {
		return NULL;
	}

	// create a new DexCode structure for our trampoline method
	u4 dexCodeSize = sizeof(DexCode) + insnsBufferSize * sizeof(u2) + tryBufferSize * sizeof(u1);
	DexCode* pNewDexCode = (DexCode*) malloc(dexCodeSize);

	// initialize the DexCode structure
	pNewDexCode->registersSize = registersSize;
	pNewDexCode->insSize = insSize;
	pNewDexCode->outsSize = outsSize;
	pNewDexCode->triesSize = triesSize;
	pNewDexCode->debugInfoOff = 0;
	pNewDexCode->insnsSize = insnsSize;

	// copy instructions
	for (int i = 0; i < insnsBufferSize; i++) {
		*(pNewDexCode->insns + i) = insns[i];
	}

	// copy try/catch block
	// TODO cleanup
	u2* tryBlockStart = pNewDexCode->insns + insnsBufferSize;
	u1* tryBlock = (u1*) tryBlockStart;

	for (int i = 0; i < tryBufferSize; i++) {
		*(tryBlock + i) = tryBuffer[i];
	}

	//LOGD("Created trampoline method of size %d at %08x", dexCodeSize, pNewDexCode);

	return pNewDexCode;
}

jmethodID replaceMethod(jmethodID methodId, jlong guardsPtr, jint guardsIdx) {
	Method* pMethod = (Method*) methodId;

	// HACK: For Samsung devices the registerMap is not 0, but we want to set it to 0.
	// Unfortunately, on Android 4.3 there are three more bools before the registerMap,
	// such that the registerMap is actually stored at inProfile. So if the stored value
	// at registerMap/inProfile looks like a pointer (greater 0x01010100)) we set it to zero.
	if(((u4) pMethod->registerMap) > 0x01010100) {
		pMethod->registerMap = (u4*) 0;
	}
	if(pMethod->inProfile > 0x01010100) {
		pMethod->inProfile = 0;
	}

	// safeguard: no abstract methods supported!
	if ((pMethod->accessFlags & ACC_ABSTRACT) != 0) {
		LOGW("Skipping abstract method: %s %s", pMethod->name, pMethod->shorty);
		return NULL;
	}

	// create a backup copy of the original method
	Method* pOrgMethod = (Method*) malloc(sizeof(Method));
	memcpy(pOrgMethod, pMethod, sizeof(Method));
	pOrgMethod->accessFlags |= ACC_PRIVATE;

	// generate the trampoline method
	DexCode* pTrampoline = createTrampolineMethod(pMethod, (uintptr_t) guardsPtr, (u4) guardsIdx);
	if (pTrampoline == NULL) {
		return NULL;
	}

	// now, set the instruction pointer of the target method to the trampoline's instructions
	pMethod->insns = pTrampoline->insns;

	// we also need to fix the register sizes
	pMethod->registersSize = pTrampoline->registersSize;
	pMethod->outsSize = pTrampoline->outsSize;
	pMethod->insSize = pTrampoline->insSize;

	// remove (potential) native flag as the trampoline method is a bytecode method
	pMethod->accessFlags &= ~ACC_NATIVE;

	return (jmethodID) pOrgMethod;
}

jmethodID lookupMethod(JNIEnv* env, jstring className, jstring methodName, jstring signature, int isStatic) {
	const jbyte* classNameStr = (*env)->GetStringUTFChars(env, className, JNI_FALSE);
	const jbyte* methodNameStr = (*env)->GetStringUTFChars(env, methodName, JNI_FALSE);
	const jbyte* signatureStr = (*env)->GetStringUTFChars(env, signature, JNI_FALSE);

	jclass clazz = (*env)->FindClass(env, classNameStr);
	(*env)->ExceptionClear(env);
	jmethodID method = NULL;

	if (clazz != NULL) {
		if (isStatic) {
			method = (*env)->GetStaticMethodID(env, clazz, methodNameStr, signatureStr);
		} else {
			method = (*env)->GetMethodID(env, clazz, methodNameStr, signatureStr);
		}

		if (method == NULL) {
			(*env)->ExceptionClear(env);
			//LOGW("Method not found: %s.%s%s", classNameStr, methodNameStr, signatureStr);
		}
	} else {
		//LOGW("Class not found: %s", classNameStr);
	}

	(*env)->ReleaseStringUTFChars(env, className, classNameStr);
	(*env)->ReleaseStringUTFChars(env, methodName, methodNameStr);
	(*env)->ReleaseStringUTFChars(env, signature, signatureStr);

	return method;
}

void unboxArguments(JNIEnv *env, jint methodPtr, jobjectArray args, jvalue nativeArgs[], jint length) {
	Method* pMethod = (Method*) methodPtr;
	const char* shorty = pMethod->shorty;

	jclass clazz;
	jmethodID method;

	for (int i = 0; i < length; ++i) {
		jobject arg = (*env)->GetObjectArrayElement(env, args, i);

		// Unbox arg
		switch (shorty[i + 1]) {
		case 'B':
			// Byte
			clazz = (*env)->FindClass(env, "java/lang/Byte");
			method = (*env)->GetMethodID(env, clazz, "byteValue","()B");

			//nativeArgs[i] = (jvalue) (*env)->CallByteMethod(env, arg, byteValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallByteMethod(env, arg, method);
			break;
		case 'C':
			// Char
			clazz = (*env)->FindClass(env, "java/lang/Character");
			method = (*env)->GetMethodID(env, clazz, "charValue","()C");

			//nativeArgs[i] = (jvalue) (*env)->CallCharMethod(env, arg, charValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallCharMethod(env, arg, method);
			break;
		case 'D':
			// Double
			clazz = (*env)->FindClass(env, "java/lang/Double");
			method = (*env)->GetMethodID(env, clazz, "doubleValue","()D");

			//nativeArgs[i] = (jvalue) (*env)->CallDoubleMethod(env, arg, doubleValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallDoubleMethod(env, arg, method);
			break;
		case 'F':
			// Float
			clazz = (*env)->FindClass(env, "java/lang/Float");
			method = (*env)->GetMethodID(env, clazz, "floatValue","()F");

			//nativeArgs[i] = (jvalue) (*env)->CallFloatMethod(env, arg, floatValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallFloatMethod(env, arg, method);
			break;
		case 'I':
			// Int
			clazz = (*env)->FindClass(env, "java/lang/Integer");
			method = (*env)->GetMethodID(env, clazz, "intValue","()I");

			//nativeArgs[i] = (jvalue) (*env)->CallIntMethod(env, arg, intValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallIntMethod(env, arg, method);
			break;
		case 'J':
			// Long
			clazz = (*env)->FindClass(env, "java/lang/Long");
			method = (*env)->GetMethodID(env, clazz, "longValue","()J");

			//nativeArgs[i] = (jvalue) (*env)->CallLongMethod(env, arg, longValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallLongMethod(env, arg, method);
			break;
		case 'S':
			// Short
			clazz = (*env)->FindClass(env, "java/lang/Short");
			method = (*env)->GetMethodID(env, clazz, "shortValue","()S");

			//nativeArgs[i] = (jvalue) (*env)->CallShortMethod(env, arg, shortValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallShortMethod(env, arg, method);
			break;
		case 'Z':
			// Boolean
			clazz = (*env)->FindClass(env, "java/lang/Boolean");
			method = (*env)->GetMethodID(env, clazz, "booleanValue","()Z");

			//nativeArgs[i] = (jvalue) (*env)->CallBooleanMethod(env, arg, booleanValueMethod);
			nativeArgs[i] = (jvalue) (*env)->CallBooleanMethod(env, arg, method);
			break;
		default:
			// Object
			nativeArgs[i] = (jvalue) arg;
			break;
		}
	}
}

JNIEXPORT jint JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_replaceMethodNative(JNIEnv* env, jclass clazz,
		jstring className, jstring methodName, jstring prototype,
		jlong guardsPtr, jint guardsIdx) {

	jmethodID methodId = lookupMethod(env, className, methodName, prototype, 0);
	if (methodId == NULL) {
		return (jint) NULL;
	}

	jmethodID orgMethod = replaceMethod(methodId, guardsPtr, guardsIdx);
	return (jint) orgMethod;
}

JNIEXPORT jint JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_replaceStaticMethodNative(JNIEnv* env, jclass clazz,
		jstring className, jstring methodName, jstring prototype,
		jlong guardsPtr, jint guardsIdx) {

	jmethodID methodId = lookupMethod(env, className, methodName, prototype, 1);
	if (methodId == NULL) {
		return (jint) NULL;
	}

	jmethodID orgMethod = replaceMethod(methodId, guardsPtr, guardsIdx);
	return (jint) orgMethod;
}

/* Debug Utils */

JNIEXPORT void JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_printRegisterMap(JNIEnv* env, jclass clazz,
		jstring className, jstring methodName, jstring signature, jboolean isStatic) {

	const jbyte* classNameStr = (*env)->GetStringUTFChars(env, className, JNI_FALSE);
	const jbyte* methodNameStr = (*env)->GetStringUTFChars(env, methodName, JNI_FALSE);
	const jbyte* signatureStr = (*env)->GetStringUTFChars(env, signature, JNI_FALSE);

	jmethodID methodId = lookupMethod(env, className, methodName, signature, (int) isStatic);
	if (methodId == NULL) {
		LOGW("PrintRegisterMap: Method not found: %s->%s%s", classNameStr, methodNameStr, signatureStr);
		return;
	}

	Method* pMethod = (Method*) methodId;
	LOGD("PrintRegisterMap: %p for method %s->%s%s", pMethod->registerMap, classNameStr, methodNameStr, signatureStr);

	(*env)->ReleaseStringUTFChars(env, className, classNameStr);
	(*env)->ReleaseStringUTFChars(env, methodName, methodNameStr);
	(*env)->ReleaseStringUTFChars(env, signature, signatureStr);
}

JNIEXPORT jint JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_getInstructionPtr(JNIEnv* env, jclass clazz,
		jstring className, jstring methodName, jstring signature, jboolean isStatic) {

	jmethodID methodId = lookupMethod(env, className, methodName, signature, (int) isStatic);
	if (methodId == NULL) {
		return 0;
	}

	Method* pMethod = (Method*) methodId;
	return (jint) pMethod->insns;
}

JNIEXPORT jint JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_getInstructionSize(JNIEnv* env, jclass clazz,
		jstring className, jstring methodName, jstring signature, jboolean isStatic) {

	jmethodID methodId = lookupMethod(env, className, methodName, signature, (int) isStatic);
	if (methodId == NULL) {
		return 0;
	}

	Method* pMethod = (Method*) methodId;
	DexCode* pDexCode = (DexCode*) (((const u1*) pMethod->insns) - offsetof(DexCode, insns));
	return (jint) pDexCode->insnsSize;
}

JNIEXPORT jbyteArray JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_readMemory(JNIEnv* env, jclass clazz,
		jint address, jint length) {

	jbyte* addr = (jbyte*) address;
	int len = (int) length;

	jbyteArray result;
	result = (*env)->NewByteArray(env, len);
	if (result == NULL) {
		return NULL; /* out of memory */
	}

	int i;
	jbyte* content = malloc(len * sizeof(jbyte));
	for (i = 0; i < len; i++) {
		*(content + i) = *(addr + i);
	}

	(*env)->SetByteArrayRegion(env, result, 0, len, content);
	return result;
}

// Virtual

JNIEXPORT void JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_callReplacedVoidMethod
  (JNIEnv *env, jclass clazz, jint methodPtr, jobject _this, jobjectArray args) {
	jmethodID methodId = (jmethodID) methodPtr;
	jclass _class = (*env)->GetObjectClass(env, _this);

	jint length = (*env)->GetArrayLength(env, args);
	jvalue nativeArgs[32]; // TODO buffer size
	unboxArguments(env, methodPtr, args, nativeArgs, length);

	(*env)->CallNonvirtualVoidMethodA(env, _this, _class, methodId, nativeArgs);
}

JNIEXPORT jint JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_callReplacedIntMethod
  (JNIEnv* env, jclass clazz, jint methodPtr, jobject _this, jobjectArray args) {
	jmethodID methodId = (jmethodID) methodPtr;
	jclass _class = (*env)->GetObjectClass(env, _this);

	jint length = (*env)->GetArrayLength(env, args);
	jvalue nativeArgs[32]; // TODO buffer size
	unboxArguments(env, methodPtr, args, nativeArgs, length);

	return (*env)->CallNonvirtualIntMethodA(env, _this, _class, methodId, nativeArgs);
}

JNIEXPORT jboolean JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_callReplacedBooleanMethod
  (JNIEnv* env, jclass clazz, jint methodPtr, jobject _this, jobjectArray args) {
	jmethodID methodId = (jmethodID) methodPtr;
	jclass _class = (*env)->GetObjectClass(env, _this);

	jint length = (*env)->GetArrayLength(env, args);
	jvalue nativeArgs[32]; // TODO buffer size
	unboxArguments(env, methodPtr, args, nativeArgs, length);

	return (*env)->CallNonvirtualBooleanMethodA(env, _this, _class, methodId, nativeArgs);
}

JNIEXPORT jobject JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_callReplacedObjectMethod
  (JNIEnv* env, jclass clazz, jint methodPtr, jobject _this, jobjectArray args) {
	jmethodID methodId = (jmethodID) methodPtr;
	jclass _class = (*env)->GetObjectClass(env, _this);

	jint length = (*env)->GetArrayLength(env, args);
	jvalue nativeArgs[32]; // TODO buffer size
	unboxArguments(env, methodPtr, args, nativeArgs, length);

	return (*env)->CallNonvirtualObjectMethodA(env, _this, _class, methodId, nativeArgs);
}

// Static

JNIEXPORT void JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_callReplacedStaticVoidMethod
  (JNIEnv* env, jclass clazz, jint methodPtr, jclass _class, jobjectArray args) {
	jmethodID methodId = (jmethodID) methodPtr;

	jint length = (*env)->GetArrayLength(env, args);
	jvalue nativeArgs[32]; // TODO buffer size
	unboxArguments(env, methodPtr, args, nativeArgs, length);

	(*env)->CallStaticVoidMethodA(env, _class, methodId, nativeArgs);
}

JNIEXPORT jobject JNICALL Java_com_srt_appguard_monitor_instrumentation_DalvikInstrumentation_callReplacedStaticObjectMethod
  (JNIEnv* env, jclass clazz, jint methodPtr, jclass _class, jobjectArray args) {
	jmethodID methodId = (jmethodID) methodPtr;

	jint length = (*env)->GetArrayLength(env, args);
	jvalue nativeArgs[32]; // TODO buffer size
	unboxArguments(env, methodPtr, args, nativeArgs, length);

	return (*env)->CallStaticObjectMethodA(env, _class, methodId, nativeArgs);
}
