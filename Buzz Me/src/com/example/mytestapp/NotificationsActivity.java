package com.example.mytestapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		final TextView tv = (TextView) findViewById(R.id.lblDate);
		final EditText titleText = (EditText) findViewById(R.id.title);
		final EditText msgText = (EditText) findViewById(R.id.msg);
		callToastNotification(tv, titleText, msgText);
		callStatusbarNotification();
		callDialogNotification();
	}

	public void callToastNotification(final TextView tv,
			final EditText titleText, final EditText msgText) {
		Button toastBtn = (Button) findViewById(R.id.toastBtn);
		toastBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toastMsg = Toast.makeText(NotificationsActivity.this,
						"Title = " + titleText.getText().toString()
								+ " Message = " + msgText.getText().toString(),
						Toast.LENGTH_LONG);
				toastMsg.show();
			}
		});
	}

	public void callStatusbarNotification() {
		Button statusBarBtn = (Button) findViewById(R.id.statusbarBtn);
		final TextView tv = (TextView) findViewById(R.id.lblDate);
		final EditText titleText = (EditText) findViewById(R.id.title);
		final EditText msgText = (EditText) findViewById(R.id.msg);
		statusBarBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv.setText(msgText.getText().toString());
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						showNotification(titleText.getText().toString(),
								msgText.getText().toString());
					}
				}, 0);
			}
		});
	}

	public void callDialogNotification() {
		Button dialogBtn = (Button) findViewById(R.id.dialogBtn);
		final EditText titleText = (EditText) findViewById(R.id.title);
		final EditText msgText = (EditText) findViewById(R.id.msg);
		dialogBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						NotificationsActivity.this);
				alertDialogBuilder.setTitle(titleText.getText().toString());
				alertDialogBuilder
						.setMessage(msgText.getText().toString())
						.setCancelable(false)
						.setPositiveButton("YES",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										NotificationsActivity.this.finish();

									}
								})
						.setNegativeButton("NO",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();

									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});

	}

	public void showNotification(String title, String message) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"A New Message!", System.currentTimeMillis());
		Intent adaptersIntent = new Intent(NotificationsActivity.this,
				AdaptersActivity.class);
		PendingIntent adaptersPendingIntent = PendingIntent.getActivity(
				NotificationsActivity.this, 0, adaptersIntent, 0);
		notification.setLatestEventInfo(NotificationsActivity.this, title,
				message, adaptersPendingIntent);
		notificationManager.notify(100001, notification);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notifications, menu);
		return true;
	}

}
