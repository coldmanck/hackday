package com.theta360.lib.ptpip.entity;

import com.theta360.lib.ptpip.util.BytesDecoder;

/**
 * Settings information of device.
 * 
 */
public class DeviceInfo {

	private int standardVersion;
	private int vendorExtensionId;
	private int vendorExtensionVersion;
	private String vendorExtensionDesc;
	private int functionalMode;
	private int[] operationsSupported;
	private int[] eventsSupported;
	private int[] devicePropertiesSupported;
	private int[] captureFormats;
	private int[] imageFormats;
	private String manufactuer;
	private String model;
	private String deviceVersion;
	private String serialNumber;

	public DeviceInfo(byte[] payload) {
		int offset = 0;
		this.standardVersion = BytesDecoder.decodeByteToShort(payload, offset);
		this.vendorExtensionId = BytesDecoder.decodeByteToInt(payload, offset += (Short.SIZE / Byte.SIZE));
		this.vendorExtensionVersion = BytesDecoder.decodeByteToShort(payload, offset += (Integer.SIZE / Byte.SIZE));
		this.vendorExtensionDesc = BytesDecoder.decodeByteToString(payload, offset += (Short.SIZE / Byte.SIZE));
		this.functionalMode = BytesDecoder.decodeByteToShort(payload, offset += BytesDecoder.loadOffsetAfterString(vendorExtensionDesc));
		this.operationsSupported = BytesDecoder.decodeByteToShortArray(payload, offset += (Short.SIZE / Byte.SIZE));
		this.eventsSupported = BytesDecoder.decodeByteToShortArray(payload, offset += BytesDecoder.loadOffsetAfterShortArray(operationsSupported));
		this.devicePropertiesSupported = BytesDecoder.decodeByteToShortArray(payload, offset += BytesDecoder.loadOffsetAfterShortArray(eventsSupported));
		this.captureFormats = BytesDecoder.decodeByteToShortArray(payload, offset += BytesDecoder.loadOffsetAfterShortArray(devicePropertiesSupported));
		this.imageFormats = BytesDecoder.decodeByteToShortArray(payload, offset += BytesDecoder.loadOffsetAfterShortArray(captureFormats));
		this.manufactuer = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterShortArray(imageFormats));
		this.model = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterString(manufactuer));
		this.deviceVersion = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterString(model));
		this.serialNumber = BytesDecoder.decodeByteToString(payload, offset += BytesDecoder.loadOffsetAfterString(deviceVersion));
	}

	public int getStandardVersion() {
		return standardVersion;
	}

	public int getVendorExtensionId() {
		return vendorExtensionId;
	}

	public int getVendorExtensionVersion() {
		return vendorExtensionVersion;
	}

	public String getVendorExtensionDesc() {
		return vendorExtensionDesc;
	}

	public int getFunctionalMode() {
		return functionalMode;
	}

	public int[] getOperationsSupported() {
		return operationsSupported;
	}

	public int[] getEventsSupported() {
		return eventsSupported;
	}

	public int[] getDevicePropertiesSupported() {
		return devicePropertiesSupported;
	}

	public int[] getCaptureFormats() {
		return captureFormats;
	}

	public int[] getImageFormats() {
		return imageFormats;
	}

	public String getManufactuer() {
		return manufactuer;
	}

	public String getModel() {
		return model;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
}
