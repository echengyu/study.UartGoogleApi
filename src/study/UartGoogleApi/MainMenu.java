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

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
	
	private SerialPortTtyUsb0 mSerialPortTtyUsb0 = null;
	private SerialPortTtyUsb1 mSerialPortTtyUsb1 = null;
	private Handler mHandler;
	
	private Button buttonTtyUsb0, buttonTtyUsb1;
	private boolean buttonTtyUsb0Enabled = true;
	private boolean buttonTtyUsb1Enabled = true;
	
	
	
	private Button ButtonServiceSetSatus 	= null;
	private Button ButtonServiceActivity0 	= null;
	private Button ButtonServiceActivity1 	= null;
	private boolean setServiceRun = false;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button buttonSetup = (Button)findViewById(R.id.ButtonSetup);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainMenu.this, SerialPortPreferences.class));
//				buttonTtyUsb0.setEnabled(true);
			}
		});
        
        final Button buttonConsoleList = (Button)findViewById(R.id.ButtonConsoleList);
        buttonConsoleList.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainMenu.this, ConsoleActivityList.class));
			}
		});
        
        final Button buttonLoopback = (Button)findViewById(R.id.ButtonLoopback);
        buttonLoopback.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainMenu.this, SerialPortServiceActivityList1.class));
			}
		});
        
        ButtonServiceActivity0 = (Button)findViewById(R.id.ButtonServiceActivity0);
        ButtonServiceActivity0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainMenu.this, SerialPortServiceActivityList0.class));
			}
		});
        
        ButtonServiceActivity1 = (Button)findViewById(R.id.ButtonServiceActivity1);
        ButtonServiceActivity1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				startActivity(new Intent(MainMenu.this, SerialPortServiceActivityList1.class));
				Intent mIntent = new Intent();
				mIntent.setAction(SerialPortService.SERVICE_UART_BROADCAST);
				mIntent.putExtra(SerialPortService.SERVICE_UART_SEND_STRING, "Pass!" + "\n");
				mIntent.putExtra(SerialPortService.SERVICE_UART_SEND_BYTE, new byte[]{0x4F, 0x6B, 0x21, 0x0A});
			    sendBroadcast(mIntent);
			}
		});

        ButtonServiceSetSatus = (Button)findViewById(R.id.ButtonServiceSetSatus);
        ButtonServiceSetSatus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setServiceRun = !setServiceRun;
				if (setServiceRun){
		            startService(new Intent(MainMenu.this, SerialPortService.class));
		            ButtonServiceSetSatus.getBackground().setColorFilter(0xFF994C00, android.graphics.PorterDuff.Mode.MULTIPLY);
		            
				} else {
					stopService(new Intent(MainMenu.this, SerialPortService.class));
					ButtonServiceSetSatus.getBackground().clearColorFilter();
				}
			}
		});
    }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mSerialPortTtyUsb0 != null)
			mSerialPortTtyUsb0.stopDevice();
		if (mSerialPortTtyUsb1 != null)
			mSerialPortTtyUsb1.stopDevice();
	}
}
