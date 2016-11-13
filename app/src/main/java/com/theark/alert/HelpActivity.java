package com.theark.alert;

//import com.example.test.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class HelpActivity extends Activity{

	TextView tv_title,tv_sub1,tv_sub2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.help_lay);
		
		tv_title = (TextView) findViewById(R.id.tv_help_title);
		tv_sub1 = (TextView) findViewById(R.id.tv_help_subtitle);
		tv_sub2 = (TextView) findViewById(R.id.tv_help_subtitle_);
		
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/trench.otf"); 
		tv_sub1.setTypeface(type);
		tv_sub2.setTypeface(type);
		tv_title.setTypeface(type);
		
	}
	
	
	
	
    @Override
    protected void onResume() {
    	//enter
    	//overridePendingTransition(R.anim.anim_enter_rl,
		//		R.anim.anim_leave_rl);
    	super.onResume();
    }
    @Override
    protected void onPause() {
    	//for back
    			//overridePendingTransition(R.anim.anim_leave,
    			//		R.anim.anim_enter);
    			super.onPause();
		super.onPause();
    }
}
