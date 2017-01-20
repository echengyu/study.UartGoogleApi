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
import android.util.Log;
import android.view.WindowManager;
import android_serialport_api.SerialPort;

public class SerialPortTtyTop {
	private final static String TAG 			= "SerialPortTtyTop";
	public final static String 	PATH 			= "/dev/ttyUSB0";
	public final static int 	BAUDRATE 		= 38400;	
	protected Application 		mApplication	= null;
	protected SerialPort 		mSerialPort		= null;
	protected OutputStream 		mOutputStream	= null;
	private InputStream 		mInputStream	= null;
	private ReadThread 			mReadThread		= null;
	private Context 			mContext		= null;
	private boolean 			isConnect 		= true;

	public SerialPortTtyTop(Context context) {
		this.mContext = context;
		mApplication = (Application) mContext.getApplicationContext();
		Log.d(TAG, TAG + " Setup");
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
		if (mSerialPort != null) {
			Log.d(TAG, "Close to " + mSerialPort.getDevice());
			mSerialPort = null;
		}
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
					byte[] buffer = new byte[16];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
//						Log.d("debug", "SerialPortTtyTop: " + byte2HexStr(buffer, size));
						
						Intent mIntent = new Intent();
						mIntent.setAction(SerialPortService.SERVICE_UART_BROADCAST);
						mIntent.putExtra(SerialPortService.SERVICE_UART_READ_STRING, hexStr2Str(byte2HexStr(buffer, size)));
						mIntent.putExtra(SerialPortService.SERVICE_UART_READ_BYTE, buffer);
						mApplication.sendBroadcast(mIntent);
						
						if ((hexStr2Str(byte2HexStr(buffer, size)).equals("open"))) {
							Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage("study.UartGoogleApi");
							if (launchIntent != null) { 
								mContext.startActivity(launchIntent);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void DisplayError(int resourceId) {
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
		mBuilder.setTitle("Error!\tPath: " + PATH);
		mBuilder.setMessage(resourceId);
		mBuilder.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				stopDevice();
			}
		});		
		AlertDialog mAlertDialog = mBuilder.create();
		mAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mAlertDialog.show();
	}
	
	private String byte2HexStr(byte[] b, int iLen) {
		char[] mChars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int n=0; n<iLen; n++) {
			sb.append(mChars[(b[n] & 0xFF) >> 4]);
			sb.append(mChars[b[n] & 0x0F]);
			sb.append(' ');
		}
		return sb.toString().trim().toUpperCase(Locale.US);
	}
	
	private String hexStr2Str(String hexStr){  
		String mHexStr = "0123456789ABCDEF";
        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();  
        byte[] bytes = new byte[hexStr.length() / 2];  
        int iTmp = 0x00;;  

        for (int i = 0; i < bytes.length; i++){  
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;  
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);  
            bytes[i] = (byte) (iTmp & 0xFF);  
        }  
        return new String(bytes);  
    }
}
