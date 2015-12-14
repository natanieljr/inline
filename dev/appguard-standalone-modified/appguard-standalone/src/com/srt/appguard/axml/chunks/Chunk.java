package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Base class for all serialized XML chunks.
 * 
 * @see <a
 *      href="http://justanapplication.wordpress.com/category/android/android-binary-xml/">http://justanapplication.wordpress.com/category/android/android-binary-xml/</a>
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public abstract class Chunk {

	protected static boolean equals(StringRef ns1, StringRef name1, StringRef ns2, StringRef name2) {
		if ((ns1 == null && ns2 == null) || (ns1 != null && ns1.equals(ns2))) {
			return name1.equals(name2);
		}
		return false;
	}

	/**
	 * Type enum with all possible XML chunk types.
	 * 
	 * @author Sven Obser <sven.obser@backes-srt.de>
	 */
	public enum Type {
		RES_NULL_TYPE(0x0000), RES_STRING_POOL_TYPE(0x0001), RES_TABLE_TYPE(0x0002), RES_XML_TYPE(0x0003),

		// Chunk types in RES_XML_TYPE
		RES_XML_START_NAMESPACE_TYPE(0x0100), RES_XML_END_NAMESPACE_TYPE(0x0101), RES_XML_START_ELEMENT_TYPE(0x0102), RES_XML_END_ELEMENT_TYPE(0x0103), RES_XML_CDATA_TYPE(
		        0x0104), RES_XML_LAST_CHUNK_TYPE(0x017f),
		// This contains a uint32_t array mapping strings in the string
		// pool back to resource identifiers. It is optional.
		RES_XML_RESOURCE_MAP_TYPE(0x0180);

		private final short type;

		Type(int type) {
			this.type = (short) type;
		}

		public static Type fromInt(int t) {
			for (Type type : values()) {
				if (type.type == t) {
					return type;
				}
			}
			throw new RuntimeException("Unknown type " + t);
		}
	}

	private final long startOffset;
	private final Type type;
	private final int headerSize;
	private final int size;

	/**
	 * Read chunk from the given stream.
	 * 
	 * @param type
	 *            The type of this chunk.
	 * @param stream
	 *            Stream to read XML from.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected Chunk(Type type, AXmlInputStream stream) throws IOException {
		// Start position of chunk is current position minus 2 bytes for type
		this.startOffset = stream.getPosition() - 2;

		this.type = type;
		this.headerSize = stream.readUint16();
		this.size = stream.readUint32();
	}

	/**
	 * Create a new chunk object.
	 * 
	 * @param startOffset
	 *            Offset in stream.
	 * @param type
	 *            The type of this chunk.
	 * @param headerSize
	 *            The header size of this chunk.
	 * @param size
	 *            The total size of this chunk.
	 */
	protected Chunk(long startOffset, Type type, int headerSize, int size) {
		this.startOffset = startOffset;
		this.type = type;
		this.headerSize = headerSize;
		this.size = size;
	}

	/**
	 * Get the offset in the stream.
	 * 
	 * @return The offset for this chunk.
	 */
	protected long getStartOffset() {
		return startOffset;
	}

	/**
	 * Get the type of this chunk.
	 * 
	 * @return The type of this chunk.
	 */
	protected Type getType() {
		return type;
	}

	/**
	 * Get the header size of this chunk.
	 * 
	 * @return The header size of this chunk.
	 */
	protected int getHeaderSize() {
		return headerSize;
	}

	/**
	 * Get the total size of this chunk.
	 * 
	 * @return The total size of this chunk.
	 */
	protected int getSize() {
		return 8;
	}

	/**
	 * Get the originally read size of this chunk.
	 * 
	 * @return The originally read size of this chunk.
	 */
	protected int getOriginalSize() {
		return size;
	}

	/**
	 * Write this chunk to the given stream.
	 * 
	 * @param outStream
	 *            The stream to write to.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected void write(AXmlOutputStream outStream) throws IOException {
		outStream.writeUint16(type.type);
		outStream.writeUint16(headerSize);
		outStream.writeUint32(getSize());
	}
}
