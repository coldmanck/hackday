package com.theta360.lib.ptpip.entity;

import java.nio.ByteOrder;

import com.theta360.lib.ptpip.util.BytesDecoder;

public class StorageInfo {

	private int storageType;

	private int fileSystemType;

	private int accessCapability;

	private long maxCapacity;

	private long freeSpaceInBytes;

	private int freeSpaceInImages;

	private String storageDescription;

	private String volumeLabel;

	public StorageInfo(byte[] payload) {
		int offset = 0;
		this.storageType = BytesDecoder.decodeByteToShort(payload, offset);
		this.fileSystemType = BytesDecoder.decodeByteToShort(payload, offset += (Short.SIZE / Byte.SIZE));
		this.accessCapability = BytesDecoder.decodeByteToShort(payload, offset += (Short.SIZE / Byte.SIZE));
		this.maxCapacity = BytesDecoder.decodeByteToLong(payload, offset += (Short.SIZE / Byte.SIZE), ByteOrder.LITTLE_ENDIAN);
		this.freeSpaceInBytes = BytesDecoder.decodeByteToLong(payload, offset += (Long.SIZE / Byte.SIZE), ByteOrder.LITTLE_ENDIAN);
		this.freeSpaceInImages = BytesDecoder.decodeByteToInt(payload, offset += (Long.SIZE / Byte.SIZE));
		this.storageDescription = BytesDecoder.decodeByteToString(payload, offset += (Integer.SIZE / Byte.SIZE));
		this.volumeLabel = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterString(storageDescription));
	}

	public int getStorageType() {
		return storageType;
	}

	public int getFileSystemType() {
		return fileSystemType;
	}

	public int getAccessCapability() {
		return accessCapability;
	}

	public long getMaxCapacity() {
		return maxCapacity;
	}

	public long getFreeSpaceInBytes() {
		return freeSpaceInBytes;
	}

	public int getFreeSpaceInImages() {
		return freeSpaceInImages;
	}

	public String getStorageDescription() {
		return storageDescription;
	}

	public String getVolumeLabel() {
		return volumeLabel;
	}

}
