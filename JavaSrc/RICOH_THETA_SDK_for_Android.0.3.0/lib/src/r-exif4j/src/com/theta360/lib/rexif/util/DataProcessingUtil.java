package com.theta360.lib.rexif.util;

/**
 * Data processing utility
 */
public class DataProcessingUtil {

	private static final int BYTE_SIZE_8 = 8;
	private static final int BYTE_SIZE_16 = 16;
	private static final int BYTE_SIZE_24 = 24;

	/**
	 * Converts coded values to non-coded values
	 * 
	 * @param value
	 * @return Converted value
	 */
	private static int signedToUnsigned(byte value) {
		int ret = (int) value;
		if (ret < 0) {
			ret += 256;
		}
		return ret;
	}

	/**
	 * Converts byte array values as non-coded LongType (32-bit) values
	 * 
	 * @param value
	 * @param offset
	 * @return Converted value
	 */
	public static long byteToLong(byte[] value, int offset) {
		long ret = 0;
		ret += signedToUnsigned(value[0 + offset]) << BYTE_SIZE_24;
		ret += signedToUnsigned(value[1 + offset]) << BYTE_SIZE_16;
		ret += signedToUnsigned(value[2 + offset]) << BYTE_SIZE_8;
		ret += signedToUnsigned(value[3 + offset]);
		return ret;
	}

	/**
	 * Converts byte array values as coded LongType (32-bit) values
	 * 
	 * @param value
	 * @param offset
	 * @return Converted value
	 */
	public static long byteToSLong(byte[] value, int offset) {
		int ret = 0;
		ret |= value[0 + offset] << BYTE_SIZE_24;
		ret |= signedToUnsigned(value[1 + offset]) << BYTE_SIZE_16;
		ret |= signedToUnsigned(value[2 + offset]) << BYTE_SIZE_8;
		ret |= signedToUnsigned(value[3 + offset]);
		return (long) ret;
	}
}
