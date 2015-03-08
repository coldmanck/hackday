package com.theta360.lib.rexif.util;

import com.theta360.lib.rexif.ExifReadException;
import com.theta360.lib.rexif.entity.EntryField;

/**
 * Analysis of IFD tag data structure
 * 
 */
public class FieldDataAnalizer {

	// TYPE
	private static final int EXIF_TAG_TYPE_BYTE = 1; // 8bit unsigned
	private static final int EXIF_TAG_TYPE_ASCII = 2; // ASCII
	private static final int EXIF_TAG_TYPE_SHORT = 3; // 16bit unsigned
	private static final int EXIF_TAG_TYPE_LONG = 4; // 32bit unsigned
	private static final int EXIF_TAG_TYPE_RATIONAL = 5; // LONG 2
	private static final int EXIF_TAG_TYPE_UNDEFINED = 7; // 8bit
	private static final int EXIF_TAG_TYPE_SLONG = 9; // 32bit signed
	private static final int EXIF_TAG_TYPE_SRATIONAL = 10; // SLONG 2

	private static final int EXIF_IMMEDIATE_BORDER = 4;

	private static final int BYTESIZE_LONG = 4;
	private static final int BYTESIZE_RATIONAL = 8;

	private static final int COUNT_MAG_BYTE = 1;
	private static final int COUNT_MAG_ASCII = 1;
	private static final int COUNT_MAG_SHORT = 2;
	private static final int COUNT_MAG_LONG = 4;
	private static final int COUNT_MAG_RATIONAL = 4 * 2;
	private static final int COUNT_MAG_UNDEFINED = 1;
	private static final int COUNT_MAG_SLONG = 4;
	private static final int COUNT_MAG_SRATIONAL = 4 * 2;

	/**
	 * Acquires field data size
	 * 
	 * @param ef
	 *            Field data to be judged
	 * @return Byte count
	 */
	public static int getDataLength(EntryField ef) {
		int ret = 0;
		int type = ef.getType();
		int count = ef.getCount();

		if (type == EXIF_TAG_TYPE_BYTE) {
			ret = count * COUNT_MAG_BYTE;
		} else if (type == EXIF_TAG_TYPE_ASCII) {
			ret = count * COUNT_MAG_ASCII;
		} else if (type == EXIF_TAG_TYPE_SHORT) {
			ret = count * COUNT_MAG_SHORT;
		} else if (type == EXIF_TAG_TYPE_LONG) {
			ret = count * COUNT_MAG_LONG;
		} else if (type == EXIF_TAG_TYPE_RATIONAL) {
			ret = count * COUNT_MAG_RATIONAL;
		} else if (type == EXIF_TAG_TYPE_UNDEFINED) {
			ret = count * COUNT_MAG_UNDEFINED;
		} else if (type == EXIF_TAG_TYPE_SLONG) {
			ret = count * COUNT_MAG_SLONG;
		} else if (type == EXIF_TAG_TYPE_SRATIONAL) {
			ret = count * COUNT_MAG_SRATIONAL;
		}
		return ret;
	}

	/**
	 * Judges offset value
	 * 
	 * @param ef
	 *            Field data to be judged
	 * @return True: Immediate value; False: offset of jump destination
	 */
	public static boolean isImmediate(EntryField ef) {
		if (getDataLength(ef) <= EXIF_IMMEDIATE_BORDER) {
			return true;
		}
		return false;
	}

	/**
	 * Acquires value as a Rational value (non-coded 32-bit numerator/denominator)
	 * 
	 * @param ef
	 *            Field data to be judged
	 * @return Data
	 * @throws ExifReadException
	 *             Unexpected type
	 */
	public static double[] convertValueToRational(EntryField ef) throws ExifReadException {
		int type = ef.getType();
		int count = ef.getCount();
		byte[] value = ef.getValue();
		if (type == EXIF_TAG_TYPE_RATIONAL) {
			double[] ret = new double[count];
			for (int i = 0; i < count; i++) {
				double num = DataProcessingUtil.byteToLong(value, i * BYTESIZE_RATIONAL);
				double dom = DataProcessingUtil.byteToLong(value, i * BYTESIZE_RATIONAL + BYTESIZE_LONG);
				ret[i] = num / dom;
			}
			return ret;
		}
		throw new ExifReadException("type unmatch");
	}

	/**
	 * Acquires value as an SRational value (coded 32-bit numerator/denominator)
	 * 
	 * @param ef
	 *            Field data to be judged
	 * @return Data
	 * @throws ExifReadException
	 *             Unexpected type
	 */
	public static double[] convertValueToSRational(EntryField ef) throws ExifReadException {
		int type = ef.getType();
		int count = ef.getCount();
		byte[] value = ef.getValue();
		if (type == EXIF_TAG_TYPE_SRATIONAL) {
			double[] ret = new double[count];
			for (int i = 0; i < count; i++) {
				double num = DataProcessingUtil.byteToSLong(value, i * BYTESIZE_RATIONAL);
				double dom = DataProcessingUtil.byteToSLong(value, i * BYTESIZE_RATIONAL + BYTESIZE_LONG);
				ret[i] = num / dom;
			}
			return ret;
		}
		throw new ExifReadException("type unmatch");
	}

}