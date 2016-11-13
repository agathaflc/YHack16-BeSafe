package com.theark.alert;

//import com.example.test.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.about_activity);
		
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/trench.otf"); 
		((TextView) findViewById(R.id.tv_about_title)).setTypeface(type);
		((TextView) findViewById(R.id.TextdsfView02)).setTypeface(type);

	}
}
