package com.orawaves.tcal;

import com.orawaves.tcal.activites.MenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;


public class FlashActivity extends Activity {
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.flash);
	
		/**setup the UI */
		creatingSplashScreen();
		
	}
	
	/**
     * setting the time for flash screen
     */
    private void creatingSplashScreen()
    {
        new CountDownTimer(2000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
            }
            public void onFinish()
            {
            	setupUI();
            }

        }.start();
    }
    
    /**
     * setup the GUI
     */
    private void setupUI(){
		
    	//Calling the menu screen
    	Intent menuIntent  = new Intent(this, MenuActivity.class);
    	startActivity(menuIntent);
    	finish();
		 
	}
    

}
