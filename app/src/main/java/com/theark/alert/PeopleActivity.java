package com.theark.alert;

//import com.example.test.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PeopleActivity extends Activity{

	Button bt_Save;
	ListView lv_mbNo;
	String[] mbnos,mbnames;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.people_lay);
		
		mbnos = new String[]{"1st E.No","2nd E.No","3rd E.No","4th E.No","5th E.No"};
		mbnames = new String[]{"1st E.No","2nd E.No","3rd E.No","4th E.No","5th E.No"};
		bt_Save = (Button) findViewById(R.id.bt_people_save);
		lv_mbNo = (ListView) findViewById(R.id.people_listView1);
		
		setAttri();
		//setAdapter();
		getContactNos();
		
		
		bt_Save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
				Editor editor = alertSharedPreferences.edit();
				//editor.putInt(WelcomeScr1.ALERT_CHECK_ACTIVITY, 0);
				editor.putString(MainActivity.ALERT_MBNO_1, mbnos[0]);
				editor.putString(MainActivity.ALERT_MBNO_2, mbnos[1]);
				editor.putString(MainActivity.ALERT_MBNO_3, mbnos[2]);
				editor.putString(MainActivity.ALERT_MBNO_4, mbnos[3]);
				editor.putString(MainActivity.ALERT_MBNO_5, mbnos[4]);
				
				editor.putString(MainActivity.ALERT_MBNO_NAME_1, mbnames[0]);
				editor.putString(MainActivity.ALERT_MBNO_NAME_2, mbnames[1]);
				editor.putString(MainActivity.ALERT_MBNO_NAME_3, mbnames[2]);
				editor.putString(MainActivity.ALERT_MBNO_NAME_4, mbnames[3]);
				editor.putString(MainActivity.ALERT_MBNO_NAME_5, mbnames[4]);
				
				editor.commit();
				
				Toast.makeText(getApplicationContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
			}
		});

		
	}
	
	private void setAttri(){
		//set Font
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/trench.otf"); 
		((TextView) findViewById(R.id.tv_people_title)).setTypeface(type);
		//set nos in listview
		SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
		mbnos[0] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_1, "1st E.No");
		mbnos[1] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_2, "2nd E.No");
		mbnos[2] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_3, "3rd E.No");
		mbnos[3] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_4, "4th E.No");
		mbnos[4] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_5, "5th E.No");
		
		mbnames[0] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_NAME_1, "1st E.No");
		mbnames[1] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_NAME_2, "2nd E.No");
		mbnames[2] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_NAME_3, "3rd E.No");
		mbnames[3] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_NAME_4, "4th E.No");
		mbnames[4] = alertSharedPreferences.getString(MainActivity.ALERT_MBNO_NAME_5, "5th E.No");
		
		setAdapter();
	}
	
	private void setAdapter(){
		adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1,  mbnames);
		 lv_mbNo.setAdapter(adapter);
	}
	
	private void getContactNos() {
		 //setAdapter();
		 lv_mbNo.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
               int position, long id) {
              
              Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
              pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
              startActivityForResult(pickContactIntent, position);
            }
       });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( resultCode == RESULT_OK) {
	        if (requestCode == 0) {
	        	String name = "";
	            {
		            Uri contactUri = data.getData();
		            String[] projection = {Phone.NUMBER};
		            Cursor cursor = getContentResolver()
		                    .query(contactUri, projection, null, null, null);
		            cursor.moveToFirst();
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            String number = cursor.getString(column);
		            Toast.makeText(getApplicationContext(), number, 0).show();
		            mbnos[0] = number;
		            name = ""+number;
	            }
	            {
		        	Uri contactData = data.getData();
		            Cursor c =  managedQuery(contactData, null, null, null, null);
		            if (c.moveToFirst()) {
		              name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		              Toast.makeText(getApplicationContext(), name, 0).show();
		            }
		            mbnames[0] = name;
		        }
	            setAdapter();
	         }else if (requestCode == 1) {
	        	 String name = "";
		            {
			            Uri contactUri = data.getData();
			            String[] projection = {Phone.NUMBER};
			            Cursor cursor = getContentResolver()
			                    .query(contactUri, projection, null, null, null);
			            cursor.moveToFirst();
			            int column = cursor.getColumnIndex(Phone.NUMBER);
			            String number = cursor.getString(column);
			            Toast.makeText(getApplicationContext(), number, 0).show();
			            mbnos[1] = number;
			            name = ""+number;
		            }
		            {
			        	Uri contactData = data.getData();
			            Cursor c =  managedQuery(contactData, null, null, null, null);
			            if (c.moveToFirst()) {
			              name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			              Toast.makeText(getApplicationContext(), name, 0).show();
			            }
			            mbnames[1] = name;
			        }
		            setAdapter();
	        }else if (requestCode == 2) {
	        	String name = "";
	            {
		            Uri contactUri = data.getData();
		            String[] projection = {Phone.NUMBER};
		            Cursor cursor = getContentResolver()
		                    .query(contactUri, projection, null, null, null);
		            cursor.moveToFirst();
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            String number = cursor.getString(column);
		            Toast.makeText(getApplicationContext(), number, 0).show();
		            mbnos[2] = number;
		            name = ""+number;
	            }
	            {
		        	Uri contactData = data.getData();
		            Cursor c =  managedQuery(contactData, null, null, null, null);
		            if (c.moveToFirst()) {
		              name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		              Toast.makeText(getApplicationContext(), name, 0).show();
		            }
		            mbnames[2] = name;
		        }
	            setAdapter();
	        }else if (requestCode == 3) {
	        	String name = "";
	            {
		            Uri contactUri = data.getData();
		            String[] projection = {Phone.NUMBER};
		            Cursor cursor = getContentResolver()
		                    .query(contactUri, projection, null, null, null);
		            cursor.moveToFirst();
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            String number = cursor.getString(column);
		            Toast.makeText(getApplicationContext(), number, 0).show();
		            mbnos[3] = number;
		            name = ""+number;
	            }
	            {
		        	Uri contactData = data.getData();
		            Cursor c =  managedQuery(contactData, null, null, null, null);
		            if (c.moveToFirst()) {
		              name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		              Toast.makeText(getApplicationContext(), name, 0).show();
		            }
		            mbnames[3] = name;
		        }
	            setAdapter();
	        }else if (requestCode == 4) {
	        	String name = "";
	            {
		            Uri contactUri = data.getData();
		            String[] projection = {Phone.NUMBER};
		            Cursor cursor = getContentResolver()
		                    .query(contactUri, projection, null, null, null);
		            cursor.moveToFirst();
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            String number = cursor.getString(column);
		            Toast.makeText(getApplicationContext(), number, 0).show();
		            mbnos[4] = number;
		            name = ""+number;
	            }
	            {
		        	Uri contactData = data.getData();
		            Cursor c =  managedQuery(contactData, null, null, null, null);
		            if (c.moveToFirst()) {
		              name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		              Toast.makeText(getApplicationContext(), name, 0).show();
		            }
		            mbnames[4] = name;
		        }
	            setAdapter();
	        }
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
    			//overridePendingTransition(R.anim.anim_leave,
    			//		R.anim.anim_enter);
    			super.onPause();
		super.onPause();
    }
}
