package com.theta360.lib.ptpip.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.theta360.lib.ptpip.util.BytesEncoder;

public class OperationRequest extends Request {

	public OperationRequest() {
		this.length = OPERATION_REQUEST_PACKET_LENGTH;
		this.packetType = PACKET_TYPE_COMMAND_REQUEST;
	}

	public static final int DATAPHASEINFO_UNKOWN = 0x0000;
	public static final int DATAPHASEINFO_DATAIN_OR_NODATA = 0x0001;
	public static final int DATAPHASEINFO_DATAOUT = 0x0002;

	public static final int DEFAULT_TRANSACTION_ID = 1;
	public static final int TERMINATE_OPEN_CAPTURE_TRANSACTION_ID = 0xFFFFFFFF;
	public static final int DEFAULT_SESSION_ID = 1;

	public final static short OPERATION_CODE_OPEN_SESSION = 0x1002;
	public final static short OPERATION_CODE_CLOSE_SESSION = 0x1003;
	public final static short OPERATION_CODE_GET_DEVICE_INFO = 0x1001;
	public final static short OPERATION_CODE_GET_STORAGE_IDS = 0x1004;
	public final static short OPERATION_CODE_GET_NUM_OBJECTS = 0x1006;
	public final static short OPERATION_CODE_GET_OBJECT_HANDLES = 0x1007;
	public final static short OPERATION_CODE_GET_OBJECT_INFO = 0x1008;
	public final static short OPERATION_CODE_GET_OBJECT = 0x1009;
	public final static short OPERATION_CODE_GET_DEVICE_PROP_VALUE = 0x1015;
	public final static short OPERATION_CODE_GET_STORAGE_INFO = 0x1005;
	public final static short OPERATION_CODE_SET_DEVICE_PROP_VALUE = 0x1016;
	public final static short OPERATION_CODE_GET_THUMBNAIL = 0x100A;
	public final static short OPERATION_CODE_GET_RESIZED_IMAGE_OBJECT = 0x1022;
	public final static short OPERATION_CODE_INITIATE_CAPTURE = 0x100E;
	public final static short OPERATION_CODE_DELETE_OBJECT = 0x100B;
	public final static short OPERATION_CODE_INITIATE_OPEN_CAPTURE = 0x101C;
	public final static short OPERATION_CODE_TERMINATE_OPEN_CAPTURE = 0x1018;
	public final static short OPERATION_CODE_WIFI_OFF = (short) 0x99A1;
	public final static short OPERATION_CODE_GET_DEVICE_PROP_DESC = 0x1014;

	private final int OPERATION_REQUEST_PACKET_LENGTH = 38;
	private final int PACKET_TYPE_COMMAND_REQUEST = 6;

	private int dataPhaseInfo = DATAPHASEINFO_UNKOWN;

	private short operationCode;

	private int transactionId;

	private int parameter1;

	private int parameter2;

	private int parameter3;

	private int parameter4;

	private int parameter5;

	public int getDataPhaseInfo() {
		return dataPhaseInfo;
	}

	public void setDataPhaseInfo(int dataPhaseInfo) {
		this.dataPhaseInfo = dataPhaseInfo;
	}

	public short getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(short operationCode) {
		this.operationCode = operationCode;
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

	public int getParameter4() {
		return parameter4;
	}

	public void setParameter4(int parameter4) {
		this.parameter4 = parameter4;
	}

	public int getParameter5() {
		return parameter5;
	}

	public void setParameter5(int parameter5) {
		this.parameter5 = parameter5;
	}

	protected byte[] createPayload() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(BytesEncoder.encodeIntTo4bytes(this.length));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.packetType));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.dataPhaseInfo));
		baos.write(BytesEncoder.encodeShortTo2bytes(this.operationCode));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.transactionId));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.parameter1));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.parameter2));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.parameter3));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.parameter4));
		baos.write(BytesEncoder.encodeIntTo4bytes(this.parameter5));
		return baos.toByteArray();
	}

}
