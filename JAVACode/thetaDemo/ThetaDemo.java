package thetaDemo;

import java.io.FileOutputStream;
import java.io.OutputStream;

import com.theta360.lib.*;
import com.theta360.lib.ptpip.entity.*;

public class ThetaDemo {
	public static void main(String[] args) {
		try {
			PtpipInitiator camera = new PtpipInitiator("192.168.1.1");
			camera.initiateCapture();
			
			Thread.sleep(5000);
			
			ObjectHandles objectHandles = camera.getObjectHandles(
                    PtpipInitiator.PARAMETER_VALUE_DEFAULT,
                    PtpipInitiator.PARAMETER_VALUE_DEFAULT,
                    PtpipInitiator.PARAMETER_VALUE_DEFAULT);
			
			int objectHandle = objectHandles.getObjectHandle(objectHandles.size() - 1);
			
			PtpObject resizedImageObject = camera.getResizedImageObject(
                    objectHandle, 2048, 1024);
			
			byte[] object = resizedImageObject.getDataObject();
			OutputStream out = new FileOutputStream("/tmp/tmp.jpg");
			//OutputStream out = new FileOutputStream("C:\\Users\\DdsSmiLe\\Desktop\\abc_small.jpg");
			out.write(object);
			out.flush();
			out.close();
			
			PtpipInitiator.close();
			System.out.println("Capture Done!");
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	
}
