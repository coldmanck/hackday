package com.theta360.lib.ptpip.entity;

import com.theta360.lib.ptpip.util.BytesDecoder;

public class Response extends CommunicationBase {

	public static final int RESPONSE_PACKET_HEADER_LENGTH = 8;
	public static final int RESPONSE_NUMBER_OF_ELEMENTS_LENGTH = 4;

	public static final int RESPONSE_TYPE_INIT_COMMAND_ACK = 2;
	public static final int RESPONSE_TYPE_INIT_FAIL = 5;
	public static final int RESPONSE_TYPE_CMD_RESPONSE = 7;
	public static final int RESPONSE_TYPE_START_DATA_PACKET = 9;
	public static final int RESPONSE_TYPE_DATA_PACKET = 10;
	public static final int RESPONSE_TYPE_END_DATA_PACKET = 12;

	public static final int RESPONSE_CODE_OK = 0x2001;
	public static final int RESPONSE_CODE_STORE_FULL = 0x200C;
	public static final int RESPONSE_CODE_DEVICE_BUSY = 0x2019;
	public static final int RESPONSE_CODE_INVALID_DEVICEPROP_VALUE = 0x201C;
	public static final int RESPONSE_CODE_COMMAND_EXCECUTING = 0xA001;

	public Response(byte[] resHeader) {
		this.length = loadPacketLength(resHeader);
		this.packetType = loadPacketType(resHeader);
	}

	private static int loadPacketType(byte[] resHeader) {
		byte[] type = new byte[Integer.SIZE / Byte.SIZE];
		int offset = Integer.SIZE / Byte.SIZE;
		int j = 0;
		for (int i = offset; i < offset + (Integer.SIZE / Byte.SIZE); i++) {
			type[j++] = resHeader[i];
		}
		int res = BytesDecoder.decodeByteToInt(type);
		return res;
	}

	private static int loadPacketLength(byte[] resHeader) {
		byte[] packetLength = new byte[Integer.SIZE / Byte.SIZE];
		int j = 0;
		for (int i = 0; i < packetLength.length; i++) {
			packetLength[j++] = resHeader[i];
		}
		int res = BytesDecoder.decodeByteToInt(packetLength);
		return res;
	}

}
