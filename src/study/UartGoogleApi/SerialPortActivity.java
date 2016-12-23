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
import java.nio.CharBuffer;
import java.security.InvalidParameterException;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android_serialport_api.SerialPort;

public abstract class SerialPortActivity extends Activity {

	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private TimeMath mTimeMath;
	private byte[] buffer = new byte[16];
	private int bufferIndex = 0;
	private long readCount = 0;
	private long readTime = 0;
	
	private final static char[] mChars = "0123456789ABCDEF".toCharArray();

	private class TimeMath extends Thread {
		
		@Override
	    public void run() {
	    	while(!isInterrupted()) {		    	
		    	long readTimeTmp = readCount;
				try {
				    Thread.sleep(100);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				readTime = readCount - readTimeTmp;
	    	}
	    }
	};

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size = 0;
				try {
					if (mInputStream == null) return;
					byte[] bufferStep0 = new byte[16];
					size = mInputStream.read(bufferStep0);
					if (size > 0){
//						Log.d("debug", "size: " + size);
						byte[] bufferStep1 = new byte[size];
						for (int i=0; i<size; i++){
							bufferStep1[i] = bufferStep0[i];
						}
						for (int i=0; i<bufferStep1.length; i++){
							if ( i + bufferIndex > buffer.length - 1){
								bufferIndex = 0;
							} else {
								buffer[i + bufferIndex] = bufferStep1[i];
							}
						}
						bufferIndex += bufferStep1.length;
						if (buffer.length > 0) {
							onDataReceived(buffer, buffer.length, readCount, readTime);
						}
						readCount++;
					}
				} catch (IOException e) {
					e.printStackTrace();
					Log.d("debug", "IOException: " + e);
					return;
				}
			}
		}
	}

	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SerialPortActivity.this.finish();
			}
		});
		b.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
			
			mTimeMath = new TimeMath();
			mTimeMath.start();
			
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
		}
	}

	protected abstract void onDataReceived(final byte[] buffer, final int size, final long readCount, final long readTime);

	@Override
	protected void onDestroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPort();
		mSerialPort = null;
		super.onDestroy();
	}
	
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
