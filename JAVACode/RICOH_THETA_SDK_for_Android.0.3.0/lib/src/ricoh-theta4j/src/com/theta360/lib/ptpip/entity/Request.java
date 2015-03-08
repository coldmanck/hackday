package com.theta360.lib.ptpip.entity;

import java.io.IOException;

import com.theta360.lib.ThetaException;
import com.theta360.lib.ptpip.connector.PtpipConnector;

public abstract class Request extends CommunicationBase {

	abstract protected byte[] createPayload() throws IOException;

	public Response sendCommand() throws ThetaException {
		if (isCommunicating) {
			throw new ThetaException("Command Excecuting", Response.RESPONSE_CODE_COMMAND_EXCECUTING);
		}
		isCommunicating = true;
		try {
			PtpipConnector.flushCommand(createPayload());

			sendDataPacket();

			byte[] resHeader = PtpipConnector.readCommand(Response.RESPONSE_PACKET_HEADER_LENGTH);
			Response response = new Response(resHeader);
			byte[] resBody = PtpipConnector.readCommand(response.getLength() - Response.RESPONSE_PACKET_HEADER_LENGTH);

			int packetType = response.getPacketType();
			switch (packetType) {
			case Response.RESPONSE_TYPE_INIT_FAIL:
				throw new ThetaException("Response Packet Type is [INIT FAIL].");
			case Response.RESPONSE_TYPE_INIT_COMMAND_ACK:
				response = new InitCommandAck(resHeader, resBody);
				break;
			case Response.RESPONSE_TYPE_CMD_RESPONSE:
				response = new CommandResponse(resHeader, resBody);
				int responseCode = ((CommandResponse) response).getResponseCode();
				if (Response.RESPONSE_CODE_OK != responseCode) {
					throw new ThetaException("Error responseCode : " + responseCode, responseCode);
				}
				break;
			case Response.RESPONSE_TYPE_START_DATA_PACKET:
				response = new DataPacket(resHeader, resBody);
				break;
			default:
				throw new ThetaException("Error packetType : " + packetType);
			}

			return response;
		} catch (IOException e) {
			throw new ThetaException("Error packet type : " + packetType);
		} finally {
			isCommunicating = false;
		}
	}

	public void sendEvent() throws ThetaException {
		try {
			PtpipConnector.flushEvent(createPayload());
		} catch (IOException e) {
			throw new ThetaException("Error packet type : " + packetType);
		}
	}

	protected void sendDataPacket() throws ThetaException, IOException {
		return;
	}

}
