package study.UartGoogleApi;

import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SerialPortServiceActivityList0 extends Activity {

	private IntentFilter 		mIntentFilter 		= null;
	private BroadcastReceiver 	mBroadcastReceiver 	= null;
	
	private ListView readListView;
	private ArrayAdapter<String> resdListAdapter;
	private EditText sendEditText;
	private int bufferIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_activity_list0);

		readListView = (ListView)findViewById(R.id.readListView);
		sendEditText = (EditText)findViewById(R.id.sendEditText);

		resdListAdapter = new ArrayAdapter<String>(this, R.layout.message);
		readListView.setAdapter(resdListAdapter);

		sendEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
				(keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
				    sendString(sendEditText.getText() + "");
					return true;
				}
				return false;
			}
		});
		
		// UART Read BroadcastReceiver
		if (mIntentFilter == null && mBroadcastReceiver == null) {
			mIntentFilter = new IntentFilter(SerialPortService.SERVICE_UART_BROADCAST);
			mBroadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String readString = intent.getExtras().getString(SerialPortService.SERVICE_UART_READ_STRING, null);
					byte[] readdByte = intent.getExtras().getByteArray(SerialPortService.SERVICE_UART_READ_BYTE);
//					if (readString != null)
//						resdListAdapter.add("Count: " + (bufferIndex++) + ", Data: " + readString);
					if (readdByte != null)
						resdListAdapter.add("Count: " + (bufferIndex++) + ", Data: " + byte2HexStr(readdByte, readdByte.length));
					if (bufferIndex > 2147483646)
						bufferIndex = 0;
				}
			};
			registerReceiver(mBroadcastReceiver, mIntentFilter);
		}
		
		final Button sendButton = (Button)findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendString(sendEditText.getText() + "");
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		resdListAdapter.clear();
		
		if (mIntentFilter != null | mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
			mIntentFilter = null;
			mBroadcastReceiver = null;
		}
	}
	
	// UART Send String BroadcastReceiver
	private void sendString(String sendString) {
		Intent mIntent = new Intent();
		mIntent.setAction(SerialPortService.SERVICE_UART_BROADCAST);
		mIntent.putExtra(SerialPortService.SERVICE_UART_SEND_STRING, sendString + "");
	    sendBroadcast(mIntent);
	}
	
	// UART Send Byte BroadcastReceiver
	private void sendByte(byte[] sendByte) {
		Intent mIntent = new Intent();
		mIntent.setAction(SerialPortService.SERVICE_UART_BROADCAST);
		mIntent.putExtra(SerialPortService.SERVICE_UART_SEND_BYTE, sendByte);
	    sendBroadcast(mIntent);
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
}