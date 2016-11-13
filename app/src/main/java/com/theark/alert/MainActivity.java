package com.theark.alert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theark.emergency.Hospital;
import com.theark.emergency.HttpManager;
import com.theark.emergency.PlacesParcer;
import com.theark.emergency.PoliceStation;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


@SuppressLint("NewApi")
public class MainActivity extends Activity {

	BroadcastReceiver mReceiver;
	public static final String ALERT_FILE_NAME = "com.theark.alert.alertdata";
	public static final String ALERT_STARTUP = null;
	public static final String ALERT_MBNO_1 = "first_mb_no";
	public static final String ALERT_MBNO_2 = "second_mb_no";
	public static final String ALERT_MBNO_3 = "third_mb_no";
	public static final String ALERT_MBNO_4 = "fourth_mb_no";
	public static final String ALERT_MBNO_5 = "fifth_mb_no";
	public static final String ALERT_MBNO_NAME_1 = "first_mb_no_name";
	public static final String ALERT_MBNO_NAME_2 = "second_mb_no_name";
	public static final String ALERT_MBNO_NAME_3 = "third_mb_no_name";
	public static final String ALERT_MBNO_NAME_4 = "fourth_mb_no_name";
	public static final String ALERT_MBNO_NAME_5 = "fifth_mb_no_name";
	public static final String ALERT_MBNO_DEFAULT = "7798501560";
	private String applink = "https://www.dropbox.com/s/tq1sty4pcdra40j/com.example.test.apk?dl=0";

	String API_KEY = "AIzaSyADCtL2JTUlk_AuA5nn3Im4eXjgqGk5iyI";
	String Radius = "1500";
	
	private int BUTTON_PRESSED = 1;
	//alert screen
	Button bt_alert;
	ImageView iv_meet,iv_alert,iv_follow,iv_escort;
	//drawer



	ImageView iv_nav;
	LinearLayout ll_home,ll_people,ll_settings,ll_help,
				ll_Share,ll_About,ll_Exit,ll_Locate,ll_password,ll_escort;
	
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mdrawerListener;

	
	GPSSet gps;
	SharedPreferences alertSharedPreferences;
	
	double latitude ;
	double longitude; 
	String fullAddress = "";
	AsyncTask sharetask;

	
	//SatelliteMenu menu;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		getLocationData();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        
        init();	//initilize objects
        setBounceAnim(iv_alert);setBounceAnim(iv_follow);setBounceAnim(iv_meet);
        SetDrawer();
        
    	checkGPS();
        
		Intent i = new Intent(getApplicationContext(), UpdateService.class);

	   getApplicationContext().startService(i);
    	
        DrawerLickListener();
        AlertButton();	//onclick listener for Alert Button
        MeetMeButton(); //onclick listener for Police Button
        FollowMeButton(); //onclick listener for Hospital Button
		EscortMeButton();  //onclick listener for escort
        
        //SateliteMenuSetUp();
        //
        Ads();
        	
    }

    public void startlocationservice()
	{
    	
		Calendar c= Calendar.getInstance();
		Long t2=c.getTimeInMillis();
		Long t3=10000L;// time after which the alarm gets called
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		 Intent intentAlarm = new Intent(this, AlarmReciever.class);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,t2,t3, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
		 Log.d("starting location service", "chill");
		 Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_SHORT).show();
	}
    
	private void Ads() {
		
		
	}



	private void init() {
		//shared Pref
		alertSharedPreferences = getSharedPreferences(ALERT_FILE_NAME, MODE_PRIVATE);
		//alert
		iv_follow = (ImageView) findViewById(R.id.iv_follow);
		iv_alert = (ImageView) findViewById(R.id.iv_alert);
		iv_meet = (ImageView) findViewById(R.id.iv_meet);
		iv_escort=(ImageView) findViewById(R.id.iv_escort);
    	//drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//drawer ids
		iv_nav =(ImageView) findViewById(R.id.imageViesdgfw1);
		ll_help = (LinearLayout) findViewById(R.id.ll_Help);
		ll_home = (LinearLayout) findViewById(R.id.ll_Home);
		ll_people = (LinearLayout) findViewById(R.id.ll_People);
		ll_settings = (LinearLayout) findViewById(R.id.ll_Settings);
		ll_Share = (LinearLayout) findViewById(R.id.ll_Share);
		ll_Locate = (LinearLayout) findViewById(R.id.ll_Locate);
		ll_About = (LinearLayout) findViewById(R.id.ll_About);
		ll_Exit = (LinearLayout) findViewById(R.id.ll_Exit);
		ll_password = (LinearLayout) findViewById(R.id.ll_Password);
		ll_escort = (LinearLayout) findViewById(R.id.ll_escort);


		
		/*menu = (SatelliteMenu) findViewById(R.id.menu);
		float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        menu.setSatelliteDistance((int) distance);
        menu.setExpandDuration(500);
        menu.setCloseItemsOnClick(true);
        menu.setTotalSpacingDegree(90);
        menu.setMainImage(R.drawable.menu_button);
        
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.ic_action_group));
        items.add(new SatelliteMenuItem(2, R.drawable.ic_action_settings));
        items.add(new SatelliteMenuItem(3, R.drawable.ic_action_help));
        items.add(new SatelliteMenuItem(4, R.drawable.ic_action_share));
        items.add(new SatelliteMenuItem(5, R.drawable.ic_action_place));
        items.add(new SatelliteMenuItem(6, R.drawable.ic_action_about));
        items.add(new SatelliteMenuItem(7, R.drawable.ic_action_discard));
//        items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
        menu.addItems(items); 
		*/
		
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/trench.otf"); 
		((TextView) findViewById(R.id.tv_mainAct_titleAlert)).setTypeface(type);
	}
	
	
	public void iv_alert()
	{
		BUTTON_PRESSED = 1;
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
	     vib.vibrate(1000);
	     getLocationData(); 
		//sendAlertSMS();
		Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
	}

	private void EscortMeButton()
	{
		iv_escort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Calcualting safest route",
						Toast.LENGTH_SHORT).show();

				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vib.vibrate(1000);

				Intent escort_map_intent=new Intent(MainActivity.this, Map.class);

				startActivity(escort_map_intent);

			}
		});

	}
	

	private void AlertButton() {
        iv_alert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Hold down to Alert", 
						Toast.LENGTH_SHORT).show();
			}
		});
		iv_alert.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				iv_alert();
				return false;
			}
		});
	}
	
	private void sendAlertSMS(){
		String[] pno = new String[5];
		pno[0] = alertSharedPreferences.getString(ALERT_MBNO_1, ALERT_MBNO_DEFAULT);
		pno[1] = alertSharedPreferences.getString(ALERT_MBNO_2, ALERT_MBNO_DEFAULT);
		pno[2] = alertSharedPreferences.getString(ALERT_MBNO_3, ALERT_MBNO_DEFAULT);
		pno[3] = alertSharedPreferences.getString(ALERT_MBNO_4, ALERT_MBNO_DEFAULT);
		pno[4] = alertSharedPreferences.getString(ALERT_MBNO_5, ALERT_MBNO_DEFAULT);
		String customMsg = alertSharedPreferences.getString(SettingsActivity.CUSTOM_ALERT_MSG, "");
		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
		
		String msgLL = "Emergency_:\n"+ customMsg +"\nLat:"+latitude+"\nlong:"+longitude;
		String msgFLL = "Emergency_:\n"+ customMsg +"\n" +fullAddress+"Lat:"+latitude+"\nlong:"+longitude;
		String msgF = "Emergency_:\n"+customMsg+"\n"+fullAddress;
		
		////////////
		//////////////
		/////////	0-->5
		//////////
		//if(msgFLL.length() <= 60){
			for(int i=0;i<5;i++)
				smsManager.sendTextMessage(pno[i], null, msgFLL , null, null);
		//}else{
		//	for(int i=0;i<5;i++){
		//		smsManager.sendTextMessage(pno[i], null, msgF , null, null);
		//		smsManager.sendTextMessage(pno[i], null, msgLL , null, null);
		//	}
		//}
		
		//call to 100 or 181
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + pno[0]));
		startActivity(intent);

	}
	//police
	private void MeetMeButton(){
		 iv_meet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Toast.makeText(getApplicationContext(), "Hold down for Police Emergency", 
							Toast.LENGTH_SHORT).show();
				}
			});
		 iv_meet.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(isConnected(getApplicationContext())){
						checkGPS();
						Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
					    vib.vibrate(1000);
						String req = "https://maps.googleapis.com/maps/api/place/search/json?location="+latitude+","+longitude+"&radius=3000&types=police&key="+API_KEY;
				        ATaskPolice aaa = new ATaskPolice();
				        aaa.execute(req);
					}else{
						Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
					    vib.vibrate(1000);
					    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:100" ));
						startActivity(intent);	
					}
					return false;
				}
			});
	}
	
	private void sendMeetMeSMS(){

		String[] pno = new String[5];
		pno[0] = alertSharedPreferences.getString(ALERT_MBNO_1, ALERT_MBNO_DEFAULT);
		pno[1] = alertSharedPreferences.getString(ALERT_MBNO_2, ALERT_MBNO_DEFAULT);
		pno[2] = alertSharedPreferences.getString(ALERT_MBNO_3, ALERT_MBNO_DEFAULT);
		pno[3] = alertSharedPreferences.getString(ALERT_MBNO_4, ALERT_MBNO_DEFAULT);
		pno[4] = alertSharedPreferences.getString(ALERT_MBNO_5, ALERT_MBNO_DEFAULT);
		
		String customMsg = alertSharedPreferences.getString(SettingsActivity.CUSTOM_MEET_ME_MSG, "");
		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
		
		String msgLL = "MeetMe_:\n"+ customMsg +"\nLat:"+latitude+"\nlong:"+longitude;
		String msgFLL = "MeetMe_:\n"+ customMsg +"\n" +fullAddress+"Lat:"+latitude+"\nlong:"+longitude;
		String msgF = "MeetMe_:\n"+customMsg+"\n"+fullAddress;
		//String msgF = "MeetMe_:\n"+fullAddress;
		
		if(msgFLL.length() <= 60){
			for(int i=0;i<5;i++)
				smsManager.sendTextMessage(pno[i], null, msgFLL , null, null);
		}else{
			for(int i=0;i<5;i++){
				smsManager.sendTextMessage(pno[i], null, msgF , null, null);
				smsManager.sendTextMessage(pno[i], null, msgLL , null, null);
			}
		}
		
		/*SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("+917798501560", null,"Emergency-\nLat:"+latitude+"\n"+longitude , null, null);*/
	//	Toast.makeText(getApplicationContext(), "work", 1).show();

	}
	
	private void FollowMeButton(){
		iv_follow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Hold down to Alert", 
						Toast.LENGTH_SHORT).show();
			}
		});
		iv_follow.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(isConnected(getApplicationContext())){
					checkGPS();
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
				    vib.vibrate(1000);
					String req = "https://maps.googleapis.com/maps/api/place/search/json?location="+latitude+","+longitude+"&radius="+Radius+"&types=hospital&key="+API_KEY;
			        ATask aaa = new ATask();
			        aaa.execute(req);
				}else{
					Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
				    vib.vibrate(1000);
				    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:102" ));
					startActivity(intent);	
				}
				
				return false;
			}
		});
	}
	
	private void sendFollowMeSMS(){
		String[] pno = new String[5];
		pno[0] = alertSharedPreferences.getString(ALERT_MBNO_1, ALERT_MBNO_DEFAULT);
		pno[1] = alertSharedPreferences.getString(ALERT_MBNO_2, ALERT_MBNO_DEFAULT);
		pno[2] = alertSharedPreferences.getString(ALERT_MBNO_3, ALERT_MBNO_DEFAULT);
		pno[3] = alertSharedPreferences.getString(ALERT_MBNO_4, ALERT_MBNO_DEFAULT);
		pno[4] = alertSharedPreferences.getString(ALERT_MBNO_5, ALERT_MBNO_DEFAULT);
		
		String customMsg = alertSharedPreferences.getString(SettingsActivity.CUSTOM_FOLLOW_ME_MSG, "");
		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
		
		String msgLL = "FollowMe_:\n"+ customMsg +"\nLat:"+latitude+"\nlong:"+longitude;
		String msgFLL = "FollowMe_:\n"+ customMsg +"\n" +fullAddress+"Lat:"+latitude+"\nlong:"+longitude;
		String msgF = "FollowMe_:\n"+customMsg+"\n"+fullAddress;
		//String msgF = "FollowMe_:\n"+fullAddress;
		
		if(msgFLL.length() <= 60){
			for(int i=0;i<5;i++)
				smsManager.sendTextMessage(pno[i], null, msgFLL , null, null);
		}else{
			for(int i=0;i<5;i++){
				smsManager.sendTextMessage(pno[i], null, msgF , null, null);
				smsManager.sendTextMessage(pno[i], null, msgLL , null, null);
			}
		}
	}
	
	public String getEmailId()
	{
		 String gmail = null;

		    Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		    Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		    for (Account account : accounts) {
		        if (gmailPattern.matcher(account.name).matches()) {
		             gmail = account.name;
		        }
		    }
		    Log.d("Gmail",gmail+"--");
		    return gmail;
	}
	
public void SendEmailandpassword(String password)
{
	final String pass=password;
	
	sharetask = new AsyncTask<String, Integer, Integer>()
			{
		protected Integer doInBackground(String... params) 
		{
			Log.d("In alarm reciever","Alarm Reciever request has been made");

			HashMap<String,String> args = new HashMap<String,String>();

			// Adding the userid to the HashMap
			args.put("email_id", getEmailId());
			args.put( "password",pass);


			ServerComm server_comm = new ServerComm(getApplicationContext());
			int result = server_comm.upload(args, "http://omkya.besaba.com/ALERT/emailandpass.php");
			Log.d("afteremailandpass",result+"--");
			return result;
		}
		protected void onPostExecute(Integer result) 
		{
	        startlocationservice();

	        
		        
		}
		
			}.execute(null, null, null);
			
			

	
	}
	
	
public void SendEmailandloc(double latitude, double longitude)
{
	final double lat=latitude;
	final double longi=longitude;
	
	sharetask = new AsyncTask<String, Integer, Integer>()
			{
		protected Integer doInBackground(String... params) 
		{
			Log.d("In alarm reciever","Alarm Reciever request has been made");

			HashMap<String,String> args = new HashMap<String,String>();

			// Adding the userid to the HashMap
			args.put("email_id", getEmailId());
			args.put( "latitude", String.valueOf(lat));
			args.put( "longitude", String.valueOf(longi));


			ServerComm server_comm = new ServerComm(getApplicationContext());
			int result = server_comm.upload(args, "https://omkya.besaba.com/ALERT/firstloc.php");
			return result;
		}
		protected void onPostExecute(Integer result) 
		{
			
		        
		}
		
			}.execute(null, null, null);
			
			

	
	}
	

    
    private void getLocationData(){
    	gps = new GPSSet(MainActivity.this);
    	// check if GPS enabled		
        if(gps.canGetLocation()){
        	latitude = gps.getLatitude();
        	longitude = gps.getLongitude();
        	
        	city_();
        	
        	SendEmailandloc(latitude,longitude);
        	
        	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
        }else{
        	// can't get location
        	// GPS or Network is not enabled
        	// Ask user to enable GPS/network in settings
        	gps.showSettingsAlert();
        }
    }
    
    
    public void city_(){
    	Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
    	try {
    	 List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);

    	 if(addresses != null) {
    		 Address returnedAddress = addresses.get(0);
    		 StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
    		 for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
    			 strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
    		 }
    		 fullAddress = strReturnedAddress.toString();
    		 //Toast.makeText(getApplicationContext(), fullAddress, 0).show();
    		 /*if(BUTTON_PRESSED ==0){
    			 sendFollowMeSMS();
    		 }else if(BUTTON_PRESSED ==2){
    			 sendMeetMeSMS();
    		 }else{
    			 sendAlertSMS();
    		 }*/
    	 }else{
    		 Toast.makeText(getApplicationContext(), "No Address returned!", 0).show();	
    	 }
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	if(BUTTON_PRESSED ==0){
			 sendFollowMeSMS();
		 }else if(BUTTON_PRESSED ==2){
			 sendMeetMeSMS();
		 }else{
			 sendAlertSMS();
		 }
    }
    
    @Override
    public void onBackPressed() {
		//System.exit(0);
    	this.finish();   	
    }

    /////////////drawer
    private void SetDrawer() {
    	/**/
    	iv_nav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});
    	
    	mdrawerListener = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open,R.string.drawer_close){
			@Override
			public void onDrawerClosed(View drawerView) {
			}
			@Override
			public void onDrawerOpened(View drawerView) {
			}
		};
		mDrawerLayout.setDrawerListener(mdrawerListener);//add the listener
		//Closing and Opening the Drawer///////////////////////////////////////
		
		//getActionBar().setHomeButtonEnabled(true);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}
    
    private void DrawerLickListener(){
    	ll_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.closeDrawers();
			}
		});
    	ll_people.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.closeDrawers();
				Intent intentPeople = new Intent(MainActivity.this,PeopleActivity.class);
				startActivity(intentPeople);
			}
		});
    	ll_settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentPeople = new Intent(MainActivity.this,SettingsActivity.class);
				startActivity(intentPeople);
				mDrawerLayout.closeDrawers();
			}
		});
    	ll_help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentPeople = new Intent(MainActivity.this,HelpActivity.class);
				startActivity(intentPeople);
				mDrawerLayout.closeDrawers();
			}
		});
    	///////
    	ll_Share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
	            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            shareIntent.setType("text/plain");
	            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app! "+applink);
	            startActivity(shareIntent);
			}
		});
ll_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent shareIntent = new Intent(Intent.ACTION_SEND);
//	            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	            shareIntent.setType("text/plain");
//	            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app! "+applink);
//	            startActivity(shareIntent);
				Toast.makeText(getApplicationContext(), "in password", Toast.LENGTH_SHORT).show();
				
				
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.password_dialog);
				dialog.setCancelable(true);
				dialog.setTitle("Password");
				Button bt_password = (Button) dialog.findViewById(R.id.diag_password);
				final EditText et_password = (EditText) dialog.findViewById(R.id.password_text);
				
//				et_lat.setText(""+latitude);
//				et_long.setText(""+longitude);
				bt_password.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String password= et_password.getText().toString();
						Editor e =alertSharedPreferences.edit();
						e.putString("password",password);
						e.commit();
						SendEmailandpassword(password);
						dialog.dismiss();
						//String uri = "geo:"+ Double.parseDouble(latitude_) + "," + Double.parseDouble(longitude_) + "?q=my+street+address";
						//startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
					}
				});
				dialog.show();
				
				
			}
		});
    	ll_Locate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.locate_dialog);
				dialog.setCancelable(true);
				dialog.setTitle("Locate");
				Button bt_locate = (Button) dialog.findViewById(R.id.diag_locate);
				final EditText et_lat = (EditText) dialog.findViewById(R.id.diag_lat);
				final EditText et_long = (EditText) dialog.findViewById(R.id.diag_long);
				
				et_lat.setText(""+latitude);
				et_long.setText(""+longitude);
				bt_locate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String latitude_= et_lat.getText().toString(),
								longitude_= et_long.getText().toString();
						
						String uri = "geo:"+ Double.parseDouble(latitude_) + "," + Double.parseDouble(longitude_) + "?q=my+street+address";
						startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
					}
				});
				dialog.show();
			}
		});
    	ll_About.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentPeople = new Intent(MainActivity.this,AboutActivity.class);
				startActivity(intentPeople);
				mDrawerLayout.closeDrawers();
			}
		});
    	ll_Exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
       	super.onPostCreate(savedInstanceState);
       	mdrawerListener.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	mdrawerListener.onConfigurationChanged(newConfig);
    }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(mdrawerListener.onOptionsItemSelected(item)){
			return true;
		}
        if(item.getItemId() == R.id.action_location){
        	checkGPS();
        }
        return super.onOptionsItemSelected(item);
    }
    
  private void checkGPS() {
	  gps = new GPSSet(MainActivity.this);
  	// check if GPS enabled		
      if(gps.canGetLocation()){
      	latitude = gps.getLatitude();
      	longitude = gps.getLongitude();
      	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
      }else{
      	gps.showSettingsAlert();
      }
	}



	//BOUNCE ANIM OF BUTTONS
  	private void setBounceAnim(View view){
  		//
  		Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_down_in_out);
  		view.setAnimation(anim);
  		anim.start();
  		//
  	}

    
    
  
    @Override
    protected void onRestart() {
    	//getLocationData();	//get the location
    	super.onRestart();
    }
    @Override
    protected void onResume() {
    	//enter
    	overridePendingTransition(R.anim.anim_enter_rl,
				R.anim.anim_leave_rl);
    	//getLocationData();	//get the location
    	super.onResume();
    }
    @Override
    protected void onPause() {
    	//for back
    			overridePendingTransition(R.anim.anim_leave,
    					R.anim.anim_enter);
    			super.onPause();
		super.onPause();
    }
    
    
    
    
    
    
    
    
    /////////////////////////////////
    ///////////////////////////////

    class ATask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			Log.d("##", "fail");
			String x= "" ;x+= HttpManager.getData(params[0]);
			
			return x;
		}
		
		@Override
		protected void onPostExecute(String result) {
		//tv.setText(result);
			ArrayList<Hospital> hosp = new ArrayList<Hospital>();
			
			hosp = PlacesParcer.parseGoogleParse(result);
			
			DeatilATask dat = new DeatilATask();
			dat.execute(hosp);
						
			super.onPostExecute(result);
		}
    }
    
    class DeatilATask extends AsyncTask<ArrayList<Hospital>, ArrayList<Hospital>, ArrayList<Hospital>>{

		@Override
		protected ArrayList<Hospital> doInBackground(ArrayList<Hospital>... params) {
			
			ArrayList<Hospital> hp = params[0];
			int cnt = 0;
			for(int i=0; i< Math.min(3, hp.size());i++){
				String dreq = "https://maps.googleapis.com/maps/api/place/details/json?reference="+ hp.get(i).getReference() +"&key="+API_KEY;
				String dstr = HttpManager.getData(dreq);
				Hospital temp = PlacesParcer.parseDetailsParse(dstr);
				hp.get(i).setPhone_number(temp.getPhone_number());
				hp.get(i).setVicinity(temp.getVicinity());
				Log.d("###DET", ""+cnt+"  "+hp.get(i).getPhone_number());

				cnt++;
			}
			
			return hp;
		}
		@Override
		protected void onPostExecute(ArrayList<Hospital> result) {
			Toast.makeText(getApplicationContext(), "Calling : "+result.get(0).getName(), 1).show();
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + result.get(0).getPhone_number()));
			startActivity(intent);
			super.onPostExecute(result);
		}
    	
    }

    
    //police
    class ATaskPolice extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			Log.d("##", "fail");
			String x= "" ;x+= HttpManager.getData(params[0]);
			
			return x;
		}
		
		@Override
		protected void onPostExecute(String result) {
		//tv.setText(result);
			ArrayList<PoliceStation> ps = new ArrayList<PoliceStation>();
			
			ps = PlacesParcer.parsePoliceStation(result);
			
			DeatilATaskPolice datp = new DeatilATaskPolice();
			datp.execute(ps);
						
			super.onPostExecute(result);
		}
    }
    
    class DeatilATaskPolice extends AsyncTask<ArrayList<PoliceStation>, ArrayList<PoliceStation>, ArrayList<PoliceStation>>{

		@Override
		protected ArrayList<PoliceStation> doInBackground(ArrayList<PoliceStation>... params) {
			
			ArrayList<PoliceStation> hp = params[0];
			int cnt = 0;
			int k = Math.min(3, hp.size());
			for(int i=0; i< k;i++){
				String dreq = "https://maps.googleapis.com/maps/api/place/details/json?reference="+ hp.get(i).getReference() +"&key="+API_KEY;
				String dstr = HttpManager.getData(dreq);
				Log.d("POSTexec--", dstr);
				PoliceStation temp = PlacesParcer.parseDetailsPolice(dstr);
				hp.get(i).setPhone_number(temp.getPhone_number());
				hp.get(i).setVicinity(temp.getVicinity());
				Log.d("###DET", ""+cnt+"  "+hp.get(i).getPhone_number());

				cnt++;
			}
			
			return hp;
		}
		@Override
		protected void onPostExecute(ArrayList<PoliceStation> result) {
			Log.d("POSTexec", result.get(0).getVicinity());
			//Toast.makeText(getApplicationContext(), ""+result.get(0).getPhone_number(), 1).show();
			for(PoliceStation pp : result){
				if(!pp.getPhone_number().equals("")){
					Toast.makeText(getApplicationContext(), "Calling : "+pp.getName(), 1).show();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +pp.getPhone_number()));
					startActivity(intent);
					break;
				}else{
					Toast.makeText(getApplicationContext(), "Calling : "+pp.getName(), 1).show();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:100"));
					startActivity(intent);
					break;
				}
			}
			super.onPostExecute(result);
		}	
    }
    
    
    
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    if (activeNetwork != null && activeNetwork.isConnected()) {
        return true;
    }

    return false;

}
    
    @Override
    protected void onStop()
    {
        //unregisterReceiver(mReceiver);
        super.onStop();
    }
}
