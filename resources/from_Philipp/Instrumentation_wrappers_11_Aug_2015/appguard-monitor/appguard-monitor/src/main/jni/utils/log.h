/*
 * log.h
 *
 *  Created on: 05.10.2012
 *      Author: Sven
 */

#include <android/log.h>

#ifndef LOG_H_
#define LOG_H_

#define  _LOG_TAG    "AppGuardMonitor"
#define  LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,_LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,_LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,_LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,_LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,_LOG_TAG,__VA_ARGS__)

#endif /* LOG_H_ */
