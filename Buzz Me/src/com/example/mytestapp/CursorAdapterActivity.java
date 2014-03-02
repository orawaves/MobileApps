package com.example.mytestapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CursorAdapterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cursor_adapter);
		ContentResolver contentResolver = getContentResolver();
		Cursor c = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);		
		accessPhoneNumbers(c, contentResolver);		
		// accessEmails(c, contentResolver);		
	}	
	
	public void saveSharedPreferencesInSameActivity(String value) {
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putString("selContFromShaPrefs", value);
		sharedPreferencesEditor.commit();
	}
	
	public void loadSharedPreferencesInSameActivity() {
		SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
		String sharedVal = sharedPrefs.getString("selContFromShaPrefs", "There is no value in shared pref!");		
		TextView tv = (TextView) findViewById(R.id.sharedPrefValtv);
		tv.setText(sharedVal);
	}
	
	public void saveSharedPrefsForOtherActivities(String param) {
		SharedPreferences sharPrefFromPrefMgr = PreferenceManager.getDefaultSharedPreferences(CursorAdapterActivity.this);
		SharedPreferences.Editor sharPrefFromPrefMgrEditor = sharPrefFromPrefMgr.edit();
		sharPrefFromPrefMgrEditor.putString("selContFromShaPrefsMgr", param.substring(param.lastIndexOf("|")+1, param.length()));
		sharPrefFromPrefMgrEditor.commit();
	}
	
	public void loadSharedPrefsForOtherActivities() {
		SharedPreferences shaPrefFromMgr = PreferenceManager.getDefaultSharedPreferences(CursorAdapterActivity.this);
		String sharedVal = shaPrefFromMgr.getString("selContFromShaPrefsMgr", "There is no value in shared pref Mgr!");		
		TextView tv = (TextView) findViewById(R.id.sharedPrefValtv);
		tv.setText(sharedVal);
	}

	public void accessPhoneNumbers(Cursor c, ContentResolver contentResolver) {
		if (c.getCount() > 0) {
			ListView contactsList = (ListView) findViewById(R.id.listView1);
			final ArrayList<String> contactsAl = new ArrayList<String>();
			// contactsList.set
			while (c.moveToNext()) {
				String id = c.getString(c
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				String hasPhoneNum = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (Integer.parseInt(hasPhoneNum) > 0) {
					Cursor pCur = contentResolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID 
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						String phN = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						// System.out.println(name + "---->>>>>" + phN);
						contactsAl.add(name + "|" + phN);

					}
					pCur.close();
				}
			}

			contactsList.setAdapter(new ArrayAdapter<String>(
					CursorAdapterActivity.this,
					android.R.layout.simple_list_item_1, contactsAl));
			contactsList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getApplicationContext(),
							contactsAl.get(position).toString(),
							Toast.LENGTH_LONG).show();
					
//					passParamtersToActivityThroughIntent(contactsAl.get(position).toString());
					
					saveSharedPreferencesInSameActivity(contactsAl.get(position).toString());					
					loadSharedPreferencesInSameActivity();
					
//					saveSharedPrefsForOtherActivities(contactsAl.get(position).toString());
//					loadSharedPrefsForOtherActivities();
					
					saveSharedPrefsForOtherActivities(contactsAl.get(position).toString());
//					forwardToAdapterActivity();
					forwardToMainActivity();
				}
			});
		}
	}
	
	public void passParamtersToActivityThroughIntent(String param) {
		Intent adaptersIntent = new Intent(CursorAdapterActivity.this, MainActivity.class);
		adaptersIntent.putExtra("selContact", param.substring(param.lastIndexOf("|")+1, param.length()));
		CursorAdapterActivity.this.startActivity(adaptersIntent);
	}
	
	public void forwardToAdapterActivity() {
		Intent adaptersIntent = new Intent(CursorAdapterActivity.this, AdaptersActivity.class);
		CursorAdapterActivity.this.startActivity(adaptersIntent);
	}	
	
	public void forwardToMainActivity() {
		Intent adaptersIntent = new Intent(CursorAdapterActivity.this, MainActivity.class);
		CursorAdapterActivity.this.startActivity(adaptersIntent);
	}

	public void accessEmails(Cursor c, ContentResolver contentResolver) {
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				String id = c.getString(c
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				String hasPhoneNum = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				Cursor emailCur = contentResolver.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = ?", new String[] { id }, null);
				while (emailCur.moveToNext()) {
					// This would allow you get several email addresses
					// if the email addresses were stored in an array
					String email = emailCur
							.getString(emailCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					String emailType = emailCur
							.getString(emailCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
				}
				emailCur.close();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cursor_adapter, menu);
		return true;
	}

}
