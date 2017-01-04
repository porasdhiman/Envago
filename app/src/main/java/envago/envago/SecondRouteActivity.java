package envago.envago;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;

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
            eventLocOnMap(lat, lng, global.getA_lat(), global.getA_lng());
        } else if (requestCode == 2) {
            global.setB_lat(global.getW_lat());
            global.setB_lng(global.getW_lng());
            global.setbPoint(global.getwPoint());
            b_letter_txtView.setText(global.getbPoint());
            eventLocOnMap(global.getA_lat(), global.getA_lng(), global.getB_lat(), global.getB_lng());
        } else {
            global.setC_lat(global.getW_lat());
            global.setC_lng(global.getW_lng());
            global.setcPoint(global.getwPoint());
            c_letter_txtView.setText(global.getcPoint());
            eventLocOnMap(global.getB_lat(), global.getB_lng(), global.getC_lat(), global.getC_lng());
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
        mMap.setMyLocationEnabled(true);
        if (mLastLocation != null) {
            lat = global.getStarting_lat();
            lng = global.getStarting_lng();
            eventLocOnMap(lat, lng, lat, lng);

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

    public void eventLocOnMap(String startLat, String startLng, String endLat, String endLng) {
        if (mMap != null) {
            mMap.clear();
        }
        {


            listing.add(new LatLng(Double.parseDouble(endLat),Double.parseDouble(endLng)));

            drawPrimaryLinePath(listing);
            LatLng postion = new LatLng(Double.parseDouble(endLat), Double.parseDouble(endLng));
            if (k == 0) {
                k = 1;
            } else {


                // mMap.addMarker(new MarkerOptions().position(postion));
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position for the marker
                markerOptions.position(postion);

                // Setting custom icon for the marker
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

                // Setting title for the infowindow


                // Adding the marker to the map
                mMap.addMarker(markerOptions);
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion, 10));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //---------------------------route--------------------------------------
    private void drawPrimaryLinePath(ArrayList<LatLng> listLocsToDraw) {
        if (mMap == null) {
            return;
        }

        if (listLocsToDraw.size() < 2) {
            return;
        }

        PolylineOptions options = new PolylineOptions();
        options.addAll(listLocsToDraw);
        options.color(Color.parseColor("#CC0000FF"));
        options.width(8);
        options.visible(true);
/*
        for (Location locRecorded : listLocsToDraw) {
            options.add(new LatLng(locRecorded.getLatitude(),
                    locRecorded.getLongitude()));
        }*/

        mMap.addPolyline(options);

    }
}
