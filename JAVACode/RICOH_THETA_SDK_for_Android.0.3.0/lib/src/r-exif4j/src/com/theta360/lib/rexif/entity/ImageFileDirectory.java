package com.theta360.lib.rexif.entity;

import java.util.List;

/**
 * IDF structure
 */
public class ImageFileDirectory {

	/**
	 * EXIF
	 */
	public static final int ENTRY_FIELD_TAG_EXIF = 0x8769;
	/**
	 * Manufacturer notes
	 */
	public static final int ENTRY_FIELD_TAG_MAKERNOTE = 0x927C;
	/**
	 * Spherical image information
	 */
	public static final int ENTRY_FIELD_TAG_SPHERE_INFO = 0x4001;
	/**
	 * Slant
	 */
	public static final int ENTRY_FIELD_TAG_INCLINATION_INFO = 0x0003;
	/**
	 * Orientation
	 */
	public static final int ENTRY_FIELD_TAG_COMPASS_INFO = 0x0004;
	/**
	 * Thumbnail SOI
	 */
	public static final int ENTRY_FIELD_TAG_THUMB_SOI = 0x0201;
	/**
	 * Thumbnail size
	 */
	public static final int ENTRY_FIELD_TAG_THUMB_SIZE = 0x0202;

	private int fieldCount;
	private List<EntryField> entryFieldList;
	private byte[] nextOffset;

	/**
	 * Creates IFD structure
	 * 
	 * @param arrayLength
	 *            Field count
	 */
	public ImageFileDirectory(int arrayLength) {
		this.fieldCount = arrayLength;
	}

	public long getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public List<EntryField> getEntryFieldList() {
		return entryFieldList;
	}

	public void setEntryFieldList(List<EntryField> entryFieldList) {
		this.entryFieldList = entryFieldList;
	}

	public byte[] getNextOffset() {
		return nextOffset;
	}

	public void setNextOffset(byte[] nextOffset) {
		this.nextOffset = nextOffset;
	}

	/**
	 * Acquires target entry field
	 * 
	 * @param tag
	 *            tag
	 * @return Entry field
	 */
	public EntryField getEntryField(int tag) {
		for (EntryField entryField : entryFieldList) {
			if (tag == entryField.getTag()) {
				return entryField;
			}
		}
		return null;
	}
}
