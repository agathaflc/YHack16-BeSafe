package com.theark.emergency;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class PlacesParcer {

    public static ArrayList<Hospital> parseGoogleParse(String response) {
		 
        ArrayList<Hospital> temp = new ArrayList<Hospital>();
        try {
 
            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);
 
            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {
 
                JSONArray jsonArray = jsonObject.getJSONArray("results");
 
                for (int i = 0; i < jsonArray.length(); i++) {
                    Hospital poi = new Hospital();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                    }if (jsonArray.getJSONObject(i).has("vicinity")) {
                        poi.setVicinity(jsonArray.getJSONObject(i).optString("vicinity"));
                     }if (jsonArray.getJSONObject(i).has("reference")) {
                        poi.setReference(jsonArray.getJSONObject(i).optString("reference"));
                     }if (jsonArray.getJSONObject(i).has("geometry")) {
                    	 JSONObject jsonGeom = new JSONObject(jsonArray.getJSONObject(i).optString("geometry"));
                         JSONObject jsonloc = new JSONObject(jsonGeom.optString("location"));
                         poi.setLatitude(jsonloc.optString("lat"));
                         poi.setLatitude(jsonloc.optString("lng"));
                         //Log.d("###", ""+jsonloc.optString("lat"));
                     }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Hospital>();
        }
        return temp;
 
    }
    
    
    
    public static Hospital parseDetailsParse(String response) {
		 
        Hospital poi = new Hospital();
        try {

			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.has("result")) {
				JSONObject jsonRes = new JSONObject(jsonObject.optString("result"));
				if (jsonRes.has("formatted_address")) {

			        poi.setVicinity(jsonRes.optString("formatted_address"));
			    }if (jsonRes.has("formatted_phone_number")) {
			    	poi.setPhone_number(jsonRes.optString("formatted_phone_number"));
			    }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return poi;
 
    }
    
    
    
    //////////////////////police
    
    
    
    
    public static ArrayList<PoliceStation> parsePoliceStation(String response) {
		 
        ArrayList<PoliceStation> temp = new ArrayList<PoliceStation>();
        try {
 
            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);
 
            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {
 
                JSONArray jsonArray = jsonObject.getJSONArray("results");
 
                for (int i = 0; i < jsonArray.length(); i++) {
                	PoliceStation poi = new PoliceStation();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                    }if (jsonArray.getJSONObject(i).has("vicinity")) {
                        poi.setVicinity(jsonArray.getJSONObject(i).optString("vicinity"));
                     }if (jsonArray.getJSONObject(i).has("reference")) {
                        poi.setReference(jsonArray.getJSONObject(i).optString("reference"));
                     }if (jsonArray.getJSONObject(i).has("geometry")) {
                    	 JSONObject jsonGeom = new JSONObject(jsonArray.getJSONObject(i).optString("geometry"));
                         JSONObject jsonloc = new JSONObject(jsonGeom.optString("location"));
                         poi.setLatitude(jsonloc.optString("lat"));
                         poi.setLatitude(jsonloc.optString("lng"));
                         //Log.d("###", ""+jsonloc.optString("lat"));
                     }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<PoliceStation>();
        }
        return temp;
 
    }
    
    
    
    public static PoliceStation parseDetailsPolice(String response) {
		 
    	PoliceStation poi = new PoliceStation();
        try {

			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.has("result")) {
				JSONObject jsonRes = new JSONObject(jsonObject.optString("result"));
				if (jsonRes.has("formatted_address")) {
			        poi.setVicinity(jsonRes.optString("formatted_address"));
			    }if (jsonRes.has("formatted_phone_number")) {
			    	poi.setPhone_number(jsonRes.optString("formatted_phone_number"));
			    }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return poi;
 
    }
	
}
