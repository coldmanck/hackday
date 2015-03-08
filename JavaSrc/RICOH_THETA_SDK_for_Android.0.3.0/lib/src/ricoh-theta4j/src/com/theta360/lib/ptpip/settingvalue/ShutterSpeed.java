package com.theta360.lib.ptpip.settingvalue;

import com.theta360.lib.ptpip.util.RationalUtil;

public enum ShutterSpeed {

	AUTO(0, 0),
	SPEED1_8000(1, 8000),
	SPEED1_6400(1, 6400),
	SPEED1_5000(1, 5000),
	SPEED1_4000(1, 4000),
	SPEED1_3200(1, 3200),
	SPEED1_2500(1, 2500),
	SPEED1_2000(1, 2000),
	SPEED1_1600(1, 1600),
	SPEED1_1250(1, 1250),
	SPEED1_1000(1, 1000),
	SPEED1_800(1, 800),
	SPEED1_640(1, 640),
	SPEED1_500(1, 500),
	SPEED1_400(1, 400),
	SPEED1_320(1, 320),
	SPEED1_250(1, 250),
	SPEED1_200(1, 200),
	SPEED1_160(1, 160),
	SPEED1_125(1, 125),
	SPEED1_100(1, 100),
	SPEED1_80(1, 80),
	SPEED1_60(1, 60),
	SPEED1_50(1, 50),
	SPEED1_40(1, 40),
	SPEED1_30(1, 30),
	SPEED1_25(1, 25),
	SPEED1_20(1, 20),
	SPEED1_15(1, 15),
	SPEED1_13(1, 13),
	SPEED1_10(1, 10),
	SPEED10_75(10, 75);

	private long value;

	private ShutterSpeed(int molecule, int denominator) {
		this.value = RationalUtil.getRational(molecule, denominator);
	}

	public static ShutterSpeed getFromValue(long value) {
		ShutterSpeed[] shutterSpeed = ShutterSpeed.values();
		for (int i = 0; i < shutterSpeed.length; i++) {
			if (shutterSpeed[i].value == value) {
				return shutterSpeed[i];
			}
		}
		return null;
	}

	public long getValue() {
		return value;
	}

}
