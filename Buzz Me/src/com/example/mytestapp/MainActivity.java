package com.example.mytestapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.support.v4.content.CursorLoader;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private static final int NOTIFICATIONS = 0;
	private static final int ADAPTERS = 1;
	private static final int DIALOG_BOX = 2;
	private static final int TOAST_MENU_ITEM = Menu.FIRST;
	private static final int STATUS_BAR_MENU_ITEM = TOAST_MENU_ITEM + 1;
	private static final int DIALOG_MENU_ITEM = STATUS_BAR_MENU_ITEM + 1;
	private static final int ADAPTER_MENU_ITEM1 = DIALOG_MENU_ITEM + 1;
	private static final int ADAPTER_MENU_ITEM2 = ADAPTER_MENU_ITEM1 + 1;
	private static final int ADAPTER_MENU_ITEM3 = ADAPTER_MENU_ITEM2 + 1;
	ProgressBar myProgressBar;
	int myProgress = 0;
	private ArrayList<Map<String, String>> mPeopleList;
	private SimpleAdapter mAdapter;
	private AutoCompleteTextView mTxtPhoneNo;
	private static final int CONTACT_PICKER_RESULT = 1001;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("*********** MainActivity onCreate() *********");
        setContentView(R.layout.activity_main);
        
        
//        navigateToContacts();
//        autoCompleteProcess();
//        onSendSmsButtonClicked();        
    }
    
    public void doLaunchContactPicker(View view) {  
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
                Contacts.CONTENT_URI);  
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
    }   
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				Cursor cursor = null;
				Cursor phones = null;
				String email = "";
				String phoneNum = "";
				try {
					Uri result = data.getData();
					Log.v("Error", "Got a contact result: " + result.toString());
					String id = result.getLastPathSegment();
					cursor = getContentResolver().query(Email.CONTENT_URI,
							null, Email.CONTACT_ID + "=?", new String[] { id },
							null);
					phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " =?", new String[] { id }, null);
					int phoneIdx = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int emailIdx = cursor.getColumnIndex(Email.DATA);
					if (cursor.moveToFirst()) {
						email = cursor.getString(emailIdx);
						Log.v("Error", "Got email: " + email);
					} else {
						Log.w("Error", "No results");
					}
					if(phones.moveToFirst()) {
						phoneNum = phones.getString(phoneIdx);
					} else {
						Log.w("Error", "No results");
					}
				} catch (Exception e) {
					Log.e("Error", "Failed to get email data", e);
				} finally {
					if (cursor != null) {
						cursor.close();
					}
					if(phones != null) {
						phones.close();
					}
					mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
					mTxtPhoneNo.setText(phoneNum);
					if (phoneNum.length() == 0) {
						Toast.makeText(this, "No phoneNum found for contact.",
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			}
		} else {
			Log.w("Error", "Warning: activity result not ok");
		}
	}
    
    public void autoCompleteProcess() {
    	mPeopleList = new ArrayList<Map<String, String>>();
		PopulatePeopleList();
		mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		mTxtPhoneNo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int index, long arg) {
			    Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
			    Iterator<String> myVeryOwnIterator = map.keySet().iterator();
			          while(myVeryOwnIterator.hasNext()) {
			            String key=(String)myVeryOwnIterator.next();
			            String value=(String)map.get(key);
			            mTxtPhoneNo.setText(value);
			    }               
			}
		});
		mAdapter = new SimpleAdapter(this, mPeopleList,
				R.layout.view_auto_comp_new, new String[] { "Name", "Phone",
						"Type" }, new int[] { R.id.ccontName, R.id.ccontNo,
						R.id.ccontType });

		mTxtPhoneNo.setAdapter(mAdapter);
    }
    
    public void PopulatePeopleList() {

		mPeopleList.clear();	
		
		Cursor people = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
//		CursorLoader peopleCursorLoader = new CursorLoader(MainActivity.this, ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

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
				while (phones.moveToNext()) {

					// store numbers and display a dialog letting the user
					// select which.
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
//				phones.close();
			}
		}		
//		people.close();
		startManagingCursor(people);		
	}
    
    public void onSendSmsButtonClicked() {
    	final EditText phoneNumTv = (EditText) findViewById(R.id.autoCompleteTextView1);
    	final EditText messageTv = (EditText) findViewById(R.id.editText2);
    	Button sendSmsBtn = (Button) findViewById(R.id.cursorBtn);
    	sendSmsBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phoneNumTv.getText().toString(), null, messageTv.getText().toString(), null, null);
			}
		});
    }
    
    @Override
    public void onStart() {
    	System.out.println("*********** MainActivity onStart() *********");
    	super.onStart();
    }
    
    @Override
    public void onResume() {
    	System.out.println("*********** MainActivity onResume() *********");
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	System.out.println("*********** MainActivity onPause() *********");
    	super.onPause();
    }
    
    @Override
    public void onRestart() {
    	System.out.println("*********** MainActivity onRestart() *********");
    	super.onRestart();
    }
    
    @Override
    public void onStop() {
    	System.out.println("*********** MainActivity onStop() *********");
    	super.onStop();
//    	if(people != null) {
//    		people.close();
//    	}
    }    
    
    @Override
    public void onDestroy() {
    	System.out.println("*********** MainActivity onDestroy() *********");
        super.onDestroy();  // Always call the superclass
        
        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }
	        
    public String getPhoneNumFromIntent() {
    	Intent intent = getIntent();
    	String phoneNumber = intent.getStringExtra("selContact");
    	return phoneNumber;
    }
    
    public String getPhoneNumFromSharedPrefs() {
    	SharedPreferences shaPrefFromMgr = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		String sharedVal = shaPrefFromMgr.getString("selContFromShaPrefsMgr", "There is no value in shared pref Mgr!");
		return sharedVal;
    }
    
    /*public void navigateToContacts() {
    	ImageButton contactBtn = (ImageButton) findViewById(R.id.imageButton1);
    	final EditText phoneNumTv = (EditText) findViewById(R.id.editText1);
    	
//    	phoneNumTv.setText(getPhoneNumFromIntent());
    	
    	phoneNumTv.setText(getPhoneNumFromSharedPrefs());
    	contactBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myProgressBar=(ProgressBar)findViewById(R.id.progressbar_default);		
				myProgressBar.setVisibility(View.VISIBLE);
			    //new Thread(myThread).start();
			    
				Intent cursAdapIntent = new Intent(MainActivity.this, CursorAdapterActivity.class);
				MainActivity.this.startActivity(cursAdapIntent);				
			}
		});
    }
    
    public void onSendSmsButtonClicked() {
    	final EditText phoneNumTv = (EditText) findViewById(R.id.editText1);
    	final EditText messageTv = (EditText) findViewById(R.id.editText2);
    	Button sendSmsBtn = (Button) findViewById(R.id.cursorBtn);
    	sendSmsBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phoneNumTv.getText().toString(), null, messageTv.getText().toString(), null, null);
			}
		});
    }*/
    
    
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0, 0, 0, "Notifications");
    	menu.add(0, 1, 0, "Adapters");
    	menu.add(0, 2, 0, "- C -");
    	menu.add(0, 4, 0, "- D -");
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	Toast.makeText(MainActivity.this, String.valueOf(item.getItemId()), Toast.LENGTH_LONG).show();
    	return super.onContextItemSelected(item);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(NOTIFICATIONS, NOTIFICATIONS, NOTIFICATIONS, "Notifications");
    	menu.add(NOTIFICATIONS, ADAPTERS, NOTIFICATIONS, "Adapters");
    	menu.add(0, DIALOG_BOX, 0,"Dialog Box");
    	menu.add(0, 3, 0,"Auto Complete");
    	menu.add(0, 4, 0,"Database Operations");
    	menu.add(0, 5, 0,"Accordion");
    	getMenuInflater().inflate(R.menu.auto_complete_new, menu);
        return true;
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	switch (item.getItemId()) {
    	case NOTIFICATIONS:
    		Intent notificationIntent = new Intent(MainActivity.this, NotificationsActivity.class);
    		MainActivity.this.startActivity(notificationIntent);
    		break;
    	case ADAPTERS:
    		Intent adapterIntent = new Intent(MainActivity.this, AdaptersActivity.class);
    		MainActivity.this.startActivity(adapterIntent);
    		break;
    	case DIALOG_BOX:
    		Intent dialogBoxIntent = new Intent(MainActivity.this, DialogBoxActivity.class);
    		MainActivity.this.startActivity(dialogBoxIntent);
    		break;
    	case 3:
    		Intent autoCompleteIntent = new Intent(MainActivity.this, AutoCompleteNewActivity.class);
    		MainActivity.this.startActivity(autoCompleteIntent);
    		break;
    	case 4:
    		Intent dbOperationsIntent = new Intent(MainActivity.this, DbOperationsActivity.class);
    		MainActivity.this.startActivity(dbOperationsIntent);
    		break;
    	case 5:
    		Intent accordionsIntent = new Intent(MainActivity.this, AccordionExampleActivity.class);
    		MainActivity.this.startActivity(accordionsIntent);
    		break;
    	default:
    		break;
    	}
    	return false;
    }
    
    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	SubMenu notificationsMenu = menu.addSubMenu("Notifications");
    	SubMenu adaptersMenu = menu.addSubMenu("Adapters");
    	notificationsMenu.add(NOTIFICATIONS, TOAST_MENU_ITEM, 0, "Toast");
    	notificationsMenu.add(NOTIFICATIONS, STATUS_BAR_MENU_ITEM, 1, "Status Bar");
    	notificationsMenu.add(NOTIFICATIONS, DIALOG_MENU_ITEM, 2, "Dialog");
    	adaptersMenu.add(ADAPTERS, ADAPTER_MENU_ITEM1, 0, "Adapter 1");
    	adaptersMenu.add(ADAPTERS, ADAPTER_MENU_ITEM2, 1, "Adapter 2");
    	return super.onCreateOptionsMenu(menu);
//    	super.onCreateOptionsMenu(menu);
//    	menu.add(0, 0, 0, "Notifications");
//    	menu.add(0, 1, 0, "Adapters");
//    	menu.add(0, 2, 0,"My Menu three");
//    	menu.add(0, 3, 0,"My Menu Four");
//    	menu.add(0, 4, 0,"My Menu Five");
//        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item);
    	TextView view = (TextView) findViewById(R.id.tv);
    	switch (item.getItemId()) {
    	case 0:
    	view.setText("Menu selected => "+item.getTitle());
    	break;
    	case TOAST_MENU_ITEM:
    	view.setText("Menu selected => "+item.getTitle());
    	showMsg("Toast");
    	break;
    	case STATUS_BAR_MENU_ITEM:
    	view.setText("Menu selected => "+item.getTitle());
    	showMsg("Status Bar");
    	break;
    	case DIALOG_MENU_ITEM:
    	view.setText("Menu selected => "+item.getTitle());
    	showMsg("Dialog");
    	break;
    	case ADAPTER_MENU_ITEM1:
    	view. setText("Menu selected => "+item.getTitle());
    	showMsg("Adapter 1");
    	break;
    	case ADAPTER_MENU_ITEM2:
    	view.setText("Menu selected => "+item.getTitle());
    	showMsg("Adapter 2");
    	break;
    	case ADAPTER_MENU_ITEM3:
    	view.setText("Menu selected => "+item.getTitle());
    	showMsg("Adapter 3");
    	break;
    	default:
    	view.setText("Nothing found.");
    	showMsg("Nothing found");
    	break;
    	}
    	return false;
    } 
    
    private void showMsg(String msg) {
    	Toast tmsg = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
    	tmsg.setGravity(Gravity.CENTER, tmsg.getXOffset() / 2, tmsg.getYOffset() / 2);
    	tmsg.show();
    }*/
    
    
    
//    public boolean onPrepareOptionsMenu(Menu menu) {
//    	return super.onPrepareOptionsMenu(menu);
//    }
    
}
