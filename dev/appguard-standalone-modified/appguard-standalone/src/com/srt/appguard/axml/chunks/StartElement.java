package com.srt.appguard.axml.chunks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Start element chunk of a serialized XML file.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class StartElement extends Chunk {

	private final int lineNumber;
	private final StringRef comment;
	private final StringRef ns;
	private final StringRef name;
	private final int attributeStart;
	private final int attributeSize;
	private final int idIndex;
	private final int classIndex;
	private final int styleIndex;
	private final List<Attribute> attributes;

	/**
	 * Read start element chunk from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @param document
	 *            The document this chunk is located in.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected StartElement(AXmlInputStream stream, XmlDocument document) throws IOException {
		super(Type.RES_XML_START_ELEMENT_TYPE, stream);

		lineNumber = stream.readUint32();
		int commentId = stream.readUint32();
		comment = document.getStringRef(commentId);

		int nsId = stream.readUint32();
		ns = document.getStringRef(nsId);
		int nameId = stream.readUint32();
		name = document.getStringRef(nameId);

		attributeStart = stream.readUint16();
		attributeSize = stream.readUint16();
		int attributeCount = stream.readUint16();
		idIndex = stream.readUint16();
		classIndex = stream.readUint16();
		styleIndex = stream.readUint16();

		attributes = new ArrayList<Attribute>(attributeCount);
		for (int i = 0; i < attributeCount; i++) {
			Attribute attribute = new Attribute(stream, document);
			attributes.add(attribute);
		}
	}

	/**
	 * Create a new start element chunk with the given name and line number.
	 * 
	 * @param lineNumber
	 *            The line number of this chunk.
	 * @param name
	 *            The name of this element.
	 */
	protected StartElement(int lineNumber, StringRef name) {
		super(0, Type.RES_XML_START_ELEMENT_TYPE, 0x10, 0x60);
		this.lineNumber = lineNumber;
		this.comment = null;
		this.ns = null;
		this.name = name;
		this.attributeStart = 0x14;
		this.attributeSize = 0x14;
		this.idIndex = 0x0;
		this.classIndex = 0x0;
		this.styleIndex = 0x0;
		this.attributes = new ArrayList<Attribute>();
	}

	/**
	 * Get the name of the element.
	 * 
	 * @return The name of the element.
	 */
	String getName() {
		return name.getValue();
	}

	/**
	 * Print the given element.
	 * 
	 * @param builder
	 *            String builder to write into.
	 */
	public void print(StringBuilder builder) {
		builder.append("<").append(name);
		for (Attribute attribute : attributes) {
			builder.append(' ');
			attribute.print(builder);
		}
		builder.append(">");
	}

	/**
	 * Set a XML attribute of this element. Adds a new attribute if it this not
	 * already exist.
	 * 
	 * @param nsId
	 *            The namespace of the attribute.
	 * @param nameId
	 *            The name of the attribute.
	 * @param resId
	 *            The resId of the attribute.
	 * @param valueId
	 *            The value for the attribute.
	 */
	void setAttribute(StringRef nsId, StringRef nameId, int resId, StringRef valueId) {
		Attribute attribute = getAttribute(nsId, nameId, resId);
		if (attribute != null) {
			attribute.setStringValue(valueId);
		} else {
			Attribute a = new Attribute(nsId, nameId, valueId);
			attributes.add(a);
			Collections.sort(attributes);

			if (attributes.size() > 0xffff) {
				throw new IllegalArgumentException("Too many attributes!");
			}
		}
	}

	/**
	 * Get the attribute of this element with the given namespace and name.
	 * 
	 * @param ns
	 *            The namespace of the attribute.
	 * @param name
	 *            The name of the attribute.
	 * @return The attribute with the given values or <code>null</code> if no
	 *         such attribute exists.
	 */
	public Attribute getAttribute(StringRef ns, StringRef name) {
		for (Attribute attribute : attributes) {
			if (attribute.equals(ns, name)) {
				return attribute;
			}
		}
		return null;
	}

	/**
	 * Get the attribute of this element with the given namespace and name or with the given resId.
	 *
	 * @param ns    The namespace of the attribute.
	 * @param name  The name of the attribute.
	 * @param resId The resId of the name of the attribute.
	 * @return The attribute with the given values or <code>null</code> if no
	 * such attribute exists.
	 */
	public Attribute getAttribute(StringRef ns, StringRef name, int resId) {
		for (Attribute attribute : attributes) {
			if (attribute.equals(ns, name)) {
				return attribute;
			}
		}

		if (resId != -1) {
			return getAttribute(resId);
		}
		return null;
	}

	/**
	 * Get the attribute of this element with the given resId.
	 *
	 * @param resId The resId of the attribute.
	 * @return The attribute with the given values or <code>null</code> if no
	 * such attribute exists.
	 */
	private Attribute getAttribute(int resId) {
		for (Attribute attribute : attributes) {
			if (attribute.getResId() == resId) {
				return attribute;
			}
		}
		return null;
	}

	@Override
	public int getSize() {
		return super.getSize() + 28 + (attributes.size() * 20);
	}

	@Override
	public void write(AXmlOutputStream outStream) throws IOException {
		super.write(outStream);

		outStream.writeUint32(lineNumber);
		outStream.writeUint32(comment != null ? comment.getIndex() : -1);

		outStream.writeUint32(ns != null ? ns.getIndex() : -1);
		outStream.writeUint32(name != null ? name.getIndex() : -1);

		outStream.writeUint16(attributeStart);
		outStream.writeUint16(attributeSize);
		outStream.writeUint16(attributes.size());
		outStream.writeUint16(idIndex);
		outStream.writeUint16(classIndex);
		outStream.writeUint16(styleIndex);

		for (Attribute attribute : attributes) {
			attribute.write(outStream);
		}
	}

	/**
	 * Compare this element to the given namespace and name.
	 * 
	 * @param ns
	 *            The namespace to compare with.
	 * @param name
	 *            The name to compare with.
	 * @return <code>True</code> if the namespace and name are equal,
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(StringRef ns, StringRef name) {
		return Chunk.equals(this.ns, this.name, ns, name);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		print(builder);
		return builder.toString();
	}
}
