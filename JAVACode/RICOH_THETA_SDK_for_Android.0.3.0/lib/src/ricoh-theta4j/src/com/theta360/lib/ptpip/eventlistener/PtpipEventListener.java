package com.theta360.lib.ptpip.eventlistener;

import java.util.EventListener;

/**
 * PTP/IP event listener
 * */
public class PtpipEventListener implements EventListener {
	/**
	 * Called when an object is added.
	 * 
	 * @param objectHandle
	 */
	public void onObjectAdded(int objectHandle) {
	}

	/**
	 * Called when an image is captured.
	 * 
	 * @param transactionId
	 */
	public void onCaptureComplete(int transactionId) {
	}

	/**
	 * Called when settings are changed.
	 * 
	 * @param devicePropCode
	 */
	public void onDevicePropChanged(int devicePropCode) {
	}

	/**
	 * Called when event acquisition is interrupted.
	 */
	public void onEventListenerInterrupted() {
	}

	/**
	 * Called when the storage is full.
	 * 
	 * @param devicePropCode
	 */
	public void onStoreFull(int devicePropCode) {
	}
}