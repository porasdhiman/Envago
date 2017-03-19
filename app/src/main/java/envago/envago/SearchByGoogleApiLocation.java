package envago.envago;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by worksdelight on 17/01/17.
 */

public class SearchByGoogleApiLocation extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    ListView search_listView;
    ArrayList<HashMap<String, String>> event_list = new ArrayList<>();
    Global global;
    TextView trending_txt;
    String lat, lng;
    Dialog dialog2;
    TextView cancel_txtView;
    boolean isLoading = false;
    int j = 1, page_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        setContentView(R.layout.search_by_location_layout);
        global = (Global) getApplicationContext();
        search_listView = (ListView) findViewById(R.id.search_list);
        trending_txt = (TextView) findViewById(R.id.trending_txt);
        trending_txt.setVisibility(View.GONE);

        search_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchByGoogleApiLocation.this, DetailsActivity.class);
                i.putExtra(GlobalConstants.EVENT_ID, event_list.get(position).get(GlobalConstants.EVENT_ID));
                i.putExtra("user", "non user");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        cancel_txtView = (TextView) findViewById(R.id.cancel_txtView);
        cancel_txtView.setVisibility(View.VISIBLE);
        cancel_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
                cancel_txtView.setVisibility(View.GONE);
            }
        });
        //-------------------------------Call AutocompleteTxtView-----------------
        buildGoogleApiClient();
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.mAutocompleteView);
        mAutocompleteView.setText(getIntent().getExtras().getString("loc"));
        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lng");
        dialogWindow();
        searchMethod();
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
        search_listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                // Ignore this method

            }


            @Override

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.i("Main", totalItemCount + "");


                int lastIndexInScreen = visibleItemCount + firstVisibleItem;


                if (lastIndexInScreen >= totalItemCount && !isLoading) {

                    // It is time to load more items
                    if (page_value >= j) {
                        isLoading = true;
                        dialogWindow();
                        searchMethod();
                    }


                }

            }

        });
        mAutocompleteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cancel_txtView.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void searchMethod() {


        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();


                Log.e("all data", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        JSONArray events = obj.getJSONArray("data");
                        if(events.length()==0){

                        }else{
                            j = j + 1;
                            // JSONObject data = obj.getJSONObject("data");
                            page_value = Integer.parseInt(obj.getString("next_page"));
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


                                event_list.add(details);

                            }
                            if(page_value>=j){
                                isLoading = false;
                                if(event_list.size()>0) {
                                    search_listView.setSelection(global.getSearchList().size());
                                    global.setSearchList(event_list);
                                    Log.e("list size", String.valueOf(global.getSearchList().size()));
                                    search_listView.setAdapter(new Adventure_list_adapter(SearchByGoogleApiLocation.this, global.getSearchList()));
                                }
                            }else{
                                if(event_list.size()>0) {
                                    global.setSearchList(event_list);
                                    Log.e("list size", String.valueOf(global.getSearchList().size()));
                                    search_listView.setAdapter(new Adventure_list_adapter(SearchByGoogleApiLocation.this, global.getSearchList()));
                                }
                            }
                        }


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

                params.put(GlobalConstants.USERID, CommonUtils.UserID(SearchByGoogleApiLocation.this));


                params.put(GlobalConstants.LATITUDE, lat);
                params.put(GlobalConstants.LONGITUDE, lng);
                params.put(GlobalConstants.RESPONSE_TYPE, "list");
                params.put("page", String.valueOf(j));
                params.put("perpage", "15");
                params.put("action", GlobalConstants.GET_EVENT_FILTER);

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SearchByGoogleApiLocation.this);
        requestQueue.add(cat_request);
    }

    public void dialogWindow() {
        dialog2 = new Dialog(SearchByGoogleApiLocation.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.progrees_dialog);
        AVLoadingIndicatorView loaderView = (AVLoadingIndicatorView) dialog2.findViewById(R.id.loader_view);
        loaderView.setVisibility(View.GONE);
        //loaderView.show();

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
            lat = completeLatLng.split(",")[0];
            lat = lat.substring(1, lat.length());
            lng = completeLatLng.split(",")[1];
            j=1;
            page_value=0;
            dialogWindow();
            searchMethod();
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
    public void onConnected(@Nullable Bundle bundle) {
        mAdapter.setGoogleApiClient(mGoogleApiClient);


        Log.i("search", "Google Places API connected.");
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


