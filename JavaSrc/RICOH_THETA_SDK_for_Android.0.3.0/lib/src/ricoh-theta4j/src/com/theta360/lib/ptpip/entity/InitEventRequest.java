package com.theta360.lib.ptpip.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.theta360.lib.ptpip.util.BytesEncoder;

public class InitEventRequest extends Request {

	private final int INIT_EVENT_REQUEST_LENGTH = 12;

	private final int PACKET_TYPE_INIT_EVENT_REQUEST = 3;

	public InitEventRequest(int connectionNumber) {
		this.length = INIT_EVENT_REQUEST_LENGTH;
		this.packetType = PACKET_TYPE_INIT_EVENT_REQUEST;
		this.ConnectionNumber = connectionNumber;
	}

	private int ConnectionNumber;

	public int getConnectionNumber() {
		return ConnectionNumber;
	}

	public void setConnectionNumber(int connectionNumber) {
		ConnectionNumber = connectionNumber;
	}

	public byte[] createPayload() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(BytesEncoder.encodeIntTo4bytes(length));
		baos.write(BytesEncoder.encodeIntTo4bytes(packetType));
		baos.write(BytesEncoder.encodeIntTo4bytes(ConnectionNumber));
		return baos.toByteArray();
	}
}
