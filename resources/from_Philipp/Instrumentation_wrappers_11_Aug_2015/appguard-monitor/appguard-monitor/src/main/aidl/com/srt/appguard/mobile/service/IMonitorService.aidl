// IRemoteService.aidl
package com.srt.appguard.mobile.service;

import com.srt.appguard.monitor.log.Message;

/** Example service interface */
interface IMonitorService {
	void log(in com.srt.appguard.monitor.log.Message message);
	void bulkLog(in com.srt.appguard.monitor.log.Message[] messages);
}