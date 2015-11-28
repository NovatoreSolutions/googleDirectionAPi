package com.novatoresols.staysafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.Toast;

import DB.TinyDB;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);
    private static final LatLng SANS_FRASISCO = new LatLng(37.773972, -122.431297);
    private static final LatLng LAHORE = new LatLng(31.54505, 74.340683);

    GoogleMap googleMap;
    GoogleMap googleMap2;
    final String TAG = "PathGoogleMapActivity";
    String data = "";

    //AutoComplete
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBpYsrkoT1DWDvWdMgtbab06797zbP1RkQ";
    AutoCompleteTextView start;
    AutoCompleteTextView end;
    LatLng EndLatLng = null;

    SharedPreferences getList;
    List<LatLng> aList;
    List<RouteModel> routeList;

    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    LocationRequest mLocationRequest;

    TextView one;
    TextView two;
    TextView three;
    ProgressDialog pd;

    LatLng c1=new LatLng(31.4355209,74.2739632);
    LatLng c2=new LatLng(31.4554714, 74.3007571);
    LatLng c3=new LatLng(31.437695, 31.437695);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        one=(TextView)findViewById(R.id.one);
        //two=(TextView)findViewById(R.id.two);
        //three=(TextView)findViewById(R.id.three);


        //AutotextView
        start = (AutoCompleteTextView) findViewById(R.id.startingPoint);
        end = (AutoCompleteTextView) findViewById(R.id.endingPoint);
        start.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        end.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        start.setOnItemClickListener(this);
        end.setOnItemClickListener(this);
        //Inilizing The Map
        try {

            //load map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*MarkerOptions options = new MarkerOptions();
        options.position(LOWER_MANHATTAN);
        options.position(BROOKLYN_BRIDGE);
        options.position(WALL_STREET);
        googleMap.addMarker(options);*/
        googleMap.setMyLocationEnabled(true);
        //  googleMap.setTrafficEnabled(true);
/*
        Location myLocation = googleMap.getMyLocation();
        LatLng myLatLng = new LatLng(myLocation.getLatitude(),
                myLocation.getLongitude());

        CameraPosition myPosition = new CameraPosition.Builder()
                .target(myLatLng).zoom(17).bearing(90).tilt(30).build();
        googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(myPosition));*/


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SANS_FRASISCO,15));

        //Reading The List

        getList = getSharedPreferences("a", Context.MODE_PRIVATE);
        String jsonFavorite = getList.getString("alist", null);
        Gson gso = new Gson();
        LatLng[] favoriteItems = gso.fromJson(jsonFavorite, LatLng[].class);
        aList = Arrays.asList(favoriteItems);
        aList = new ArrayList(aList);

        for (int i = 0; i < 45; i++) {
            googleMap.addMarker(new MarkerOptions().position(aList.get(i))
                    .title("Fatal Accident")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_accidentmarker)));
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void gettingStarted(View v) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.inputLocation);
        ll.setVisibility(View.VISIBLE);
        LinearLayout l = (LinearLayout) findViewById(R.id.gettingStarted);
        l.setVisibility(View.INVISIBLE);
        try {

            //load map
            initilizeMap2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap2.setMyLocationEnabled(true);
        googleMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(SANS_FRASISCO, 12));


    }


    public void calulatesRoutes(View v) {

        if (start.getText().toString().length() == 0) {
            start.setError("Fill Field");
        } else if (end.getText().toString().length() == 0) {
            end.setError("Fill Field");
        } else {

            hidekeyboared();

            googleMap.clear();

            //Reading The List
            getList = getSharedPreferences("a", Context.MODE_PRIVATE);
            String jsonFavorite = getList.getString("alist", null);
            Gson gso = new Gson();
            LatLng[] favoriteItems = gso.fromJson(jsonFavorite, LatLng[].class);
            aList = Arrays.asList(favoriteItems);
            aList = new ArrayList(aList);

            for (int i = 0; i < 45; i++) {
                googleMap.addMarker(new MarkerOptions().position(aList.get(i))
                        .title("Fatal Accident")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_accidentmarker)));
            }

            LinearLayout ll = (LinearLayout) findViewById(R.id.inputLocation);
            ll.setVisibility(View.INVISIBLE);

            LinearLayout l = (LinearLayout) findViewById(R.id.gettingStarted);
            l.setVisibility(View.VISIBLE);


            Button accidentshow=(Button)findViewById(R.id.accidentshow);
            String[] array = getResources().getStringArray(R.array.accidentarray);
            String randomStr = array[new Random().nextInt(array.length)];
            accidentshow.setText("Last Fatal Accident In"+" "+randomStr);
            accidentshow.setVisibility(View.VISIBLE);


            RelativeLayout contentheader=(RelativeLayout)findViewById(R.id.contentheader);
            contentheader.setVisibility(View.VISIBLE);

            pd = ProgressDialog.show(MainActivity.this, "Loading Data...", "Please Wait", true, false, null);

            String startingPoint = start.getText().toString();
            String endingPoint = end.getText().toString();
            LatLng StartLatLng = null;


            try {
                Geocoder selected_place_geocoder = new Geocoder(MainActivity.this);
                List<Address> address;

                address = selected_place_geocoder.getFromLocationName(startingPoint, 5);
                Address location = address.get(0);

                address = selected_place_geocoder.getFromLocationName(endingPoint, 5);
                Address location2 = address.get(0);
                StartLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                EndLatLng = new LatLng(location2.getLatitude(), location2.getLongitude());

            } catch (Exception e) {
                e.printStackTrace();
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(StartLatLng, 12));
            addMarkers(StartLatLng, EndLatLng);

            String url = getMapsApiDirectionsUrl(StartLatLng, EndLatLng);
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);


        }


    }

    public void ClearFrom(View v) {
        start.setText("");

    }

    public void ClearTo(View v) {
        end.setText("");
    }

    public void hidekeyboared() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void viaCycle(View v) {
        Toast.makeText(MainActivity.this, "Route Via Cycle Selected", Toast.LENGTH_SHORT).show();
    }

    public void viaCar(View v) {
        Toast.makeText(MainActivity.this, "Route Via Car Selected", Toast.LENGTH_SHORT).show();
    }

    public void shareRoute(View v) {

        String subject="Fastest Route " + "  From" + start.getText().toString() + "  T0" + end.getText().toString();
        String ones="Fast Route Via  "+ routeList.get(0).getViaRoute() + "\n" + "Distance            " + routeList.get(0).getDistance() + "\n" + "Time                   "+ routeList.get(0).getTime();

        try
        { Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT,subject);
            i.putExtra(Intent.EXTRA_TEXT, ones);
            startActivity(Intent.createChooser(i, "choose one"));
        }
        catch(Exception e)
        { //e.toString();
        }



    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapp)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void initilizeMap2() {
        if (googleMap2 == null) {
            googleMap2 = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap2 == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
        mGoogleApiClient.connect();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getMapsApiDirectionsUrl(LatLng s, LatLng e) {
        String waypoints = "waypoints=optimize:true|"
                + s.latitude + "," + s.longitude
                + "|" + "|" + e.latitude + ","
                + e.longitude;
        String OriDest = "origin=" + s.latitude + "," + s.longitude + "&destination=" + e.latitude + "," + e.longitude;

        String sensor = "sensor=false";
        String params = OriDest + "&%20" + waypoints + "&" + sensor + "&alternatives=true";
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private void addMarkers(LatLng s, LatLng e) {
        if (googleMap != null) {

           /* googleMap.addMarker(new MarkerOptions().position(s)
                    .title("Current"));
            googleMap.addMarker(new MarkerOptions().position(e)
                    .title("Second Point"));*/

            googleMap.addMarker(new MarkerOptions()
                    .position(s)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current)));

            googleMap.addMarker(new MarkerOptions()
                    .position(e)
                    .title("Destination Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));

        }
    }


    //Code For Parser and read json
    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routeList=RouteModelParder.parseRouteRecords(jObject);
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            pd.dismiss();
            String ones="Fast Route Via  "+ routeList.get(0).getViaRoute() + "\n" + "Distance            " + routeList.get(0).getDistance() + "\n" + "Time                   "+ routeList.get(0).getTime();


            one.setText(ones);
           // two.setText(twos);
            //three.setText(threes);

            Boolean reline=false;
            Boolean yelloline=false;
            Boolean geenline=false;
            //int redcount
            LatLng t;




            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);


                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                   // t=position;
                    if (aList.contains(position)){
                        reline=true;
                    }




                }
                if (reline==true) {
                    polyLineOptions.addAll(points);
                    polyLineOptions.width(16);
                    polyLineOptions.color(Color.RED);
                    googleMap.addPolyline(polyLineOptions);
                    reline=false;
                }
                else {
                    polyLineOptions.addAll(points);
                    polyLineOptions.width(16);
                    polyLineOptions.color(Color.GREEN);
                    googleMap.addPolyline(polyLineOptions);
                }
            }



            //googleMap.addPolyline(polyLineOptions);
        }
    }


    //AutoCompletion Code
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String str = (String) parent.getItemAtPosition(position);
        // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();


    }

    public static ArrayList autocomplete(String input) {

        ArrayList resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Error processing Places API URL", e);

            return resultList;

        } catch (IOException e) {

            Log.e(LOG_TAG, "Error connecting to Places API", e);

            return resultList;

        } finally {

            if (conn != null) {

                conn.disconnect();

            }

        }


        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


            // Extract the Place descriptions from the results

            resultList = new ArrayList(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {

                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));

                System.out.println("============================================================");

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);

        }

        return resultList;

    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);

        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected android.widget.Filter.FilterResults performFiltering(CharSequence constraint) {
                    android.widget.Filter.FilterResults filterResults = new android.widget.Filter.FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, android.widget.Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }


    //Code For getting current location and set it

    public String getAddress(Location loc) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        StringBuilder s1 = null;
        String s = null;
        List<Address> ss = null;
        try {
            ss = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            s1 = new StringBuilder();
            if (ss.get(0).getFeatureName() != null) {
                s1.append(ss.get(0).getFeatureName()).append(" ");
            }
            if (ss.get(0).getSubLocality() != null) {
                s1.append(ss.get(0).getSubLocality()).append(" ");
            }
            if (ss.get(0).getLocality() != null) {
                s1.append(ss.get(0).getLocality());
            }
            return s1.toString();
        }
        // Misplaced declaration of an exception variable
        catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(s1);
    }
    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {

        }

        String s = getAddress(mLastLocation);
        start.setText(s);
        end.requestFocus();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }






}
