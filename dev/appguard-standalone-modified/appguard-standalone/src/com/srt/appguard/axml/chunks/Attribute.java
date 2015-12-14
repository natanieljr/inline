package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Attribute of a XML element.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class Attribute implements Comparable<Attribute> {

	private final StringRef ns;
	private final StringRef name;
	private ResValue data;

	/**
	 * Read attribute from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @param document
	 *            The document this chunk is located in.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public Attribute(AXmlInputStream stream, XmlDocument document) throws IOException {
		int nsId = stream.readUint32();
		ns = document.getStringRef(nsId);
		int nameId = stream.readUint32();
		name = document.getStringRef(nameId);
		data = new ResValue(stream, document);
	}

	/**
	 * Create a new attribute with the given values.
	 * 
	 * @param nsId
	 *            The namespace of the attribute.
	 * @param nameId
	 *            The name of the attribute.
	 * @param value
	 *            The value for this attribute.
	 */
	public Attribute(StringRef nsId, StringRef nameId, StringRef value) {
		ns = nsId;
		name = nameId;
		data = new ResValue(value);
	}

	/**
	 * Print this attribute.
	 * 
	 * @param builder
	 *            The string builder to write to.
	 */
	public void print(StringBuilder builder) {
		builder.append(ns).append(":").append(name).append("=\"").append(data).append("\"");
	}

	/**
	 * Change the value of this attribute.
	 * 
	 * @param value
	 *            The new value as a string reference.
	 */
	public void setStringValue(StringRef value) {
		data = new ResValue(value);
	}

	/**
	 * Get the value of this attribute.
	 * 
	 * @return The value of this attribute.
	 */
	public String getStringValue() {
		return data.toString();
	}

	/**
	 * Write this attribute to the given stream.
	 * 
	 * @param outStream
	 *            The stream to write to.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	public void write(AXmlOutputStream outStream) throws IOException {
		outStream.writeUint32(ns != null ? ns.getIndex() : -1);
		outStream.writeUint32(name != null ? name.getIndex() : -1);
		data.write(outStream);
	}

	/**
	 * Get the resource ID of the name of this attribute.
	 * 
	 * @return The resId of the attribute name.
	 */
	int getResId() {
		return name.getResId();
	}

	@Override
	public int compareTo(Attribute another) {
		return getResId() - another.getResId();
	}

	/**
	 * Compare this attribute to the given namespace and name.
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
}