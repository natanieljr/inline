package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Namespace chunk of a serialized XML file.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public abstract class Namespace extends Chunk {

	private final int lineNumber;
	private final StringRef comment;
	private final StringRef prefix;
	private final StringRef uri;

	/**
	 * Read namespace chunk from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @param document
	 *            The document this chunk is located in.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected Namespace(Type type, AXmlInputStream stream, XmlDocument document) throws IOException {
		super(type, stream);

		lineNumber = stream.readUint32();
		int commentId = stream.readUint32();
		comment = document.getStringRef(commentId);
		int prefixIdx = stream.readUint32();
		prefix = document.getStringRef(prefixIdx);
		int uriIdx = stream.readUint32();
		uri = document.getStringRef(uriIdx);
	}

	@Override
	protected int getSize() {
		return super.getSize() + 16;
	}

	@Override
	protected void write(AXmlOutputStream outStream) throws IOException {
		super.write(outStream);

		outStream.writeUint32(lineNumber);
		outStream.writeUint32(comment != null ? comment.getIndex() : -1);
		outStream.writeUint32(prefix != null ? prefix.getIndex() : -1);
		outStream.writeUint32(uri != null ? uri.getIndex() : -1);
	}

	@Override
	public String toString() {
		return prefix + ":" + uri;
	}
}
