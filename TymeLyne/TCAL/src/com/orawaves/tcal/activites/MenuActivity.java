package com.orawaves.tcal.activites;

import com.orawaves.tcal.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MenuActivity extends Activity implements OnClickListener{


	private Button btnTextShare,btnAudioShare,btnImageShare,btnLocationShare,btnSettings;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu);


		//Reffer the xml components
		btnTextShare = (Button) findViewById(R.id.btnTextShare);
		btnAudioShare = (Button) findViewById(R.id.btnAudioShare);
		btnImageShare = (Button) findViewById(R.id.bntImageShare);
		btnLocationShare = (Button) findViewById(R.id.btnLocationShare);
		btnSettings = (Button) findViewById(R.id.btnSettings);


		//Setting the listeners to buttons
		btnTextShare.setOnClickListener(this);
		btnAudioShare.setOnClickListener(this);
		btnImageShare.setOnClickListener(this);
		btnLocationShare.setOnClickListener(this);
		btnSettings.setOnClickListener(this);

		setupGUI();
	}

	/**
	 *  Any UI updations goes here
	 */
	private void setupGUI()
	{

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnTextShare:
			
			intent = new Intent(this,TextActivity.class);
			startActivity(intent);
			break;

		case R.id.btnAudioShare:
			
			intent = new Intent(this,AudioActivity.class);
			startActivity(intent);

			break;
		case R.id.bntImageShare:
			
			intent = new Intent(this,ImageActivity.class);
			startActivity(intent);

			break;
		case R.id.btnLocationShare:
			
			intent = new Intent(this,LocationActivity.class);
			startActivity(intent);

			break;
		case R.id.btnSettings:
			
			intent = new Intent(this,SettingsActivity.class);
			startActivity(intent);

			break;
		}

	}

}
