#ifndef _DALVIK_REFERENCETABLE
#define _DALVIK_REFERENCETABLE

/*
 * Table definition.
 *
 * The expected common operations are adding a new entry and removing a
 * recently-added entry (usually the most-recently-added entry).
 *
 * If "allocEntries" is not equal to "maxEntries", the table may expand when
 * entries are added, which means the memory may move.  If you want to keep
 * pointers into "table" rather than offsets, use a fixed-size table.
 *
 * (This structure is still somewhat transparent; direct access to
 * table/nextEntry is allowed.)
 */
typedef struct ReferenceTable {
    Object**        nextEntry;          /* top of the list */
    Object**        table;              /* bottom of the list */

    int             allocEntries;       /* #of entries we have space for */
    int             maxEntries;         /* max #of entries allowed */
} ReferenceTable;

#endif /*_DALVIK_REFERENCETABLE*/
