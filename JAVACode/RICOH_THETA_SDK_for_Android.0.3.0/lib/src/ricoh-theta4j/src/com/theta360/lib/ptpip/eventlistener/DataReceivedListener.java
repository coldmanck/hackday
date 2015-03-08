package com.theta360.lib.ptpip.eventlistener;

abstract public class DataReceivedListener {
	/**
	 * Called each time a Data_Packet is received.
	 * 
	 * @param progress
	 *            A value expressing current data receipt progress as [0-100]%.
	 */
	abstract public void onDataPacketReceived(int progress);

}
