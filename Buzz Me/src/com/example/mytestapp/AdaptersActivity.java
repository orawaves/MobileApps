package com.example.mytestapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdaptersActivity extends Activity {
	private Button listAdpBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adapters);		
		final CharSequence[] items = {"Cricket", "Soccer", "Basketball"};
		addListenerOnButton(items);
		TextView contactTv = (TextView) findViewById(R.id.textView2);
		Intent myIntent = getIntent();
		String selectedContact = myIntent.getStringExtra("selContact");
		contactTv.setText(selectedContact);
		
		loadSharedPreferences();
		
		Button cursorAdapBtn = (Button) findViewById(R.id.cursorBtn);
		cursorAdapBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AdaptersActivity.this, CursorAdapterActivity.class);
				AdaptersActivity.this.startActivity(intent);
			}
		});
	}
	
	public void loadSharedPreferences() {		
		SharedPreferences shaPrefFromMgr = PreferenceManager.getDefaultSharedPreferences(AdaptersActivity.this);
		String sharedVal = shaPrefFromMgr.getString("selContFromShaPrefsMgr", "There is no value in shared pref Mgr!");		
		TextView tv = (TextView) findViewById(R.id.sharedPrefValtv);
		tv.setText(sharedVal);
	}
		
	public void addListenerOnButton(final CharSequence[] items) {
		listAdpBtn = (Button) findViewById(R.id.listAdpBtn);
		listAdpBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdaptersActivity.this);
				alertDialogBuilder.setTitle("List View");
				alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						TextView tv = (TextView) findViewById(R.id.sharedPrefValtv);
						tv.setText("You Selected ---->>>>> "+items[which]);
					}
				});
				AlertDialog alertDial = alertDialogBuilder.create();
				alertDial.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adapters, menu);
		return true;
	}

}
