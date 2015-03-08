package com.theta360.lib.ptpip.util;

import java.nio.ByteOrder;

public class RationalUtil {

	public static long getRational(int molecule, int denominator) {
		byte[] bytesMolecule = BytesEncoder.encodeIntTo4bytes(molecule);
		byte[] bytesDenominator = BytesEncoder.encodeIntTo4bytes(denominator);

		byte[] bytes = new byte[bytesMolecule.length + bytesDenominator.length];
		for (int i = 0; i < bytesMolecule.length; i++) {
			bytes[i] = bytesMolecule[i];
		}
		for (int i = bytesMolecule.length; i < bytes.length; i++) {
			bytes[i] = bytesDenominator[i - bytesMolecule.length];
		}

		return BytesDecoder.decodeByteToLong(bytes, ByteOrder.BIG_ENDIAN);
	}
}
