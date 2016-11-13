package com.theark.alert;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

//import com.easyrewards.data.AppVars;

public class ServerComm {
	Context mContext;
	String url,keys[],values[] = null;
	int numargs = 0;
	String LastServerCommResult;

	public ServerComm(Context context) {
		mContext = context;
		 LastServerCommResult=null;
	}

	@SuppressWarnings("rawtypes")
	public int upload(HashMap<String, String> args, String i_url) {

		if(false==testConnectivity(mContext))
		{
			// TODO: IMP: Handle this case carefully - this could be a very common case.
			return -1;
		}


		InputStream is = null;
		StringBuffer response = null;

		URL url;
		HttpURLConnection connection = null;  
		try {
			//Create connection
			url = new URL(i_url);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			String urlParameters = null;
			int numargs=args.size();


			if(numargs>0)
			{
				// Get a set of the entries
				Set set = args.entrySet();
				// Get an iterator
				Iterator i = set.iterator();
				boolean first_item=true;
				// Display elements

				while(i.hasNext()) {
					Map.Entry me = (Map.Entry)i.next();
					// Encode the first key-value pair normally.
					if(true==first_item)
					{
						urlParameters = me.getKey() + "=" + URLEncoder.encode((String) me.getValue(), "UTF-8");
						first_item=false;
					}
					else
					{
						Log.d("In ServeComm",me.getKey()+"--");

						urlParameters += "&" + me.getKey() + "=" + URLEncoder.encode((String) me.getValue(), "UTF-8");
					}
					Log.d("In ServeComm", "Key: "+me.getKey()+" Value: "+me.getValue());
				}
			}
			Log.d("In ServeComm", "UrlsParams: "+urlParameters);

			connection.setRequestProperty("Content-Length", "" + 
					Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();

			//Get Response and test the response code	
			int responseCode = connection.getResponseCode();
			if(responseCode!=HttpURLConnection.HTTP_OK)
			{
				// We got an error. // TODO: Handle this error
				// TODO: Debug Only - Remove this in production
				Log.d("In ServeComm", "Invalid HTTP Reponse Code: " + responseCode + " found! Handle Error!");
				return -2; // Return error
			}
			Log.d("In ServeComm", "HTTP Reponse Code: " + responseCode);
			int response_code=responseCode;
			is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

		} catch (Exception e) {

			e.printStackTrace();
			return -2; // Return error

		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}

		// If files are downloaded, then you can ask the user to go ahead 
		Log.d("In ServeComm", "Server Reponse : " + response.toString());

		LastServerCommResult = response.toString();

		return 0;
	}
	
	static public boolean testConnectivity(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} 
		Log.i("In ServeComm", "No internet!");
		return false;
	}

	public void showSuccessDialog(String success_msg)
	{

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					((Activity) mContext).finish();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(success_msg).setPositiveButton("Finish", dialogClickListener)
		.setTitle("Confirmation").show();
	}

}
