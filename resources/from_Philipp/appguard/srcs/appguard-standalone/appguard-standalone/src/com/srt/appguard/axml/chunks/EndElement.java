package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * End element chunk of a serialized XML file.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class EndElement extends Chunk {

	private final int lineNumber;
	private final StringRef comment;
	private final StringRef ns;
	private final StringRef name;

	/**
	 * Read end element chunk from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @param document
	 *            The document this chunk is located in.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected EndElement(AXmlInputStream stream, XmlDocument document) throws IOException {
		super(Type.RES_XML_END_ELEMENT_TYPE, stream);

		lineNumber = stream.readUint32();
		int commentId = stream.readUint32();
		comment = document.getStringRef(commentId);
		int nsId = stream.readUint32();
		ns = document.getStringRef(nsId);
		int nameId = stream.readUint32();
		name = document.getStringRef(nameId);
	}

	/**
	 * Create a new end element chunk with the given name and line number.
	 * 
	 * @param lineNumber
	 *            The line number of this chunk.
	 * @param name
	 *            The name of this element.
	 */
	protected EndElement(int lineNumber, StringRef name) {
		super(0, Type.RES_XML_END_ELEMENT_TYPE, 0x10, 0x18);
		this.lineNumber = lineNumber;
		this.comment = null;
		this.ns = null;
		this.name = name;
	}

	/**
	 * Print the given element.
	 * 
	 * @param builder
	 *            String builder to write into.
	 */
	public void print(StringBuilder builder) {
		builder.append("</").append(name).append(">");
	}

	/**
	 * Get the line number of this element.
	 * 
	 * @return The line number of this element.
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public int getSize() {
		return super.getSize() + 16;
	}

	@Override
	public void write(AXmlOutputStream outStream) throws IOException {
		super.write(outStream);

		outStream.writeUint32(lineNumber);
		outStream.writeUint32(comment != null ? comment.getIndex() : -1);
		outStream.writeUint32(ns != null ? ns.getIndex() : -1);
		outStream.writeUint32(name != null ? name.getIndex() : -1);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		print(builder);
		return builder.toString();
	}
}
