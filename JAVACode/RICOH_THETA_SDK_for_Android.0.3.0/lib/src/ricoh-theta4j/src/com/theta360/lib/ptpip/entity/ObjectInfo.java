package com.theta360.lib.ptpip.entity;

import com.theta360.lib.ptpip.util.BytesDecoder;

public class ObjectInfo {

	public static final short OBJECT_FORMAT_CODE_EXIF_JPEG = 0x3801;

	private int storageID;
	private int objectFormat;
	private int protectionStatus;
	private int objectCompressedSize;
	private int thumbFormat;
	private int thumbCompressedSize;
	private int thumbPixWidth;
	private int thumbPixHeight;
	private int imagePixWidth;
	private int imagePixHeight;
	private int imageBitDepth;
	private int parentObject;
	private int associationType;
	private int associationDesc;
	private int sequenceNumber;
	private String filename = null;
	private String captureDate = null;
	private String modificationDate = null;
	private String keywords = null;

	public ObjectInfo(byte[] payload) {
		int offset = 0;
		storageID = BytesDecoder.decodeByteToInt(payload, offset);
		objectFormat = BytesDecoder.decodeByteToShort(payload, offset += (Integer.SIZE / Byte.SIZE));
		protectionStatus = BytesDecoder.decodeByteToShort(payload, offset += (Short.SIZE / Byte.SIZE));
		objectCompressedSize = BytesDecoder.decodeByteToInt(payload, offset += (Short.SIZE / Byte.SIZE));
		thumbFormat = BytesDecoder.decodeByteToShort(payload, offset += (Integer.SIZE / Byte.SIZE));
		thumbCompressedSize = BytesDecoder.decodeByteToInt(payload, offset += (Short.SIZE / Byte.SIZE));
		thumbPixWidth = BytesDecoder.decodeByteToInt(payload, offset += (Integer.SIZE / Byte.SIZE));
		thumbPixHeight = BytesDecoder.decodeByteToInt(payload, offset += (Integer.SIZE / Byte.SIZE));
		imagePixWidth = BytesDecoder.decodeByteToInt(payload, offset += (Integer.SIZE / Byte.SIZE));
		imagePixHeight = BytesDecoder.decodeByteToInt(payload, offset += (Integer.SIZE / Byte.SIZE));
		imageBitDepth = BytesDecoder.decodeByteToInt(payload, offset += (Integer.SIZE / Byte.SIZE));
		parentObject = BytesDecoder.decodeByteToInt(payload, offset += (Integer.SIZE / Byte.SIZE));
		associationType = BytesDecoder.decodeByteToShort(payload, offset += (Integer.SIZE / Byte.SIZE));
		associationDesc = BytesDecoder.decodeByteToInt(payload, offset += (Short.SIZE / Byte.SIZE));
		sequenceNumber = BytesDecoder.decodeByteToInt(payload, offset += (Integer.SIZE / Byte.SIZE));
		filename = BytesDecoder.decodeByteToString(payload, offset += (Integer.SIZE / Byte.SIZE));
		captureDate = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterString(filename));
		modificationDate = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterString(captureDate));
		keywords = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterString(modificationDate));
	}

	public int getStorageID() {
		return storageID;
	}

	public int getObjectFormat() {
		return objectFormat;
	}

	public int getProtectionStatus() {
		return protectionStatus;
	}

	public int getObjectCompressedSize() {
		return objectCompressedSize;
	}

	public int getThumbFormat() {
		return thumbFormat;
	}

	public int getThumbCompressedSize() {
		return thumbCompressedSize;
	}

	public int getThumbPixWidth() {
		return thumbPixWidth;
	}

	public int getThumbPixHeight() {
		return thumbPixHeight;
	}

	public int getImagePixWidth() {
		return imagePixWidth;
	}

	public int getImagePixHeight() {
		return imagePixHeight;
	}

	public int getImageBitDepth() {
		return imageBitDepth;
	}

	public int getParentObject() {
		return parentObject;
	}

	public int getAssociationType() {
		return associationType;
	}

	public int getAssociationDesc() {
		return associationDesc;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public String getFilename() {
		return filename;
	}

	public String getCaptureDate() {
		return captureDate;
	}

	public String getModificationDate() {
		return modificationDate;
	}

	public String getKeywords() {
		return keywords;
	}

}
