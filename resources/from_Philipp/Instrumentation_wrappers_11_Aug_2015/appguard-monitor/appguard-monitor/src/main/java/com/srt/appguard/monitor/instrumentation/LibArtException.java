package com.srt.appguard.monitor.instrumentation;

public class LibArtException extends RuntimeException {
	public LibArtException(String msg, Throwable e) {
		super(msg, e);
	}
}
