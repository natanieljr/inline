package com.srt.appguard.axml.chunks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * String pool of a serialized XML file.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class StringPool extends Chunk {

	private static final int SORTED_FLAG = 1;
	private static final int UTF8_FLAG = 1 << 8;

	private static final Comparator<? super StringRef> STRING_REF_COMPARATOR = new Comparator<StringRef>() {
		@Override
		public int compare(StringRef lhs, StringRef rhs) {
			int lResId = lhs.getResId();
			int rResId = rhs.getResId();
			if (lResId == -1 && rResId != -1) {
				return 1;
			} else if (rResId == -1 && lResId != -1) {
				return -1;
			} else {
				return 0;
			}
		}
	};

	private static final Comparator<? super StringRef> STRING_REF_COMPARATOR_SORTED = new Comparator<StringRef>() {
		@Override
		public int compare(StringRef lhs, StringRef rhs) {
			int resCompare = STRING_REF_COMPARATOR.compare(lhs, rhs);
			if (resCompare != 0) {
				return resCompare;
			} else {
				return lhs.getValue().compareTo(rhs.getValue());
			}
		}
	};

	private final int flags;

	private final List<StringRef> strings;
	private final List<StyleData> styles;

	/**
	 * Read string pool from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected StringPool(AXmlInputStream stream) throws IOException {
		super(Type.RES_STRING_POOL_TYPE, stream);

		int stringCount = stream.readUint32();
		int styleCount = stream.readUint32();
		flags = stream.readUint32();
		int stringStart = stream.readUint32(); // stringsStart
		int styleStart = stream.readUint32(); // stylesStart

		int[] offsets = new int[stringCount];
		for (int i = 0; i < stringCount; i++) {
			offsets[i] = stream.readUint32(); // offset
		}

		stream.moveTo(getStartOffset() + stringStart);
		Map<Integer, String> stringsByOffset = new HashMap<Integer, String>();
		strings = new ArrayList<StringRef>(stringCount);
		for (int i = 0; i < stringCount; i++) {
			int offset = offsets[i];
			String stringValue = stringsByOffset.get(offset);
			if (stringValue == null) {
				stringValue = stream.readString(isUtf8());
				stringsByOffset.put(offset, stringValue);
			}
			strings.add(new StringRef(this, stringValue, -1));
		}

		stream.moveTo(getStartOffset() + styleStart);
		styles = new ArrayList<StyleData>(styleCount);
		for (int i = 0; i < styleCount; i++) {
			StyleData styleData = new StyleData(stream);
			styles.add(styleData);
		}

		stream.align();
	}

	/**
	 * Update the resource ID of a string reference.
	 * 
	 * @param index
	 *            The index of the string reference.
	 * @param resId
	 *            The new resource ID for the string reference.
	 */
	void updateResId(int index, int resId) {
		strings.get(index).resId = resId;
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
		if (index == -1) {
			return null;
		}
		return strings.get(index);
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
		for (StringRef s : strings) {
			if (s.getValue().equals(name)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Get the {@link StringRef} with the given resource ID.
	 *
	 * @param resId
	 *            The resource ID of the string reference.
	 * @return The string reference with the given values or <code>null</code>
	 *         if none exists.
	 */
	StringRef getStringRefByResId(int resId) {
		for (StringRef s : strings) {
			if (s.resId == resId) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Get the {@link StringRef} with the given name and resource ID.
	 * 
	 * @param name
	 *            The name of the string reference.
	 * @param resId
	 *            The resource ID of the string reference.
	 * @return The string reference with the given values or <code>null</code>
	 *         if none exists.
	 */
	private StringRef getStringRef(String name, int resId) {
		for (StringRef s : strings) {
			if (s.resId == resId && s.getValue().equals(name)) {
				return s;
			}
		}

		if (resId != -1) {
			return getStringRefByResId(resId);
		}
		return null;
	}

	/**
	 * Get the list of all currently registered string.
	 * 
	 * @return The list of all registered string.
	 */
	List<StringRef> getStrings() {
		return strings;
	}

	/**
	 * Get the index of the given string reference.
	 * 
	 * @param stringRef
	 *            The string reference.
	 * @return The index of the given string reference or <code>-1</code> if it
	 *         is not registered.
	 */
	int getIndex(StringRef stringRef) {
		return strings.indexOf(stringRef);
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
	StringRef register(String name, int resId) {
		StringRef s = getStringRef(name, resId);
		if (s == null) {
			StringRef newString = new StringRef(this, name, resId);
			addString(newString);
			s = newString;
		}
		return s;
	}

	/**
	 * Add a new string to the internal list and sort the list afterwards.
	 * 
	 * @param name
	 *            The string to add.
	 */
	private void addString(StringRef name) {
		strings.add(name);
		if (isSorted()) {
			Collections.sort(strings, STRING_REF_COMPARATOR_SORTED);
		} else {
			Collections.sort(strings, STRING_REF_COMPARATOR);
		}
	}

	@Override
	protected int getSize() {
		int size = getHeaderPlusStringOffsetSize();
		size += getStringsSize();
		size += getStyleSize();
		size += size % 4;
		return size;
	}

	private int getHeaderPlusStringOffsetSize() {
		return super.getSize() + 20 + (strings.size() * 4);
	}

	private int getStringsSize() {
		int size = 0;
		for (StringRef s : strings) {
			size += s.getSize(isUtf8());
		}
		return size;
	}

	private int getStyleSize() {
		int size = 0;
		for (StyleData s : styles) {
			size += s.getSize();
		}
		return size;
	}

	@Override
	protected void write(AXmlOutputStream outStream) throws IOException {
		int stringsStart = getHeaderPlusStringOffsetSize();
		int stylesStart = (styles.size() != 0) ? stringsStart + getStringsSize() : 0;

		super.write(outStream);
		outStream.writeUint32(strings.size());
		outStream.writeUint32(styles.size());
		outStream.writeUint32(flags);
		outStream.writeUint32(stringsStart);
		outStream.writeUint32(stylesStart);

		int offset = 0;
		for (StringRef s : strings) {
			outStream.writeUint32(offset);
			offset += s.getSize(isUtf8());
		}

		for (StringRef s : strings) {
			s.write(outStream, isUtf8());
		}

		for (StyleData s : styles) {
			s.write(outStream);
		}

		outStream.align();
	}

	private boolean isSorted() {
		return (flags & SORTED_FLAG) != 0;
	}

	private boolean isUtf8() {
		return (flags & UTF8_FLAG) != 0;
	}

	@Override
	public String toString() {
		return strings.toString() + "\n" + styles.toString();
	}

	static class StringRef {

		private final StringPool stringPool;
		private String value;
		private int resId;

		public StringRef(StringPool stringPool, String value, int resId) {
			this.stringPool = stringPool;
			this.value = value;
			this.resId = resId;
		}

		private int getSize(boolean utf8) {
			int size = 2;
			if (value.length() > 32767) {
				size += 2;
			}
			size += value.length() * (utf8 ? 1 : 2);
			size += 2;
			return size;
		}

		String getValue() {
			return value;
		}

		private void write(AXmlOutputStream outStream, boolean utf8) throws IOException {
			outStream.writeString(value, utf8);
		}

		int getResId() {
			return resId;
		}

		int getIndex() {
			return stringPool.getIndex(this);
		}

		@Override
		public String toString() {
			return value;
		}
	}

	static class StyleData {

		private final int stringRef;
		private final int start;
		private final int end;

		public StyleData(AXmlInputStream stream) throws IOException {
			stringRef = stream.readUint32();
			if (stringRef != 0xFFFFFFFF) {
				start = stream.readUint32();
				end = stream.readUint32();
			} else {
				start = 0;
				end = 0;
			}
		}

		public int getSize() {
			if (stringRef != 0xFFFFFFFF) {
				return 12;
			} else {
				return 4;
			}
		}

		public void write(AXmlOutputStream outStream) throws IOException {
			outStream.writeUint32(stringRef);
			if (stringRef != 0xFFFFFFFF) {
				outStream.writeUint32(start);
				outStream.writeUint32(end);
			}
		}
	}
}
