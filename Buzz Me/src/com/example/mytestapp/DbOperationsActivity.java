package com.example.mytestapp;

import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DbOperationsActivity extends Activity {
	
	private SQLiteDatabase db;
	private String table_name = "NotificationTable";
	private String db_name = "Test.db";
	public static final String KEY_SQID = "SID";
	public static final String KEY_TITLE = "TITLE";
	public static final String KEY_MESSAGE = "MESSAGE";
	private EditText titleEt;
	private EditText messageEt;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_db_operations);
		titleEt = (EditText) findViewById(R.id.recipeText);
		messageEt = (EditText) findViewById(R.id.editText2);
		dbOperations();
	}
	
	public boolean validateFields() {
		String title = titleEt.getText().toString();
		String message = messageEt.getText().toString();
		
		if (title.equalsIgnoreCase("")) {
			titleEt.setError("Enter title.");
			return true;
		}
		if (message.equalsIgnoreCase("")) {
			messageEt.setError("Enter message.");
			return true;
		}
		return false;
	}
	
	public void dbOperations() {
		db = openOrCreateDatabase(db_name, SQLiteDatabase.CREATE_IF_NECESSARY,
				null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ table_name
				+ " (SID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, MESSAGE TEXT);");
		Button createBtn = (Button) findViewById(R.id.button1);
		Button readBtn = (Button) findViewById(R.id.button2);
		Button updateBtn = (Button) findViewById(R.id.postMsg);
		Button deleteBtn = (Button) findViewById(R.id.button4);
		
		createBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!validateFields())
					insertOperation(db);				
			}
		});
		
		readBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				readOperation(db);				
			}
		});
		
		updateBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateOperation(db);				
			}
		});
		
		deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteOperation(db);				
			}
		});		
	}
	
	public void insertOperation(SQLiteDatabase db) {
		String title = titleEt.getText().toString();
		String message = messageEt.getText().toString();
		ContentValues contentValues = createNotification(title, message);
		db.insert(table_name, null, contentValues);
		Toast.makeText(this, "Record Inserted", Toast.LENGTH_SHORT).show();
	}
	
	public void readOperation(SQLiteDatabase db) {
		String title = titleEt.getText().toString();
		String message = messageEt.getText().toString();
		ContentValues contentValues = createNotification(title, message);
		Cursor cursor = db.query(table_name, new String[] {KEY_SQID,  KEY_TITLE,  KEY_MESSAGE}, KEY_TITLE+" = ?", null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		TextView tv2 = (TextView) findViewById(R.id.textView2);
		tv1.setText(cursor.getString(1));
		tv2.setText(cursor.getString(2));
	}
	
	public void updateOperation(SQLiteDatabase db) {
		String title = titleEt.getText().toString();
		String message = messageEt.getText().toString();
		ContentValues contentValues = createNotification(title, message);
		db.update(table_name, contentValues, KEY_TITLE + " = testing", null);
		Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT);
	}
	
	public void deleteOperation(SQLiteDatabase db) {
		String title = "testing 4";
		db.delete(table_name, KEY_TITLE+" = "+title, null);
		Toast.makeText(this, "Record Deleted", Toast.LENGTH_LONG).show();
	}
	
	public ContentValues createNotification(String title, String message) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_TITLE, title);
		contentValues.put(KEY_MESSAGE, message);
		return contentValues;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.db_operations, menu);
		return true;
	}

}
