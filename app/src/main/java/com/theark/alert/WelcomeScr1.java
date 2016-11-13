package com.theark.alert;

//import com.example.test.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeScr1 extends Activity {

	public static final String ALERT_CHECK_ACTIVITY = "alert_check_activity";
	Button bt_getStarted;
	TextView tv_welcomeTitle;
	
	SharedPreferences alertSharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_scr1);
		
		checkActivity();
		
		init();
		setAttri();
		
		bt_getStarted.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent getStartedIntent = new Intent(WelcomeScr1.this,WelcomeScr_Detials.class);
				
				startActivity(getStartedIntent);
				//perfect enter
				overridePendingTransition(R.anim.anim_enter_rl,
						R.anim.anim_leave_rl);
				
			}
		});
	}

	private void checkActivity() {
		alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
		if(alertSharedPreferences.getInt(ALERT_CHECK_ACTIVITY, 1)==1){
			
		}else{
			Intent getStartedIntent = new Intent(WelcomeScr1.this,MainActivity.class);
			startActivity(getStartedIntent);
			finish();
		}
	}

	private void init() {
		bt_getStarted = (Button) findViewById(R.id.bt_wel_getStarted);
		tv_welcomeTitle = (TextView) findViewById(R.id.tv_wel1_title);
	}
	
	private void setAttri(){
		//set Font
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/trench.otf"); 
		tv_welcomeTitle.setTypeface(type);
	}
	
    

	
	@Override
	protected void onResume() {
		if(alertSharedPreferences.getInt(ALERT_CHECK_ACTIVITY, 1)==1);
			//this.finish();
		super.onResume();
	}
	@Override
	public void onBackPressed() {
		//this.finish();
		java.lang.System.exit(0);
		super.onBackPressed();
	}
}
