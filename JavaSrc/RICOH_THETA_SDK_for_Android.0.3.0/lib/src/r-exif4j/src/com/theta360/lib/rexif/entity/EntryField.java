package com.theta360.lib.rexif.entity;

/**
 * Entry field
 */
public class EntryField {

	private int tag;
	private int type;
	private int count;
	private byte[] offset;
	private byte[] value;

	public long getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public byte[] getOffset() {
		return offset;
	}

	public void setOffset(byte[] offset) {
		this.offset = offset;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

}