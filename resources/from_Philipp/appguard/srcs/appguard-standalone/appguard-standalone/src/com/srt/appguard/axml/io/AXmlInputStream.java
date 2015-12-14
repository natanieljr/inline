package com.srt.appguard.axml.io;

import java.io.IOException;
import java.io.InputStream;

public class AXmlInputStream implements AXmlStream {
	private static final int NO_BUF = -2;
	private InputStream stream;
	private long pos;
	private int buf;

	public AXmlInputStream(InputStream stream) {
		this.stream = stream;
		this.pos = 0;
		this.buf = NO_BUF;
	}

	public boolean isEof() throws IOException {
		// Read one byte ahead to see if we reached the EOF
		if (buf == NO_BUF) {
			buf = stream.read();
			return buf == -1;
		} else {
			return buf == -1;
		}
	}

	public int readByte() throws IOException {
		int byteValue;
		if (buf == NO_BUF) {
			byteValue = stream.read();
		} else {
			byteValue = buf;
			buf = NO_BUF;
		}
		pos++;
		return byteValue;
	}

	public int readUint8() throws IOException {
		return readByte();
	}

	public int readUint16() throws IOException {
		int low = readByte();
		int high = readByte();

		return ((high << 8) | (low & 0xff));
	}

	public int readUint32() throws IOException {
		int low = readUint16();
		int high = readUint16();

		return (high << 16) | (low & 0xffff);
	}

	public String readString(boolean utf8) throws IOException {
		if (!utf8) {
			return readString();
		} else {
			return readUtf8String();
		}
	}

	private String readUtf8String() throws IOException {
		readStringLength8();
		int lengthEncoding = readStringLength8();

		byte[] bytes = new byte[lengthEncoding];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) readByte();
		}
		int zero = readUint8();
		assert zero == 0;

		return new String(bytes);
	}

	private String readString() throws IOException {
		int length = readStringLength16();

		byte[] bytes = new byte[length * 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) readByte();
		}
		int zero = readUint16();
		assert zero == 0;

		return new String(bytes, "UTF-16LE");
	}

	private int readStringLength8() throws IOException {
		int length = readUint8();
		if ((length & UINT_16_LENGTH_MASK) != 0) {
			length = (length << 8) & (~UINT_16_LENGTH_MASK);
			length += readUint8();
		}
		return length;
	}

	private int readStringLength16() throws IOException {
		int length = readUint16();
		if ((length & UINT_32_LENGTH_MASK) != 0) {
			length = (length << 16) & (~UINT_32_LENGTH_MASK);
			length += readUint16();
		}
		return length;
	}

	public void align() throws IOException {
		while (pos % 4 != 0) {
			int zero = readByte();
			assert zero == 0;
		}
	}

	public void close() throws IOException {
		stream.close();
	}

	public long getPosition() {
		return pos;
	}

	public void moveTo(long pos) throws IOException {
		long skip = pos - this.pos;
		while (skip > 0) {
			long skipped = stream.skip(skip);
			skip -= skipped;
		}
		this.pos = pos;
	}
}
