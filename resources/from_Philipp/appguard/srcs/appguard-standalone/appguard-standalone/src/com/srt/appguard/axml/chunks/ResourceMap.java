package com.srt.appguard.axml.chunks;

import java.io.IOException;

import com.srt.appguard.axml.chunks.StringPool.StringRef;
import com.srt.appguard.axml.io.AXmlInputStream;
import com.srt.appguard.axml.io.AXmlOutputStream;

/**
 * Resource map of a serialized XML file.
 * 
 * @author Sven Obser <sven.obser@backes-srt.de>
 */
public class ResourceMap extends Chunk {

	private StringPool stringPool;

	/**
	 * Read resource map from the given stream.
	 * 
	 * @param stream
	 *            Stream to read XML from.
	 * @throws IOException
	 *             In case of an I/O error.
	 */
	protected ResourceMap(AXmlInputStream stream, XmlDocument document) throws IOException {
		super(Type.RES_XML_RESOURCE_MAP_TYPE, stream);
		this.stringPool = document.getStringPool();

		int numIds = (super.getOriginalSize() - super.getHeaderSize()) / 4;
		for (int i = 0; i < numIds; i++) {
			int resId = stream.readUint32();
			stringPool.updateResId(i, resId);
		}
	}

	@Override
	public int getSize() {
		int size = super.getSize();
		for (StringRef s : stringPool.getStrings()) {
			if (s.getResId() != -1) {
				size += 4;
			}
		}
		return size;
	}

	@Override
	public void write(AXmlOutputStream outStream) throws IOException {
		super.write(outStream);
		for (StringRef s : stringPool.getStrings()) {
			int resId = s.getResId();
			if (resId != -1) {
				outStream.writeUint32(resId);
			}
		}
	}
}