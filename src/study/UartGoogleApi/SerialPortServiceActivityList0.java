package study.UartGoogleApi;

import java.io.IOException;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SerialPortServiceActivityList0 extends Activity {

	private ListView readListView;
	private ArrayAdapter<String> resdListAdapter;
	private EditText sendEditText;
	private Button sendButton;
	private final static char[] mChars = "0123456789ABCDEF".toCharArray();

	private Receiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console_list);

		startService(new Intent(SerialPortServiceActivityList0.this, SerialPortService.class));
		mReceiver = new Receiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("study.UartGoogleApi.SerialPortService");
		SerialPortServiceActivityList0.this.registerReceiver(mReceiver, filter);

		readListView = (ListView)findViewById(R.id.readListView);
		sendEditText = (EditText)findViewById(R.id.sendEditText);
		sendButton = (Button)findViewById(R.id.sendButton);

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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		resdListAdapter.clear();
	}

	public void sendButtonOnClick(View view) {
		sendString(sendEditText.getText() + "");
	}

	public void sendString(String msg) {
//		CharSequence t = msg;
//		char[] text = new char[t.length()];
//		for (int i=0; i<t.length(); i++) {
//			text[i] = t.charAt(i);
//		}
//		try {
//			mOutputStream.write(new String(text).getBytes());
//			mOutputStream.write('\n');
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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

	public class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle= intent.getExtras();
			Log.d("debug", "Read: " + bundle.getString("UART_READ"));
		}
	}
}


