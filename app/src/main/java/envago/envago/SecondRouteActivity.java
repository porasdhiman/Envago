package envago.envago;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vikas on 04-01-2017.
 */

public class SecondRouteActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, View.OnClickListener ,View.OnTouchListener{
    TextView starting_ponit_txtView, add_next_point_txtView, end_point_txtView;

    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mLastLocation;
    String lat, lng;
    ArrayList<HashMap<String, String>> event_list = new ArrayList<>();
    Global global;

    Marker mark;
    Marker marker;
    Dialog dialog2;
    int k = 0,requestCode;
    ImageView back_button_create;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    ArrayList<LatLng> listing = new ArrayList<>();
    private GoogleApiClient client;
    int i = 0;
    RelativeLayout a_letter_layout, b_letter_layout, c_letter_layout;

    boolean is_letterA = false, is_letterB = false, is_letterC = false;
    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView a_letter_txtView,b_letter_txtView, c_letter_txtView;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    RelativeLayout main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.second_route_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        buildGoogleApiClient();
        main_layout=(RelativeLayout) findViewById(R.id.main_layout);
        Fonts.overrideFonts(this,main_layout);
        back_button_create=(ImageView)findViewById(R.id.back_button_create);
        a_letter_layout = (RelativeLayout) findViewById(R.id.a_letter_layout);
        b_letter_layout = (RelativeLayout) findViewById(R.id.b_letter_layout);
        c_letter_layout = (RelativeLayout) findViewById(R.id.c_letter_layout);

        starting_ponit_txtView = (TextView) findViewById(R.id.starting_point_txtView);
        add_next_point_txtView = (TextView) findViewById(R.id.add_next_point_txtView);
        end_point_txtView = (TextView) findViewById(R.id.end_point_txtView);
        add_next_point_txtView.setOnClickListener(this);
        end_point_txtView.setOnClickListener(this);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        /*options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)        //	Display Stub Image
                .showImageForEmptyUri(R.mipmap.ic_launcher)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();*/
        mapFragment.getMapAsync(this);

        starting_ponit_txtView.setText(global.getStartingPoint());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        //-------------------------------Call AutocompleteTxtView-----------------
        a_letter_txtView = (AutoCompleteTextView) findViewById(R.id.a_letter_txtView);
        b_letter_txtView = (AutoCompleteTextView) findViewById(R.id.b_letter_txtView);
        c_letter_txtView = (AutoCompleteTextView) findViewById(R.id.c_letter_txtView);

        a_letter_txtView.setOnItemClickListener(mAutocompleteClickListener);
        b_letter_txtView.setOnItemClickListener(mAutocompleteClickListener);
        c_letter_txtView.setOnItemClickListener(mAutocompleteClickListener);

        a_letter_txtView.setOnTouchListener(this);
        b_letter_txtView.setOnTouchListener(this);
        c_letter_txtView.setOnTouchListener(this);
        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(14);
                return view;
            }
        };

        a_letter_txtView.setThreshold(1);

        a_letter_txtView.setAdapter(mAdapter);
        b_letter_txtView.setThreshold(1);

        b_letter_txtView.setAdapter(mAdapter);
        c_letter_txtView.setThreshold(1);

        c_letter_txtView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_next_point_txtView:
                if (i < 4) {
                    i = i + 1;
                    if (i == 1) {
                        a_letter_layout.setVisibility(View.VISIBLE);
                    } else if (i == 2) {
                        b_letter_layout.setVisibility(View.VISIBLE);
                    } else if (i == 3) {
                        c_letter_layout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SecondRouteActivity.this, "You can select only three location", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            case R.id.end_point_txtView:
finish();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mAdapter.setGoogleApiClient(mGoogleApiClient);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            lat = global.getStarting_lat();
            lng = global.getStarting_lng();
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.oval));
            mMap.addMarker(options);
            LatLng postion = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion, 10));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        } else {
            // Toast.makeText(this, "Please check GPS and restart App", Toast.LENGTH_LONG).show();
        }
        Log.i("search", "Google Places API connected.");
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    //---------------------------------------Draw path--------------------
    private String getMapsApiDirectionsUrl1() {
        String waypoints = "waypoints=optimize:true|"
                + lat + "," + lng
                + "|" + "|" + global.getA_lat() + ","
                + global.getA_lng();

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params + "&key=AIzaSyBON_2IQQca_cYPhAH5Ij632akLmp9Jh9Q";
        String url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lat + "," + lng + "&waypoints=" + global.getA_lat() + "," + global.getA_lng() + "&destination=" + global.getA_lat() + "," + global.getA_lng() + "&sensor=true&mode=walking";

        return url1;
    }

    private String getMapsApiDirectionsUrl2() {
        String waypoints = "waypoints=optimize:true|"
                + lat + "," + lng
                + "|" + "|" + global.getA_lat() + ","
                + global.getA_lng() + "|" + global.getB_lat() + ","
                + global.getB_lng();

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params + "&key=AIzaSyBON_2IQQca_cYPhAH5Ij632akLmp9Jh9Q";
        String url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lat + "," + lng + "&waypoints=" + global.getA_lat() + "," + global.getA_lng() + "%7C" + global.getB_lat() + "," + global.getB_lng() + "&destination=" + global.getB_lat() + "," + global.getB_lng() + "&sensor=true&mode=walking";

        return url1;
    }

    private String getMapsApiDirectionsUrl3() {
        String waypoints = "waypoints=optimize:true|"
                + lat + "," + lng
                + "|" + "|" + global.getA_lat() + ","
                + global.getA_lng() + "|" + global.getB_lat() + ","
                + global.getB_lng() + "|" + global.getC_lat() + ","
                + global.getC_lng();
        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params + "&key=AIzaSyBON_2IQQca_cYPhAH5Ij632akLmp9Jh9Q";
        String url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lat + "," + lng + "&waypoints=" + global.getA_lat() + "," + global.getA_lng() + "%7C" + global.getB_lat() + "," + global.getB_lng() + "%7C" + global.getC_lat() + "," + global.getC_lng() + "&destination=" + global.getC_lat() + "," + global.getC_lng() + "&sensor=true&mode=walking";

        return url1;
    }


    private void addMarkers1() {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(global.getA_lat()), Double.parseDouble(global.getA_lng())))
                    .title("First Point"));

        }
    }

    private void addMarkers2() {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(global.getA_lat()), Double.parseDouble(global.getA_lng())))
                    .title("First Point"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(global.getB_lat()), Double.parseDouble(global.getB_lng())))
                    .title("Second Point"));

        }
    }

    private void addMarkers3() {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(global.getA_lat()), Double.parseDouble(global.getA_lng())))
                    .title("First Point"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(global.getB_lat()), Double.parseDouble(global.getB_lng())))
                    .title("Second Point"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(global.getC_lat()), Double.parseDouble(global.getC_lng())))
                    .title("Third Point"));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.a_letter_txtView:
                requestCode=1;
                break;
            case R.id.b_letter_txtView:
                requestCode=2;
                break;
            case R.id.c_letter_txtView:
                requestCode=3;
                break;
        }
        return false;
    }

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

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
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
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(7);
                polyLineOptions.color(Color.BLUE);
            }
            dialog2.dismiss();

            if(polyLineOptions!=null){
    mMap.addPolyline(polyLineOptions);
}else{
    Toast.makeText(SecondRouteActivity.this,"Google Api not found path",Toast.LENGTH_SHORT).show();
}

        }

    }

    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow() {
        dialog2 = new Dialog(SecondRouteActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.progrees_dialog);
        AVLoadingIndicatorView loaderView = (AVLoadingIndicatorView) dialog2.findViewById(R.id.loader_view);
        loaderView.show();

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        dialog2.show();
    }

    //-------------------------------Autolocation Method------------------------
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             * Retrieve the place ID of the selected item from the Adapter. The
			 * adapter stores each Place suggestion in a PlaceAutocomplete
			 * object from which we read the place ID.
			 */

            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            //  Log.i("TAG", "placeid: " + global.getPlace_id());
            Log.i("TAG", "Autocomplete item selected: " + item.description);

			/*
             * Issue a request to the Places Geo Data API to retrieve a Place
			 * object with additional details about the place.
			 */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            //Toast.makeText(getApplicationContext(), "Clicked: " + item.description, Toast.LENGTH_SHORT).show();
            Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);

        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the
     * first place result in the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {

                Log.e("Tag", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);

            final CharSequence thirdPartyAttribution = places.getAttributions();
            CharSequence attributions = places.getAttributions();


            //------------Place.getLatLng use for get Lat long According to select location name-------------------
            String latlong = place.getLatLng().toString().split(":")[1];
            String completeLatLng = latlong.substring(1, latlong.length() - 1);
            // Toast.makeText(MapsActivity.this,completeLatLng,Toast.LENGTH_SHORT).show();
            String lat = completeLatLng.split(",")[0];
            lat = lat.substring(1, lat.length());
            String lng = completeLatLng.split(",")[1];

            if (requestCode == 1) {
                global.setW_lat(lat);
                global.setW_lng(lng);
                global.setwPoint(a_letter_txtView.getText().toString());
                global.setA_lat(global.getW_lat());
                global.setA_lng(global.getW_lng());
                global.setaPoint(global.getwPoint());
                a_letter_txtView.setText(global.getaPoint());
                // mMap.clear();
                String url = getMapsApiDirectionsUrl1();
                dialogWindow();
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");

                options.position(new LatLng(Double.parseDouble(global.getA_lat()), Double.parseDouble(global.getA_lng())));
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.a)).title("First Point");


                mMap.addMarker(options);
                LatLng postion = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion, 10));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

            } else if (requestCode == 2) {
                global.setW_lat(lat);
                global.setW_lng(lng);
                global.setwPoint(b_letter_txtView.getText().toString());
                global.setB_lat(global.getW_lat());
                global.setB_lng(global.getW_lng());
                global.setbPoint(global.getwPoint());
                b_letter_txtView.setText(global.getbPoint());
                //  mMap.clear();
                String url = getMapsApiDirectionsUrl2();
                dialogWindow();
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");

                options.position(new LatLng(Double.parseDouble(global.getA_lat()), Double.parseDouble(global.getA_lng()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.a)).title("first Point");
                options.position(new LatLng(Double.parseDouble(global.getB_lat()), Double.parseDouble(global.getB_lng())));
                options .icon(BitmapDescriptorFactory.fromResource(R.drawable.b)).title("second Point");

                mMap.addMarker(options);
                LatLng postion = new LatLng(Double.parseDouble(global.getB_lat()), Double.parseDouble(global.getB_lng()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion, 10));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

            } else {
                global.setW_lat(lat);
                global.setW_lng(lng);
                global.setwPoint(c_letter_txtView.getText().toString());
                global.setC_lat(global.getW_lat());
                global.setC_lng(global.getW_lng());
                global.setcPoint(global.getwPoint());
                c_letter_txtView.setText(global.getcPoint());
                //  mMap.clear();
                String url = getMapsApiDirectionsUrl3();
                dialogWindow();
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");
                options.position(new LatLng(Double.parseDouble(global.getA_lat()), Double.parseDouble(global.getA_lng()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.a)).title("first Point");
                options.position(new LatLng(Double.parseDouble(global.getB_lat()), Double.parseDouble(global.getB_lng()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.b)).title("second Point");
                options.position(new LatLng(Double.parseDouble(global.getC_lat()), Double.parseDouble(global.getC_lng())));
                options .icon(BitmapDescriptorFactory.fromResource(R.drawable.c)).title("third Point");
                mMap.addMarker(options);
                LatLng postion = new LatLng(Double.parseDouble(global.getB_lat()), Double.parseDouble(global.getB_lng()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion, 10));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

            }


            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id, CharSequence address,
                                              CharSequence phoneNumber, Uri websiteUri) {
        Log.e("Tag", res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("TAG", "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state
        // and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }



    @Override
    public void onConnectionSuspended(int i) {
        mAdapter.setGoogleApiClient(null);
        Log.e("search", "Google Places API connection suspended.");
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();
    }
}
