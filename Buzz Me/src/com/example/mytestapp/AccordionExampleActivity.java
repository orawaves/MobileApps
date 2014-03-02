package com.example.mytestapp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mytestapp.receiver.AlarmReceiver;

public class AccordionExampleActivity extends Activity {
	private ArrayList<Map<String, String>> mPeopleList;
	private SimpleAdapter mAdapter;
	private AutoCompleteTextView mTxtPhoneNo;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	// variables to save user selected date and time
	public int year, month, day, hour, minute;
	// declare the variables to show the date and time when Time and Date Picker
	// Dialog first appears
	private int mYear, mMonth, mDay, mHour, mMinute;
	Button dateBtn;
	Button timeBtn;
	Button postMsgBtn;
	Button shareBtn;
	Button scheduleMsgBtn;
	SocialAuthAdapter adapter;
	Profile profileMap;

	public AccordionExampleActivity() {
		final Calendar c = Calendar.getInstance(TimeZone.getDefault());
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	public void onCheckboxClicked(View v) {
		boolean checked = ((CheckBox) v).isChecked();
		switch (v.getId()) {
		case R.id.checkFacebook:
			if (checked) {				
				EditText titleEt = (EditText) findViewById(R.id.txtName);
				titleEt.setVisibility(View.GONE);

				dateBtn.setVisibility(View.GONE);
				timeBtn.setVisibility(View.GONE);
				postMsgBtn.setVisibility(View.GONE);
				scheduleMsgBtn.setVisibility(View.GONE);

				View panelProfile = findViewById(R.id.panelRmdr);
				panelProfile.setVisibility(View.VISIBLE);

				View panelSettings = findViewById(R.id.panelEmail);
				panelSettings.setVisibility(View.GONE);

				View panelPrivacy = findViewById(R.id.panelSettings);
				panelPrivacy.setVisibility(View.GONE);
			} else {

			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accordion_example);
		dateBtn = (Button) findViewById(R.id.button1);
		timeBtn = (Button) findViewById(R.id.button2);
		postMsgBtn = (Button) findViewById(R.id.postMsg);
		Button btnProfile = (Button) findViewById(R.id.btnProfile);
		Button btnSettings = (Button) findViewById(R.id.btnSettings);
		Button btnPrivacy = (Button) findViewById(R.id.btn1);
		Button recipeSearchBtn = (Button) findViewById(R.id.btn3);
		scheduleMsgBtn = (Button) findViewById(R.id.msgB);
		shareBtn = (Button) findViewById(R.id.shareBtn);
		
		//Recipe Search code starts
		
		Button recipeBtn = (Button) findViewById(R.id.button3);
		
		recipeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText recipeEt = (EditText) findViewById(R.id.recipeText);
				String searchForRecipe = recipeEt.getText().toString();
				showCustomToastMsg("You are searching for " + searchForRecipe + " Recipe.");
				
//				String url = "http://api.yummly.com/v1/api/recipe/Avocado-cream-pasta-sauce-recipe-306039?_app_id=0825fdeb&_app_key=8b06defa3399b6c1c7644d6b28bd9090";
				WebserviceOperations webserviceOps = new WebserviceOperations();
				//webserviceOps.postRequest();
				//webserviceOps.postRequest2();
				//webserviceOps.readTwitterFeed();
				webserviceOps.parseStaticJson();
				
			}
		});
		//Recipe Search code ends

		final CheckBox smsCheck = (CheckBox) findViewById(R.id.checkSms);
		
		CheckBox facebookCheck = (CheckBox) findViewById(R.id.checkFacebook);
		facebookCheck.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				onCheckboxClicked(v);				
			}
		});
		
		adapter = new SocialAuthAdapter(new ResponseListener());
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
		// Providers require setting user call Back url
//		adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
		adapter.enable(shareBtn);
		
		
		


		View panelProfile = findViewById(R.id.panelRmdr);
		panelProfile.setVisibility(View.GONE);

		View panelSettings = findViewById(R.id.panelEmail);
		panelSettings.setVisibility(View.GONE);

		View panelPrivacy = findViewById(R.id.panelSettings);
		panelPrivacy.setVisibility(View.GONE);

		btnProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View panelProfile = findViewById(R.id.panelRmdr);
				panelProfile.setVisibility(View.VISIBLE);

				View panelSettings = findViewById(R.id.panelEmail);
				panelSettings.setVisibility(View.GONE);

				View panelPrivacy = findViewById(R.id.panelSettings);
				panelPrivacy.setVisibility(View.GONE);

			}
		});

		btnSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View panelProfile = findViewById(R.id.panelRmdr);
				panelProfile.setVisibility(View.GONE);

				View panelSettings = findViewById(R.id.panelEmail);
				panelSettings.setVisibility(View.VISIBLE);

				View panelPrivacy = findViewById(R.id.panelSettings);
				panelPrivacy.setVisibility(View.GONE);

			}
		});
		

		btnPrivacy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View panelProfile = findViewById(R.id.panelRmdr);
				panelProfile.setVisibility(View.GONE);

				View panelSettings = findViewById(R.id.panelEmail);
				panelSettings.setVisibility(View.GONE);

				View panelPrivacy = findViewById(R.id.panelSettings);
				panelPrivacy.setVisibility(View.VISIBLE);

				smsCheck.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (smsCheck.isChecked()) {
							mPeopleList = new ArrayList<Map<String, String>>();
							populateContactsList();
							mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
							mTxtPhoneNo
									.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> av, View v,
												int index, long arg) {
											Map<String, String> map = (Map<String, String>) av
													.getItemAtPosition(index);
											Iterator<String> myVeryOwnIterator = map
													.keySet().iterator();
											while (myVeryOwnIterator.hasNext()) {
												String key = (String) myVeryOwnIterator
														.next();
												String value = (String) map
														.get(key);
												mTxtPhoneNo.setText(value);
											}
										}
									});
							mAdapter = new SimpleAdapter(
									AccordionExampleActivity.this, mPeopleList,
									R.layout.view_auto_comp_new, new String[] {
											"Name", "Phone", "Type" },
									new int[] { R.id.ccontName, R.id.ccontNo,
											R.id.ccontType });
							mTxtPhoneNo.setAdapter(mAdapter);
							// onSendSmsButtonClicked();
							mTxtPhoneNo.setVisibility(View.VISIBLE);

							EditText titleEt = (EditText) findViewById(R.id.txtName);
							titleEt.setVisibility(View.GONE);

							dateBtn.setVisibility(View.VISIBLE);
							timeBtn.setVisibility(View.VISIBLE);
							postMsgBtn.setVisibility(View.GONE);

							View panelProfile = findViewById(R.id.panelRmdr);
							panelProfile.setVisibility(View.VISIBLE);

							View panelSettings = findViewById(R.id.panelEmail);
							panelSettings.setVisibility(View.GONE);

							View panelPrivacy = findViewById(R.id.panelSettings);
							panelPrivacy.setVisibility(View.GONE);
						} else {
							mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
							mTxtPhoneNo.setVisibility(View.GONE);

							EditText titleEt = (EditText) findViewById(R.id.txtName);
							titleEt.setVisibility(View.VISIBLE);

							View panelProfile = findViewById(R.id.panelRmdr);
							panelProfile.setVisibility(View.VISIBLE);

							View panelSettings = findViewById(R.id.panelEmail);
							panelSettings.setVisibility(View.GONE);

							View panelPrivacy = findViewById(R.id.panelSettings);
							panelPrivacy.setVisibility(View.GONE);
						}
					}
				});

			}
		});
	}

	/*
	 * public void populateContactsList() { ContentResolver cr =
	 * getContentResolver(); Cursor cur =
	 * cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	 * String id = null, name = null, email = null, phone = null, note = null,
	 * orgName = null, title = null; String Phone1 = "unknown", Phone2 =
	 * "unknown", Phone3 = "unknown", type1 = "unknown", type2 = "unknown",
	 * type3 = "unknown"; int size = cur.getCount(); if (size > 0) { int cnt =
	 * 1; while (cur.moveToNext()) { email = ""; name = ""; cnt++; id =
	 * cur.getString(cur .getColumnIndex(ContactsContract.Contacts._ID)); name =
	 * cur .getString(cur
	 * .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); if (name !=
	 * null && name != "") { if (!checkEmail(name)) { email = ""; } else { email
	 * = name; name = ""; } }
	 * 
	 * if (Integer .parseInt(cur.getString(cur
	 * .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	 * System.out.println("name : " + name); Cursor pCur = cr.query(
	 * ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
	 * ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]
	 * { id }, null); Phone1 = " "; Phone2 = " "; Phone3 = " "; while
	 * (pCur.moveToNext()) { String phonetype = pCur .getString(pCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)); String
	 * MainNumber = pCur .getString(pCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); if
	 * (phonetype.equalsIgnoreCase("1")) { Phone1 = MainNumber; type1 = "home";
	 * } else if (phonetype.equalsIgnoreCase("2")) { Phone2 = MainNumber; type2
	 * = "mobile"; } else { Phone3 = MainNumber; type3 = "work"; } }
	 * pCur.close(); }
	 * 
	 * Cursor addrCur = cr
	 * .query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
	 * null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID +
	 * " = ?", new String[] { id }, null); if (addrCur.getCount() == 0) {
	 * addbuffer.append("unknown"); } else { int cntr = 0; while
	 * (addrCur.moveToNext()) { cntr++; String poBox = addrCur
	 * .getString(addrCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal
	 * .POBOX)); if (poBox == null) { poBox = " "; } String street = addrCur
	 * .getString(addrCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal
	 * .STREET)); if (street == null) { street = " "; } String neb = addrCur
	 * .getString(addrCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal
	 * .NEIGHBORHOOD)); if (neb == null) { neb = " "; } String city = addrCur
	 * .getString(addrCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
	 * if (city == null) { city = " "; } String state = addrCur
	 * .getString(addrCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal
	 * .REGION)); if (state == null) { state = " "; } String postalCode =
	 * addrCur .getString(addrCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds
	 * .StructuredPostal.POSTCODE)); if (postalCode == null) { postalCode = " ";
	 * } String country = addrCur .getString(addrCur
	 * .getColumnIndex(ContactsContract
	 * .CommonDataKinds.StructuredPostal.COUNTRY)); if (country == null) {
	 * country = " "; } String type = addrCur .getString(addrCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
	 * if (type == null) { type = " "; } } }
	 * 
	 * addrCur.close();
	 * 
	 * String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " +
	 * ContactsContract.Data.MIMETYPE + " = ?"; String[] noteWhereParams = new
	 * String[] { id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
	 * Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null,
	 * noteWhere, noteWhereParams, null); note = " "; if (noteCur.moveToFirst())
	 * { note = noteCur .getString(noteCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)); if (note ==
	 * null) { note = " "; } } noteCur.close();
	 * 
	 * String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " +
	 * ContactsContract.Data.MIMETYPE + " = ?"; String[] orgWhereParams = new
	 * String[] { id,
	 * ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE }; Cursor
	 * orgCur = cr.query(ContactsContract.Data.CONTENT_URI, null, orgWhere,
	 * orgWhereParams, null); orgName = " "; if (orgCur.moveToFirst()) { orgName
	 * = orgCur .getString(orgCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
	 * } if (orgName == null) { orgName = " "; } orgCur.close();
	 * 
	 * Cursor emailCur = cr.query(
	 * ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
	 * ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]
	 * { id }, null); email = "unknown"; while (emailCur.moveToNext()) {
	 * 
	 * email = emailCur .getString(emailCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); String
	 * emailType = emailCur .getString(emailCur
	 * .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
	 * 
	 * if (email == null) { email = "unknown"; } if
	 * (emailType.equalsIgnoreCase("1")) { } else { } }
	 * 
	 * // add emailCur.close();
	 * 
	 * } } }
	 */

	public void populateContactsList() {

		mPeopleList.clear();

		Cursor people = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		// Cursor people = managedQuery(ContactsContract.Contacts.CONTENT_URI,
		// null, null, null, null);
		while (people.moveToNext()) {
			String contactName = people.getString(people
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			String contactId = people.getString(people
					.getColumnIndex(ContactsContract.Contacts._ID));
			String hasPhone = people
					.getString(people
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if ((Integer.parseInt(hasPhone) > 0)) {

				// You know have the number so now query it like this
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);

				// Cursor phones = managedQuery(
				// ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				// null,
				// ContactsContract.CommonDataKinds.Phone.CONTACT_ID
				// + " = " + contactId, null, null);
				while (phones.moveToNext()) {

					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					String numberType = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

					Map<String, String> NamePhoneType = new HashMap<String, String>();

					NamePhoneType.put("Name", contactName);
					NamePhoneType.put("Phone", phoneNumber);

					if (numberType.equals("0"))
						NamePhoneType.put("Type", "Work");
					else if (numberType.equals("1"))
						NamePhoneType.put("Type", "Home");
					else if (numberType.equals("2"))
						NamePhoneType.put("Type", "Mobile");
					else
						NamePhoneType.put("Type", "Other");

					// Then add this map to the list.
					mPeopleList.add(NamePhoneType);
				}
				phones.close();
			}
		}
		people.close();
		// startManagingCursor(people);
	}

	public void onSendSmsButtonClicked() {
		final EditText phoneNumTv = (EditText) findViewById(R.id.autoCompleteTextView1);
		final EditText messageTv = (EditText) findViewById(R.id.txtMsg);
		Button sendSmsBtn = (Button) findViewById(R.id.msgB);
		sendSmsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phoneNumTv.getText().toString(),
						null, messageTv.getText().toString(), null, null);
//				Toast.makeText(AccordionExampleActivity.this,
//						"SMS sent to " + mTxtPhoneNo.getText().toString(),
//						Toast.LENGTH_LONG).show();
				showCustomToastMsg("SMS sent to " + mTxtPhoneNo.getText().toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accordion_example, menu);
		return true;
	}

	public void saveSharedPrefsForOtherActivities(String param) {
		SharedPreferences sharPrefFromPrefMgr = PreferenceManager
				.getDefaultSharedPreferences(AccordionExampleActivity.this);
		SharedPreferences.Editor sharPrefFromPrefMgrEditor = sharPrefFromPrefMgr
				.edit();
		sharPrefFromPrefMgrEditor.putString("phoneNum",
				param.substring(param.lastIndexOf("|") + 1, param.length()));
		sharPrefFromPrefMgrEditor.commit();
	}

	public void scheduleMessageAlarm(View V) {
		EditText phoneNumTv = (EditText) findViewById(R.id.autoCompleteTextView1);
		EditText messageTv = (EditText) findViewById(R.id.txtMsg);
		String dateSelected = dateBtn.getText().toString();
		String timeSelected = timeBtn.getText().toString();
		String phoneNumStr = phoneNumTv.getText().toString();
		String msgStr = messageTv.getText().toString();
		String[] paramsArr = { phoneNumStr, msgStr, dateSelected, timeSelected };
		// time at which alarm will be scheduled here alarm is scheduled at 1
		// day from current time,
		// we fetch the current time in milliseconds and added 1 day time
		// i.e. 24*60*60*1000= 86,400,000 milliseconds in a day
		// Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;

		String startAfter = year + ":";
		SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyy-M-d H:m");
		Long time = new GregorianCalendar().getTimeInMillis() + 60 * 1000;

		// create an Intent and set the class which will execute when Alarm
		// triggers, here we have
		// given AlarmReciever in the Intent, the onRecieve() method of this
		// class will execute when
		// alarm triggers and
		// we will write the code to send SMS inside onRecieve() method of
		// AlarmReciever class
		Intent intentAlarm = new Intent(this, AlarmReceiver.class);
		intentAlarm.putExtra("paramsArr", paramsArr);

		// create the object
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// set the alarm for particular time
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent
				.getBroadcast(this, 1, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));
//		Toast.makeText(
//				this,
//				"Message will be sent to " + phoneNumStr + " on "
//						+ dateSelected + " | " + timeSelected,
//				Toast.LENGTH_LONG).show();
		showCustomToastMsg("Message will be sent to " + phoneNumStr + " on "
						+ dateSelected + " | " + timeSelected);

	}

	public void showDateDialog(View v) {
		// Show the DatePickerDialog
		showDialog(DATE_DIALOG_ID);
	}

	public void showTimeDialog(View v) {
		// Show the TimePickerDialog
		showDialog(TIME_DIALOG_ID);
	}

	// Register DatePickerDialog listener
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// the callback received when the user "sets" the Date in the
		// DatePickerDialog
		public void onDateSet(DatePicker view, int yearSelected,
				int monthOfYear, int dayOfMonth) {
			year = yearSelected;
			month = monthOfYear + 1;
			day = dayOfMonth;
			// Set the Selected Date in Select date Button
			dateBtn.setText(day + "-" + month + "-" + year);
		}
	};

	// Register TimePickerDialog listener
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		// the callback received when the user "sets" the TimePickerDialog in
		// the dialog
		public void onTimeSet(TimePicker view, int hourOfDay, int min) {
			hour = hourOfDay;
			minute = min;
			// Set the Selected Date in Select date Button
			timeBtn.setText(hour + "-" + minute);
		}
	};

	// Method automatically gets Called when you call showDialog() method
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// create a new DatePickerDialog with values you want to show
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
			// create a new TimePickerDialog with values you want to show
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);

		}
		return null;
	}
	
	
	public void showCustomToastMsg(String message) {
		LinearLayout layout= new LinearLayout(this);
        layout.setBackgroundColor(Color.GRAY);
        TextView tv = new TextView(this);
        tv.setTextColor(Color.RED);
        tv.setTextSize(15);   
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setText(message);
        
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.facebook);
        layout.addView(img);
        layout.addView(tv);
        Toast toast=new Toast(this);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
	}

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			Log.d("FacebookCheckbox", "Authentication Successful");

			// Get name of provider after authentication
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Log.d("FacebookCheckbox", "Provider Name = " + providerName);
			shareBtn.setVisibility(View.GONE);
			
//			Toast t = Toast.makeText(AccordionExampleActivity.this,
//					providerName + " connected", Toast.LENGTH_LONG);
//			t.setGravity(Gravity.TOP|Gravity.LEFT, 200, 500);
//			t.show();
			showCustomToastMsg(providerName + " connected");

			final EditText messageTv = (EditText) findViewById(R.id.txtMsg);

			postMsgBtn.setVisibility(View.VISIBLE);
			// Please avoid sending duplicate message. Social Media Providers block duplicate messages.

			postMsgBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					adapter.updateStatus(messageTv.getText().toString(),
							new MessageListener(), false);

					// to share on multiple providers
//					adapter.updateStatus(messageTv.getText().toString(),
//							new MessageListener(), false);
				}
			});

			// // Share via Email Intent
			// if (providerName.equalsIgnoreCase("share_mail")) {
			// // Use your own code here
			// Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
			// Uri.fromParts("mailto",
			// "vineet.aggarwal@3pillarglobal.com", null));
			// emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
			// "Test");
			// File file = new
			// File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
			// "image5964402.png");
			// Uri uri = Uri.fromFile(file);
			// emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			// startActivity(Intent.createChooser(emailIntent, "Test"));
			// }
			//
			// // Share via mms intent
			// if (providerName.equalsIgnoreCase("share_mms")) {
			//
			// // Use your own code here
			// File file = new
			// File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
			// "image5964402.png");
			// Uri uri = Uri.fromFile(file);
			//
			// Intent mmsIntent = new Intent(Intent.ACTION_SEND, uri);
			// mmsIntent.putExtra("sms_body", "Test");
			// mmsIntent.putExtra(Intent.EXTRA_STREAM, uri);
			// mmsIntent.setType("image/png");
			// startActivity(mmsIntent);
			// }

		}
		
		

		@Override
		public void onError(SocialAuthError error) {
			Log.d("FacebookCheckbox", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("FacebookCheckbox", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("FacebookCheckbox", "Dialog Closed by pressing Back Key");
		}

	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onExecute(String provider, Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204){
//				Toast.makeText(AccordionExampleActivity.this,
//						"Message posted on " + provider, Toast.LENGTH_LONG)
//						.show();
				showCustomToastMsg("Message posted on " + provider);
			}
			else {
//				Toast.makeText(AccordionExampleActivity.this,
//						"Message not posted on " + provider, Toast.LENGTH_LONG)
//						.show();
				showCustomToastMsg("Message not posted on " + provider);
			}
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}

}
