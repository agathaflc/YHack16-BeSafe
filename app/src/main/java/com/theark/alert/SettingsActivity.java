package com.theark.alert;

//import com.example.test.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity{

	public static final String CUSTOM_FOLLOW_ME_MSG = "custom_follow";
	public static final String CUSTOM_ALERT_MSG ="custom_alert";
	public static final String CUSTOM_MEET_ME_MSG = "custom_meet";
	LinearLayout ll_reset,ll_delete,ll_alertMsg,ll_followMsg,ll_meetMsg;
	Switch sw_gps;
	//EditText et_alert,et_follow,et_meet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.settings_lay);
	
		init();
		setAttri();
		Reset();
		//Delete();
		SetAlertMsg();
		SetMeetMeMsg();
		SetFollowMeMsg();
		SwitchGPS();
	}
	
	
	
	private void setAttri() {
		SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
		//et_alert.setText(alertSharedPreferences.getString(CUSTOM_ALERT_MSG, ""));
		//et_follow.setText(alertSharedPreferences.getString(CUSTOM_FOLLOW_ME_MSG, ""));
		//et_meet.setText(alertSharedPreferences.getString(CUSTOM_MEET_ME_MSG, ""));
	
	}



	private void SetFollowMeMsg() {
		ll_followMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
				final Dialog diag = new Dialog(SettingsActivity.this);
				diag.setContentView(R.layout.setmessage_dialog);
				diag.setTitle("Custom FollowMe Message");
				Button bt_set = (Button) diag.findViewById(R.id.diag_set);
				final EditText et_msg = (EditText) diag.findViewById(R.id.diag_message);
				et_msg.setText(alertSharedPreferences.getString(CUSTOM_MEET_ME_MSG, ""));
				
				bt_set.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String newFollow = "";
						newFollow = et_msg.getText().toString();
						SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
						Editor editor = alertSharedPreferences.edit();
						editor.putString(CUSTOM_FOLLOW_ME_MSG, newFollow);
						editor.commit();
						Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
					     vib.vibrate(50);
						Toast.makeText(getApplicationContext(), "Set FollowMe Msg Successful!",Toast.LENGTH_SHORT).show();
						diag.dismiss();
					}
				});
				diag.show();
			}
		});
	}

	private void SetMeetMeMsg() {
		ll_meetMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
				final Dialog diag = new Dialog(SettingsActivity.this);
				diag.setContentView(R.layout.setmessage_dialog);
				diag.setTitle("Custom MeetMe Message");
				Button bt_set = (Button) diag.findViewById(R.id.diag_set);
				final EditText et_msg = (EditText) diag.findViewById(R.id.diag_message);
				et_msg.setText(alertSharedPreferences.getString(CUSTOM_MEET_ME_MSG, ""));
				
				bt_set.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String newMeet = "";
						newMeet = et_msg.getText().toString();
						SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
						Editor editor = alertSharedPreferences.edit();
						editor.putString(CUSTOM_MEET_ME_MSG, newMeet);
						editor.commit();
						Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
					     vib.vibrate(50);
						Toast.makeText(getApplicationContext(), "Set MeetMe Msg Successful!",Toast.LENGTH_SHORT).show();
						diag.dismiss();
					}
				});
				diag.show();
			}
		});
	}



	private void SetAlertMsg() {
		ll_alertMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
				final Dialog diag = new Dialog(SettingsActivity.this);
				diag.setContentView(R.layout.setmessage_dialog);
				diag.setTitle("Custom Alert Message");
				Button bt_set = (Button) diag.findViewById(R.id.diag_set);
				final EditText et_msg = (EditText) diag.findViewById(R.id.diag_message);
				et_msg.setText(alertSharedPreferences.getString(CUSTOM_ALERT_MSG, ""));
				
				bt_set.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String newAlert = "";
						newAlert = et_msg.getText().toString();
						SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
						Editor editor = alertSharedPreferences.edit();
						editor.putString(CUSTOM_ALERT_MSG, newAlert);
						editor.commit();
						Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
					     vib.vibrate(50);
						Toast.makeText(getApplicationContext(), "Set Alert Msg Successful!",Toast.LENGTH_SHORT).show();
						diag.dismiss();
					}
				});
				diag.show();
			}
		});
	}



	private void SwitchGPS() {
		// TODO Auto-generated method stub
		
	}



	private void Delete() {
		ll_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog diag = new Dialog(SettingsActivity.this);
				diag.setContentView(R.layout.warning_dialog);
				diag.setTitle("Warning!");
				Button bt_ok = (Button) diag.findViewById(R.id.diag_warning_ok);
				Button bt_cancel = (Button) diag.findViewById(R.id.diag_warning_cancel);
				TextView tv_msg = (TextView) diag.findViewById(R.id.diag_warning_tv);
				tv_msg.setText("The Application will be Reset to Dafault.\n Proceed..?");
				
				bt_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String[] mbnodefault = new String[]{"1st E.No","2nd E.No","3rd E.No","4th E.No","5th E.No"};
						SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
						Editor editor = alertSharedPreferences.edit();
						editor.putInt(WelcomeScr1.ALERT_CHECK_ACTIVITY, 1);
						editor.putString(MainActivity.ALERT_MBNO_1, mbnodefault[0]);
						editor.putString(MainActivity.ALERT_MBNO_2, mbnodefault[1]);
						editor.putString(MainActivity.ALERT_MBNO_3, mbnodefault[2]);
						editor.putString(MainActivity.ALERT_MBNO_4, mbnodefault[3]);
						editor.putString(MainActivity.ALERT_MBNO_5, mbnodefault[4]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_1, mbnodefault[0]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_2, mbnodefault[1]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_3, mbnodefault[2]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_4, mbnodefault[3]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_5, mbnodefault[4]);
						editor.commit();
						Toast.makeText(getApplicationContext(), "Contacts Deleted",Toast.LENGTH_SHORT).show();
					}
				});
				diag.show();
			}
		});
	}



	private void Reset() {
		ll_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog diag = new Dialog(SettingsActivity.this);
				diag.setContentView(R.layout.warning_dialog);
				diag.setTitle("Warning!");
				Button bt_ok = (Button) diag.findViewById(R.id.diag_warning_ok);
				Button bt_cancel = (Button) diag.findViewById(R.id.diag_warning_cancel);
				TextView tv_msg = (TextView) diag.findViewById(R.id.diag_warning_tv);
				tv_msg.setText("The Application will be Reset to Dafault.\nProceed..?");
				tv_msg.setTextSize(20);
				bt_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String[] mbnodefault = new String[]{"1st E.No","2nd E.No","3rd E.No","4th E.No","5th E.No"};
						SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
						Editor editor = alertSharedPreferences.edit();
						editor.putInt(WelcomeScr1.ALERT_CHECK_ACTIVITY, 1);
						editor.putString(MainActivity.ALERT_MBNO_1, mbnodefault[0]);
						editor.putString(MainActivity.ALERT_MBNO_2, mbnodefault[1]);
						editor.putString(MainActivity.ALERT_MBNO_3, mbnodefault[2]);
						editor.putString(MainActivity.ALERT_MBNO_4, mbnodefault[3]);
						editor.putString(MainActivity.ALERT_MBNO_5, mbnodefault[4]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_1, mbnodefault[0]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_2, mbnodefault[1]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_3, mbnodefault[2]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_4, mbnodefault[3]);
						editor.putString(MainActivity.ALERT_MBNO_NAME_5, mbnodefault[4]);
						editor.commit();
						Toast.makeText(getApplicationContext(), "Reset Complete",Toast.LENGTH_SHORT).show();
						diag.dismiss();
					}
				});
				bt_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						diag.dismiss();
					}
				});
				diag.show();
			}
		});
	}



	private void init() {
		//ll_delete = (LinearLayout) findViewById(R.id.ll_set_delete);
		ll_reset = (LinearLayout) findViewById(R.id.ll_set_reset);
		ll_alertMsg = (LinearLayout) findViewById(R.id.ll_set_alertmsg);
		ll_meetMsg = (LinearLayout) findViewById(R.id.ll_set_meetme);
		ll_followMsg = (LinearLayout) findViewById(R.id.ll_set_followmemsg);
		
		ll_followMsg.setVisibility(View.INVISIBLE);
		ll_meetMsg.setVisibility(View.INVISIBLE);
		//et_alert = (EditText) findViewById(R.id.et_set_alertmsg);
		//et_meet = (EditText) findViewById(R.id.et_set_meetmemsg);
		//et_follow = (EditText) findViewById(R.id.et_set_followme);
		
		//font
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/trench.otf"); 
		((TextView) findViewById(R.id.tv_set_titleAlert)).setTypeface(type);
	}


	private void turnGPSOn(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}

	private void turnGPSOff(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);


	   }
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
    		//	overridePendingTransition(R.anim.anim_leave,
    		//			R.anim.anim_enter);
    			super.onPause();
		super.onPause();
    }
}
