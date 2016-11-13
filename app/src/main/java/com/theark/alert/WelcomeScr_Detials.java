package com.theark.alert;

//import com.example.test.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeScr_Detials extends Activity{

	
	
	TextView tv_title;
	Button bt_prev,bt_done;
	ListView lv_mbNo;
	String[] mbnos,mbnames;
	ArrayAdapter<String> adapter;
	//public ArrayAdapter<ContactDetials> Contacts_;
	//public ArrayAdapter<ContactDetials> listContactId;
	//public ArrayAdapter<ContactDetials> listMobileNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.welcome_sce_detials);
	
		init();
		setAttri();
		//getContacts();
		buttonListeners();
		getContactNos();
	}
	
	private void getContactNos() {
		 setAdapter();
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
	private void setAdapter(){
		adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1,  mbnames);
		 lv_mbNo.setAdapter(adapter);
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
	
	private void buttonListeners() {
		bt_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mbnos[0].equals("1st E.No") || mbnos[1].equals("2nd E.No") || mbnos[2].equals("3rd E.No")
						|| mbnos[3].equals("4th E.No") || mbnos[4].equals("5th E.No")){
					Toast.makeText(getApplicationContext(), "Add 5 Emergency Numbers",Toast.LENGTH_SHORT).show();
				}else{
						SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
						Editor editor = alertSharedPreferences.edit();
						editor.putInt(WelcomeScr1.ALERT_CHECK_ACTIVITY, 0);
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
						Intent doneIntent = new Intent(WelcomeScr_Detials.this,MainActivity.class);
						//perfect enter
						startActivity(doneIntent);
						
				}
			}
		});
		
		bt_prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences alertSharedPreferences = getSharedPreferences(MainActivity.ALERT_FILE_NAME, MODE_PRIVATE);
				Editor editor = alertSharedPreferences.edit();
				editor.putInt(WelcomeScr1.ALERT_CHECK_ACTIVITY, 1);
				editor.commit();
				Intent prevIntent = new Intent(WelcomeScr_Detials.this,WelcomeScr1.class);
				startActivity(prevIntent);
			}
		});
	}

/*	private void getContacts() {
		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null, null,null);
		ContactDetials contdet  = new ContactDetials();
		while (cursor.moveToNext()) {
			
			contdet.name=(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
		    //listContactId.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
		    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {                
		        Cursor pCur = getContentResolver().query(
		        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),null, null);
		        while (pCur.moveToNext()) {
		            contdet.phNo = (pCur.getString(pCur.getColumnIndex("DATA1")));
		        } 
		        pCur.close();
		    } else
		    	contdet.phNo ="";
		} 
		Contacts_.add(contdet);
	}*/

	private void init() {
		mbnos = new String[]{"1st E.No","2nd E.No","3rd E.No","4th E.No","5th E.No"};
		mbnames = new String[]{"1st E.No","2nd E.No","3rd E.No","4th E.No","5th E.No"};
		tv_title = (TextView) findViewById(R.id.tv_wel2_title);
		bt_prev = (Button) findViewById(R.id.bt_wel2_prev);
		bt_done = (Button) findViewById(R.id.bt_wel2_done);
		lv_mbNo = (ListView) findViewById(R.id.listView1);
	}

	private void setAttri(){
		//set Font
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/trench.otf"); 
		tv_title.setTypeface(type);
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
	
	@Override
	protected void onPause() {
		//for back
		overridePendingTransition(R.anim.anim_leave,
				R.anim.anim_enter);
		super.onPause();
	}
}
