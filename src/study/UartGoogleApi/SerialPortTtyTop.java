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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.util.Log;
import android_serialport_api.SerialPort;

public class SerialPortTtyTop {
	private final static String TAG = "SerialPortTtyTop";


//	ReadThread mReadThread = null;
//	private static MainStaticRun signal = null;
//	public static MainStaticRun Init(){
//		if(signal == null){
//			signal = new MainStaticRun();
//		}
//		return signal;
//
//
//	}

	protected static Application mApplication;
	protected static SerialPort mSerialPort;
	protected static OutputStream mOutputStream;
	private static InputStream mInputStream;
	private static ReadThread mReadThread;
	private static boolean isConnect = true;

	private static SerialPortTtyTop mSerialPortTtyTop;
	private SerialPortTtyTop() {};


	public static SerialPortTtyTop Init() {
		if(mSerialPortTtyTop == null) {
			mSerialPortTtyTop = new SerialPortTtyTop();
		}
		return mSerialPortTtyTop;
	}

	public static void openDevice() {
		try {
			mSerialPort = mApplication.getSerialPortTtyTop();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = ReadThread.Init();
			mReadThread.start();

			Log.d(TAG, "Open to " + mSerialPort.getDevice());
		} catch (SecurityException e) {
			isConnect = false;
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			isConnect = false;
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			isConnect = false;
			DisplayError(R.string.error_configuration);
		}
	}

	public static void stopDevice() {
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPortTtyTop();
		mSerialPort = null;
	}





//	private Handler mHandler;
//	private Context mContext;
//	private boolean isConnect = true;
//
//
//
//	public SerialPortTtyTop(Handler handler, Context context) {
//		this.mHandler = handler;
//		this.mContext = context;
//
//		mApplication = (Application) mContext.getApplicationContext();
//
//		Log.d("debug", "FtdiUartConnect is Setup");
//	}
//
//	public void openDevice() {
//		try {
//			mSerialPort = mApplication.getSerialPortTtyTop();
//			mOutputStream = mSerialPort.getOutputStream();
//			mInputStream = mSerialPort.getInputStream();
//
//			/* Create a receiving thread */
//			mReadThread = new ReadThread();
//			mReadThread.start();
//
//			Log.d(TAG, "Open to " + mSerialPort.getDevice());
//		} catch (SecurityException e) {
//			isConnect = false;
//			DisplayError(R.string.error_security);
//		} catch (IOException e) {
//			isConnect = false;
//			DisplayError(R.string.error_unknown);
//		} catch (InvalidParameterException e) {
//			isConnect = false;
//			DisplayError(R.string.error_configuration);
//		}
//	}
//
//	public void stopDevice() {
//		if (mReadThread != null)
//			mReadThread.interrupt();
//		mApplication.closeSerialPortTtyTop();
//		mSerialPort = null;
//	}
//
//	public boolean isConnect() {
//		return this.isConnect;
//	}
//
//	public void sendString(String msg) {
//		if (isConnect) {
//			CharSequence t = msg;
//			char[] text = new char[t.length()];
//			for (int i=0; i<t.length(); i++) {
//				text[i] = t.charAt(i);
//			}
//			try {
//				mOutputStream.write(new String(text).getBytes());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public void sendByte(byte[] b) {
//		if (isConnect){
//			try {
//				mOutputStream.write(b);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
	private static class ReadThread extends Thread {
		
		private static ReadThread mstaticReadThread;
		private ReadThread() {};
		public static ReadThread Init() {
			if(mstaticReadThread == null)
				mstaticReadThread = new ReadThread();
			return mstaticReadThread;
		}

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[4096];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						Log.d("debug", "SerialPortTtyTop: " + byte2HexStr(buffer, size));
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private static void DisplayError(int resourceId) {
//		AlertDialog.Builder b = new AlertDialog.Builder(mContext);
//		b.setTitle("Error");
//		b.setMessage(resourceId);
//		b.setPositiveButton("OK", new OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				stopDevice();
//			}
//		});
//		b.show();
	}

	private final static char[] mChars = "0123456789ABCDEF".toCharArray();
	public static String byte2HexStr(byte[] b, int iLen) {
		StringBuilder sb = new StringBuilder();
		for (int n=0; n<iLen; n++) {
			sb.append(mChars[(b[n] & 0xFF) >> 4]);
			sb.append(mChars[b[n] & 0x0F]);
			sb.append(' ');
		}
		return sb.toString().trim().toUpperCase(Locale.US);
	}
}
