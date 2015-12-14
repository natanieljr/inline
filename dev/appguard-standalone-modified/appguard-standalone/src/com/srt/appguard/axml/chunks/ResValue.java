package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Class that holds a resource value.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class ResValue {

	protected final StringRef rawValue;
	protected final int size;
	protected final int zero;
	protected final int dataType;
	protected final Object data;

	/**
	 * Read resource value from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @param document
	 *            The document this chunk is located in.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected ResValue(AXmlInputStream stream, XmlDocument document) throws IOException {
		int rawValueId = stream.readUint32();
		rawValue = document.getStringRef(rawValueId);
		size = stream.readUint16();
		zero = stream.readByte();
		dataType = stream.readByte();
		int dataValue = stream.readUint32();

		if (dataType == 3) {
			data = document.getStringRef(dataValue);
		} else {
			data = dataValue;
		}
	}

	/**
	 * Create new resource value with the given string value.
	 * 
	 * @param value
	 *            The value as a string reference.
	 */
	public ResValue(StringRef value) {
		this.rawValue = value;
		this.size = 8;
		this.zero = 0;
		this.dataType = 3;
		this.data = value;
	}

	/**
	 * Write resource value to stream.
	 * 
	 * @param outStream
	 *            The stream to write to.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public void write(AXmlOutputStream outStream) throws IOException {
		int dataValue;
		if (dataType == 3) {
			dataValue = ((StringRef) data).getIndex();
		} else {
			dataValue = (Integer) data;
		}
		outStream.writeUint32(rawValue != null ? rawValue.getIndex() : -1);
		outStream.writeUint16(size);
		outStream.writeByte(zero);
		outStream.writeByte(dataType);
		outStream.writeUint32(dataValue);
	}

	@Override
	public String toString() {
		return data.toString();
	}
}
