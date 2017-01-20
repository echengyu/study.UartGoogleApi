/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package study.UartGoogleApi;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;


import android.content.SharedPreferences;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class Application extends android.app.Application {

	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialPort mSerialPort = null;
	

	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			/* Read serial port parameters */
			SharedPreferences sp = getSharedPreferences("UartApiDeviceInfo", MODE_PRIVATE);
			String path = sp.getString("DEVICE", "");
			int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

			/* Check parameters */
			if ( (path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}

			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate, 0);
		}
		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
	
	/* ttyUSB0 */
	private SerialPort mSerialPortTtyUsb0 = null;
	private String pathTtyUsb0 = "/dev/ttyUSB0";
	private int baudrateTtyUsb0 = 38400;
	
	public SerialPort getSerialPortTtyUsb0() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPortTtyUsb0 == null) {
			mSerialPortTtyUsb0 = new SerialPort(new File(pathTtyUsb0), baudrateTtyUsb0, 0);
		} 
		return mSerialPortTtyUsb0;
	}
	
	public void closeSerialPortTtyUsb0() {
		if (mSerialPortTtyUsb0 != null) {
			mSerialPortTtyUsb0.close();
			mSerialPortTtyUsb0 = null;
		}
	}
	
	/* ttyUSB1 */
	private SerialPort mSerialPortTtyUsb1 = null;
	private String pathTtyUsb1 = "/dev/ttyUSB1";
	private int baudrateTtyUsb1 = 38400;
	
	public SerialPort getSerialPortTtyUsb1() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPortTtyUsb1 == null) {
			mSerialPortTtyUsb1 = new SerialPort(new File(pathTtyUsb1), baudrateTtyUsb1, 0);
		} 
		return mSerialPortTtyUsb1;
	}
	
	public void closeSerialPortTtyUsb1() {
		if (mSerialPortTtyUsb1 != null) {
			mSerialPortTtyUsb1.close();
			mSerialPortTtyUsb1 = null;
		}
	}
	
	/* ttyTop */
	private SerialPort mSerialPortTtyTop = null;
	
	public SerialPort getSerialPortTtyTop() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPortTtyTop == null) {
			mSerialPortTtyTop = new SerialPort(new File(SerialPortTtyTop.pathTtyTop), SerialPortTtyTop.baudrateTtyTop, 0);
		} 
		return mSerialPortTtyTop;
	}
	
	public void closeSerialPortTtyTop() {
		if (mSerialPortTtyTop != null) {
			mSerialPortTtyTop.close();
			mSerialPortTtyTop = null;
		}
	}
}
