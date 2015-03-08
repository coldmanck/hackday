package com.theta360.lib.ptpip.entity;

import com.theta360.lib.ptpip.util.BytesDecoder;

public class StorageIds {
	private final int STORAGE_ID_LENGTH = 4;
	private final int ID_DATA_LENGTH = 2;
	private final int SHIFT_SIZE = 65536;

	private byte[] sizePacket;
	private byte[] storageIds;

	public StorageIds(byte[] dataPayload) {
		sizePacket = new byte[Response.RESPONSE_NUMBER_OF_ELEMENTS_LENGTH];
		storageIds = new byte[dataPayload.length - Response.RESPONSE_NUMBER_OF_ELEMENTS_LENGTH];
		for (int i = 0, j = 0; i < dataPayload.length; i++) {
			if (i < Response.RESPONSE_NUMBER_OF_ELEMENTS_LENGTH) {
				sizePacket[i] = dataPayload[i];
			} else {
				storageIds[j] = dataPayload[i];
				j++;
			}
		}
	}

	public int getStorageId(int index) {
		int storageIndex = index * STORAGE_ID_LENGTH;
		byte[] pData = new byte[ID_DATA_LENGTH];
		byte[] lData = new byte[ID_DATA_LENGTH];
		for (int i = 0; i < ID_DATA_LENGTH; i++) {
			pData[i] = storageIds[i + storageIndex];
			lData[i] = storageIds[i + storageIndex + ID_DATA_LENGTH];
		}
		int pId = BytesDecoder.decodeByteToInt(pData);
		int lId = BytesDecoder.decodeByteToInt(lData);
		int storageId = (lId * SHIFT_SIZE + pId);
		return storageId;
	}

	/**
	 * Acquires the number of elements of the storage ID.
	 * 
	 * @return Number of elements of storage ID
	 */
	public int size() {
		return BytesDecoder.decodeByteToInt(sizePacket);
	}

}
