package com.theta360.lib.ptpip.entity;

import java.nio.ByteOrder;
import java.util.ArrayList;

import com.theta360.lib.ThetaException;
import com.theta360.lib.ptpip.util.BytesDecoder;

/**
 * Defines the settings information of the device.
 * 
 */
public class DevicePropertyDesc {

	private static final int FIELDSIZE_DEVICE_PROPERTY_CODE = 2;
	private static final int FIELDSIZE_DATA_TYPE = 2;
	private static final int FIELDSIZE_GET_SET = 1;
	private static final int FORMFLAG_GET_SET = 1;

	private static final int NEXT_FIELD_RANGE = 0x01;
	private static final int NEXT_FIELD_ENUMERATION = 0x02;

	protected short devicePropertyCode;
	protected DataType dataType;
	protected byte getSet;
	protected Value defaultValue;
	protected String defaultString;
	protected Value currentValue;
	protected String currentString;
	protected byte formFlag;

	// Range Form
	protected Value minimumValue = null;
	protected Value maximumValue = null;
	protected Value stepSize = null;

	// Enumeration Form
	protected short numberOfValues;
	protected Value[] supportedValue = null;

	public DevicePropertyDesc(byte[] dataPayload) {
		int offset = 0;

		devicePropertyCode = BytesDecoder.decodeByteToShort(readFieldData(dataPayload, FIELDSIZE_DEVICE_PROPERTY_CODE, offset));
		offset += FIELDSIZE_DEVICE_PROPERTY_CODE;

		dataType = new DataType(BytesDecoder.decodeByteToShort(readFieldData(dataPayload, FIELDSIZE_DATA_TYPE, offset)));
		offset += FIELDSIZE_DATA_TYPE;

		getSet = readFieldData(dataPayload, FIELDSIZE_GET_SET, offset)[0];
		offset += FIELDSIZE_GET_SET;

		if (DataType.STR == dataType.type()) {
			defaultString = BytesDecoder.decodeByteToString(dataPayload, offset);
			offset += BytesDecoder.loadOffsetAfterString(defaultString);

			currentString = BytesDecoder.decodeByteToString(dataPayload, offset);
			offset += BytesDecoder.loadOffsetAfterString(currentString);
		} else {
			defaultValue = new Value(readFieldData(dataPayload, dataType.size(), offset), dataType);
			offset += dataType.size();

			currentValue = new Value(readFieldData(dataPayload, dataType.size(), offset), dataType);
			offset += dataType.size();
		}

		formFlag = readFieldData(dataPayload, FORMFLAG_GET_SET, offset)[0];
		offset += FORMFLAG_GET_SET;

		if (NEXT_FIELD_RANGE == formFlag) {
			minimumValue = new Value(readFieldData(dataPayload, dataType.size(), offset), dataType);
			offset += dataType.size();

			maximumValue = new Value(readFieldData(dataPayload, dataType.size(), offset), dataType);
			offset += dataType.size();

			stepSize = new Value(readFieldData(dataPayload, dataType.size(), offset), dataType);
			offset += dataType.size();
		} else if (NEXT_FIELD_ENUMERATION == formFlag) {
			numberOfValues = BytesDecoder.decodeByteToShort(readFieldData(dataPayload, FIELDSIZE_DEVICE_PROPERTY_CODE, offset));
			offset += FIELDSIZE_DEVICE_PROPERTY_CODE;

			supportedValue = new Value[numberOfValues];
			for (int i = 0; i < numberOfValues; i++) {
				supportedValue[i] = new Value(readFieldData(dataPayload, dataType.size(), offset), dataType);
				offset += dataType.size();
			}
		}
	}

	private byte[] readFieldData(byte[] dataArray, int size, int offset) {
		byte[] ret = new byte[size];

		for (int i = 0; i < size; i++) {
			ret[i] = dataArray[i + offset];
		}
		return ret;
	}

	public short getDevicePropertyCode() {
		return devicePropertyCode;
	}

	public short getDataType() {
		return dataType.type();
	}

	/**
	 * Acquires access permission for the data. <br>
	 * 0x00 : Get (read only)<br>
	 * 0x01 : Get / Set (read - write)
	 * 
	 * @return Access permission
	 */
	public byte getGetSet() {
		return getSet;
	}

	/**
	 * Acquires factory default value. <br>
	 * The data type is the type specified in DataType.
	 * 
	 * @return Factory default value
	 * @throws ThetaException
	 */
	public long getDefaultValue() throws ThetaException {
		if (DataType.STR == dataType.type()) {
			throw new ThetaException("data type is string");
		}
		return defaultValue.getData();
	}

	/**
	 * Acquires factory default value. <br>
	 * A Theta Exception is sent if the data type is not String.
	 * 
	 * @return Factory default value
	 * @throws ThetaException
	 */
	public String getDefaultString() throws ThetaException {
		if (DataType.STR != dataType.type()) {
			throw new ThetaException("data type is not string");
		}
		return defaultString;
	}

	public long getCurrentValue() throws ThetaException {
		if (DataType.STR == dataType.type()) {
			throw new ThetaException("data type is string");
		}
		return currentValue.getData();
	}

	public String getCurrentString() throws ThetaException {
		if (DataType.STR != dataType.type()) {
			throw new ThetaException("data type is not string");
		}
		return currentString;
	}

	/**
	 * Acquires the following data format types. <br>
	 * 0x00 : None (no data)<br>
	 * 0x01 : Range Form<br>
	 * 0x02 : Enumeration Form
	 * 
	 * @return Data format type
	 */
	public byte getFormFlag() {
		return formFlag;
	}

	public long getMinimumValue() throws ThetaException {
		return minimumValue.getData();
	}

	public long getMaximumValue() throws ThetaException {
		return maximumValue.getData();
	}

	public long getStepSize() throws ThetaException {
		return stepSize.getData();
	}

	public short getNumberOfValues() {
		return numberOfValues;
	}

	public ArrayList<Long> getAllSupportedValue() throws ThetaException {
		ArrayList<Long> list = new ArrayList<Long>();
		for (Value value : supportedValue) {
			list.add(value.getData());
		}
		return list;
	}

	public long getSupportedValue(int index) throws ThetaException {
		if (index > numberOfValues) {
			throw new ThetaException("index is too large [" + index + " > " + numberOfValues + "]");
		}
		return supportedValue[index].getData();
	}

	private class Value {

		private short type;
		private byte[] data;

		public Value(byte[] dataPayload, DataType dataType) {
			type = dataType.type();
			data = dataPayload;
		}

		public long getData() throws ThetaException {
			long ret = 0;
			if (DataType.INT8 == type) {
				ret = BytesDecoder.decodeBytesToSignedData(data);
			} else if (DataType.UINT8 == type) {
				ret = BytesDecoder.decodeByteToLong(data, ByteOrder.LITTLE_ENDIAN);
			} else if (DataType.INT16 == type) {
				ret = BytesDecoder.decodeBytesToSignedData(data);
			} else if (DataType.UINT16 == type) {
				ret = BytesDecoder.decodeByteToLong(data, ByteOrder.LITTLE_ENDIAN);
			} else if (DataType.INT32 == type) {
				ret = BytesDecoder.decodeBytesToSignedData(data);
			} else if (DataType.UINT32 == type) {
				ret = BytesDecoder.decodeByteToLong(data, ByteOrder.LITTLE_ENDIAN);
			} else if (DataType.INT64 == type) {
				ret = BytesDecoder.decodeBytesToSignedData(data);
			} else if (DataType.UINT64 == type) {
				ret = BytesDecoder.decodeByteToLong(data, ByteOrder.BIG_ENDIAN);
			} else if (DataType.INT128 == type) {
				throw new ThetaException("data type is not support");
			} else if (DataType.UINT128 == type) {
				throw new ThetaException("data type is not support");
			}
			return ret;
		}
	}
}
