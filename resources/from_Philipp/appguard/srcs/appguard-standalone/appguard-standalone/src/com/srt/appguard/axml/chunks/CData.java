package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * CData chunk of a serialized XML file.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class CData extends Chunk {

	private final int lineNumber;
	private final StringRef comment;
	private final ResValue data;

	/**
	 * Read CData chunk from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @param document
	 *            The document this chunk is located in.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected CData(AXmlInputStream stream, XmlDocument document) throws IOException {
		super(Type.RES_XML_CDATA_TYPE, stream);

		lineNumber = stream.readUint32();
		int commentId = stream.readUint32();
		comment = document.getStringRef(commentId);
		data = new ResValue(stream, document);
	}

	@Override
	protected int getSize() {
		return super.getSize() + 20;
	}

	@Override
	public void write(AXmlOutputStream outStream) throws IOException {
		super.write(outStream);
		outStream.writeUint32(lineNumber);
		outStream.writeUint32(comment != null ? comment.getIndex() : -1);
		data.write(outStream);
	}

	@Override
	public String toString() {
		return "<CDATA " + data + " />";
	}
}
