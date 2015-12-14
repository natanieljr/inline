#ifndef _Included_dex_h
#define _Included_dex_h

#include "dalvik/dalvik.h"

#define NIBBLE_MAX_VALUE 	16
#define BYTE_MAX_VALUE 		256
#define SHORT_MAX_VALUE 	65536
#define INTEGER_MIN_VALUE 	0x80000000


/* Item Lookup Utils */

int dexFindStringIdx(const DexFile* pDexFile, const char* pNeedle);

int dexFindTypeIdx(const DexFile* pDexFile, const char* pDescriptor);

int dexFindFieldIdx(const DexFile* pDexFile, int classTypeIdx, const char* pName);

int dexFindMethodIdx(const DexFile* pDexFile, int classTypeIdx, const char* pName, const char* pShorty);


/* Instruction Factory */

void dexInstrConst(u2* pInsns, u4* offset, u4 regDst, u4 value);

void dexInstrConstString(u2* pInsns, u4* offset, u4 regDst, u4 stringIdx);

void dexInstrReturnVoid(u2* pInsns, u4* offset);

void dexInstrReturn(u2* pInsns, u4* offset, u4 regDst);

void dexInstrReturnWide(u2* pInsns, u4* offset, u4 regDst);

void dexInstrReturnObject(u2* pInsns, u4* offset, u4 regDst);

void dexInstrMoveResult(u2* pInsns, u4* offset, u4 regDst);

void dexInstrMoveResultWide(u2* pInsns, u4* offset, u4 regDst);

void dexInstrMoveResultObject(u2* pInsns, u4* offset, u4 regDst);

void dexInstrMoveException(u2* pInsns, u4* offset, u4 regDst);

void dexInstrNewArray(u2* pInsns, u4* offset, u4 regDst, u4 regSize, u4 typeIdx);

void dexInstrAgetObject(u2* pInsns, u4* offset, u4 regDst, u4 regArray, u4 regIndex);

void dexInstrAputObject(u2* pInsns, u4* offset, u4 regValue, u4 regArray, u4 regIndex);

void dexInstrSgetObject(u2* pInsns, u4* offset, u4 regDst, u4 fieldIdx);

void dexInstrCheckCast(u2* pInsns, u4* offset, u4 regObject, u4 typeIdx);

void dexInstrThrow(u2* pInsns, u4* offset, u4 regException);

void dexInstrInvokeVirtual(u2* pInsns, u4* offset, u4 methodIdx, u4 regCount, u4 regC, u4 regD, u4 regE, u4 regF, u4 regG);

void dexInstrInvokeStatic(u2* pInsns, u4* offset, u4 methodIdx, u4 regCount, u4 regC, u4 regD, u4 regE, u4 regF, u4 regG);

void dexInstrInvokeVirtualRange(u2* pInsns, u4* offset, u4 methodIdx, u4 regStart, u1 regCount);


/* Misc Utils */

bool dexIsStaticMethod(Method* pMethod);

const char* dexGetReturnTypeDescriptor(const DexFile* pDexFile, Method* pMethod);

void dexWriteUnsignedLeb128(u1* pBuffer, u4* offset, u4 value);

void dexWriteSignedLeb128(u1* pBuffer, u4* offset, int value);

#endif
