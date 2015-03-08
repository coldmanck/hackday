package com.theta360.lib.ptp.service;

import java.io.IOException;

import com.theta360.lib.ThetaException;
import com.theta360.lib.ptpip.connector.PtpipConnector;
import com.theta360.lib.ptpip.entity.EventPacket;
import com.theta360.lib.ptpip.eventlistener.PtpipEventListener;

public class PtpipEventListeningService {
	private static Thread eventListeningThread;
	private static PtpipEventListener ptpipEventListener;

	public static void start() {
		if (null == eventListeningThread || !eventListeningThread.isAlive()) {
			eventListeningThread = createEventListeningThread();
			eventListeningThread.start();
		}
	}

	public static void stop() {
		if (null != eventListeningThread) {
			eventListeningThread.interrupt();
			eventListeningThread = null;
			if (null != ptpipEventListener) {
				ptpipEventListener.onEventListenerInterrupted();
			}
		}
	}

	public static void setPtpipEventListener(PtpipEventListener paramListener) {
		ptpipEventListener = paramListener;
	}

	private static Thread createEventListeningThread() {
		return new Thread(new Runnable() {
			public void run() {
				Thread thisThread = Thread.currentThread();
				while (!thisThread.isInterrupted()) {

					EventPacket eventPacket = null;
					try {
						eventPacket = new EventPacket(PtpipConnector.readEvent(EventPacket.LENGTH));

						if (null != eventPacket && null != ptpipEventListener) {
							short eventCode = eventPacket.getEventCode();

							if (EventPacket.EVENT_OBJECT_ADDED == eventCode) {
								ptpipEventListener.onObjectAdded(eventPacket.getParameter1());
							} else if (EventPacket.EVENT_CAPTURE_COMPLETE == eventCode) {
								ptpipEventListener.onCaptureComplete(eventPacket.getParameter1());
							} else if (EventPacket.EVENT_DEVICE_PROP_CHANGED == eventCode) {
								ptpipEventListener.onDevicePropChanged(eventPacket.getParameter1());
							} else if (EventPacket.EVENT_STORE_FULL == eventCode) {
								ptpipEventListener.onStoreFull(eventPacket.getParameter1());
							}
						}
					} catch (ThetaException e) {
						stop();
					} catch (IOException e) {
						stop();
					}
				}
			}
		});
	}
}
