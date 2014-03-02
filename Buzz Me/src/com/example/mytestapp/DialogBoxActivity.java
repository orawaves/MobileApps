package com.example.mytestapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class DialogBoxActivity extends Activity {
	private TextView tvDate;
	private DatePicker datePicker;
	private Button btn;
	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_box);
		setCurrentDateOnView();
		addListenerOnButton();
	}

	private void setCurrentDateOnView() {
		tvDate = (TextView) findViewById(R.id.tvDate);
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		tvDate.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));
		datePicker.init(year, month, day, null);
	}

	public void addListenerOnButton() {
		btn = (Button) findViewById(R.id.chgDtBtn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	public Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			tvDate.setText(new StringBuilder().append(month + 1).append("-")
					.append(day).append("-").append(year).append(" "));
			datePicker.init(year, month, day, null);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog_box, menu);
		return true;
	}

}
