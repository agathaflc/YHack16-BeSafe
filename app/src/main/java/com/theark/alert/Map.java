package com.theark.alert;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.theark.alert.R.id.map;
//import static com.theark.alert.map;

public class Map extends FragmentActivity implements LocationListener, OnMapReadyCallback {
    GoogleMap googleMap;
    Context context;
    String lat, lng = "";
    AsyncTask<String, Void, String> shareRegidTask;
    List<List<LatLng>> polydecoded_paths = new ArrayList<List<LatLng>>();

    List<LatLng> unsafe_trial = new ArrayList<LatLng>();

    List<LatLng> final_path_trace = new ArrayList<LatLng>();

    PolylineOptions lineOptions;


    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;

    private LocationManager locationManager;
    private LocationListener locationListener;
    public static ViolenceMapClient client;
    private TweetsClient tweetsClient;
    private ArrayList<LatLng> vLocations = new ArrayList<>();

    static boolean gotCurrent = false;

    public static Location currentLocation;
    private Marker currentLocMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Map.this, ReportActivity.class));
            }
        });

        Button findSafestPathButton = (Button) findViewById(R.id.bFindPath);
        if (findSafestPathButton != null) {
            findSafestPathButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Finding safest path..", Toast.LENGTH_LONG).show();
                    String orig = ((EditText) findViewById(R.id.etOrigin)).getText().toString();
                    String dest = ((EditText) findViewById(R.id.etDest)).getText().toString();
                    String[] params = {orig, dest};
                    initialise();
                    shareRegidTask.execute(params);
                }
            });
        }

        // Initialize service generator
        // Create a very simple REST adapter which points the GitHub API endpoint.
        client = ServiceGenerator.createService(ViolenceMapClient.class);

        tweetsClient = ServiceGenerator.createService(TweetsClient.class);

        // Initialize location manager instance
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                currentLocation = location;
                // Put marker for current location
                if (currentLocation != null) {
                    LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    if (currentLocMarker == null) {
                        MarkerOptions a = new MarkerOptions().position(currentLatLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                .title("Current location");
                        currentLocMarker = googleMap.addMarker(a);
                        gotCurrent = true;
                        Toast.makeText(getBaseContext(), "Location found", Toast.LENGTH_SHORT).show();
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentLatLng, 5);
                        googleMap.animateCamera(yourLocation);
                    } else {
                        // change location
                        currentLocMarker.setPosition(currentLatLng);
                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        lineOptions = new PolylineOptions();

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        //
//		LatLng ltln = new LatLng(Double.parseDouble(lat),
//				Double.parseDouble(lng));
//		Marker TP = googleMap.addMarker(new MarkerOptions().position(ltln)
//				.title("yo"));
//
//		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(ltln, 12);
//		googleMap.animateCamera(yourLocation);

        fill_unsafe();
        ////googleMap.addPolyline(lineOptions);

    }

    public void fill_unsafe() {

//		unsafe_trial.add(new LatLng(41.316880000000005,-72.93305000000001));
//		unsafe_trial.add(new LatLng(41.316810000000004,-72.9333));
//		unsafe_trial.add(new LatLng(41.312540000000006,-72.93252000000001));
//		unsafe_trial.add(new LatLng(41.31721,-72.93144000000001));
//		unsafe_trial.add(new LatLng(41.30829000000001,-72.92792));
//		unsafe_trial.add(new LatLng(41.313860000000005,-72.92525));
//		unsafe_trial.add(new LatLng(41.313840000000006,-72.92513000000001));

        for (LatLng latLng : vLocations) {
            unsafe_trial.add(latLng);
        }

    }

    public void initialise() {
        shareRegidTask = new AsyncTask<String , Void, String>() {


            protected String doInBackground(String ...p) {
                String to_be_returned = "empty";
                try {
                //    sherman%20avenue%20new%20haven&destination=ikea%20home%20furnishings%20new%20haven%20&key=AIzaSyC6x6YcC_422XnCBiXRCs8yNzQphAUBseg&alternatives=true
                    URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+p[0].replace(" ","")+"&destination="+p[1].replace(" ","")+"&key=AIzaSyC6x6YcC_422XnCBiXRCs8yNzQphAUBseg&alternatives=true");
                    /** STEP 2 -- Open Connection */

                Log.d("###url","https://maps.googleapis.com/maps/api/directions/json?origin="+p[0].replace(" ","")+"&destination="+p[1].replace(" ","")+"&key=AIzaSyC6x6YcC_422XnCBiXRCs8yNzQphAUBseg&alternatives=true");

                    HttpURLConnection con = (HttpURLConnection) url
                            .openConnection();

                    //writer_nudity.write(Constants.nudity_api+file_name+"/"+mfile.frames[0]);

                    con.setRequestMethod("GET");


                    int responseCode = con.getResponseCode();
                    //System.out.println("response "+responseCode);
                    InputStream in = con.getInputStream();
                    //String data = convertStreamToString(in);

                    BufferedReader in_buff = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in_buff.readLine()) != null) {
                        response.append(inputLine);
                    }
                    String data = response.toString();
                    in.close();
                    //System.out.println("op :- " + data);
                    to_be_returned = data;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return to_be_returned;
            }

            protected void onPostExecute(String feed) {
                // TODO: check this.exception
                // TODO: do something with the feed
                try {
                    JSONObject jsonObject = new JSONObject(feed);
                    JSONArray ja_frames = jsonObject.getJSONArray("routes");
                    JSONObject bound_object;

                    for (int i = 0; i < ja_frames.length(); i++) {
                        bound_object = ja_frames.getJSONObject(i);
                        polydecoded_paths.add(PolyUtil.decode(bound_object.getJSONObject("overview_polyline").getString("points")));
                    }

                    int count = 0;
                    int min_count = 10000;
                    int index_path = 0;

                    for (int i = 0; i < polydecoded_paths.size(); i++) {
                        List<LatLng> current_path = polydecoded_paths.get(i);
                        String path = "";
                        for (int j = 0; j < current_path.size(); j++) {
                            for (int k = 0; k < vLocations.size(); k++) {
                                if (!check_if_closeby(vLocations.get(k), current_path.get(j))) {
                                    count++;
                                }
                            }
                            path += current_path.get(j) + "--";

                        }
                        if (count < min_count) {
                            min_count = count;
                            index_path = i;
                        }
                        Log.d("paths", "##########");
                        Log.d("paths", path);
                        count = 0;
                    }
                    String safe_path = "";
                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    int size = polydecoded_paths.get(index_path).size();
                    for (int k = 0; k < size; k++) {
                        safe_path += polydecoded_paths.get(index_path).get(k) + "---";
                        //final_path_trace.add()
                        options.add(polydecoded_paths.get(index_path).get(k));


                    }

                    Log.d("saf_paths", "##########");
                    Log.d("saf_paths", safe_path);


                    final_path_trace = polydecoded_paths.get(index_path);
                    lineOptions.addAll(final_path_trace);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);

                    googleMap.addPolyline(options);

                    drawPolyLineOnMap(polydecoded_paths.get(index_path));

                    if (lineOptions != null) {
                        googleMap.addPolyline(lineOptions);
                    } else {
                        Log.d("onPostExecute", "without Polylines drawn");
                    }

                    // put markers on dest and orig

                    googleMap.addMarker(new MarkerOptions().position(polydecoded_paths.get(index_path).get(0))
                            .title("Origin:")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    googleMap.addMarker(new MarkerOptions().position(polydecoded_paths.get(index_path).get(size-1))
                            .title("Destination:")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


                    Log.d("safe_paths", "##########");
                    Log.d("safe_paths", safe_path);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                shareRegidTask.cancel(true);

            }

        };
    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(5);
        polyOptions.addAll(list);
        googleMap.clear();
        googleMap.addPolyline(polyOptions);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
        final LatLngBounds bounds = builder.build();
        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        googleMap.animateCamera(cu);
    }

    public boolean check_if_closeby(LatLng a, LatLng b) {
        if (SphericalUtil.computeDistanceBetween(a, b) < 200) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //	getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        finish();

    }

    public void getdirections() {

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        Call<List<ViolenceLocation>> call = client.getLocations();
        call.enqueue(new retrofit2.Callback<List<ViolenceLocation>>() {
            @Override
            public void onResponse(Call<List<ViolenceLocation>> call, Response<List<ViolenceLocation>> response) {
                if (response.isSuccessful()) {
                    for (ViolenceLocation location : response.body()) {
                        Log.d("locations", location.toString());
                        LatLng place = new LatLng(location.getLat(), location.getLng());
                        googleMap.addMarker(new MarkerOptions().position(place).title("News:").snippet(location.getInfo())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                        vLocations.add(place);
                    }

                } else {
                    Log.d("onresponse", "unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<ViolenceLocation>> call, Throwable t) {
                Log.d("onfailure", t.getMessage());
            }
        });

        Call<List<Tweets>> tweetCall = tweetsClient.getLocationTweet();
        tweetCall.enqueue(new retrofit2.Callback<List<Tweets>>() {
            @Override
            public void onResponse(Call<List<Tweets>> call, Response<List<Tweets>> response) {
                if (response.isSuccessful()) {
                    for (Tweets tweet : response.body()) {
                        LatLng place = new LatLng(tweet.getLat(), tweet.getLng());
                        googleMap.addMarker((new MarkerOptions().position(place).title("Tweet:").snippet(tweet.getInfo())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))));
                        ;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                        vLocations.add(place);
                    }
                } else {
                    Log.d("onresponse", "unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<Tweets>> call, Throwable t) {
                Log.d("onfailure", t.getMessage());
            }
        });

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoContent = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                ;
                TextView titleText = (TextView) infoContent.findViewById(R.id.tvTitle);
                SpannableString textTitle = new SpannableString(marker.getTitle());
                textTitle.setSpan(new ForegroundColorSpan(Color.BLACK), 0, textTitle.length(), 0);
                titleText.setText(textTitle);

                TextView descUi = (TextView) infoContent.findViewById(R.id.tvDesc);
                if (marker.getSnippet() != null) {
                    SpannableString textDesc = new SpannableString(marker.getSnippet());
                    textDesc.setSpan(new ForegroundColorSpan(Color.BLACK), 0, textDesc.length(), 0);
                    descUi.setText(textDesc);
                }
                return infoContent;
            }
        });
    }
}

