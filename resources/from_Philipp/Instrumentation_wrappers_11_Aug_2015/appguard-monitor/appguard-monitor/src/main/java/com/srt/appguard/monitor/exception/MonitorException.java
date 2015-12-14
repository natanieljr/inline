package com.srt.appguard.monitor.exception;


public class MonitorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private Object value;
	private int intValue;
	private byte byteValue;
	private char charValue;
	private long longValue;
	private short shortValue;
	private float floatValue;
	private double doubleValue;
	private boolean booleanValue;
	
	public MonitorException() {
		this(null);
	}
	
	public MonitorException(final Object value) {
		this.value = value;
	}
	
	public MonitorException(final int value) {
		intValue = value;
	}

	public MonitorException(final byte value) {
		byteValue = value;
	}

	public MonitorException(final char value) {
		charValue = value;
	}

	public MonitorException(final long value) {
		longValue = value;
	}

	public MonitorException(final short value) {
		shortValue = value;
	}

	public MonitorException(final float value) {
		floatValue = value;
	}

	public MonitorException(final double value) {
		doubleValue = value;
	}

	public MonitorException(final boolean value) {
		booleanValue = value;
	}

	public Object getValue() {
		return value;
	}

	public int getIntValue() {
		return intValue;
	}

	public byte getByteValue() {
		return byteValue;
	}

	public char getCharValue() {
		return charValue;
	}

	public long getLongValue() {
		return longValue;
	}

	public short getShortValue() {
		return shortValue;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}
}
