package com.theta360.lib.ptpip.entity;

import java.io.IOException;

import com.theta360.lib.ptpip.util.BytesDecoder;

public class InitCommandAck extends Response {

	private final int CMD_ACK_PACKET_CONNECTION_NUM_OFFSET = (Integer.SIZE / Byte.SIZE);
	private final int CMD_ACK_PACKET_GUID_OFFSET = CMD_ACK_PACKET_CONNECTION_NUM_OFFSET + (Long.SIZE / Byte.SIZE);

	private final int CMD_ACK_PACKET_PROTOCOL_VERSION_OFFSET = this.length - (Integer.SIZE / Byte.SIZE) - Response.RESPONSE_PACKET_HEADER_LENGTH;

	public InitCommandAck(byte[] resHeader, byte[] resBody) throws IOException {
		super(resHeader);

		byte[] byteConNum = new byte[(Integer.SIZE / Byte.SIZE)];
		int j = 0;
		for (int i = 0; i < CMD_ACK_PACKET_CONNECTION_NUM_OFFSET; i++) {
			byteConNum[j++] += resBody[i];
		}
		this.connectionNumber = BytesDecoder.decodeByteToInt(byteConNum);

		byte[] byteGuid = new byte[(Long.SIZE / Byte.SIZE)];
		j = 0;
		for (int i = CMD_ACK_PACKET_CONNECTION_NUM_OFFSET; i < CMD_ACK_PACKET_GUID_OFFSET; i++) {
			byteGuid[j++] += resBody[i];
		}
		this.guid = BytesDecoder.decodeBytesForGUID(byteGuid);

		byte[] byteFriNam = new byte[this.length - CMD_ACK_PACKET_GUID_OFFSET + (Integer.SIZE / Byte.SIZE)];
		j = 0;
		for (int i = CMD_ACK_PACKET_GUID_OFFSET; i < CMD_ACK_PACKET_PROTOCOL_VERSION_OFFSET; i++) {
			byteFriNam[j++] = resBody[i];
		}
		this.friendlyName = BytesDecoder.decodeBytesForString(byteFriNam);

		byte[] byteProVer = new byte[(Integer.SIZE / Byte.SIZE)];
		j = 0;
		for (int i = CMD_ACK_PACKET_PROTOCOL_VERSION_OFFSET; i < (Integer.SIZE / Byte.SIZE); i++) {
			byteProVer[j++] = resBody[i];
		}
		this.protocolVersion = BytesDecoder.decodeByteToInt(byteProVer);
	}

	private int connectionNumber;

	private String guid;

	private String friendlyName;

	private int protocolVersion;

	public int getPacketType() {
		return packetType;
	}

	public void setPacketType(int packetType) {
		this.packetType = packetType;
	}

	public int getConnectionNumber() {
		return connectionNumber;
	}

	public void setConnectionNumber(int connectionNumber) {
		this.connectionNumber = connectionNumber;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
}
