#ifndef _DALVIK_JNIINTERNAL
#define _DALVIK_JNIINTERNAL

#include <jni.h>
#include "Thread.h"

/*
 * Our data structures for JNIEnv and JavaVM.
 *
 * Native code thinks it has a pointer to a pointer.  We know better.
 */
struct JavaVMExt;

typedef struct JNIEnvExt {
    const struct JNINativeInterface* funcTable;     /* must be first */

    const struct JNINativeInterface* baseFuncTable;

    /* pointer to the VM we are a part of */
    // XXX
    //struct JavaVMExt* vm;

    u4      envThreadId;
    Thread* self;

    /* if nonzero, we are in a "critical" JNI call */
    int     critical;

    // XXX
    int     test;

    /* keep a copy of this here for speed */
    bool    forceDataCopy;

    struct JNIEnvExt* prev;
    struct JNIEnvExt* next;
} JNIEnvExt;

typedef struct JavaVMExt {
    const struct JNIInvokeInterface* funcTable;     /* must be first */

    const struct JNIInvokeInterface* baseFuncTable;

    /* if multiple VMs are desired, add doubly-linked list stuff here */

    /* per-VM feature flags */
    bool    useChecked;
    bool    warnError;
    bool    forceDataCopy;

    /* head of list of JNIEnvs associated with this VM */
    JNIEnvExt*      envList;
    pthread_mutex_t envListLock;
} JavaVMExt;

#endif /*_DALVIK_JNIINTERNAL*/
