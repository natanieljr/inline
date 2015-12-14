#ifndef _DALVIK_INDIRECTREFTABLE
#define _DALVIK_INDIRECTREFTABLE

typedef enum IndirectRefKind {
    kIndirectKindInvalid    = 0,
    kIndirectKindLocal      = 1,
    kIndirectKindGlobal     = 2,
    kIndirectKindWeakGlobal = 3
} IndirectRefKind;

typedef void* IndirectRef;

#define kIRTPrevCount   4
typedef struct IndirectRefSlot {
    u4          serial;         /* slot serial */
    Object*     previous[kIRTPrevCount];
} IndirectRefSlot;

typedef union IRTSegmentState {
    u4          all;
    struct {
        u4      topIndex:16;            /* index of first unused entry */
        u4      numHoles:16;            /* #of holes in entire table */
    } parts;
} IRTSegmentState;

typedef struct IndirectRefTable {
    /* semi-public - read/write by interpreter in native call handler */
    IRTSegmentState segmentState;

    /* semi-public - read-only during GC scan; pointer must not be kept */
    Object**        table;              /* bottom of the stack */

    /* private */
    IndirectRefSlot* slotData;          /* extended debugging info */
    int             allocEntries;       /* #of entries we have space for */
    int             maxEntries;         /* max #of entries allowed */
    IndirectRefKind kind;               /* bit mask, ORed into all irefs */

    // TODO: want hole-filling stats (#of holes filled, total entries scanned)
    //       for performance evaluation.
} IndirectRefTable;

#endif /*_DALVIK_INDIRECTREFTABLE*/
