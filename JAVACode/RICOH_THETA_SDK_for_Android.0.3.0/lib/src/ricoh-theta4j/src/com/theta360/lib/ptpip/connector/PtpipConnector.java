package com.theta360.lib.ptpip.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.theta360.lib.ThetaException;
import com.theta360.lib.ptp.service.PtpipEventListeningService;
import com.theta360.lib.ptpip.entity.InitCommandAck;
import com.theta360.lib.ptpip.entity.InitCommandRequest;
import com.theta360.lib.ptpip.entity.InitEventRequest;
import com.theta360.lib.ptpip.entity.OperationRequest;
import com.theta360.lib.ptpip.entity.Response;

public class PtpipConnector {

	private static final int PTPIP_PORT = 15740;
	private static final int SOCKET_TIMEOUT_SEC = 5000;
	private static final int RETRY_COUNT = 2;

	private static Socket commandSocket;
	private static Socket eventSocket;
	private static OutputStream commandOs;
	public static InputStream commandIs;
	private static OutputStream eventOs;
	public static InputStream eventIs;

	public static void open(String ipAddr) throws IOException, ThetaException {
		commandSocket = new Socket(ipAddr, PTPIP_PORT);
		commandSocket.setSoTimeout(SOCKET_TIMEOUT_SEC);
		commandOs = commandSocket.getOutputStream();
		InitCommandRequest icr = new InitCommandRequest();
		commandIs = commandSocket.getInputStream();

		InitCommandAck ica = (InitCommandAck) icr.sendCommand();

		try {
			eventSocket = new Socket(ipAddr, PTPIP_PORT);
		} catch (IOException e) {
			commandOs.close();
			commandIs.close();
			commandSocket.close();
			throw new IOException("eventSocket is not open");
		}

		eventOs = eventSocket.getOutputStream();
		InitEventRequest ier = new InitEventRequest(ica.getConnectionNumber());
		ier.sendEvent();

		eventIs = eventSocket.getInputStream();

		readEvent(Response.RESPONSE_PACKET_HEADER_LENGTH);

		openSession(ipAddr);
	}

	public static void flushCommand(byte[] payload) throws ThetaException {
		if (isCommandSocketConnected()) {
			try {
				commandOs.write(payload);
				commandOs.flush();
			} catch (IOException e) {
				throw new ThetaException("Failed to stream write");
			}
		} else {
			throw new ThetaException("CommandSocket hasn't connected");
		}
	}

	public static byte[] readCommand(int length) throws ThetaException {
		byte[] res = new byte[length];
		int readingLength = 0;

		while (readingLength < length) {
			try {
				byte[] bRead = new byte[length - readingLength];
				int readLength = commandIs.read(bRead);
				int j = 0;
				for (int i = readingLength; i < readingLength + readLength; i++) {
					res[i] = bRead[j];
					j++;
				}
				readingLength += readLength;
				if (readingLength < 0) {
					throw new ThetaException("command input stream is closed");
				}
			} catch (IOException e) {
				throw new ThetaException("Failed command input stream read");
			}
		}
		return res;
	}

	public static void flushEvent(byte[] payload) throws ThetaException {
		if (isEventSocketConnected()) {
			try {
				eventOs.write(payload);
				eventOs.flush();
			} catch (IOException e) {
				throw new ThetaException("Failed to stream write");
			}
		} else {
			throw new ThetaException("EventSocket hasn't connected");
		}
	}

	public static byte[] readEvent(int length) throws IOException, ThetaException {
		byte[] res = new byte[length];
		int readingLength = 0;
		int continueCount = 0;

		while (readingLength < length) {
			byte[] bRead = new byte[length - readingLength];
			if (null == eventIs) {
				throw new ThetaException("event input stream is closed");
			}

			int readLength;
			try {
				readLength = eventIs.read(bRead);
			} catch (SocketException e) {
				if (++continueCount > RETRY_COUNT) {
					throw new ThetaException("event input stream is closed");
				}
				continue;
			}
			int j = 0;
			for (int i = readingLength; i < readingLength + readLength; i++) {
				res[i] = bRead[j];
				j++;
			}
			readingLength += readLength;
			if (readingLength < 0) {
				throw new ThetaException("event input stream is closed");
			}
		}
		return res;
	}

	public static void close() throws ThetaException {
		try {
			PtpipEventListeningService.stop();
			if (isConnected()) {
				closeSession();
			}
		} catch (ThetaException e) {
			throw e;
		} finally {
			try {
				if (isConnected()) {
					commandIs.close();
					commandOs.close();
					commandSocket.close();
					eventIs.close();
					eventOs.close();
					eventSocket.close();
				}
			} catch (IOException e) {
				throw new ThetaException(e);
			} finally {
				commandIs = null;
				commandOs = null;
				commandSocket = null;
				eventIs = null;
				eventOs = null;
				eventSocket = null;
			}
		}
	}

	/**
	 * Starts session. <br>
	 * Multiple sessions are not supported.
	 * 
	 * @param ipAddr
	 * @throws ThetaException
	 * @throws IOException
	 */
	public static void openSession(String ipAddr) throws ThetaException, IOException {
		if (isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_OPEN_SESSION);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(OperationRequest.DEFAULT_SESSION_ID);
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Ends session.
	 * 
	 * @throws ThetaException
	 */
	public static void closeSession() throws ThetaException {
		if (isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_CLOSE_SESSION);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.sendCommand();
		}
	}

	public static boolean isConnected() {
		return null != commandSocket && null != eventSocket && commandSocket.isConnected() && eventSocket.isConnected();
	}

	public static boolean isCommandSocketConnected() {
		return null != commandSocket && commandSocket.isConnected();
	}

	public static boolean isEventSocketConnected() {
		return null != eventSocket && eventSocket.isConnected();
	}
}
