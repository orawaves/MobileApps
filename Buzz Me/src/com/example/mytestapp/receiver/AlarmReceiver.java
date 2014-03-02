package com.example.mytestapp.receiver;

import com.example.mytestapp.CursorAdapterActivity;
import com.example.mytestapp.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String[] paramArr = intent.getStringArrayExtra("paramsArr");
		String phoneNumberReciver = paramArr[0];
		String message = paramArr[1];
		String date = paramArr[2];
		String time = paramArr[3];
		System.out.println(phoneNumberReciver + " | " + message  + " | " + date  + " | " + time);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumberReciver, null, message, null, null);
		// Show the toast like in above screen shot
		Toast.makeText(context, "Alarm Triggered and SMS Sent",
				Toast.LENGTH_LONG).show();
	}

}
