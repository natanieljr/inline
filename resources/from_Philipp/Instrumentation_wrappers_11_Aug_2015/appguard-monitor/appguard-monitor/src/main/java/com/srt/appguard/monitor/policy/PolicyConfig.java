package com.srt.appguard.monitor.policy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PolicyConfig {

	protected Map<String, Object> values = new HashMap<String, Object>();
	
	public void read(final Node node) {
		values = readMap(node);
	}
	
	public void write(final Node node) {
		writeMap(node, values);
	}

	public <T> T get(final String id, final Class<T> type) {
		return get(id, type, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(final String id, final Class<T> type, final T defaultValue) {
		final Object value = values.get(id);
		if (value == null) {
			return defaultValue;
		}

		if (type.isAssignableFrom(value.getClass())) {
			return (T) value;
		}

		final String str = value.toString();
		if (Boolean.class.isAssignableFrom(type)) {
			return (T) Boolean.valueOf(str);
		} else if (Integer.class.isAssignableFrom(type)) {
			return (T) Integer.valueOf(str);
		} else {
			throw new UnsupportedOperationException("values of type " + type.getName() + " not supported (id: " + id + ")");
		}
	}

	public void put(final String id, final Object value) {
		final Class<?> type = value.getClass();
		if (Map.class.isAssignableFrom(type) ||
			List.class.isAssignableFrom(type) ||
			String.class.isAssignableFrom(type)) {

			values.put(id, value);
			return;
		}

		values.put(id, value.toString());
	}
	
	
	private static Map<String, Object> readMap(final Node node) {
		final Map<String, Object> map = new HashMap<String, Object>();
		
		final NodeList children = node.getChildNodes();
		final int numChildren = children.getLength();
		for (int i = 0; i < numChildren; ++i) {
			final Node child = children.item(i);
			final String tag = child.getNodeName();
			final String id = getId(child);
			
			final Object value;
			if ("value".equals(tag)) {
				value = readValue(child);
			} else if ("list".equals(tag)) {
				value = readList(child);
			} else {
				value = readMap(child);
			}
			map.put(id, value);
		}

		return map;
	}

	private static List<Object> readList(final Node node) {
		final List<Object> list = new LinkedList<Object>();
		
		final NodeList children = node.getChildNodes();
		final int numChildren = children.getLength();
		for (int i = 0; i < numChildren; ++i) {
			final Node child = children.item(i);
			final String tag = child.getNodeName();
			
			final Object value;
			if ("value".equals(tag)) {
				value = readValue(child);
			} else if ("list".equals(tag)) {
				value = readList(child);
			} else {
				value = readMap(child);
			}
			list.add(value);
		}

		return list;
	}

	private static Object readValue(final Node node) {
		if (!node.hasChildNodes()) {
			return null;
		}
		return node.getFirstChild().getNodeValue();
	}

	
	@SuppressWarnings("unchecked")
	private static void writeMap(final Node node, final Map<String, Object> map) {
		final Document xml = node.getOwnerDocument();
		for (final Entry<String, Object> entry : map.entrySet()) {
			final Object value = entry.getValue();
			final Node child;
			if (value instanceof Map) {
				child = xml.createElement("map");
				writeMap(child, (Map<String, Object>) value);
			} else if (value instanceof List) {
				child = xml.createElement("list");
				writeList(child, (List<Object>) value);
			} else {
				child = xml.createElement("value");
				writeValue(child, value);
			}
			
			// set id
			final String id = entry.getKey();
			setId(child, id);

			node.appendChild(child);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void writeList(final Node node, final List<Object> list) {
		final Document xml = node.getOwnerDocument();
		for (final Object value : list) {
			final Node child;
			if (value instanceof Map) {
				child = xml.createElement("map");
				writeMap(child, (Map<String, Object>) value);
			} else if (value instanceof List) {
				child = xml.createElement("list");
				writeList(child, (List<Object>) value);
			} else {
				child = xml.createElement("value");
				writeValue(child, value);
			}
			
			node.appendChild(child);
		}
	}

	private static void writeValue(final Node node, final Object value) {
		final Document xml = node.getOwnerDocument();
		if (value != null) {
			final Node text =  xml.createTextNode(value.toString());
			node.appendChild(text);
		}
	}
	
	
	private static String getId(final Node node) {
		final NamedNodeMap attribs = node.getAttributes();
		if (attribs != null) {
			final Node idAttrib = attribs.getNamedItem("id");
			if (idAttrib != null) {
				return idAttrib.getNodeValue();
			}
		}
		return null;
	}
	
	private static void setId(final Node node, final String id) {
		final Document xml = node.getOwnerDocument();
		final Node idAttrib = xml.createAttribute("id");
		idAttrib.setNodeValue(id);

		final NamedNodeMap attribs = node.getAttributes();
		attribs.setNamedItem(idAttrib);
	}
}
