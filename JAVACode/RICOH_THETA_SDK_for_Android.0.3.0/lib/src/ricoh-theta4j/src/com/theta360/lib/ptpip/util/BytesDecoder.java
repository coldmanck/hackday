package com.theta360.lib.ptpip.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BytesDecoder {

	private final static int DIGIT_KEEP = 1;
	private final static int DIGIT_UP = 256;

	private final static int BYTE_MASK = 0xFF;

	private final static int RADIX_HEX = 16;

	private final static String DECODE_CHARSET = "UTF8";

	static final String NULL_STRING = "\0";

	static final int STRING_LENGTH_PACKET_SIZE = 1;
	static final int PTP_CHAR_SIZE = 2;

	public static String decodeBytesForString(byte[] bytes) throws UnsupportedEncodingException {
		String res = new String(bytes, DECODE_CHARSET);
		return res;
	}

	public static String decodeBytesForGUID(byte[] bytes) {
		String res = "";
		for (byte b : bytes) {
			int i = b;
			if (i < 0) {
				i += DIGIT_UP;
			}
			String t = Integer.toString(i, RADIX_HEX);
			if (t.length() < PTP_CHAR_SIZE) {
				res += "0" + t;
			} else {
				res += t;
			}
		}
		return res;
	}

	public static short decodeByteToShort(byte[] data) {
		return decodeByteToShort(data, 0);
	}

	public static short decodeByteToShort(byte[] data, int offset) {
		byte[] longData = new byte[2];
		for (int i = 0; i < 2; i++, offset++) {
			if (i < data.length) {
				longData[i] = data[offset];
			} else {
				longData[i] = 0x0;
			}
		}
		ByteBuffer buffer = ByteBuffer.wrap(longData);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		return buffer.getShort();
	}

	public static int decodeByteToInt(byte[] data) {
		return decodeByteToInt(data, 0);
	}

	public static int decodeByteToInt(byte[] data, int offset) {
		byte[] longData = new byte[4];
		for (int i = 0; i < 4; i++, offset++) {
			if (i < data.length) {
				longData[i] = data[offset];
			} else {
				longData[i] = 0x0;
			}
		}
		ByteBuffer buffer = ByteBuffer.wrap(longData);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		return buffer.getInt();

	}

	public static long decodeByteToLong(byte[] data, ByteOrder byteOrder) {
		return decodeByteToLong(data, 0, byteOrder);
	}

	public static long decodeByteToLong(byte[] data, int offset, ByteOrder byteOrder) {
		byte[] longData = new byte[8];
		for (int i = 0; i < 8; i++, offset++) {
			if (i < data.length) {
				longData[i] = data[offset];
			} else {
				longData[i] = 0x0;
			}
		}
		ByteBuffer buffer = ByteBuffer.wrap(longData);
		buffer.order(byteOrder);
		return buffer.getLong();
	}

	public static String decodeByteToString(byte[] data) {
		return decodeByteToString(data, 0);
	}

	public static String decodeByteToString(byte[] data, int offset) {
		if (data[offset] == 0) {
			return null;
		}
		String ret = new String(data, offset + 1, (data[offset] - 1) * PTP_CHAR_SIZE);
		ret = ret.replace(NULL_STRING, "");
		return ret;
	}

	public static int loadOffsetAfterString(String str) {
		if (str == null) {
			return STRING_LENGTH_PACKET_SIZE;
		}
		return STRING_LENGTH_PACKET_SIZE + (str.length() + NULL_STRING.length()) * PTP_CHAR_SIZE;
	}

	public static int[] decodeByteToShortArray(byte[] data, int offset) {
		int size = decodeByteToInt(data, offset);
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = decodeByteToShort(data, offset + (Integer.SIZE / Byte.SIZE) + (Short.SIZE / Byte.SIZE) * i);
		}
		return array;
	}

	public static int loadOffsetAfterShortArray(int[] array) {
		return (Integer.SIZE / Byte.SIZE) + (Short.SIZE / Byte.SIZE) * array.length;
	}

	public static long decodeBytesToSignedData(byte[] bytes) {
		int res = 0;
		for (int i = 0; i < bytes.length; i++) {
			int a = 0;
			if (i == 0) {
				a = DIGIT_KEEP;
			} else {
				a = DIGIT_UP;
			}
			for (int j = 0; j < (i - 1); j++) {
				a = a * DIGIT_UP;
			}
			if (i == bytes.length - 1) {
				res += bytes[i] * a;
			} else {
				res += (bytes[i] & BYTE_MASK) * a;
			}
		}
		return res;
	}

}
