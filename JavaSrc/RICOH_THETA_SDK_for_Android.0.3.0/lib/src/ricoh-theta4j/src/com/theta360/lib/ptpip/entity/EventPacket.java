package com.theta360.lib.ptpip.entity;

import com.theta360.lib.ThetaException;
import com.theta360.lib.ptpip.util.BytesDecoder;

public class EventPacket {
	public static final int SHORT_SIZE = Short.SIZE / Byte.SIZE;
	public static final int INT_SIZE = Integer.SIZE / Byte.SIZE;

	public static final int EVENT_OBJECT_ADDED = 0x4002;
	public static final int EVENT_DEVICE_PROP_CHANGED = 0x4006;
	public static final int EVENT_STORE_FULL = 0x400A;
	public static final int EVENT_CAPTURE_COMPLETE = 0x400D;

	private static final int EVENT_PACKET_HEADER_LENGTH_OFFSET = INT_SIZE;
	private static final int EVENT_PACKET_HEADER_TYPE_OFFSET = EVENT_PACKET_HEADER_LENGTH_OFFSET + INT_SIZE;
	private static final int EVENT_PACKET_EVENT_CODE_OFFSET = EVENT_PACKET_HEADER_TYPE_OFFSET + SHORT_SIZE;
	private static final int EVENT_PACKET_TRANSACTION_ID_OFFSET = EVENT_PACKET_EVENT_CODE_OFFSET + INT_SIZE;
	private static final int EVENT_PACKET_PARAMETER1_OFFSET = EVENT_PACKET_TRANSACTION_ID_OFFSET + INT_SIZE;
	private static final int EVENT_PACKET_PARAMETER2_OFFSET = EVENT_PACKET_PARAMETER1_OFFSET + INT_SIZE;
	private static final int EVENT_PACKET_PARAMETER3_OFFSET = EVENT_PACKET_PARAMETER2_OFFSET + INT_SIZE;

	public static final int LENGTH = EVENT_PACKET_PARAMETER3_OFFSET;

	public EventPacket(byte[] eventPacket) throws ThetaException {
		int len = eventPacket.length;
		byte[] byteLen = new byte[INT_SIZE];
		for (int i = 0; i < EVENT_PACKET_HEADER_LENGTH_OFFSET; i++) {
			byteLen[i] = eventPacket[i];
		}
		int lenParam = BytesDecoder.decodeByteToInt(byteLen);
		if (len != lenParam) {
			throw new ThetaException("Packet Length is mismatch:" + len + "/" + lenParam);
		}
		this.length = lenParam;

		byte[] bytePacTyp = new byte[INT_SIZE];
		int j = 0;
		for (int i = EVENT_PACKET_HEADER_LENGTH_OFFSET; i < EVENT_PACKET_HEADER_TYPE_OFFSET; i++) {
			bytePacTyp[j++] = eventPacket[i];
		}
		this.packetType = BytesDecoder.decodeByteToInt(bytePacTyp);

		byte[] byteEvnCode = new byte[SHORT_SIZE];
		j = 0;
		for (int i = EVENT_PACKET_HEADER_TYPE_OFFSET; i < EVENT_PACKET_EVENT_CODE_OFFSET; i++) {
			byteEvnCode[j++] = eventPacket[i];
		}
		this.eventCode = BytesDecoder.decodeByteToShort(byteEvnCode);

		byte[] byteTrnId = new byte[INT_SIZE];
		j = 0;
		for (int i = EVENT_PACKET_EVENT_CODE_OFFSET; i < EVENT_PACKET_TRANSACTION_ID_OFFSET; i++) {
			byteTrnId[j++] = eventPacket[i];
		}
		this.transactionId = BytesDecoder.decodeByteToInt(byteTrnId);

		byte[] byteParam1 = new byte[INT_SIZE];
		j = 0;
		for (int i = EVENT_PACKET_TRANSACTION_ID_OFFSET; i < EVENT_PACKET_PARAMETER1_OFFSET; i++) {
			byteParam1[j++] = eventPacket[i];
		}
		this.parameter1 = BytesDecoder.decodeByteToInt(byteParam1);

		byte[] byteParam2 = new byte[INT_SIZE];
		j = 0;
		for (int i = EVENT_PACKET_PARAMETER1_OFFSET; i < EVENT_PACKET_PARAMETER2_OFFSET; i++) {
			byteParam2[j++] = eventPacket[i];
		}
		this.parameter2 = BytesDecoder.decodeByteToInt(byteParam2);

		byte[] byteParam3 = new byte[INT_SIZE];
		j = 0;
		for (int i = EVENT_PACKET_PARAMETER2_OFFSET; i < EVENT_PACKET_PARAMETER3_OFFSET; i++) {
			byteParam3[j++] = eventPacket[i];
		}
		this.parameter3 = BytesDecoder.decodeByteToInt(byteParam3);
	}

	private int length;

	private int packetType;

	private short eventCode;

	private int sessionId;

	private int transactionId;

	private int parameter1;

	private int parameter2;

	private int parameter3;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPacketType() {
		return packetType;
	}

	public void setPacketType(int packetType) {
		this.packetType = packetType;
	}

	public short getEventCode() {
		return eventCode;
	}

	public void setEventCode(short eventCode) {
		this.eventCode = eventCode;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getSessionId() {
		return sessionId;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getParameter1() {
		return parameter1;
	}

	public void setParameter1(int parameter1) {
		this.parameter1 = parameter1;
	}

	public int getParameter2() {
		return parameter2;
	}

	public void setParameter2(int parameter2) {
		this.parameter2 = parameter2;
	}

	public int getParameter3() {
		return parameter3;
	}

	public void setParameter3(int parameter3) {
		this.parameter3 = parameter3;
	}
}
