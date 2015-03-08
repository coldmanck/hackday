package com.theta360.lib.ptpip.entity;

public class CommunicationBase {

	protected int length;
	protected int packetType;
	protected static boolean isCommunicating = false;

	public int getLength() {
		return length;
	}

	public void setLength(int lenght) {
		this.length = lenght;
	}

	public int getPacketType() {
		return packetType;
	}

	public void setPacketType(int packetType) {
		this.packetType = packetType;
	}

}
