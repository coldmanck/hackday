package com.theta360.lib;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.theta360.lib.ptp.service.PtpipEventListeningService;
import com.theta360.lib.ptpip.connector.PtpipConnector;
import com.theta360.lib.ptpip.entity.CommandResponse;
import com.theta360.lib.ptpip.entity.DataPacket;
import com.theta360.lib.ptpip.entity.DeviceInfo;
import com.theta360.lib.ptpip.entity.DevicePropertyDesc;
import com.theta360.lib.ptpip.entity.ObjectHandles;
import com.theta360.lib.ptpip.entity.ObjectInfo;
import com.theta360.lib.ptpip.entity.OperationRequest;
import com.theta360.lib.ptpip.entity.PtpObject;
import com.theta360.lib.ptpip.entity.SendDataRequest;
import com.theta360.lib.ptpip.entity.StorageIds;
import com.theta360.lib.ptpip.entity.StorageInfo;
import com.theta360.lib.ptpip.eventlistener.DataReceivedListener;
import com.theta360.lib.ptpip.eventlistener.PtpipEventListener;
import com.theta360.lib.ptpip.settingvalue.BatteryLevel;
import com.theta360.lib.ptpip.settingvalue.ISOSpeed;
import com.theta360.lib.ptpip.settingvalue.ShutterSpeed;
import com.theta360.lib.ptpip.settingvalue.WhiteBalance;
import com.theta360.lib.ptpip.util.BytesEncoder;

public class PtpipInitiator {

	// Property Code
	public static final int DEVICE_PROP_CODE_SET_TIME = 0x5011;
	public static final int DEVICE_PROP_CODE_SET_GPS = 0xD801;
	public static final int DEVICE_PROP_CODE_SET_EXPOSURE_BIAS_COMPENSATION = 0x5010;
	public static final int DEVICE_PROP_CODE_SET_SLEEP_DELAY = 0xD803;
	public static final int DEVICE_PROP_CODE_SET_AUTO_POWER_OFF_DELAY = 0xD802;
	public static final int DEVICE_PROP_CODE_SET_ISO = 0x500F;
	public static final int DEVICE_PROP_CODE_SET_WHITE_BALANCE = 0x5005;
	public static final int DEVICE_PROP_CODE_ERROR_INFO = 0xD006;
	public static final int DEVICE_PROP_CODE_SHUTTER_SPEED = 0xD00F;
	public static final int DEVICE_PROP_CODE_SET_TIMELAPSE_NUMBER = 0x501A;
	public static final int DEVICE_PROP_CODE_SET_TIMELAPSE_INTERVAL = 0x501B;
	public static final int DEVICE_PROP_CODE_AUDIO_VOLUME = 0x502C;
	public static final int DEVICE_PROP_CODE_CHANNEL_NUMBER = 0xD807;
	public static final int DEVICE_PROP_CODE_GET_CAPTURE_STATUS = 0xD808;
	public static final int DEVICE_PROP_CODE_GET_BATTERY_LEVEL = 0x5001;
	public static final int DEVICE_PROP_CODE_STILL_CAPTURE_MODE = 0x5013;
	public static final int DEVICE_PROP_CODE_RECORDING_TIME = 0xD809;
	public static final int DEVICE_PROP_CODE_REMAINING_RECORDING_TIME = 0xD80A;

	// Parameter Code
	public static final int PARAMETER_VALUE_DEFAULT = 0xFFFFFFFF;
	public static final int PARAMETER_VALUE_NONE = 0;

	// Setting Data
	public static final short DEVICE_PROP_VALUE_UNDEFINED_CAPTURE_MODE = 0;
	public static final short DEVICE_PROP_VALUE_SINGLE_CAPTURE_MODE = 1;
	public static final short DEVICE_PROP_VALUE_TIMELAPSE_CAPTURE_MODE = 3;

	public static final short DEVICE_PROP_VALUE_CAPTURE_STATUS_WAIT = 0;
	public static final short DEVICE_PROP_VALUE_CAPTURE_STATUS_CONTINUOUS_SHOOTING_RUNNING = 1;

	public static final int TIMELAPSE_NUMBER_MIN = 0;
	public static final int TIMELAPSE_NUMBER_MAX = 65535;
	public static final int TIMELAPSE_INTERVAL_MIN = 5000;
	public static final int TIMELAPSE_INTERVAL_MAX = 3600000;

	private final int DATE_MAX_LENGTH = 17;

	/**
	 * @param ipAddr
	 *            IP address
	 * @throws IOException
	 * @throws ThetaException
	 */
	public PtpipInitiator(String ipAddr) throws IOException, ThetaException {
		open(ipAddr);
		PtpipEventListeningService.start();
	}

	private void open(String ipAddr) throws IOException, ThetaException {
		if (!PtpipConnector.isConnected()) {
			PtpipConnector.open(ipAddr);
		}
	}

	/**
	 * Disconnects PTP connection. (evtSession Close -&gt; cmdSession Close
	 * -&gt; disconnect PTP)
	 * 
	 * @throws ThetaException
	 */
	public static void close() throws ThetaException {
		PtpipConnector.close();
	}

	/**
	 * Acquires device information.
	 * 
	 * @return Device information data
	 * @throws ThetaException
	 */
	public DeviceInfo getDeviceInfo() throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_DEVICE_INFO);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			DeviceInfo deviceInfo = new DeviceInfo(dataPacket.loadPayload());
			return deviceInfo;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns list of currently valid storage IDs.
	 * 
	 * @return Storage ID list
	 * @throws ThetaException
	 */
	public StorageIds getStorageIDs() throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_STORAGE_IDS);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			StorageIds storageIDs = new StorageIds(dataPacket.loadPayload());
			return storageIDs;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns number of objects corresponding to specified storage ID.
	 * 
	 * @param storageId
	 *            storage ID
	 * @param format
	 *            Object type (specify 0)
	 * @param handle
	 *            Subelement handle (specify 0)
	 * @return Object count
	 * @throws ThetaException
	 */
	public int getNumObjects(int storageId, int format, int handle) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_NUM_OBJECTS);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(storageId);
			or.setParameter2(format);
			or.setParameter3(handle);
			CommandResponse commandResponse = (CommandResponse) or.sendCommand();
			byte[] bytes = commandResponse.getResBody();
			int value = 0;
			for (int i = 0; i < 2; i++) {
				value += (bytes[i + 6] & 0xff) << 8 * i;
			}
			return value;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns object handle corresponding to specified storage ID.
	 * 
	 * @param storageId
	 * @param format
	 *            (Option) Object type
	 * @param handle
	 *            (Option) Subelement handle
	 * @return Object handle list
	 * @throws ThetaException
	 */
	public ObjectHandles getObjectHandles(int storageId, int format, int handle) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_OBJECT_HANDLES);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(storageId);
			or.setParameter2(format);
			or.setParameter3(handle);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			ObjectHandles objectHandles = new ObjectHandles(dataPacket.loadPayload());
			return objectHandles;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns object information corresponding to specified object handle.
	 * 
	 * @param handle
	 *            Object handle
	 * @return Object information
	 * @throws ThetaException
	 */
	public ObjectInfo getObjectInfo(int handle) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_OBJECT_INFO);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(handle);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			ObjectInfo objectInfo = new ObjectInfo(dataPacket.loadPayload());
			return objectInfo;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns objects corresponding to specified object handle.
	 * 
	 * @param handle
	 *            Object handle
	 * @return Object
	 * @throws ThetaException
	 */
	public byte[] getObject(int handle) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_OBJECT);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(handle);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			return dataPacket.loadPayload();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns thumbnail in specified object handle.
	 * 
	 * @param handle
	 *            Object handle
	 * @return Thumbnail
	 * @throws ThetaException
	 */
	public PtpObject getThumb(int handle) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_THUMBNAIL);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(handle);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			PtpObject ptpObject = new PtpObject(dataPacket.loadPayload());
			return ptpObject;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Acquires object corresponding to specified object handle, resized to
	 * 2048x1024 pixels.
	 * 
	 * @param handle
	 * @param imgWidth
	 * @param imgHeight
	 * @return Resized image
	 * @throws ThetaException
	 */
	public PtpObject getResizedImageObject(int handle, int imgWidth, int imgHeight) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			return getResizedImageObject(handle, imgWidth, imgHeight, null);
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Acquires object corresponding to specified object handle, resized to
	 * 2048x1024 pixels.
	 * 
	 * @param handle
	 * @param imgWidth
	 * @param imgHeight
	 * @param dataReceiveListener
	 * @return Resized image
	 * @throws ThetaException
	 */
	public PtpObject getResizedImageObject(int handle, int imgWidth, int imgHeight, DataReceivedListener dataReceiveListener) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_RESIZED_IMAGE_OBJECT);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(handle);
			or.setParameter2(imgWidth);
			or.setParameter3(imgHeight);
			or.setParameter4(PARAMETER_VALUE_NONE);
			or.setParameter5(PARAMETER_VALUE_NONE);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			dataPacket.setDataReceiveListener(dataReceiveListener);
			PtpObject ptpObject = new PtpObject(dataPacket.loadPayload());
			return ptpObject;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Sets device property value for specified device property.
	 * 
	 * @param propCode
	 *            Property code
	 * @param sendData
	 *            Set data
	 * @throws ThetaException
	 */
	public void setDevicePropValue(int propCode, String sendData) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			SendDataRequest or = new SendDataRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_SET_DEVICE_PROP_VALUE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAOUT);
			or.setParameter1(propCode);
			or.setData(BytesEncoder.encodeStringToPTPsendData(sendData));
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Acquires GPS information. Returns to default setting when power is turned
	 * off.
	 * 
	 * @return GPS Information
	 * @throws ThetaException
	 */
	public String getGpsInfo() throws ThetaException {
		return getDevicePropDesc(DEVICE_PROP_CODE_SET_GPS).getCurrentString();
	}

	/**
	 * Sets GPS information. Returns to default setting when power is turned
	 * off.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 * @param dateTime
	 *            Current date and time
	 * @param datum
	 *            Geodetic datum (can only be specified for WGS84)
	 * @throws ThetaException
	 */
	public void setGpsInfo(double latitude, double longitude, double altitude, Date dateTime, String datum) throws ThetaException {
		StringBuilder data = new StringBuilder();
		data.append(String.format(Locale.US, "%1$.6f", latitude)); // {|-}nn.mmmmmm
		data.append(",");
		data.append(String.format(Locale.US, "%1$.6f", longitude)); // {|-}nnn.mmmmmm
		data.append(String.format(Locale.US, "%1$+.2f", altitude)); // {+|-}nnn.mm
		data.append("m@");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS", Locale.US);
		String time = format.format(dateTime);
		if (time.length() > DATE_MAX_LENGTH) {
			time = time.substring(0, DATE_MAX_LENGTH);
		}
		data.append(time); // "YYYYMMDDThhmmss.S"
		data.append(new SimpleDateFormat("Z").format(new Date())); // {+|-}hhmm
		data.append(",");
		data.append(datum);
		data.append("\0");

		setDevicePropValue(DEVICE_PROP_CODE_SET_GPS, data.toString());
	}

	/**
	 * Starts shooting.
	 * 
	 * @throws ThetaException
	 */
	public void initiateCapture() throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_INITIATE_CAPTURE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Starts shooting.
	 * 
	 * @param ptpipEventListener
	 *            Event listener. If the listener is no longer needed after it
	 *            is called, pass "null" in
	 *            {@link PtpipInitiator#setPtpipEventListener(PtpipEventListener)}
	 *            to disable the listener so that it is no longer called.
	 * @throws ThetaException
	 *             If remote shutter fails
	 */
	public void initiateCapture(PtpipEventListener ptpipEventListener) throws ThetaException {
		PtpipEventListeningService.setPtpipEventListener(ptpipEventListener);
		initiateCapture();
	}

	/**
	 * Acquires date and time.
	 * 
	 * @return Date data
	 * @throws ThetaException
	 */
	public Date getDateTime() throws ThetaException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());
		try {
			return format.parse(getDevicePropDesc(DEVICE_PROP_CODE_SET_TIME).getCurrentString());
		} catch (ParseException e) {
			throw new ThetaException(e);
		}
	}

	/**
	 * Sets date and time.
	 * 
	 * @param date
	 *            Date data
	 * @throws ThetaException
	 */
	public void setDateTime(Date date) throws ThetaException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());
		String CurrentValue = format.format(date);
		setDevicePropValue(DEVICE_PROP_CODE_SET_TIME, CurrentValue);
	}

	private void setDevicePropValue(int propCode, short sendData) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			SendDataRequest or = new SendDataRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_SET_DEVICE_PROP_VALUE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAOUT);
			or.setParameter1(propCode);
			or.setData(BytesEncoder.encodeShortTo2bytes(sendData));
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	private void setDevicePropValue(int propCode, byte sendData) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			SendDataRequest or = new SendDataRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_SET_DEVICE_PROP_VALUE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAOUT);
			or.setParameter1(propCode);
			or.setData(BytesEncoder.encodeByteTo1byte(sendData));
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	private void setDevicePropValue(int propCode, int sendData) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			SendDataRequest or = new SendDataRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_SET_DEVICE_PROP_VALUE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAOUT);
			or.setParameter1(propCode);
			or.setData(BytesEncoder.encodeIntTo4bytes(sendData));
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	private void setDevicePropValue(int propCode, long sendData) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			SendDataRequest or = new SendDataRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_SET_DEVICE_PROP_VALUE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAOUT);
			or.setParameter1(propCode);
			or.setData(BytesEncoder.encodeLongToByte(sendData));
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns device property description for specified device property.
	 * 
	 * @param propCode
	 *            Property code
	 * @return Device Property Description
	 * @throws ThetaException
	 */
	public DevicePropertyDesc getDevicePropDesc(int propCode) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_DEVICE_PROP_DESC);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(propCode);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			DevicePropertyDesc descData = new DevicePropertyDesc(dataPacket.loadPayload());
			return descData;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns device property value for specified device property.
	 * 
	 * @param propCode
	 *            Property code
	 * @return Device property value
	 * @throws ThetaException
	 */
	public byte[] getDevicePropValue(int propCode) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_DEVICE_PROP_VALUE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(propCode);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			return dataPacket.loadPayload();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Sets exposure correction value. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @param exposure
	 *            Exposure compensation value
	 * @throws ThetaException
	 */
	public void setExposureBiasCompensation(short exposure) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_SET_EXPOSURE_BIAS_COMPENSATION, exposure);
	}

	/**
	 * Acquires exposure correction value.
	 * 
	 * @return Exposure compensation value
	 * @throws ThetaException
	 */
	public int getExposureBiasCompensation() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_SET_EXPOSURE_BIAS_COMPENSATION).getCurrentValue();
	}

	/**
	 * Sets the sleep mode (seconds) of the camera.
	 * 
	 * @param second
	 *            0: Auto sleep OFF, 1 to 29: 30 seconds, 30 to 1800: Specified
	 *            number of seconds. An exception occurs if any other value is
	 *            passed.
	 * @throws ThetaException
	 *             In the case of second &lt; 0 or 1800 &lt; second
	 */
	public void setSleepDelay(short second) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_SET_SLEEP_DELAY, second);
	}

	/**
	 * Acquires the sleep mode (seconds) of the camera. <br>
	 * Sleep mode is not activated if 0 is set.
	 * 
	 * @return Time after which the camera enters sleep mode (seconds)
	 * @throws ThetaException
	 */
	public int getSleepDelay() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_SET_SLEEP_DELAY).getCurrentValue();
	}

	/**
	 * Acquires the auto power off setting of the camera (minutes). <br>
	 * Auto power off is not set if 0 is set.
	 * 
	 * @return Time after which auto power off occurs (minutes)
	 * @throws ThetaException
	 */
	public int getAutoPowerOffDelay() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_SET_AUTO_POWER_OFF_DELAY).getCurrentValue();
	}

	/**
	 * Sets the auto power off setting of the camera (minutes). <br>
	 * 0 to 30 (minutes) can be set <br>
	 * Auto power off is not set if 0 is set.
	 * 
	 * @param minute
	 *            Time after which auto power off occurs (minutes)
	 * @throws ThetaException
	 */
	public void setAutoPowerOffDelay(int minute) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_SET_AUTO_POWER_OFF_DELAY, (byte) minute);
	}

	/**
	 * Acquires error information.
	 * 
	 * @return Error information
	 * @throws ThetaException
	 */
	public int getErrorInfo() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_ERROR_INFO).getCurrentValue();
	}

	/**
	 * Acquires shutter speed. <br>
	 * The data type is the same as the Exif 2.3 standard rational type. For
	 * example, (1, 8000) is 1/8000 of a second. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @return Shutter speed
	 * @throws ThetaException
	 */
	public ShutterSpeed getShutterSpeed() throws ThetaException {
		long value = getDevicePropDesc(DEVICE_PROP_CODE_SHUTTER_SPEED).getCurrentValue();
		ShutterSpeed shutterSpeed = ShutterSpeed.getFromValue(value);
		if (shutterSpeed == null) {
			throw new ThetaException("Invalid ShutterSpeed Value");
		}
		return shutterSpeed;
	}

	/**
	 * Sets shutter speed. <br>
	 * The data type is the same as the Exif 2.3 standard rational type. For
	 * example, (1, 8000) is 1/8000 of a second. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @param shutterSpeed
	 * @throws ThetaException
	 */
	public void setShutterSpeed(ShutterSpeed shutterSpeed) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_SHUTTER_SPEED, shutterSpeed.getValue());
	}

	/**
	 * Acquires white balance. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @return White balance mode
	 * @throws ThetaException
	 */
	public WhiteBalance getWhiteBalance() throws ThetaException {
		short value = (short) getDevicePropDesc(DEVICE_PROP_CODE_SET_WHITE_BALANCE).getCurrentValue();
		WhiteBalance whiteBalance = WhiteBalance.getFromValue(value);
		if (whiteBalance == null) {
			throw new ThetaException("Invalid WhiteBalance Value");
		}
		return whiteBalance;
	}

	/**
	 * Sets white balance. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @param whiteBalance
	 *            White balance mode
	 * @throws ThetaException
	 */
	public void setWhiteBalance(WhiteBalance whiteBalance) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_SET_WHITE_BALANCE, whiteBalance.getValue());
	}

	/**
	 * Deletes objects corresponding to specified object handle.
	 * 
	 * @param handle
	 *            Object handle
	 * @param format
	 *            Object type
	 * @throws ThetaException
	 */
	public void deleteObject(int handle, int format) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_DELETE_OBJECT);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(handle);
			or.setParameter2(format);
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Returns information corresponding to specified storage ID.
	 * 
	 * @param storageId
	 * @return Information corresponding to specified storage ID
	 * @throws ThetaException
	 */
	public StorageInfo getStorageInfo(int storageId) throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_GET_STORAGE_INFO);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(storageId);
			DataPacket dataPacket = (DataPacket) or.sendCommand();
			StorageInfo storageInfo = new StorageInfo(dataPacket.loadPayload());
			return storageInfo;
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * @param ptpipEventListener
	 *            Event listener. All events are ignored if "null" is passed.
	 */
	public static void setPtpipEventListener(PtpipEventListener ptpipEventListener) {
		PtpipEventListeningService.setPtpipEventListener(ptpipEventListener);
	}

	/**
	 * Acquires ISO sensitivity. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @return ISO sensitivity
	 * @throws ThetaException
	 */
	public ISOSpeed getExposureIndex() throws ThetaException {
		short value = (short) getDevicePropDesc(DEVICE_PROP_CODE_SET_ISO).getCurrentValue();
		ISOSpeed iSOSpeed = ISOSpeed.getFromValue(value);
		if (iSOSpeed == null) {
			throw new ThetaException("Invalid ISOSpeed Value");
		}
		return iSOSpeed;
	}

	/**
	 * Sets ISO sensitivity. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @param iSOSpeed
	 *            ISO sensitivity
	 * @throws ThetaException
	 */
	public void setExposureIndex(ISOSpeed iSOSpeed) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_SET_ISO, iSOSpeed.getValue());
	}

	/**
	 * Sets maximum number of shots for interval shooting. <br>
	 * No shooting limit is applied if 0x0000 is set. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @param timeLapseNumber
	 *            Maximum number of shots
	 * @throws ThetaException
	 */
	public void setTimeLapseNumber(int timeLapseNumber) throws ThetaException {
		if (timeLapseNumber > TIMELAPSE_NUMBER_MAX || timeLapseNumber < TIMELAPSE_NUMBER_MIN) {
			throw new ThetaException("Invalid TimeLapse Number:" + timeLapseNumber);
		} else {
			setDevicePropValue(DEVICE_PROP_CODE_SET_TIMELAPSE_NUMBER, (short) timeLapseNumber);
		}
	}

	/**
	 * Acquires maximum number of shots for interval shooting. <br>
	 * No shooting limit is applied if 0x0000 is set. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @return Maximum number of shots
	 * @throws ThetaException
	 */
	public int getTimeLapseNumber() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_SET_TIMELAPSE_NUMBER).getCurrentValue();
	}

	/**
	 * Sets shooting interval for interval shooting. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @param timeLapseInterval
	 *            Shooting interval for interval shooting (milliseconds)
	 * @throws ThetaException
	 */
	public void setTimeLapseInterval(int timeLapseInterval) throws ThetaException {
		if (timeLapseInterval > TIMELAPSE_INTERVAL_MAX || timeLapseInterval < TIMELAPSE_INTERVAL_MIN) {
			throw new ThetaException("Invalid TimeLapse Interval:" + timeLapseInterval);
		} else {
			setDevicePropValue(DEVICE_PROP_CODE_SET_TIMELAPSE_INTERVAL, timeLapseInterval);
		}
	}

	/**
	 * Acquires shooting interval for interval shooting. <br>
	 * Returns to default setting when power is turned off.
	 * 
	 * @return Shooting interval for interval shooting (milliseconds)
	 * @throws ThetaException
	 */
	public int getTimeLapseInterval() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_SET_TIMELAPSE_INTERVAL).getCurrentValue();
	}

	/**
	 * Sets wireless LAN channel. <br>
	 * Reflected when the Wi-Fi is turned OFF/ON. <br>
	 * Random operation occurs if 0 is set.
	 * 
	 * @param channelNumber
	 *            Wireless LAN channel
	 * @throws ThetaException
	 */
	public void setChannelNumber(int channelNumber) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_CHANNEL_NUMBER, (byte) channelNumber);
	}

	/**
	 * Acquires wireless LAN channel. <br>
	 * Reflected when the Wi-Fi is turned OFF/ON. <br>
	 * Random operation occurs if 0 is set.
	 * 
	 * @return Wireless LAN channel
	 * @throws ThetaException
	 */
	public int getChannelNumber() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_CHANNEL_NUMBER).getCurrentValue();
	}

	/**
	 * Acquires shooting operation status of camera.
	 * 
	 * @return Shooting operation status of camera
	 * @throws ThetaException
	 */
	public short getCaptureStatus() throws ThetaException {
		return (short) getDevicePropDesc(DEVICE_PROP_CODE_GET_CAPTURE_STATUS).getCurrentValue();
	}

	/**
	 * Acquires charge level. There are four detection levels.
	 * 
	 * @return Charge level
	 * @throws ThetaException
	 */
	public BatteryLevel getBatteryLevel() throws ThetaException {
		short currentValue = (short) getDevicePropDesc(DEVICE_PROP_CODE_GET_BATTERY_LEVEL).getCurrentValue();
		return BatteryLevel.getFromValue(currentValue);
	}

	/**
	 * Sets the shooting mode. <br>
	 * Returns to default setting when power is turned off or when
	 * InitiateOpenCapture ends.
	 * 
	 * @param mode
	 *            Shooting mode
	 * @throws ThetaException
	 */
	public void setStillCaptureMode(short mode) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_STILL_CAPTURE_MODE, mode);
	}

	/**
	 * Acquires shooting mode. <br>
	 * Returns to default setting when power is turned off or when
	 * InitiateOpenCapture ends.
	 * 
	 * @return Shooting mode
	 * @throws ThetaException
	 */
	public short getStillCaptureMode() throws ThetaException {
		return (short) getDevicePropDesc(DEVICE_PROP_CODE_STILL_CAPTURE_MODE).getCurrentValue();
	}

	/**
	 * Starts capture. Video (RICOH THETA m15) and interval.
	 * 
	 * @throws ThetaException
	 */
	public void initiateOpenCapture() throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_INITIATE_OPEN_CAPTURE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Starts capture. Video (RICOH THETA m15) and interval.<br>
	 * If the listener is no longer needed after it is called, pass "null" in
	 * {@link PtpipInitiator#setPtpipEventListener(PtpipEventListener)} to
	 * disable the listener so that it is no longer called.
	 * 
	 * @param ptpipEventListener
	 *            Event listener
	 * @throws ThetaException
	 */
	public void initiateOpenCapture(PtpipEventListener ptpipEventListener) throws ThetaException {
		PtpipEventListeningService.setPtpipEventListener(ptpipEventListener);
		initiateOpenCapture();
	}

	/**
	 * Ends continuous shooting.
	 * 
	 * @throws ThetaException
	 */
	public void terminateOpenCapture() throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_TERMINATE_OPEN_CAPTURE);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.setParameter1(OperationRequest.TERMINATE_OPEN_CAPTURE_TRANSACTION_ID);
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Turns Wi-Fi OFF.
	 * 
	 * @throws ThetaException
	 */
	public void wlanPowerControl() throws ThetaException {
		if (PtpipConnector.isConnected()) {
			OperationRequest or = new OperationRequest();
			or.setOperationCode(OperationRequest.OPERATION_CODE_WIFI_OFF);
			or.setTransactionId(OperationRequest.DEFAULT_TRANSACTION_ID);
			or.setDataPhaseInfo(OperationRequest.DATAPHASEINFO_DATAIN_OR_NODATA);
			or.sendCommand();
		} else {
			throw new ThetaException("Socket hasn't connected");
		}
	}

	/**
	 * Acquires video recording time (seconds). (RICOH THETA m15)
	 * 
	 * @return Video recording time (seconds)
	 * @throws ThetaException
	 */
	public int getRecordingTime() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_RECORDING_TIME).getCurrentValue();
	}

	/**
	 * Acquires available video shooting time (seconds). (RICOH THETA m15)
	 * 
	 * @return Available video shooting time (seconds)
	 * @throws ThetaException
	 */
	public int getRemainingRecordingTime() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_REMAINING_RECORDING_TIME).getCurrentValue();
	}

	/**
	 * Acquires shutter sound volume of connected camera
	 * 
	 * @return Shutter sound volume (%)
	 * @throws ThetaException
	 */
	public int getAudioVolume() throws ThetaException {
		return (int) getDevicePropDesc(DEVICE_PROP_CODE_AUDIO_VOLUME).getCurrentValue();
	}

	/**
	 * Sets shutter sound volume of connected camera<br>
	 * Returns to default setting (100%) when power is turned off.<br>
	 * 0% to 100% can be set
	 * 
	 * @param volume
	 *            Shutter sound volume (%)
	 * @throws ThetaException
	 */
	public void setAudioVolume(int volume) throws ThetaException {
		setDevicePropValue(DEVICE_PROP_CODE_AUDIO_VOLUME, volume);
	}

}
