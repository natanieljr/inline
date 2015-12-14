package com.srt.appguard.axml.chunks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Android XML parser that allows to parse and modify serialized XML files.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class XmlDocument extends Chunk {

	/**
	 * Read XML document from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @return The parsed XML document.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public static XmlDocument readDocument(AXmlInputStream stream) throws IOException {
		XmlDocument xmlDocument = (XmlDocument) ChunkFactory.readChunk(null, stream);
		stream.close();

		return xmlDocument;
	}

	private StringPool stringPool;
	private Element root;
	private final List<Chunk> chunks;

	/**
	 * Create a new XML document from the given stream.
	 * 
	 * @param stream
	 *            Stream to read from.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	XmlDocument(AXmlInputStream stream) throws IOException {
		super(Type.RES_XML_TYPE, stream);
		chunks = new ArrayList<Chunk>();

		Stack<Element> elementStack = new Stack<Element>();
		while (!stream.isEof()) {
			Chunk chunk = ChunkFactory.readChunk(this, stream);
			if (chunk != null) {
				chunks.add(chunk);

				if (chunk.getType() == Type.RES_XML_START_ELEMENT_TYPE) {
					Element element = new Element(this, (StartElement) chunk);
					if (!elementStack.isEmpty()) {
						Element parent = elementStack.peek();
						parent.addChild(element);
					} else {
						root = element;
					}
					elementStack.add(element);
				} else if (chunk.getType() == Type.RES_XML_END_ELEMENT_TYPE) {
					Element element = elementStack.pop();
					element.setEndElement((EndElement) chunk);
				} else if (chunk.getType() == Type.RES_STRING_POOL_TYPE) {
					stringPool = (StringPool) chunk;
				}
			}
		}
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
		StringRef nameRef = stringPool.getStringRef(name);
		return root.findElement(null, nameRef);
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
		Element newChild = parent.createElement(name);

		EndElement endElement = parent.getEndElement();
		chunks.add(chunks.indexOf(endElement), newChild.getStartElement());
		chunks.add(chunks.indexOf(endElement), newChild.getEndElement());

		return newChild;
	}

	@Override
	public int getSize() {
		int size = super.getSize();
		for (Chunk chunk : chunks) {
			size += chunk.getSize();
		}
		return size;
	}

	@Override
    public void write(AXmlOutputStream outStream) throws IOException {
		super.write(outStream);
		for (Chunk chunk : chunks) {
			chunk.write(outStream);
		}
	}

	@Override
	public String toString() {
		return root.print();
	}

	/**
	 * Get the string reference with the given index.
	 * 
	 * @param index
	 *            The index of the reference.
	 * @return The string reference or <code>null</code> if the given index was
	 *         <code>-1</code>.
	 */
	StringRef getStringRef(int index) {
		return stringPool.getStringRef(index);
	}

	/**
	 * Get the {@link StringRef} with the given name.
	 * 
	 * @param name
	 *            The name of the string reference.
	 * @return The string reference or <code>null</code> if no such string
	 *         exists.
	 */
	StringRef getStringRef(String name) {
		return stringPool.getStringRef(name);
	}

	/**
	 * Get the string pool instance of this document.
	 * 
	 * @return The string pool instance.
	 */
	StringPool getStringPool() {
		return stringPool;
	}

	/**
	 * Add a new string to the string pool. Its resource ID will be
	 * <code>-1</code>.
	 * 
	 * @param name
	 *            The value of the string reference.
	 * @return The existing string reference with the given values or a newly
	 *         registered one.
	 */
	StringRef registerString(String name) {
		return registerString(name, -1);
	}

	/**
	 * Add a new string to the string pool.
	 * 
	 * @param name
	 *            The value of the string reference.
	 * @param resId
	 *            The resource ID for the string reference.
	 * @return The existing string reference with the given values or a newly
	 *         registered one.
	 */
	StringRef registerString(String name, int resId) {
		if (name == null) {
			return null;
		}
		return stringPool.register(name, resId);
	}
}
