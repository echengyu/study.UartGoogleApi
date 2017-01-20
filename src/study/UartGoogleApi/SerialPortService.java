package study.UartGoogleApi;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class SerialPortService extends Service {
	
	public static final String TAG = "SerialPortService";
	public static final String SERVICE_UART_BROADCAST	= "BROADCAST";
	public static final String SERVICE_UART_READ_STRING	= "READ_STRING";
	public static final String SERVICE_UART_READ_BYTE	= "READ_BYTE";
	public static final String SERVICE_UART_SEND_STRING	= "SEND_STRING";
	public static final String SERVICE_UART_SEND_BYTE	= "SEND_BYTE";

	private SerialPortTtyTop 	mSerialPortTtyTop 	= null;
	private IntentFilter 		mIntentFilter 		= null;
	private BroadcastReceiver 	mBroadcastReceiver 	= null;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate() executed");
		
		// SerialPortTtyTop
		if (mSerialPortTtyTop == null) {
			mSerialPortTtyTop = new SerialPortTtyTop(this.getApplicationContext());
			mSerialPortTtyTop.openDevice();
		}
		
		// BroadcastReceiver
		if (mIntentFilter == null && mBroadcastReceiver == null) {
			mIntentFilter = new IntentFilter(SERVICE_UART_BROADCAST);
			mBroadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String sendString = intent.getExtras().getString(SERVICE_UART_SEND_STRING, null);
					byte[] sendByte = intent.getExtras().getByteArray(SERVICE_UART_SEND_BYTE);
					if (mSerialPortTtyTop != null && sendString != null)
						mSerialPortTtyTop.sendString(sendString);
					if (mSerialPortTtyTop != null && sendByte != null)
						mSerialPortTtyTop.sendByte(sendByte);
				}
			};
			registerReceiver(mBroadcastReceiver, mIntentFilter);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() executed");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy() executed");

		if (mSerialPortTtyTop != null) {
			mSerialPortTtyTop.stopDevice();
			mSerialPortTtyTop = null;
		}
		
		if (mIntentFilter != null | mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
			mIntentFilter = null;
			mBroadcastReceiver = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}