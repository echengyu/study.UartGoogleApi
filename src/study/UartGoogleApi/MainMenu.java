package study.UartGoogleApi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {

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
        
        final Button ButtonServiceActivity0 = (Button)findViewById(R.id.ButtonServiceActivity0);
        ButtonServiceActivity0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainMenu.this, SerialPortServiceActivityList0.class));
			}
		});
        
        final Button ButtonServiceActivity1 = (Button)findViewById(R.id.ButtonServiceActivity1);
        ButtonServiceActivity1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainMenu.this, SerialPortServiceActivityList1.class));
			}
		});

        final Button ButtonServiceSetSatus = (Button)findViewById(R.id.ButtonServiceSetSatus);
        ButtonServiceSetSatus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setServiceRun = !setServiceRun;
				if (setServiceRun){
		            startService(new Intent(MainMenu.this, SerialPortService.class));
		            ButtonServiceSetSatus.setText(R.string.service_stop);
		            ButtonServiceSetSatus.getBackground().setColorFilter(0xFF994C00, android.graphics.PorterDuff.Mode.MULTIPLY);
		            
				} else {
					stopService(new Intent(MainMenu.this, SerialPortService.class));
					ButtonServiceSetSatus.setText(R.string.service_start);
					ButtonServiceSetSatus.getBackground().clearColorFilter();
				}
			}
		});
    }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService(new Intent(MainMenu.this, SerialPortService.class));
	}
}
