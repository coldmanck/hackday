package com.theta360.lib.ptpip.entity;

import com.theta360.lib.ptpip.util.BytesDecoder;

public class CommandResponse extends Response {

	private int responseCode;
	private byte[] resBody;

	public CommandResponse(byte[] resHeader, byte[] resBody) {
		super(resHeader);

		this.responseCode = loadResponseCode(resBody);
		this.resBody = resBody;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public byte[] getResBody() {
		return resBody;
	}

	private static short loadResponseCode(byte[] resBody) {
		byte[] responseCode = new byte[Short.SIZE / Byte.SIZE];
		int j = 0;
		for (int i = 0; i < responseCode.length; i++) {
			responseCode[j++] = resBody[i];
		}
		short res = BytesDecoder.decodeByteToShort(responseCode);
		return res;
	}

}
