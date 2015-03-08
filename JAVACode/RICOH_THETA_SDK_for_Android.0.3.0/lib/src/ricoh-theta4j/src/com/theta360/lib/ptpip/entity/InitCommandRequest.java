package com.theta360.lib.ptpip.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.theta360.lib.ptpip.util.BytesEncoder;

public class InitCommandRequest extends Request {

	public InitCommandRequest() {
		this.length = INIT_COMMAND_REQUEST_LENGTH;
		this.packetType = INIT_COMMAND_REQUEST_PACKET_TYPE;

	}

	private static final int INIT_COMMAND_REQUEST_LENGTH = 0x00000042;
	private static final int INIT_COMMAND_REQUEST_PACKET_TYPE = 0x00000001;
	private static final String INIT_COMMAND_REQUEST_GUID = "f5f36abb37114be2b4594e077826fbf8";
	private static final String INIT_COMMAND_REQUEST_FRIENDLY_NAME = "PTPLib";
	private static final int INIT_COMMAND_REQUEST_PROTOCOL_VERSION = 0x00000110;

	private static final String CHARSET_NAME = "Unicode";

	private static final int PAYLOAD_BASE_LENGTH = 14;

	private String guid = INIT_COMMAND_REQUEST_GUID;

	private String friendlyName = INIT_COMMAND_REQUEST_FRIENDLY_NAME;

	private int protocolVersion = INIT_COMMAND_REQUEST_PROTOCOL_VERSION;

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

	protected byte[] createPayload() throws IOException {
		byte[] guidBytes = BytesEncoder.encodeGuidStringToBytes(guid);
		byte[] nameBytes = friendlyName.getBytes(CHARSET_NAME);
		this.length = nameBytes.length + guidBytes.length + PAYLOAD_BASE_LENGTH;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(BytesEncoder.encodeIntTo4bytes(length));
		baos.write(BytesEncoder.encodeIntTo4bytes(packetType));
		baos.write(guidBytes);
		baos.write(nameBytes);
		baos.write((byte) 0x00);
		baos.write((byte) 0x00);
		baos.write(BytesEncoder.encodeIntTo4bytes(protocolVersion));
		return baos.toByteArray();
	}

}
