package com.theta360.lib.ptpip.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.theta360.lib.ThetaException;
import com.theta360.lib.ptpip.connector.PtpipConnector;
import com.theta360.lib.ptpip.util.BytesEncoder;

public class SendDataRequest extends OperationRequest {

	private static final int START_DATA_PACKET_LENGTH = 20;
	private static final int END_DATA_PACKET_LENGTH = 12;

	private static final int PACKET_TYPE_START_DATA = 9;
	private static final int PACKET_TYPE_END_DATA = 12;

	private byte[] data;

	@Override
	protected void sendDataPacket() throws ThetaException, IOException {
		PtpipConnector.flushCommand(createStartDataPacket());
		PtpipConnector.flushCommand(createEndDataPacket());
	}

	private byte[] createStartDataPacket() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(BytesEncoder.encodeIntTo4bytes(START_DATA_PACKET_LENGTH));
		baos.write(BytesEncoder.encodeIntTo4bytes(PACKET_TYPE_START_DATA));
		baos.write(BytesEncoder.encodeIntTo4bytes(DEFAULT_TRANSACTION_ID));
		baos.write(BytesEncoder.encodeIntTo8bytes(data.length));
		return baos.toByteArray();
	}

	private byte[] createEndDataPacket() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(BytesEncoder.encodeIntTo4bytes(END_DATA_PACKET_LENGTH + data.length));
		baos.write(BytesEncoder.encodeIntTo4bytes(PACKET_TYPE_END_DATA));
		baos.write(BytesEncoder.encodeIntTo4bytes(DEFAULT_TRANSACTION_ID));
		baos.write(data);
		return baos.toByteArray();
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
