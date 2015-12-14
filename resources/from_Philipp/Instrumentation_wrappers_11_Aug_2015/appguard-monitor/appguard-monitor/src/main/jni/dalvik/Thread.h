#ifndef _DALVIK_THREAD
#define _DALVIK_THREAD

#include <pthread.h>
#include <jni.h>

/*
 * Current status; these map to JDWP constants, so don't rearrange them.
 * (If you do alter this, update the strings in dvmDumpThread and the
 * conversion table in VMThread.java.)
 *
 * Note that "suspended" is orthogonal to these values (so says JDWP).
 */
typedef enum ThreadStatus {
    THREAD_UNDEFINED    = -1,       /* makes enum compatible with int32_t */

    /* these match up with JDWP values */
    THREAD_ZOMBIE       = 0,        /* TERMINATED */
    THREAD_RUNNING      = 1,        /* RUNNABLE or running now */
    THREAD_TIMED_WAIT   = 2,        /* TIMED_WAITING in Object.wait() */
    THREAD_MONITOR      = 3,        /* BLOCKED on a monitor */
    THREAD_WAIT         = 4,        /* WAITING in Object.wait() */
    /* non-JDWP states */
    THREAD_INITIALIZING = 5,        /* allocated, not yet running */
    THREAD_STARTING     = 6,        /* started, not yet on thread list */
    THREAD_NATIVE       = 7,        /* off in a JNI native method */
    THREAD_VMWAIT       = 8,        /* waiting on a VM resource */
    THREAD_SUSPENDED    = 9,        /* suspended, usually by GC or debugger */
} ThreadStatus;

/*
 * Our per-thread data.
 *
 * These are allocated on the system heap.
 */
typedef struct Thread {
    /* small unique integer; useful for "thin" locks and debug messages */
    u4          threadId;

    /*
     * Thread's current status.  Can only be changed by the thread itself
     * (i.e. don't mess with this from other threads).
     */
    volatile ThreadStatus status;

    /*
     * This is the number of times the thread has been suspended.  When the
     * count drops to zero, the thread resumes.
     *
     * "dbgSuspendCount" is the portion of the suspend count that the
     * debugger is responsible for.  This has to be tracked separately so
     * that we can recover correctly if the debugger abruptly disconnects
     * (suspendCount -= dbgSuspendCount).  The debugger should not be able
     * to resume GC-suspended threads, because we ignore the debugger while
     * a GC is in progress.
     *
     * Both of these are guarded by gDvm.threadSuspendCountLock.
     *
     * (We could store both of these in the same 32-bit, using 16-bit
     * halves, to make atomic ops possible.  In practice, you only need
     * to read suspendCount, and we need to hold a mutex when making
     * changes, so there's no need to merge them.  Note the non-debug
     * component will rarely be other than 1 or 0 -- not sure it's even
     * possible with the way mutexes are currently used.)
     */
    int         suspendCount;
    int         dbgSuspendCount;

    /* thread handle, as reported by pthread_self() */
    pthread_t   handle;

    /* thread ID, only useful under Linux */
    pid_t       systemTid;

    /* start (high addr) of interp stack (subtract size to get malloc addr) */
    u1*         interpStackStart;

    /* current limit of stack; flexes for StackOverflowError */
    const u1*   interpStackEnd;

    /* interpreter stack size; our stacks are fixed-length */
    int         interpStackSize;
    bool        stackOverflowed;

    /* FP of bottom-most (currently executing) stack frame on interp stack */
    void*       curFrame;

    /* current exception, or NULL if nothing pending */
    Object*     exception;

    /* the java/lang/Thread that we are associated with */
    Object*     threadObj;

    /* the JNIEnv pointer associated with this thread */
    JNIEnv*     jniEnv;

    /* internal reference tracking */
    ReferenceTable  internalLocalRefTable;

    // XXX

//#if defined(WITH_JIT)
//    /*
//     * Whether the current top VM frame is in the interpreter or JIT cache:
//     *   NULL    : in the interpreter
//     *   non-NULL: entry address of the JIT'ed code (the actual value doesn't
//     *             matter)
//     */
//    void*       inJitCodeCache;
//#if defined(WITH_SELF_VERIFICATION)
//    /* Buffer for register state during self verification */
//    void* shadowSpace; //ShadowSpace*
//#endif
//#endif

    /* JNI local reference tracking */
//#ifdef USE_INDIRECT_REF
    IndirectRefTable jniLocalRefTable;
//#else
//    ReferenceTable  jniLocalRefTable;
//#endif

    /* JNI native monitor reference tracking (initialized on first use) */
    ReferenceTable  jniMonitorRefTable;

    /* hack to make JNI_OnLoad work right */
    Object*     classLoaderOverride;

    /* mutex to guard the interrupted and the waitMonitor members */
    pthread_mutex_t    waitMutex;

    /* pointer to the monitor lock we're currently waiting on */
    /* guarded by waitMutex */
    /* TODO: consider changing this to Object* for better JDWP interaction */
    u4      waitMonitor; // Monitor*

    /* thread "interrupted" status; stays raised until queried or thrown */
    /* guarded by waitMutex */
    bool        interrupted;

    /* links to the next thread in the wait set this thread is part of */
    struct Thread*     waitNext;

    /* object to sleep on while we are waiting for a monitor */
    pthread_cond_t     waitCond;

    /*
     * Set to true when the thread is in the process of throwing an
     * OutOfMemoryError.
     */
    bool        throwingOOME;

    /* links to rest of thread list; grab global lock before traversing */
    struct Thread* prev;
    struct Thread* next;

    /* used by threadExitCheck when a thread exits without detaching */
    int         threadExitCheckCount;

    // XXX ignore, we don't need these

    /* JDWP invoke-during-breakpoint support */
//    DebugInvokeReq  invokeReq;

//#ifdef WITH_MONITOR_TRACKING
//    /* objects locked by this thread; most recent is at head of list */
//    struct LockedObjectData* pLockedObjects;
//#endif
//
//#ifdef WITH_ALLOC_LIMITS
//    /* allocation limit, for Debug.setAllocationLimit() regression testing */
//    int         allocLimit;
//#endif

//    /* base time for per-thread CPU timing (used by method profiling) */
//    bool        cpuClockBaseSet;
//    u8          cpuClockBase;

    /* memory allocation profiling state */
//    AllocProfState allocProf;

//#ifdef WITH_JNI_STACK_CHECK
//    u4          stackCrc;
//#endif
//
//#if WITH_EXTRA_GC_CHECKS > 1
//    /* PC, saved on every instruction; redundant with StackSaveArea */
//    const u2*   currentPc2;
//#endif
} Thread;

#endif /*_DALVIK_THREAD*/
