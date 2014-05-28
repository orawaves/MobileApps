package com.orawaves.tcal.activites;

import java.io.File;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orawaves.tcal.R;

public class TextActivity extends Activity implements OnClickListener {

	private EditText edtTextToShare;
	private Button btnFBShare;
	SocialAuthAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_share);
		
		edtTextToShare = (EditText) findViewById(R.id.edtTextShare);
		btnFBShare = (Button) findViewById(R.id.btnFbShare);
		
		btnFBShare.setOnClickListener(this);
		
		
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnFbShare:
			
			// Add it to Library
			adapter = new SocialAuthAdapter(new ResponseListener());
			// Add providers
			adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		  //adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
   		  //adapter.addProvider(Provider.GOOGLEPLUS, R.drawable.googleplus);
	        // Providers require setting user call Back url
			adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
			adapter.addCallBack(Provider.YAMMER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

			// Enable Provider
			adapter.enable(btnFBShare);
			
			break;
		}
		
	}
	
	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			Log.d("ShareButton", "Authentication Successful");
			btnFBShare.setText("Post Wall");

			// Get name of provider after authentication
			final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);
			Toast.makeText(TextActivity.this, providerName + " connected", Toast.LENGTH_LONG).show();

			

			// Please avoid sending duplicate message. Social Media Providers
			// block duplicate messages.

			btnFBShare.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					adapter.updateStatus("Share text", new MessageListener(), false);
				}
			});

			// Share via Email Intent
			if (providerName.equalsIgnoreCase("share_mail")) {
				// Use your own code here
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
						"vineet.aggarwal@3pillarglobal.com", null));
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test");
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
						"image5964402.png");
				Uri uri = Uri.fromFile(file);
				emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivity(Intent.createChooser(emailIntent, "Test"));
			}

			// Share via mms intent
			if (providerName.equalsIgnoreCase("share_mms")) {

				// Use your own code here
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
						"image5964402.png");
				Uri uri = Uri.fromFile(file);

				Intent mmsIntent = new Intent(Intent.ACTION_SEND, uri);
				mmsIntent.putExtra("sms_body", "Test");
				mmsIntent.putExtra(Intent.EXTRA_STREAM, uri);
				mmsIntent.setType("image/png");
				startActivity(mmsIntent);
			}

		}

		@Override
		public void onError(SocialAuthError error) {
			Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("ShareButton", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Share-Button", "Dialog Closed by pressing Back Key");
		}

	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onExecute(String provider, Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
				Toast.makeText(TextActivity.this, "Message posted on " + provider, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(TextActivity.this, "Message not posted on " + provider, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}
	
}
