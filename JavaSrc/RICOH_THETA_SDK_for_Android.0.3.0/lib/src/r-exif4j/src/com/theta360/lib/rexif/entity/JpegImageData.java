package com.theta360.lib.rexif.entity;

/**
 * Image information
 */
public class JpegImageData {
	private byte[] data;
	private int size;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
