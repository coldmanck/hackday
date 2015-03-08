package com.theta360.lib.ptpip.entity;

public class PtpObject {

	private byte[] dataObject;

	public PtpObject(byte[] payload) {
		this.dataObject = payload;
	}

	public byte[] getDataObject() {
		return dataObject;
	}
}
