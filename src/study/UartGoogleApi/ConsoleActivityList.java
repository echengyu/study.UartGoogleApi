package study.UartGoogleApi;

import java.io.IOException;
import java.util.Locale;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ConsoleActivityList extends SerialPortActivity {

	private ListView readListView;
	private ArrayAdapter<String> resdListAdapter;
	private EditText sendEditText;
	private Button sendButton;
	private final static char[] mChars = "0123456789ABCDEF".toCharArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console_list);

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

	@Override
	protected void onDataReceived(final byte[] buffer, final int size, final long readCount, final long readTime) {
		runOnUiThread(new Runnable() {
			public void run() {
				resdListAdapter.add("Count: " + readCount + " ,Hz: " + (readTime * 10) + " ,Data: " + byte2HexStr(buffer, size));
				if (readCount >  2147483646){
					resdListAdapter.clear();
				}
			}
		});
	}

	public void sendButtonOnClick(View view) {
		sendString(sendEditText.getText() + "");
	}

	public void sendString(String msg) {
		CharSequence t = msg;
		char[] text = new char[t.length()];
		for (int i=0; i<t.length(); i++) {
			text[i] = t.charAt(i);
		}
		try {
			mOutputStream.write(new String(text).getBytes());
			mOutputStream.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
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


