package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.io.AXmlInputStream;

/**
 * Start namespace chunk of a serialized XML file.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class StartNamespace extends Namespace {

	/**
	 * Read start namespace chunk from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @param document
	 *            The document this chunk is located in.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected StartNamespace(AXmlInputStream stream, XmlDocument document) throws IOException {
		super(Type.RES_XML_START_NAMESPACE_TYPE, stream, document);
	}
}
