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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android_serialport_api.SerialPortFinder;

public class SerialPortPreferences extends PreferenceActivity {

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;
		
		final SharedPreferences sp = getSharedPreferences("UartApiDeviceInfo", MODE_PRIVATE);
		String devicesName = sp.getString("DEVICE", "Please Select Devices");
		String devicesBaudrates = sp.getString("BAUDRATE", "Please Select Baud rates");
		
		addPreferencesFromResource(R.xml.serial_port_preferences);

		// Devices
		final ListPreference devices = (ListPreference)findPreference("DEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devicesName);
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				sp.edit().putString("DEVICE", (String)newValue).commit();
				return true;
			}
		});

		// Baud rates
		final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE");
		baudrates.setSummary(devicesBaudrates);
		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				sp.edit().putString("BAUDRATE", (String)newValue).commit();
				return true;
			}
		});
	}
}
