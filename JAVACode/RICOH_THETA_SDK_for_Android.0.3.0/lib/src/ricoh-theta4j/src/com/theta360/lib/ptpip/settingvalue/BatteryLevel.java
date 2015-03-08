package com.theta360.lib.ptpip.settingvalue;

public enum BatteryLevel {

	/**
	 * Battery level: 100%
	 */
	FULL(100),
	/**
	 * Battery level: 67%
	 */
	HALF(67),
	/**
	 * Battery level: 33%
	 */
	NEAR_END(33),
	/**
	 * Battery level: 0%
	 */
	END(0);

	private short percentage;

	private BatteryLevel(int percentage) {
		this.percentage = (short) percentage;
	}

	public static BatteryLevel getFromValue(short percentage) {
		BatteryLevel[] batteryLevels = BatteryLevel.values();
		for (int i = 0; i < batteryLevels.length; i++) {
			if (batteryLevels[i].percentage == percentage) {
				return batteryLevels[i];
			}
		}
		return null;
	}

	/**
	 * Acquires battery level as a value (%).
	 * 
	 * @return Battery level (%)
	 */
	public short getValue() {
		return percentage;
	}

}
