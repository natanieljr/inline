package com.srt.appguard.axml.io;

import java.io.IOException;
import java.io.OutputStream;

public class AXmlOutputStream implements AXmlStream {

	private OutputStream stream;
	private int pos;

	public AXmlOutputStream(OutputStream stream) {
		this.stream = stream;
		this.pos = 0;
	}

	public void writeByte(int b) throws IOException {
		stream.write(b);
		pos++;
	}

	public void writeUint8(int i) throws IOException {
		writeByte(i);
	}

	public void writeUint16(int i) throws IOException {
		byte low = (byte) i;
		byte high = (byte) (i >> 8);

		writeByte(low);
		writeByte(high);
	}

	public void writeUint32(int i) throws IOException {
		short low = (short) i;
		short high = (short) (i >> 16);

		writeUint16(low);
		writeUint16(high);
	}

	public void writeString(String s, boolean utf8) throws IOException {
		if (!utf8) {
			writeString(s);
		} else {
			writeUtf8String(s);
		}
	}

	private void writeUtf8String(String s) throws IOException {
		byte[] bytes = s.getBytes();

		writeStringLength8(s.length());
		writeStringLength8(bytes.length);

		for (int i = 0; i < bytes.length; i++) {
			writeByte(bytes[i]);
		}

		writeUint16(0);
	}

	private void writeString(String s) throws IOException {
		writeStringLength16(s.length());

		byte[] bytes = s.getBytes("UTF-16LE");
		for (int i = 0; i < bytes.length; i++) {
			writeByte(bytes[i]);
		}

		writeUint16((short) 0);
	}

	private void writeStringLength8(int i) throws IOException {
		if (i > 0x7f) {
			short high = (short) ((i >> 8) | UINT_16_LENGTH_MASK);
			short low = (short) i;
			writeUint8(high);
			writeUint8(low);
		} else {
			short length = (short) (i & ~UINT_16_LENGTH_MASK);
			writeUint8(length);
		}
	}

	private void writeStringLength16(int i) throws IOException {
		if (i > 0x7fff) {
			short high = (short) ((i >> 16) | UINT_32_LENGTH_MASK);
			short low = (short) i;
			writeUint16(high);
			writeUint16(low);
		} else {
			short length = (short) (i & ~UINT_32_LENGTH_MASK);
			writeUint16(length);
		}
	}

	public void align() throws IOException {
		while (pos % 4 != 0) {
			writeByte((byte) 0);
		}
	}
}
