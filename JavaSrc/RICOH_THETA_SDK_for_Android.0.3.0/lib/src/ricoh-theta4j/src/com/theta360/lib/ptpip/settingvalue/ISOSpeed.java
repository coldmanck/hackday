package com.theta360.lib.ptpip.settingvalue;

public enum ISOSpeed {

	AUTO(0xffff), 
	ISO100(100), 
	ISO125(125), 
	ISO160(160), 
	ISO200(200), 
	ISO250(250), 
	ISO320(320), 
	ISO400(400), 
	ISO500(500), 
	ISO640(640), 
	ISO800(800), 
	ISO1000(1000), 
	ISO1250(1250), 
	ISO1600(1600);

	private short value;

	private ISOSpeed(int value) {
		this.value = (short) value;
	}

	public static ISOSpeed getFromValue(short value) {
		ISOSpeed[] iSOSpeed = ISOSpeed.values();
		for (int i = 0; i < iSOSpeed.length; i++) {
			if (iSOSpeed[i].value == value) {
				return iSOSpeed[i];
			}
		}
		return null;
	}

	public short getValue() {
		return value;
	}

}
