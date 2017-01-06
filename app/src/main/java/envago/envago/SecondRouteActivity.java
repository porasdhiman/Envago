package envago.envago;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vikas on 04-01-2017.
 */

public class SecondRouteActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, View.OnClickListener {
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
    int k = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    ArrayList<LatLng> listing = new ArrayList<>();
    private GoogleApiClient client;
    int i = 0;
    RelativeLayout a_letter_layout, b_letter_layout, c_letter_layout;
    TextView a_letter_txtView, b_letter_txtView, c_letter_txtView;
    boolean is_letterA = false, is_letterB = false, is_letterC = false;

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
        a_letter_layout = (RelativeLayout) findViewById(R.id.a_letter_layout);
        b_letter_layout = (RelativeLayout) findViewById(R.id.b_letter_layout);
        c_letter_layout = (RelativeLayout) findViewById(R.id.c_letter_layout);
        a_letter_txtView = (TextView) findViewById(R.id.a_letter_txtView);
        b_letter_txtView = (TextView) findViewById(R.id.b_letter_txtView);
        c_letter_txtView = (TextView) findViewById(R.id.c_letter_txtView);
        starting_ponit_txtView = (TextView) findViewById(R.id.starting_point_txtView);
        add_next_point_txtView = (TextView) findViewById(R.id.add_next_point_txtView);
        end_point_txtView = (TextView) findViewById(R.id.end_point_txtView);
        add_next_point_txtView.setOnClickListener(this);
        end_point_txtView.setOnClickListener(this);
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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        a_letter_txtView.setOnClickListener(this);
        b_letter_txtView.setOnClickListener(this);
        c_letter_txtView.setOnClickListener(this);
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

                break;
            case R.id.a_letter_txtView:
                Intent i = new Intent(this, LocationActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.b_letter_txtView:
                Intent j = new Intent(this, LocationActivity.class);
                startActivityForResult(j, 2);
                break;
            case R.id.c_letter_txtView:
                Intent k = new Intent(this, LocationActivity.class);
                startActivityForResult(k, 3);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.

        mGoogleApiClient.disconnect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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

    @Override
    public void onConnectionSuspended(int i) {

        Log.e("search", "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //------------------------Current lat long------------------------
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                /*.enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)*/
                .build();
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
                polyLineOptions.width(5);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
            dialog2.dismiss();
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
}
