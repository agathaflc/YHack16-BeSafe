package com.theark.chathead;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Creates the head layer view which is displayed directly on window manager.
 * It means that this view is above every application's view on your phone -
 * until another application does the same.
 */
public class HeadLayer extends View{

    private Context context;
    private FrameLayout frameLayout;
    private WindowManager windowManager;

    public HeadLayer(Context context) {
        super(context);
        
        this.context = context;

        createHeadView();
    }

    /**
     * Creates head view and adds it to the window manager.
     */
    private void createHeadView() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.NO_GRAVITY;

        frameLayout = new FrameLayout(context);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(frameLayout, params);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Here is the place where you can inject whatever layout you want.
        layoutInflater.inflate(com.theark.alert.R.layout.head_chat, frameLayout);
        
        
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
        	  private int initialX;
        	  private int initialY;
        	  private float initialTouchX;
        	  private float initialTouchY;

        	  @Override public boolean onTouch(View v, MotionEvent event) {
        	    switch (event.getAction()) {
        	   
        	      case MotionEvent.ACTION_DOWN:
        	    	  //Toast.makeText(context, "yeeee!!",1).show();
        	        
        	    	initialX = params.x;
        	        initialY = params.y;
        	        initialTouchX = event.getRawX();
        	        initialTouchY = event.getRawY();
        	        return true;
        	      case MotionEvent.ACTION_UP:
        	    	  dialogview();
        	        return true;
        	      case MotionEvent.ACTION_MOVE:
        	        params.x = initialX + (int) (event.getRawX() - initialTouchX);
        	        params.y = initialY + (int) (event.getRawY() - initialTouchY);
        	        windowManager.updateViewLayout(frameLayout, params);
        	        
        	        return true;
        	    }
        	    return false;
        	  }
        	});
        
        frameLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "yeeee!!",1).show();
			}
		});
    }

    protected void dialogview() {
		final Dialog d = new Dialog(context);
		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		d.setContentView(com.theark.alert.R.layout.head_chat_dialog);
		d.setTitle("Alert");
		d.setCancelable(true);
		stopHeadService();
		TextView tv = (TextView) d.findViewById(com.theark.alert.R.id.tv_tt);
		Button bt = (Button) d.findViewById(com.theark.alert.R.id.hc_getdir);
		SharedPreferences sp = context.getSharedPreferences("sms", 0);
		String name = sp.getString("sms_name", "Unknown");
		final String lat = sp.getString("sms_lat", "");
		final String longi = sp.getString("sms_long", "");
		final String num = sp.getString("sms_num", "");
		tv.setText(name+"\n( "+ num +" )\n"+"is in Danger.\nHelp her/him.");
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + lat + "," + longi));
		        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Only if initiating from a Broadcast Receiver
		        String mapsPackageName = "com.google.android.apps.maps";
		        /*if (Utility.isPackageExisted(context, mapsPackageName)) {
		            i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity");
		            i.setPackage(mapsPackageName);
		        }*/
		        d.dismiss();
		        context.startActivity(i);
			}
		});
		d.show();
	}

	/**
     * Removes the view from window manager.
     */
    public void destroy() {
        windowManager.removeView(frameLayout);
    }
    
    private void stopHeadService() {
        //Context context = getActivity();
        context.stopService(new Intent(context, HeadService.class));
    }

	/*@Override
	public void onClick(View v) {
		if(v.getId() == R.id.headid){
			Log.d("###", "touched");
			Toast.makeText(context, "clicked", 1).show();
		}
	}*/
}
