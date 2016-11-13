package com.theark.alert;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ReadSMS extends BroadcastReceiver {

	String msg_from;
	Double lati=0.0,longi=0.0;
	SharedPreferences alertSharedPreferences;
	public static final String ALERT_FILE_NAME = "com.theark.alert.alertdata";
	GPSSet gps;


	@SuppressLint("ShowToast") @Override
	public void onReceive(Context arg0, Intent intent) {

		alertSharedPreferences = arg0.getSharedPreferences(ALERT_FILE_NAME, arg0.MODE_PRIVATE);

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                	Log.d("SMS","ala rey sms");
                	Toast.makeText(arg0,"ala rey sms" , Toast.LENGTH_SHORT).show();
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                    	Log.d("SMS","reading sms"+msgBody);
                    	Toast.makeText(arg0,"reading sms"+msgBody , Toast.LENGTH_SHORT).show();


                        if(msgBody.contains("Alert@"+alertSharedPreferences.getString("password", "")))
                        {
                        	gps = new GPSSet(arg0);
                        	  SmsManager sms = SmsManager.getDefault(); 
                        	  
                        	  Geocoder geocoder = new Geocoder(arg0, Locale.ENGLISH);
                          	
                          	 List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
                          	StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                        	 if(addresses != null) {
                        		 Address returnedAddress = addresses.get(0);
                        		 
                        		 for(int ij=0; ij<returnedAddress.getMaxAddressLineIndex(); ij++) {
                        			 strReturnedAddress.append(returnedAddress.getAddressLine(ij)).append("\n");
                        		 }
                        	 }
                          	 
                         	String uri = "http://maps.google.com/?q="+gps.getLatitude()
                         			+","+gps.getLongitude()+" Address : "+ strReturnedAddress.toString();

                          	Log.d("SMS","sending sms"+uri+"----"+msg_from);
                        	Toast.makeText(arg0,"sending sms"+uri+"----"+msg_from, Toast.LENGTH_LONG).show();


                           	 sms.sendTextMessage(msg_from, null,"My location "+uri, null, null);



                        }
                        if(msgBody.contains("Emergency_:")||
                        		msgBody.contains("MeetMe_:")||
                        		msgBody.contains("FollowMe_:")){
                        	Toast.makeText(arg0, msgBody, Toast.LENGTH_LONG).show();
                            
                        	 AlarmManager am = (AlarmManager) arg0.getSystemService(Context.ALARM_SERVICE);
                        	 
                            Vibrator vib = (Vibrator) arg0.getSystemService(Context.VIBRATOR_SERVICE);  
            			     	vib.vibrate(8000);
            			     	startHeadService(arg0);
            			     	if(msgBody.contains("Lat:") || msgBody.contains("long:")){
            			     	try {
									String[] tem = msgBody.split("Lat:");
									lati = Double.parseDouble(tem[1].substring(0, 9));
									String[] tem1 = msgBody.split("long:");
									longi = Double.parseDouble(tem1[1].substring(1, 9));
									Uri uri = Uri.parse("geo:" + lati  + "," + longi +"?z=10");
									Toast.makeText(arg0, "lat->"+lati+"\nlong->"+longi, 0).show();
									Intent mapintent = new Intent(android.content.Intent.ACTION_VIEW, uri);
									mapintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									arg0.startActivity(mapintent);
								} catch (Exception e) {
									e.printStackTrace();
								}
            			     	}
                             
                        }
                        ///////
          /*              double latitude = 40.714728;
                        double longitude = -73.998672;
                        String uri = "geo:"+ latitude + "," + longitude + "?q=my+street+address";
                        arg0.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
*/
                        //////
                        
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
	}
	
	private void startHeadService(Context c) {
        //Context context = getActivity();
		MediaPlayer mediaPlayer = MediaPlayer.create(c, R.raw.aud);
		mediaPlayer.start();
		SharedPreferences sp = c.getSharedPreferences("sms", 0);
		Editor ed = sp.edit();
		ed.putString("sms_name", getContactName(c, getContactName(c, msg_from)));
		ed.putString("sms_lat", ""+lati);
		ed.putString("sms_long", ""+longi);
		ed.putString("sms_num", ""+msg_from);
		ed.commit();
		Intent hi = new Intent(c, com.theark.chathead.HeadService.class);
		c.startService(hi);
    }
	
	
	public static String getContactName(Context context, String phoneNumber) {
	    ContentResolver cr = context.getContentResolver();
	    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
	    Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
	    if (cursor == null) {
	        return null;
	    }
	    String contactName = null;
	    if(cursor.moveToFirst()) {
	        contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
	    }

	    if(cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }

	    return contactName;
	}

}
