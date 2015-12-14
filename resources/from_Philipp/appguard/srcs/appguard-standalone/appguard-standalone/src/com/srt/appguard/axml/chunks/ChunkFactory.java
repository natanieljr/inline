package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.Chunk.Type;
import com.srt.appguard.axml.io.AXmlInputStream;

/**
 * Factory to read {@link Chunk}s from a stream.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class ChunkFactory {

	/**
	 * Read a new chunk from the given stream.
	 * 
	 * @param document
	 *            The document currently read.
	 * @param stream
	 *            Stream to read XML from.
	 * @return The read chunk or <code>null</code> for unsupported types.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	static Chunk readChunk(XmlDocument document, AXmlInputStream stream) throws IOException {
		Type type = readType(stream);

		switch (type) {
		case RES_XML_TYPE:
			return new XmlDocument(stream);
		case RES_STRING_POOL_TYPE:
			return new StringPool(stream);
		case RES_XML_RESOURCE_MAP_TYPE:
			return new ResourceMap(stream, document);
		case RES_XML_START_NAMESPACE_TYPE:
			return new StartNamespace(stream, document);
		case RES_XML_END_NAMESPACE_TYPE:
			return new EndNamespace(stream, document);
		case RES_XML_START_ELEMENT_TYPE:
			return new StartElement(stream, document);
		case RES_XML_END_ELEMENT_TYPE:
			return new EndElement(stream, document);
		case RES_XML_CDATA_TYPE:
			return new CData(stream, document);
		case RES_NULL_TYPE:
		case RES_TABLE_TYPE:
		case RES_XML_LAST_CHUNK_TYPE:
		default:
			return null;
		}
	}

	/**
	 * Read the chunk type from the stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @return The read chunk type.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	static Type readType(AXmlInputStream stream) throws IOException {
		return Type.fromInt(stream.readUint16());
	}
}
