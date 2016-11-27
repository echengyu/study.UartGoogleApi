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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android_serialport_api.SerialPortFinder;

public class SerialPortPreferencesNew extends Activity {

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;
	
	private ArrayAdapter<String> mDevicesArrayAdapter;
	private ArrayAdapter<String> mBaudArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);

		// Set result CANCELED in case the user backs out
		setResult(Activity.RESULT_CANCELED);
		
		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;
		
		String[] entryValues = mSerialPortFinder.getAllDevicesPath();
	    for (int i=0; i<entryValues.length; i++)
	    	Log.d("debug", "entryValues: " + entryValues[i]);
//		mBaudArrayAdapter.addAll(entryValues);
		
		// ??å?é?Žç?„è?ç½®
		// Find and set up the ListView for paired devices
//		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
//		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
//		pairedListView.setOnItemClickListener(mDeviceClickListener);
		
		// ?‰¾?ˆ°??„è?ç½®
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mBaudArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

//		mApplication = (Application) getApplication();
//		mSerialPortFinder = mApplication.mSerialPortFinder;
//
//		addPreferencesFromResource(R.xml.serial_port_preferences);
//
//		// Devices
//		final ListPreference devices = (ListPreference)findPreference("DEVICE");
//        String[] entries = mSerialPortFinder.getAllDevices();
//        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
//        
//        for (int i=0; i<entries.length; i++)
//        	Log.d("debug", "entries: " + entries[i]);
//        
//        for (int i=0; i<entryValues.length; i++)
//        	Log.d("debug", "entryValues: " + entryValues[i]);
//        
//		devices.setEntries(entries);
//		devices.setEntryValues(entryValues);
//		devices.setSummary(devices.getValue());
//		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				preference.setSummary((String)newValue);
//				return true;
//			}
//		});
//
//		// Baud rates
//		final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE");
//		baudrates.setSummary(baudrates.getValue());
//		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				preference.setSummary((String)newValue);
//				Log.d("debug", "(String)newValue: " + (String)newValue);
//				return true;
//			}
//		});
	}
	
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

		}
	};
}
