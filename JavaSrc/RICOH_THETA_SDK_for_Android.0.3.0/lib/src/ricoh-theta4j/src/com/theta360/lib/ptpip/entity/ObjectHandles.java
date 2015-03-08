package com.theta360.lib.ptpip.entity;

import com.theta360.lib.ptpip.util.BytesDecoder;

public class ObjectHandles {

	private final int HANDLE_DATA_LENGTH = 4;

	private byte[] sizePacket;
	private byte[] objectHandles;

	public ObjectHandles(byte[] dataPayload) {
		sizePacket = new byte[Response.RESPONSE_NUMBER_OF_ELEMENTS_LENGTH];
		objectHandles = new byte[dataPayload.length - Response.RESPONSE_NUMBER_OF_ELEMENTS_LENGTH];
		for (int i = 0, j = 0; i < dataPayload.length; i++) {
			if (i < Response.RESPONSE_NUMBER_OF_ELEMENTS_LENGTH) {
				sizePacket[i] = dataPayload[i];
			} else {
				objectHandles[j] = dataPayload[i];
				j++;
			}
		}
	}

	public int getObjectHandle(int index) {
		int handleIndex = index * HANDLE_DATA_LENGTH;
		byte[] data = new byte[HANDLE_DATA_LENGTH];
		for (int i = 0; i < HANDLE_DATA_LENGTH; i++) {
			data[i] = objectHandles[i + handleIndex];
		}
		int handle = BytesDecoder.decodeByteToInt(data);

		return (handle);
	}

	public int size() {
		return BytesDecoder.decodeByteToInt(sizePacket);
	}

}
