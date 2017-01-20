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
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android_serialport_api.SerialPort;

public class SerialPortTtyTop {
	private final static String TAG = "SerialPortTtyTop";
	
	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	
	private Handler mHandler;
	private Context mContext;
	private boolean isConnect = true;
	
	private final static char[] mChars = "0123456789ABCDEF".toCharArray();
	
	public SerialPortTtyTop(Handler handler, Context context) {
		this.mHandler = handler;
		this.mContext = context;
		
		mApplication = (Application) mContext.getApplicationContext();

		Log.d("debug", "FtdiUartConnect is Setup");
	}
	
	public SerialPortTtyTop(Context context) {
		this.mContext = context;
		
		mApplication = (Application) mContext.getApplicationContext();

		Log.d("debug", "FtdiUartConnect is Setup");
	}
	
	public SerialPortTtyTop() {

	}
	
	public void openDevice() {
		try {
			mSerialPort = mApplication.getSerialPortTtyTop();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadThread();
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

	public void stopDevice() {
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPortTtyTop();
		mSerialPort = null;
	}
	
	public boolean isConnect() {
		return this.isConnect;
	}

	public void sendString(String msg) {
		if (isConnect) {
			CharSequence t = msg;
			char[] text = new char[t.length()];
			for (int i=0; i<t.length(); i++) {
				text[i] = t.charAt(i);
			}
			try {
				mOutputStream.write(new String(text).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendByte(byte[] b) {
		if (isConnect){
			try {
				mOutputStream.write(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ReadThread extends Thread {

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
						
						Intent intent=new Intent();
					    intent.putExtra("UART_READ", byte2HexStr(buffer, size));
					    intent.setAction("study.UartGoogleApi.SerialPortService");
					    mContext.sendBroadcast(intent);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void DisplayError(int resourceId) {
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
