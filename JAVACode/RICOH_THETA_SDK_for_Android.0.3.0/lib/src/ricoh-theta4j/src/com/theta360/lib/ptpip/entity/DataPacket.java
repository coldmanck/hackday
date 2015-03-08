package com.theta360.lib.ptpip.entity;

import java.nio.ByteOrder;

import com.theta360.lib.ThetaException;
import com.theta360.lib.ptpip.connector.PtpipConnector;
import com.theta360.lib.ptpip.eventlistener.DataReceivedListener;
import com.theta360.lib.ptpip.util.BytesDecoder;

public class DataPacket extends Response {

	private static final int TRANSACTION_ID_PACKET_SIZE = Integer.SIZE / Byte.SIZE;
	private static final int TOTAL_DATA_PACKET_SIZE = Long.SIZE / Byte.SIZE;

	private int transactionId;

	private int totalDataLength;

	private byte[] dataPayload;

	private DataReceivedListener dataReceiveListener = null;

	public DataPacket(byte[] resHeader, byte[] resBody) {
		super(resHeader);
		this.totalDataLength = loadTotalDataLength(resBody);
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getTotalDataLength() {
		return totalDataLength;
	}

	public void setTotalDataLength(int totalDataLength) {
		this.totalDataLength = totalDataLength;
	}

	public byte[] getDataPayload() {
		return dataPayload;
	}

	public void setDataPayload(byte[] dataPayload) {
		this.dataPayload = dataPayload;
	}

	public void setDataReceiveListener(DataReceivedListener dataReceiveListener) {
		this.dataReceiveListener = dataReceiveListener;
	}

	public byte[] loadPayload() throws ThetaException {
		if (isCommunicating) {
			throw new ThetaException("Command Excecuting", Response.RESPONSE_CODE_COMMAND_EXCECUTING);
		}
		isCommunicating = true;
		this.dataPayload = new byte[this.totalDataLength];

		int j = 0;
		int packetType;
		readPacket: do {
			byte[] resHeader = PtpipConnector.readCommand(Response.RESPONSE_PACKET_HEADER_LENGTH);
			Response response = new Response(resHeader);
			byte[] resBody = PtpipConnector.readCommand(response.getLength() - Response.RESPONSE_PACKET_HEADER_LENGTH);
			this.transactionId = loadTransactionId(resBody);

			packetType = response.getPacketType();
			switch (packetType) {
			case RESPONSE_TYPE_DATA_PACKET:
				for (int i = TRANSACTION_ID_PACKET_SIZE; i < resBody.length; i++) {
					this.dataPayload[j++] = resBody[i];
				}
				if (null != dataReceiveListener) {
					int progress = j * 100 / totalDataLength;
					dataReceiveListener.onDataPacketReceived(progress);
				}
				break;
			case RESPONSE_TYPE_END_DATA_PACKET:
				for (int i = TRANSACTION_ID_PACKET_SIZE; i < resBody.length; i++) {
					this.dataPayload[j++] = resBody[i];
				}
				resHeader = PtpipConnector.readCommand(Response.RESPONSE_PACKET_HEADER_LENGTH);
				response = new Response(resHeader);
				resBody = PtpipConnector.readCommand(response.getLength() - Response.RESPONSE_PACKET_HEADER_LENGTH);
				response = new CommandResponse(resHeader, resBody);
				int responseCode = ((CommandResponse) response).getResponseCode();
				if (Response.RESPONSE_CODE_OK != responseCode) {
					throw new ThetaException("Error responseCode : " + responseCode);
				}
				break readPacket;
			default:
				throw new ThetaException("Error packetType : " + packetType);
			}
		} while (packetType == RESPONSE_TYPE_DATA_PACKET);

		isCommunicating = false;
		return this.dataPayload;
	}

	private int loadTransactionId(byte[] resBody) {
		byte[] transactionIdPacket = new byte[TRANSACTION_ID_PACKET_SIZE];
		int j = 0;
		for (int i = 0; i < TRANSACTION_ID_PACKET_SIZE; i++) {
			transactionIdPacket[j++] = resBody[i];
		}
		long transactionId = BytesDecoder.decodeByteToInt(transactionIdPacket);
		return (int) transactionId;
	}

	private int loadTotalDataLength(byte[] resBody) {
		byte[] totalDataLengthPacket = new byte[TOTAL_DATA_PACKET_SIZE];
		int j = 0;
		for (int i = TRANSACTION_ID_PACKET_SIZE; i < resBody.length; i++) {
			totalDataLengthPacket[j++] = resBody[i];
		}
		long totalDataLength = BytesDecoder.decodeByteToLong(totalDataLengthPacket, ByteOrder.LITTLE_ENDIAN);
		return (int) totalDataLength;
	}
}
