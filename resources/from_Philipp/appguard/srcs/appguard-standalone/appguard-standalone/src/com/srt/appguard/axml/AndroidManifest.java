package com.srt.appguard.axml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.srt.appguard.axml.chunks.Element;
import com.srt.appguard.axml.chunks.XmlDocument;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Class to parse and modify serialized AndroidManifest.xml files.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class AndroidManifest {

	private XmlDocument xmlDocument;

	/**
	 * Parse new AndroidManifest.xml from given stream.
	 * 
	 * @param stream
	 *            Stream to read AndroidManifest from.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public AndroidManifest(AXmlInputStream stream) throws IOException {
		xmlDocument = XmlDocument.readDocument(stream);
	}

	/**
	 * Parse new AndroidManifest.xml from given stream.
	 * 
	 * @param stream
	 *            Stream to read AndroidManifest from.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public AndroidManifest(InputStream stream) throws IOException {
		this(new AXmlInputStream(stream));
	}

	/**
	 * Get the first XML element with the given name.
	 * 
	 * @param name
	 *            The name of the element.
	 * @return The first occurrence of the element or <code>null</code> if no
	 *         such element exists.
	 */
	public Element getElement(String name) {
		return xmlDocument.getElement(name);
	}

	/**
	 * Create a new XML element with the given name
	 * 
	 * @param parent
	 *            The parent for the element.
	 * @param name
	 *            The name if the XML element.
	 * @return The newly created XML element.
	 */
	public Element createElement(Element parent, String name) {
		return xmlDocument.createElement(parent, name);
	}

	/**
	 * Write the (modified) XML document in its serialized form to the given
	 * stream.
	 * 
	 * @param outStream
	 *            The stream to write to.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public void write(OutputStream outStream) throws IOException {
		write(new AXmlOutputStream(outStream));
	}

	/**
	 * Write the (modified) XML document in its serialized form to the given
	 * stream.
	 * 
	 * @param outStream
	 *            The stream to write to.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public void write(AXmlOutputStream outStream) throws IOException {
		xmlDocument.write(outStream);
	}

	@Override
	public String toString() {
		return xmlDocument.toString();
	}
}
