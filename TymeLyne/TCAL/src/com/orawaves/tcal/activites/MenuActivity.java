package com.orawaves.tcal.activites;

import com.orawaves.tcal.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MenuActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu);
		
		
		setupGUI();
	}
	
	private void setupGUI()
	{
		
	}
	
}