package study.UartGoogleApi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SerialPortService extends Service {  
	
	private SerialPortTtyTop mSerialPortTtyTop = null;
  
    public static final String TAG = "MyService";  
  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        Log.d(TAG, "onCreate() executed"); 
        
        if (mSerialPortTtyTop == null) {
        	mSerialPortTtyTop = new SerialPortTtyTop(this);
        	mSerialPortTtyTop.openDevice();
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
    }  
  
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
}  