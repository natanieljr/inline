#include <jni.h>
#include <stdio.h>
#include <sys/types.h>
#include <string.h>

#include "dex.h"
#include "dalvik/dalvik.h"
#include "utils/log.h"

/* taken from DexFile.h >>>> */

/* return the const char* string data referred to by the given string_id */
const char* dexGetStringData(const DexFile* pDexFile,
        const DexStringId* pStringId) {
    const u1* ptr = pDexFile->baseAddr + pStringId->stringDataOff;

    // Skip the uleb128 length.
    while (*(ptr++) > 0x7f) /* empty */ ;

    return (const char*) ptr;
}

/* return the StringId with the specified index */
const DexStringId* dexGetStringId(const DexFile* pDexFile, u4 idx) {
    return &pDexFile->pStringIds[idx];
}

/* return the UTF-8 encoded string with the specified string_id index */
const char* dexStringById(const DexFile* pDexFile, u4 idx) {
    const DexStringId* pStringId = dexGetStringId(pDexFile, idx);
    return dexGetStringData(pDexFile, pStringId);
}

/* return the TypeId with the specified index */
const DexTypeId* dexGetTypeId(const DexFile* pDexFile, u4 idx) {
    return &pDexFile->pTypeIds[idx];
}

/*
 * Get the descriptor string associated with a given type index.
 * The caller should not free() the returned string.
 */
const char* dexStringByTypeIdx(const DexFile* pDexFile, u4 idx) {
    const DexTypeId* typeId = dexGetTypeId(pDexFile, idx);
    return dexStringById(pDexFile, typeId->descriptorIdx);
}

/* return the MethodId with the specified index */
const DexMethodId* dexGetMethodId(const DexFile* pDexFile, u4 idx) {
    return &pDexFile->pMethodIds[idx];
}

/* return the FieldId with the specified index */
const DexFieldId* dexGetFieldId(const DexFile* pDexFile, u4 idx) {
    return &pDexFile->pFieldIds[idx];
}

/* return the ProtoId with the specified index */
const DexProtoId* dexGetProtoId(const DexFile* pDexFile, u4 idx) {
    return &pDexFile->pProtoIds[idx];
}

/* <<<< taken from DexFile.h */



/* Item Lookup Utils */

int dexFindStringIdx(const DexFile* pDexFile, const char* pNeedle) {
	u4 hi = pDexFile->pHeader->stringIdsSize - 1;
	u4 lo = 0;
	u4 cur;

	while (hi >= lo) {
		int cmp;
		cur = (lo + hi) / 2;

		const DexStringId* pStringId = dexGetStringId(pDexFile, cur);
		const char* pString = dexGetStringData(pDexFile, pStringId);

		cmp = strcmp(pString, pNeedle);
		if (cmp < 0) {
			lo = cur + 1;
		} else if (cmp > 0) {
			hi = cur - 1;
		} else {
			break;
		}
	}

	if (hi < lo) {
		//LOGD("StringId not found in dexFile %08x: %s", pDexFile, pNeedle);
		return -1;
	}

	return cur;
}

int dexFindTypeIdx(const DexFile* pDexFile, const char* pDescriptor) {
	int descriptorIdx = dexFindStringIdx(pDexFile, pDescriptor);

	u4 typeIdsSize = pDexFile->pHeader->typeIdsSize;
	for (int i = 0; i < typeIdsSize; i++) {
		const DexTypeId* pTypeId = dexGetTypeId(pDexFile, i);
		if (pTypeId->descriptorIdx == descriptorIdx) {
			return i;
		}
	}

	//LOGD("TypeId not found in dexFile %08x: %s", pDexFile, pDescriptor);
	return -1;
}

// TODO compare type
int dexFindFieldIdx(const DexFile* pDexFile, int classTypeIdx, const char* pName) {
	int nameIdx = dexFindStringIdx(pDexFile, pName);

	u4 fieldIdsSize = pDexFile->pHeader->fieldIdsSize;
	for (int i = 0; i < fieldIdsSize; i++) {
		const DexFieldId* pFieldId = dexGetFieldId(pDexFile, i);
		if (pFieldId->classIdx == classTypeIdx &&
			pFieldId->nameIdx == nameIdx) {
			return i;
		}
	}

	//LOGD("FieldId not found in dexFile %08x: %s", pDexFile, pName);
	return -1;
}

int dexFindMethodIdx(const DexFile* pDexFile, int classTypeIdx, const char* pName, const char* pShorty) {
	int nameIdx = dexFindStringIdx(pDexFile, pName);
	int shortyIdx = dexFindStringIdx(pDexFile, pShorty);

	u4 methodIdsSize = pDexFile->pHeader->methodIdsSize;
	for (int i = 0; i < methodIdsSize; i++) {
		const DexMethodId* pMethodId = dexGetMethodId(pDexFile, i);
		if (pMethodId->classIdx == classTypeIdx &&
			pMethodId->nameIdx == nameIdx) {

			const DexProtoId* pProtoId = dexGetProtoId(pDexFile, pMethodId->protoIdx);
			if (pProtoId->shortyIdx == shortyIdx) {
				return i;
			}
		}
	}

	//LOGD("MethodId not found in dexFile %08x: %s", pDexFile, pName);
	return -1;
}


/* Instruction Factory */

/* Instruction Formats */
void dexFormat10x(u2* pInsns, u4* offset, u1 op) {
	u4 idx = *offset;
	pInsns[idx++] = op;
	*offset = idx;
}

void dexFormat11n(u2* pInsns, u4* offset, u1 op, u4 regA, u4 regB) {
	u4 idx = *offset;
	pInsns[idx++] = ((regB & 0x0000000f) << 12) | ((regA & 0x0000000f) << 8) | op;
	*offset = idx;
}

void dexFormat11x(u2* pInsns, u4* offset, u1 op, u4 regA) {
	u4 idx = *offset;
	pInsns[idx++] = ((regA & 0x000000ff) << 8) | op;
	*offset = idx;
}

void dexFormat21(u2* pInsns, u4* offset, u1 op, u4 regA, u4 regB) {
	u4 idx = *offset;
	pInsns[idx++] = ((regA & 0x000000ff) << 8) | op;
	pInsns[idx++] = regB & 0x0000ffff;
	*offset = idx;
}

void dexFormat31(u2* pInsns, u4* offset, u1 op, u4 regA, u4 regB) {
	u4 idx = *offset;
	pInsns[idx++] = ((regA & 0x000000ff) << 8) | op;
	pInsns[idx++] = regB & 0x0000ffff;
	pInsns[idx++] = (regB >> 16) & 0x0000ffff;
	*offset = idx;
}

void dexFormat3rc(u2* pInsns, u4* offset, u1 op, u4 regA, u4 regB, u4 regC) {
	u4 idx = *offset;
	pInsns[idx++] = (regA << 8) | op;
	pInsns[idx++] = regB & 0x0000ffff;
	pInsns[idx++] = regC & 0x0000ffff;
	*offset = idx;
}

void dexFormat35c(u2* pInsns, u4* offset, u1 op, u4 regA, u4 regB, u4 regC, u4 regD, u4 regE, u4 regF, u4 regG) {
	// A|G|op BBBB F|E|D|C
	u4 idx = *offset;
	pInsns[idx++] = ((regA & 0x0000000f) << 12) | ((regG & 0x0000000f) << 8) | op;
	pInsns[idx++] = regB & 0x0000ffff;
	pInsns[idx++] = ((regF & 0x0000000f) << 12) | ((regE & 0x0000000f) << 8) |
					((regD & 0x0000000f) << 4)  | (regC & 0x0000000f);
	*offset = idx;
}

void dexFormat22(u2* pInsns, u4* offset, u1 op, u4 regA, u4 regB, u4 regC) {
	u4 idx = *offset;
	pInsns[idx++] = ((regB & 0x0000000f) << 12) | ((regA & 0x0000000f) << 8) | op;
	pInsns[idx++] = regC & 0x0000ffff;
	*offset = idx;
}

void dexFormat23x(u2* pInsns, u4* offset, u1 op, u4 regA, u4 regB, u4 regC) {
	u4 idx = *offset;
	pInsns[idx++] = ((regA & 0x000000ff) << 8) | op;
	pInsns[idx++] = ((regC & 0x000000ff) << 8) | (regB & 0x000000ff);
	*offset = idx;
}


/* Instructions */

/* Opcode:	const
 * 12 11n	const/4 vA, #+B
 * 13 21s	const/16 vAA, #+BBBB
 * 14 31i	const vAA, #+BBBBBBBB
 */
void dexInstrConst(u2* pInsns, u4* offset, u4 regDst, u4 value) {
	if (regDst < NIBBLE_MAX_VALUE && value < NIBBLE_MAX_VALUE / 2) {
		// const/4
		dexFormat11n(pInsns, offset, 0x12, regDst, value);
	} else if (value < SHORT_MAX_VALUE / 2) {
		// const/16
		dexFormat21(pInsns, offset, 0x13, regDst, value);
	} else {
		// const
		dexFormat31(pInsns, offset, 0x14, regDst, value);
	}
}

/* Opcode:	const-string
 * 1a 21c	const-string vAA, string@BBBB
 * 1b 31c	const-string/jumbo vAA, string@BBBBBBBB
 */
void dexInstrConstString(u2* pInsns, u4* offset, u4 regDst, u4 stringIdx) {
	if (stringIdx < SHORT_MAX_VALUE) {
		// const-string
		dexFormat21(pInsns, offset, 0x1a, regDst, stringIdx);
	} else {
		// const-string/jumbo
		dexFormat31(pInsns, offset, 0x1b, regDst, stringIdx);
	}
}

/* Opcode:	return-void
 * 0e 10x	return-void
 */
void dexInstrReturnVoid(u2* pInsns, u4* offset) {
	dexFormat10x(pInsns, offset, 0x0e);
}

/* Opcode:	return
 * 0f 11x	return vAA
 */
void dexInstrReturn(u2* pInsns, u4* offset, u4 regDst) {
	dexFormat11x(pInsns, offset, 0x0f, regDst);
}

/* Opcode:	return-wide
 * 10 11x	return-wide vAA
 */
void dexInstrReturnWide(u2* pInsns, u4* offset, u4 regDst) {
	dexFormat11x(pInsns, offset, 0x10, regDst);
}

/* Opcode:	return-object
 * 11 11x	return-object vAA
 */
void dexInstrReturnObject(u2* pInsns, u4* offset, u4 regDst) {
	dexFormat11x(pInsns, offset, 0x11, regDst);
}

/* Opcode:	move-result
 * 0a 11x	move-result vAA
 */
void dexInstrMoveResult(u2* pInsns, u4* offset, u4 regDst) {
	dexFormat11x(pInsns, offset, 0x0a, regDst);
}

/* Opcode:	move-result-wide
 * 0b 11x	move-result-wide vAA
 */
void dexInstrMoveResultWide(u2* pInsns, u4* offset, u4 regDst) {
	dexFormat11x(pInsns, offset, 0x0b, regDst);
}

/* Opcode:	move-result-object
 * 0c 11x	move-result-object vAA
 */
void dexInstrMoveResultObject(u2* pInsns, u4* offset, u4 regDst) {
	dexFormat11x(pInsns, offset, 0x0c, regDst);
}

/* Opcode:	move-exception
 * 0d 11x	move-exception vAA
 */
void dexInstrMoveException(u2* pInsns, u4* offset, u4 regDst) {
	dexFormat11x(pInsns, offset, 0x0d, regDst);
}

/* Opcode:	new-array
 * 23 22c	new-array vA, vB, type@CCCC
 */
void dexInstrNewArray(u2* pInsns, u4* offset, u4 regDst, u4 regSize, u4 typeIdx) {
	dexFormat22(pInsns, offset, 0x23, regDst, regSize, typeIdx);
}

/* Opcode:	aget-object
 * 46 23x	aget-object vAA, vBB, vCC
 */
void dexInstrAgetObject(u2* pInsns, u4* offset, u4 regDst, u4 regArray, u4 regIndex) {
	dexFormat23x(pInsns, offset, 0x46, regDst, regArray, regIndex);
}

/* Opcode:	aput-object
 * 4d 23x	aput-object vAA, vBB, vCC
 */
void dexInstrAputObject(u2* pInsns, u4* offset, u4 regValue, u4 regArray, u4 regIndex) {
	dexFormat23x(pInsns, offset, 0x4d, regValue, regArray, regIndex);
}

/* Opcode:	sget-object
 * 62 21c	sget-object vAA, field@BBBB
 */
void dexInstrSgetObject(u2* pInsns, u4* offset, u4 regDst, u4 fieldIdx) {
	dexFormat21(pInsns, offset, 0x62, regDst, fieldIdx);
}

/* Opcode:	check-cast
 * 1f 21c	check-cast vAA, type@BBBB
 */
void dexInstrCheckCast(u2* pInsns, u4* offset, u4 regObject, u4 typeIdx) {
	dexFormat21(pInsns, offset, 0x1f, regObject, typeIdx);
}

/* Opcode:	throw
 * 27 11x	throw vAA
 */
void dexInstrThrow(u2* pInsns, u4* offset, u4 regException) {
	dexFormat11x(pInsns, offset, 0x27, regException);
}

/* Opcode:	invoke-virtual
 * 6e 35c	invoke-virtual {vC, vD, vE, vF, vG}, meth@BBBB
 */
void dexInstrInvokeVirtual(u2* pInsns, u4* offset, u4 methodIdx, u4 regCount, u4 regC, u4 regD, u4 regE, u4 regF, u4 regG) {
	dexFormat35c(pInsns, offset, 0x6e, regCount, methodIdx, regC, regD, regE, regF, regG);
}

/* Opcode:	invoke-static
 * 71 35c	invoke-static {vC, vD, vE, vF, vG}, meth@BBBB
 */
void dexInstrInvokeStatic(u2* pInsns, u4* offset, u4 methodIdx, u4 regCount, u4 regC, u4 regD, u4 regE, u4 regF, u4 regG) {
	dexFormat35c(pInsns, offset, 0x71, regCount, methodIdx, regC, regD, regE, regF, regG);
}

/* Opcode:	invoke-virtual/range
 * 74 3rc	invoke-virtual/range {vCCCC .. vNNNN}, meth@BBBB
 */
void dexInstrInvokeVirtualRange(u2* pInsns, u4* offset, u4 methodIdx, u4 regStart, u1 regCount) {
	dexFormat3rc(pInsns, offset, 0x74, regCount, methodIdx, regStart);
}


/* Misc Utils */

bool dexIsStaticMethod(Method* pMethod) {
    return (pMethod->accessFlags & ACC_STATIC) != 0;
}

const char* dexGetReturnTypeDescriptor(const DexFile* pDexFile, Method* pMethod) {
	const DexProtoId* pProtoId = dexGetProtoId(pDexFile, pMethod->protoIdx);
	const DexTypeId* pTypeId = dexGetTypeId(pDexFile, pProtoId->returnTypeIdx);
	const DexStringId* pStringId = dexGetStringId(pDexFile, pTypeId->descriptorIdx);
	return dexGetStringData(pDexFile, pStringId);
}

void dexWriteUnsignedLeb128(u1* pBuffer, u4* offset, u4 value) {
	u4 remaining = value >> 7;
	u4 i = *offset;

	while (remaining != 0) {
		pBuffer[i++] = (u1) ((value & 0x7f) | 0x80);
		value = remaining;
		remaining >>= 7;
	}

	pBuffer[i++] = (u1) (value & 0x7f);
	*offset = i;
}

void dexWriteSignedLeb128(u1* pBuffer, u4* offset, int value) {
	int remaining = value >> 7;
	bool hasMore = 1;
	int end = ((value & INTEGER_MIN_VALUE) == 0) ? 0 : -1;
	u4 i = *offset;

	while (hasMore) {
		hasMore = (remaining != end)
			|| ((remaining & 1) != ((value >> 6) & 1));

		pBuffer[i++] = (value & 0x7f) | (hasMore ? 0x80 : 0);
		value = remaining;
		remaining >>= 7;
	}

	*offset = i;
}



/* Debug Helpers */
/*
static void printDexCode(DexCode* pDexCode) {
    LOGD("DexCode at %08x", pDexCode);
    LOGD("-- registersSize: %d", pDexCode->registersSize);
    LOGD("-- insSize: %d", pDexCode->insSize);
    LOGD("-- outsSize: %d", pDexCode->outsSize);
    LOGD("-- triesSize: %d", pDexCode->triesSize);
    LOGD("-- debugInfoOff: %d", pDexCode->debugInfoOff);
    LOGD("-- insnsSize: %d", pDexCode->insnsSize);
    LOGD("-- insns: %08x", pDexCode->insns);
}
*/
