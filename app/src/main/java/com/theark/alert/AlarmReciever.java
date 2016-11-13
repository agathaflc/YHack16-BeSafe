package com.theark.alert;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

/*
 * this class gets called after every 1 minute. hence here sending delivery guy's location and fetching new delivery dat from server 
 *  
 * 
 */

public class AlarmReciever extends BroadcastReceiver implements LocationListener{
Hashtable<String, Integer> ht;
SharedPreferences prefs;
private static double lat, longi;
private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1;
protected LocationManager locationManager;
boolean gps_enabled,network_enabled;
Location Loca;
Context con;
SharedPreferences settings;
AsyncTask sharetask;




	@Override
	public void onReceive(Context con1, Intent arg1) {
		// TODO Auto-generated method stub

		con=con1;
		locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
		gps_enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		network_enabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
		 Log.d("recieved location service", "chill");

        //choose the best location provide and check if they are not null also configure them to display lastknown location of user to minimize delay in finding location.
		if (network_enabled) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			Log.d("In Alarm Reciever","praying to be here");
			if (locationManager != null)
            {
				Log.d("In Alarm Reciever","dangerous circumstances");

            Loca = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            
            if(Loca!=null)
            {			Log.d("In Alarm Reciever","this could be bad");

           onLocationChanged(Loca); 

            }

            locationManager.requestLocationUpdates(provider, 0, 0,this);    
            }

		} else if (gps_enabled) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		}
		
		else if (!gps_enabled && !network_enabled)
         {
             // no network provider is enabled
			Log.d("In Alarm Reciever","i am finished");
         }
	 
	}




//come here whenver delivery guy's location updates i.e after min_distance and min_time as specified above
	@Override
	public void onLocationChanged(Location loca) {
		// TODO Auto-generated method stub
		try
		{
		//if(loca!=null)
		{
		lat = loca.getLatitude();
		longi = loca.getLongitude();
		
		 settings = con.getSharedPreferences("com.theark.alert.alertdata", 0);

		  SharedPreferences.Editor editor = settings.edit();
	        editor.putString("latitude", lat+"");
	        editor.putString("longitude", longi+"");
	        editor.commit();
		
	        //send location and delivery guy id in an async task.
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


				ServerComm server_comm = new ServerComm(con);
				int result = server_comm.upload(args, "http://omkya.besaba.com/ALERT/updateloc.php");
				return result;
			}
			protected void onPostExecute(Integer result) 
			{
				// Log.v(AppVars.LOG_TAG, "Result Querying deliveries: "+result);
			       // Log.d("Requested Data :- ",AppVars.LastServerCommResult+" --  ");
			      //  String requested_data=AppVars.LastServerCommResult;
			        
//			        try {
//			        	
//			        	//parse the recied delivery data in json format and store it in aglobal variable in appvars
//			        	
//						AppVars.DeliveryJSON=new JSONObject(requested_data.substring(requested_data.indexOf("{"), requested_data.lastIndexOf("}") + 1));
//						
//						
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			        catch(NullPointerException ne)
//			        {
//			        	ne.printStackTrace();
//			        	//Toast.makeText(con,"No new data recieved", Toast.LENGTH_SHORT).show();
//			        }
//			        catch(StringIndexOutOfBoundsException se)
//			        {
//			        	se.printStackTrace();
//			        	//Toast.makeText(con,"No useful data recieved", Toast.LENGTH_SHORT).show();
//			        }
//			        catch(UnknownHostException uhe)
//			        {
//			        	uhe.printStackTrace();
//			        	//Toast.makeText(con,"No useful data recieved", Toast.LENGTH_SHORT).show();
//			        }
			        
			}
			
				}.execute(null, null, null);
				
				

		
		}
		}catch(NullPointerException ne)
		{
			ne.printStackTrace();
		}
		
	}


	
	
	



	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	// this method is called to store the parsed json data in appropriate data structures in load class
	
	
	public String getEmailId()
	{
		 String gmail = null;

		    Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		    Account[] accounts = AccountManager.get(con).getAccounts();
		    for (Account account : accounts) {
		        if (gmailPattern.matcher(account.name).matches()) {
		             gmail = account.name;
		        }
		    }
		    Log.d("Gmail",gmail+"--");
		    return gmail;
	}

}
