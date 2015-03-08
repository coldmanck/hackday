package com.theta360.lib.ptpip.util;

import java.nio.ByteBuffer;

public class BytesEncoder {

	public static int encode4bytesToIntForPTP(byte[] data) {
		int res = 0;
		res += data[0] << Byte.SIZE * 3;
		res += data[1] << Byte.SIZE * 2;
		res += data[2] << Byte.SIZE;
		res += data[3];
		return res;
	}

	public static byte[] encodeIntTo8bytes(int val) {
		byte[] res = new byte[(Long.SIZE / Byte.SIZE)];
		res[0] = (byte) val;
		res[1] = (byte) (val >> Byte.SIZE);
		res[2] = (byte) (val >> Byte.SIZE * 2);
		res[3] = (byte) (val >> Byte.SIZE * 3);
		res[4] = (byte) (val >> Byte.SIZE * 4);
		res[5] = (byte) (val >> Byte.SIZE * 5);
		res[6] = (byte) (val >> Byte.SIZE * 6);
		res[7] = (byte) (val >> Byte.SIZE * 7);
		return res;
	}

	public static byte[] encodeLongToByte(long sendData) {
		int arraySize = Long.SIZE / Byte.SIZE;
		ByteBuffer buffer = ByteBuffer.allocate(arraySize);
		return buffer.putLong(sendData).array();
	}

	public static byte[] encodeIntTo4bytes(int val) {
		byte[] res = new byte[(Integer.SIZE / Byte.SIZE)];
		res[0] = (byte) val;
		res[1] = (byte) (val >> Byte.SIZE);
		res[2] = (byte) (val >> Byte.SIZE * 2);
		res[3] = (byte) (val >> Byte.SIZE * 3);
		return res;
	}

	public static byte[] encodeShortTo2bytes(short val) {
		byte[] res = new byte[(Short.SIZE / Byte.SIZE)];
		res[0] = (byte) val;
		res[1] = (byte) (val >> Byte.SIZE);
		return res;
	}

	public static byte[] encodeByteTo1byte(byte val) {
		byte[] res = new byte[(Byte.SIZE / Byte.SIZE)];
		res[0] = val;
		return res;
	}

	public static byte[] encodeStringTobytes(String val) {
		byte[] res = new byte[val.length() * BytesDecoder.PTP_CHAR_SIZE];
		byte[] conv = val.getBytes();
		int i = 0;
		int j = 0;
		for (; i < res.length;) {
			res[i] = conv[j];
			res[i + 1] = 0;

			i += BytesDecoder.PTP_CHAR_SIZE;
			j++;
		}
		return res;
	}

	public static byte[] encodeStringToPTPsendData(String propData) {
		ByteBuffer bBuf = ByteBuffer.allocate(BytesDecoder.STRING_LENGTH_PACKET_SIZE + (propData.length() + BytesDecoder.NULL_STRING.length()) * BytesDecoder.PTP_CHAR_SIZE);
		bBuf.put((byte) (propData.length() + BytesDecoder.NULL_STRING.length()));
		bBuf.put(encodeStringTobytes(propData + BytesDecoder.NULL_STRING));
		return bBuf.array();
	}

	public static byte[] encodeGuidStringToBytes(String val) {
		byte[] guidBytes = new byte[val.length() / BytesDecoder.PTP_CHAR_SIZE];
		int i = 0;
		for (int j = 0; j < val.length(); j += BytesDecoder.PTP_CHAR_SIZE) {
			String t = val.substring(i, i + BytesDecoder.PTP_CHAR_SIZE);
			int k = Integer.parseInt(t, 16);
			guidBytes[i++] = (byte) k;
		}
		return guidBytes;
	}

}
