package com.example.mytestapp;

import java.util.Iterator;
import java.util.Map;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AutoCompleteActivity extends Activity {
	private AutoCompleteTextView mAuto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_complete);
		mAuto = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);		
		ContentResolver content = getContentResolver();
		Cursor cursor = content.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				PEOPLE_PROJECTION, null, null, null);
		ContactListAdapter adapter = new ContactListAdapter(this, cursor);
		mAuto.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auto_complete, menu);
		return true;
	}
	
//	@Override
//	public void onItemClick(AdapterView<?> av, View v, int index, long arg) {
//	    Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
//	    Iterator<String> myVeryOwnIterator = map.keySet().iterator();
//	          while(myVeryOwnIterator.hasNext()) {
//	            String key=(String)myVeryOwnIterator.next();
//	            String value=(String)map.get(key);
//	            mAuto.setText(value);
//	        }               
//	    }
//	});

	public static class ContactListAdapter extends CursorAdapter implements
			Filterable {
		public ContactListAdapter(Context context, Cursor c) {
			super(context, c);
			mContent = context.getContentResolver();
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final TextView view = (TextView) inflater.inflate(
					android.R.layout.simple_expandable_list_item_1, parent,
					false);
			view.setText(cursor.getString(2));
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((TextView) view).setText(cursor.getString(2));

		}

		@Override
		public String convertToString(Cursor cursor) {
			return cursor.getString(2);
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			if (getFilterQueryProvider() != null) {
				return getFilterQueryProvider().runQuery(constraint);
			}

			StringBuilder buffer = null;
			String[] args = null;
			if (constraint != null) {
				buffer = new StringBuilder();
				buffer.append("UPPER(");
				buffer.append(ContactsContract.Contacts.DISPLAY_NAME);
				buffer.append(") GLOB ?");
				args = new String[] { constraint.toString().toUpperCase() + "*" };
			}

			return mContent.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					PEOPLE_PROJECTION,
					buffer == null ? null : buffer.toString(), args, null);
		}

		private ContentResolver mContent;
	}

	private static final String[] PEOPLE_PROJECTION = new String[] {
			ContactsContract.Contacts._ID,
			ContactsContract.CommonDataKinds.Phone.NUMBER,
			ContactsContract.Contacts.DISPLAY_NAME, };

}
