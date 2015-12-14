package com.srt.appguard.axml.chunks;

import java.util.ArrayList;
import java.util.List;

import com.srt.appguard.axml.chunks.StringPool.StringRef;

/**
 * Element of a XML file. Consist of a {@link StartElement}, an
 * {@link EndElement} and a list of child elements.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class Element {

	private final XmlDocument document;
	private final StartElement startElement;
	private final List<Element> children;
	private EndElement endElement;

	/**
	 * Create a new XML element.
	 * 
	 * @param document
	 *            The document this element is located in.
	 * @param startElement
	 *            The start element of this element.
	 */
	public Element(XmlDocument document, StartElement startElement) {
		this.document = document;
		this.startElement = startElement;
		this.children = new ArrayList<Element>();
	}

	/**
	 * Create a new XML element.
	 * 
	 * @param document
	 *            The document this element is located in.
	 * @param startElement
	 *            The start element of this element.
	 * @param endElement
	 *            The end element of this element.
	 */
	public Element(XmlDocument document, StartElement startElement, EndElement endElement) {
		this(document, startElement);
		this.endElement = endElement;
	}

	/**
	 * Add a new child element.
	 * 
	 * @param child The new child to add.
	 */
	void addChild(Element child) {
		children.add(child);
	}

	/**
	 * Set the end element of this element.
	 * 
	 * @param endElement
	 *            The new end element of this element.
	 */
	void setEndElement(EndElement endElement) {
		this.endElement = endElement;
	}

	/**
	 * 
	 * @return String representation of this element.
	 */
	public String print() {
		StringBuilder builder = new StringBuilder();
		print(builder, 0);
		return builder.toString();
	}

	/**
	 * Print the given element.
	 * 
	 * @param builder
	 *            String builder to write into.
	 * @param indent
	 *            Indentation level.
	 */
	private void print(StringBuilder builder, int indent) {
		for (int i = 0; i < indent; i++) {
			builder.append('\t');
		}
		startElement.print(builder);
		for (Element child : children) {
			builder.append("\n");
			child.print(builder, indent + 1);
		}
		if (children.size() > 0) {
			builder.append("\n");
			for (int i = 0; i < indent; i++) {
				builder.append('\t');
			}
		}
		endElement.print(builder);
	}

	/**
	 * Get the name of the element.
	 * 
	 * @return The name of the element.
	 */
	public String getName() {
		return startElement.getName();
	}

	/**
	 * Recursively find the element with the given namespace and name.
	 * 
	 * @param ns
	 *            The namespace of the searched element.
	 * @param name
	 *            The name of the searched element.
	 * @return This element if it has the given values, or one of its children,
	 *         or <code>null</code> if no such element exists.
	 */
	public Element findElement(StringRef ns, StringRef name) {
		if (startElement.equals(ns, name)) {
			return this;
		} else {
			for (Element child : children) {
				Element element = child.findElement(ns, name);
				if (element != null) {
					return element;
				}
			}
			return null;
		}
	}

	/**
	 * Create a new element with the name and add it as a child to this element.
	 * 
	 * @param name
	 *            The name of the new element.
	 * @return The newly created element.
	 */
	protected Element createElement(String name) {
		StringRef nameRef = document.registerString(name);
		int lineNumber = endElement.getLineNumber();

		StartElement startElement = new StartElement(lineNumber, nameRef);
		EndElement endElement = new EndElement(lineNumber, nameRef);

		Element element = new Element(document, startElement, endElement);
		children.add(element);

		return element;
	}

	/**
	 * Get the start element of this element.
	 * 
	 * @return The start element of this element.
	 */
	protected StartElement getStartElement() {
		return startElement;
	}

	/**
	 * Get the end element of this element.
	 * 
	 * @return The end element of this element.
	 */
	protected EndElement getEndElement() {
		return endElement;
	}

	/**
	 * Add or overwrite an attribute of this element.
	 * 
	 * @param ns
	 *            The namespace of the attribute (can be null).
	 * @param name
	 *            The name of the attribute (not null).
	 * @param resId
	 *            The resource id of the attribute (see android.R.attr).
	 * @param value
	 *            The value for the attribute.
	 */
	public void setAttribute(String ns, String name, int resId, String value) {
		StringRef nsId = document.registerString(ns);
		StringRef nameId = document.registerString(name, resId);
		StringRef valueId = document.registerString(value);
		startElement.setAttribute(nsId, nameId, resId, valueId);
	}

	/**
	 * Get the value for a specific attribute.
	 * 
	 * @param ns
	 *            The namespace of the attribute (can be null).
	 * @param name
	 *            The name of the attribute (not null).
	 * @return The value of the attribute or <code>null</code> if no such
	 *         attribute exists.
	 */
	public String getAttributeValue(String ns, String name) {
		StringRef nsId = document.getStringRef(ns);
		StringRef nameId = document.getStringRef(name);
		if (nameId != null) {
			Attribute attribute = startElement.getAttribute(nsId, nameId);
			if (attribute != null) {
				return attribute.getStringValue();
			}
		}
		return null;
	}

	/**
	 * Get the value for a specific attribute.
	 *
	 * @param ns
	 *            The namespace of the attribute (can be null).
	 * @param name
	 *            The name of the attribute (not null).
	 * @param resId
	 *            The resId of the attribute.
	 * @return The value of the attribute or <code>null</code> if no such
	 *         attribute exists.
	 */
	public String getAttributeValue(String ns, String name, int resId) {
		StringRef nsId = document.getStringRef(ns);
		StringRef nameId = document.getStringRef(name);
		Attribute attribute = startElement.getAttribute(nsId, nameId, resId);
		if (attribute != null) {
			return attribute.getStringValue();
		}
		return null;
	}

	@Override
	public String toString() {
		return print();
	}
}
