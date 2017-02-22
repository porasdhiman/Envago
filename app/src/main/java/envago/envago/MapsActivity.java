package envago.envago;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {
    Marker marker;
    private GoogleMap mMap;
    private Hashtable<String, String> markers;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Location mLastLocation;
    ImageView previous_btn;
    //--------------Google search api variable------------
    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    String lat, lng;
    ArrayList<HashMap<String, String>> event_list = new ArrayList<>();
    Global global;
    int i;
    Marker mark;
    Dialog dialog2;
    final HashMap<String, Integer> map = new HashMap<>();
    LinearLayout show_info_layout;
    TextView event_name, event_date, event_price;
    ImageView event_image;
    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        global = (Global) getApplicationContext();

       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();*/
        buildGoogleApiClient();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        show_info_layout = (LinearLayout) findViewById(R.id.show_info_layout);
        event_name = (TextView) findViewById(R.id.event_name);
        event_date = (TextView) findViewById(R.id.event_date);
        event_price = (TextView) findViewById(R.id.event_price);
        event_image = (ImageView) findViewById(R.id.event_img);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        initImageLoader();
        markers = new Hashtable<String, String>();
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showStubImage(0)        //	Display Stub Image
                .showImageForEmptyUri(0)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        mapFragment.getMapAsync(this);

        //-------------------------------Call AutocompleteTxtView-----------------
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.mAutocompleteView);

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
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

        mAutocompleteView.setThreshold(1);

        mAutocompleteView.setAdapter(mAdapter);
        mAutocompleteView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);

        previous_btn = (ImageView) findViewById(R.id.previous_btn);
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAutocompleteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mAutocompleteView.setText("");
                return false;
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


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
            lat = completeLatLng.split(",")[0];
            lat = lat.substring(1, lat.length());
            lng = completeLatLng.split(",")[1];

if(event_list.size()>0){
    event_list.clear();
}
            dialogWindow();
            get_list();
            places.release();
        }
    };

    public void openMarkerView() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final int pos = map.get(marker.getId());

                show_info_layout.setVisibility(View.VISIBLE);

                event_name.setText(cap(global.getEvent_list().get(pos).get(GlobalConstants.EVENT_NAME)));


                event_date.setText(formatdate2(global.getEvent_list().get(pos).get(GlobalConstants.EVENT_START_DATE)));

                event_price.setText("$" + global.getEvent_list().get(pos).get(GlobalConstants.EVENT_PRICE));
                String url = GlobalConstants.IMAGE_URL + global.getEvent_list().get(pos).get(GlobalConstants.EVENT_IMAGES);
                if (url != null && !url.equalsIgnoreCase("null")
                        && !url.equalsIgnoreCase("")) {
                    imageLoader.displayImage(url, event_image, options,
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri,
                                                              View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view,
                                            loadedImage);

                                }
                            });
                } else {
                    event_image.setImageResource(0);
                }
                show_info_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MapsActivity.this, DetailsActivity.class);
                        i.putExtra(GlobalConstants.EVENT_ID, global.getEvent_list().get(pos).get(GlobalConstants.EVENT_ID));
                        i.putExtra("user", "non user");
                        startActivity(i);

                    }
                });
                return false;
            }
        });

    }

    public String formatdate2(String fdate) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat d = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }

    public String cap(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

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
            lat = String.valueOf(mLastLocation.getLatitude());
            lng = String.valueOf(mLastLocation.getLongitude());

            eventLocOnMap();

        } else {
            // Toast.makeText(this, "Please check GPS and restart App", Toast.LENGTH_LONG).show();
        }
        Log.i("search", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAdapter.setGoogleApiClient(null);
        Log.e("search", "Google Places API connection suspended.");
    }

    //------------------------Adapter for info window--------------------------------
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;
        Marker marker;

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_info_window,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (this.marker != null
                    && this.marker.isInfoWindowShown()) {
                this.marker.hideInfoWindow();
                this.marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            this.marker = marker;

            String url = null;

            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null &&
                        markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final ImageView image = ((ImageView) view.findViewById(R.id.badge));

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {
                imageLoader.displayImage(url, image, options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view,
                                        loadedImage);
                                getInfoContents(marker);
                            }
                        });
            } else {
                image.setImageResource(R.mipmap.ic_launcher);
            }
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Toast.makeText(MapsActivity.this, marker.getId(), Toast.LENGTH_SHORT).show();
                       /* Intent j=new Intent(MapsActivity.this,DetailsActivity.class);
                        j.putExtra(GlobalConstants.EVENT_ID,global.getEvent_list().get(i).get(GlobalConstants.EVENT_ID));
                        startActivity(j);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/

                }
            });

            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }

            final String snippet = marker.getSnippet();
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }

            return view;
        }
    }

    //----------------------------Imageloader method--------------------
    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager)
                    getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
                .build();

        ImageLoader.getInstance().init(config);
    }

    //------------------------Current lat long------------------------
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();
    }

    public void get_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();
                Log.e("map data", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        JSONObject data = obj.getJSONObject("data");

                        JSONArray events = data.getJSONArray("events");
                        for (int i = 0; i < events.length(); i++) {
                            JSONObject arrobj = events.getJSONObject(i);

                            HashMap<String, String> details = new HashMap<>();

                            details.put(GlobalConstants.EVENT_ID, arrobj.getString(GlobalConstants.EVENT_ID));
                            details.put(GlobalConstants.EVENT_NAME, arrobj.getString(GlobalConstants.EVENT_NAME));
                            details.put(GlobalConstants.EVENT_LOC, arrobj.getString(GlobalConstants.EVENT_LOC));
                            details.put(GlobalConstants.EVENT_PRICE, arrobj.getString(GlobalConstants.EVENT_PRICE));
                            details.put(GlobalConstants.LATITUDE, arrobj.getString(GlobalConstants.LONGITUDE));
                            details.put(GlobalConstants.EVENT_FAV, arrobj.getString(GlobalConstants.EVENT_FAV));
                            details.put(GlobalConstants.EVENT_IMAGES, arrobj.getString(GlobalConstants.EVENT_IMAGES));
                            JSONArray arr = arrobj.getJSONArray("event_dates");
                            JSONObject objArr = arr.getJSONObject(0);
                            details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));
                            Log.e("list value", String.valueOf(i));

                            event_list.add(details);

                        }

                        global.setEvent_list(event_list);
                        Log.e("sie of map data", String.valueOf(global.getEvent_list().size()));

                        eventLocOnMap1();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog2.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(GlobalConstants.USERID, CommonUtils.UserID(MapsActivity.this));


                params.put(GlobalConstants.LATITUDE, lat);
                params.put(GlobalConstants.LONGITUDE, lng);
                params.put(GlobalConstants.RESPONSE_TYPE, "map");

                params.put("action", GlobalConstants.GET_EVENT_FILTER);

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
        requestQueue.add(cat_request);
    }

    public void eventLocOnMap() {


            //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            // Add a marker in Sydney and move the camera
        Log.e("event list value ",global.getEvent_list().toString());
            for (i = 0; i < global.getEvent_list().size(); i++) {
                LatLng postion = new LatLng(Double.parseDouble(global.getEvent_list().get(i).get(GlobalConstants.LATITUDE)), Double.parseDouble(global.getEvent_list().get(i).get(GlobalConstants.LONGITUDE)));
                mark = mMap.addMarker(new MarkerOptions().position(postion).title(global.getEvent_list().get(i).get(GlobalConstants.EVENT_NAME)).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_pin)));

                // markers.put(mark.getId(), "http://envagoapp.com/uploads/" + global.getEvent_list().get(i).get(GlobalConstants.EVENT_IMAGES));
                map.put(mark.getId(), i);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            }

        LatLng postion = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        mark = mMap.addMarker(new MarkerOptions().position(postion).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion, 6));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
        openMarkerView();


    }

    public void eventLocOnMap1() {
        if (mMap != null) {
            mMap.clear();
            map.clear();
            //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            // Add a marker in Sydney and move the camera
            Log.e("event list value1 ",global.getEvent_list().toString());
            for (i = 0; i < global.getEvent_list().size(); i++) {
                LatLng postion = new LatLng(Double.parseDouble(global.getEvent_list().get(i).get(GlobalConstants.LATITUDE)), Double.parseDouble(global.getEvent_list().get(i).get(GlobalConstants.LONGITUDE)));
                mark = mMap.addMarker(new MarkerOptions().position(postion).title(global.getEvent_list().get(i).get(GlobalConstants.EVENT_NAME)).icon(BitmapDescriptorFactory.fromResource(R.drawable.red_pin)));

                // markers.put(mark.getId(), "http://envagoapp.com/uploads/" + global.getEvent_list().get(i).get(GlobalConstants.EVENT_IMAGES));
                map.put(mark.getId(), i);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            }

            LatLng postion = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            mark = mMap.addMarker(new MarkerOptions().position(postion).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion, 6));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
            openMarkerView();

        }

    }


    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow() {
        dialog2 = new Dialog(this);
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
