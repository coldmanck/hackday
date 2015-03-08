package com.theta360.lib.ptpip.settingvalue;

public enum WhiteBalance {

	AUTO(0x0002),
	OUTSIDE(0x0004),
	SHADE(0x8001),
	CLOUDINESS(0x8002),
	INCANDESCENT_LAMP1(0x0006),
	INCANDESCENT_LAMP2(0x8020),
	/**
	 * Daylight color
	 */
	FLUORESCENT_LAMP1(0x8003),
	/**
	 * Daylight white light color
	 */
	FLUORESCENT_LAMP2(0x8004),
	/**
	 * White
	 */
	FLUORESCENT_LAMP3(0x8005),
	/**
	 * Light bulb color
	 */
	FLUORESCENT_LAMP4(0x8006); 

	private short value;

	private WhiteBalance(int value) {
		this.value = (short) value;
	}

	public static WhiteBalance getFromValue(short value) {
		WhiteBalance[] whiteBalances = WhiteBalance.values();
		for (int i = 0; i < whiteBalances.length; i++) {
			if (whiteBalances[i].value == value) {
				return whiteBalances[i];
			}
		}
		return null;
	}

	public short getValue() {
		return value;
	}

}
