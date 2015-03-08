package com.theta360.lib.ptpip.entity;

public class DataType {

	public static final short UNDEF = 0x0000;
	public static final short INT8 = 0x0001;
	public static final short UINT8 = 0x0002;
	public static final short INT16 = 0x0003;
	public static final short UINT16 = 0x0004;
	public static final short INT32 = 0x0005;
	public static final short UINT32 = 0x0006;
	public static final short INT64 = 0x0007;
	public static final short UINT64 = 0x0008;
	public static final short INT128 = 0x0009;
	public static final short UINT128 = 0x000A;
	public static final short AINT8 = 0x4001;
	public static final short AUINT8 = 0x4002;
	public static final short AINT16 = 0x4003;
	public static final short AUINT16 = 0x4004;
	public static final short AINT32 = 0x4005;
	public static final short AUINT32 = 0x4006;
	public static final short AINT64 = 0x4007;
	public static final short AUINT64 = 0x4008;
	public static final short AINT128 = 0x4009;
	public static final short AUINT128 = 0x400A;
	public static final short STR = (short) 0xFFFF;

	public static final short INT8_SIZE = 1;
	public static final short INT16_SIZE = 2;
	public static final short INT32_SIZE = 4;
	public static final short INT64_SIZE = 8;
	public static final short INT128_SIZE = 16;

	private short size = 0;
	private short type = 0;

	public DataType(short dataType) {
		if (dataType == INT8) {
			size = INT8_SIZE;
			type = INT8;
		} else if (dataType == UINT8) {
			size = INT8_SIZE;
			type = UINT8;
		} else if (dataType == INT16) {
			size = INT16_SIZE;
			type = INT16;
		} else if (dataType == UINT16) {
			size = INT16_SIZE;
			type = UINT16;
		} else if (dataType == INT32) {
			size = INT32_SIZE;
			type = INT32;
		} else if (dataType == UINT32) {
			size = INT32_SIZE;
			type = UINT32;
		} else if (dataType == INT64) {
			size = INT64_SIZE;
			type = INT64;
		} else if (dataType == UINT64) {
			size = INT64_SIZE;
			type = UINT64;
		} else if (dataType == INT128) {
			size = INT128_SIZE;
			type = INT128;
		} else if (dataType == UINT128) {
			size = INT128_SIZE;
			type = UINT128;
		} else if (dataType == STR) {
			type = STR;
		}
	}

	public short size() {
		return size;
	}

	public short type() {
		return type;
	}

}
