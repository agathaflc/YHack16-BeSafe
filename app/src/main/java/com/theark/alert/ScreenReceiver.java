package com.theark.alert;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.MailTo;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class ScreenReceiver extends BroadcastReceiver 
{
	
	 static int count=0;
	static CountDownTimer cdt=new CountDownTimer(3000,1000) {
		
		@Override
		public void onTick(long arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			count=0;
		}
	};
	public static CountDownTimer getTimer()
	{
		return cdt;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) ||intent.getAction().equals(Intent.ACTION_SCREEN_ON)) 
		{
		Log.d("onercv","onrecv");
         count++;
         if(count==1)
        	 cdt.start();
         if(count==3)
         {
        	 
        	 Log.d("count",count+"");
        	 
//        	 KeyguardManager km = (KeyguardManager)context. getSystemService(Context.KEYGUARD_SERVICE); 
//        	 final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock"); 
//        	 kl.disableKeyguard(); 
//
//        	 PowerManager pm = (PowerManager)context. getSystemService(Context.POWER_SERVICE); 
//        	 WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
//        	                                  | PowerManager.ACQUIRE_CAUSES_WAKEUP
//        	                                  | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
//        	 wakeLock.acquire();
        	 
        	 
             	GPSSet gps = new GPSSet(context);
             	  SmsManager sms = SmsManager.getDefault(); 
             	  
             	  Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
               	
               	 List<Address> addresses = null;
				try {
					addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               	StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
             	 if(addresses != null) {
             		 Address returnedAddress = addresses.get(0);
             		 
             		 for(int ij=0; ij<returnedAddress.getMaxAddressLineIndex(); ij++) {
             			 strReturnedAddress.append(returnedAddress.getAddressLine(ij)).append("\n");
             		 }
             	 
              	String uri = "http://maps.google.com/?q="+gps.getLatitude()
              			+","+gps.getLongitude()+" Address : "+ strReturnedAddress.toString();

              	
              	String msg_from = context.getSharedPreferences("com.theark.alert.alertdata", context.MODE_PRIVATE).getString("first_mb_no", "+919860328030");
               	Log.d("SMS","sending sms"+uri+"----"+msg_from);
             	Toast.makeText(context,"sending sms"+uri+"----"+msg_from, Toast.LENGTH_LONG).show();


                	 sms.sendTextMessage(msg_from, null,"My location "+uri, null, null);

        	 
        	
        	 
				/*Intent intentPeople = new Intent(context,MainActivity.class);
			   	 intentPeople.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				context.startActivity(intentPeople)*/;
        	 count=0;
         }
		}
     
        
	}

}}
