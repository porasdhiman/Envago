package envago.envago;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by vikas on 15-10-2016.
 */
public class DetailsActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, View.OnTouchListener {
    protected View view;
    private ImageButton btnNext, btnFinish;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    Dialog dialog2, rating_dialog;
    ListView review_list;
    RelativeLayout event_info_layout;

    ArrayList<HashMap<String, String>> review_list_array = new ArrayList<>();
    TextView status_text, lower_description_txtView, admin_name, places_txtView, level_no1, level_no2,
            level_no3, admin_description, level_no4, date_details, meeting_desc, time_txtVIew,
            location_name_txtView, rating, about_txtView, route_txtView, rating_save, rating_cancel, review_txtview, header_textview;
    LinearLayout about_layout, map_layout, review_layout;
    ImageView heart_img, accomodation_txtView, transport_txtView, meal_txtView, gear_txtView, tent_txtView;
    CircleImageView orginiser_img;
    ArrayList<String> list = new ArrayList<>();
    Button purchase_btn;
    RatingBar stars;
    String meeting_loc, meeting_lat, meeting_long, ending_loc, ending_lat, ending_long;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    ImageView back_button;
    ArrayList<HashMap<String, String>> mapLatLong = new ArrayList<>();
    Button signup_btn;
    int i;
    //--------------------------------------MAp object-----------
    Marker marker;
    private GoogleMap mMap;
    private Hashtable<String, String> markers;

    private Location mLastLocation;
    ArrayList<Location> listing = new ArrayList<>();
    //--------------Google search api variable------------
    protected GoogleApiClient mGoogleApiClient;

    String months[] = { " ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec", };

    //-----------------------------------Paypal variable

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "Af64A4qjlISjmdbpxvgMs47i5Yl_Quuh8u4Ah-tfxo_DRH2ya-9MQpIEY2XLElcraeq2ox8Xvx6kUJRx";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;

    Global global;
    Date startDate,endDate;

    TextView days_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        review_txtview = (TextView) findViewById(R.id.review_txtView);
        review_layout = (LinearLayout) findViewById(R.id.review_layout);
        review_list = (ListView) findViewById(R.id.review_list);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        stars = (RatingBar) findViewById(R.id.stars);
        stars.setOnTouchListener(null);
        about_layout = (LinearLayout) findViewById(R.id.about_layout);
        map_layout = (LinearLayout) findViewById(R.id.map_layout);
        status_text = (TextView) findViewById(R.id.status_text);
        admin_name = (TextView) findViewById(R.id.event_name);
        about_txtView = (TextView) findViewById(R.id.about_txtView);
        route_txtView = (TextView) findViewById(R.id.route_txtView);
        date_details = (TextView) findViewById(R.id.date_details);
        meeting_desc = (TextView) findViewById(R.id.meeting_desc);
        time_txtVIew = (TextView) findViewById(R.id.time_txtView);
        level_no1 = (TextView) findViewById(R.id.level1);
        level_no2 = (TextView) findViewById(R.id.level2);
        level_no3 = (TextView) findViewById(R.id.level3);
        level_no4 = (TextView) findViewById(R.id.level4);
        header_textview = (TextView) findViewById(R.id.header_text);
        admin_description = (TextView) findViewById(R.id.upper_description);
        location_name_txtView = (TextView) findViewById(R.id.location_name);
        heart_img = (ImageView) findViewById(R.id.heart_img);
        heart_img.setOnClickListener(this);
        orginiser_img = (CircleImageView) findViewById(R.id.orginiser_img);
        rating = (TextView) findViewById(R.id.counter);
        lower_description_txtView = (TextView) findViewById(R.id.lower_description);
        accomodation_txtView = (ImageView) findViewById(R.id.accomodation);
        transport_txtView = (ImageView) findViewById(R.id.transport);
        meal_txtView = (ImageView) findViewById(R.id.meals);
        gear_txtView = (ImageView) findViewById(R.id.gear);
        tent_txtView = (ImageView) findViewById(R.id.tent);
        back_button = (ImageView) findViewById(R.id.detail_back_button);
        places_txtView = (TextView) findViewById(R.id.places_count_txtView);
        event_info_layout = (RelativeLayout) findViewById(R.id.event_info_layout);
        event_info_layout.setOnClickListener(this);
        purchase_btn = (Button) findViewById(R.id.purchase_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        days_details = (TextView)findViewById(R.id.days_details);
        signup_btn.setOnClickListener(this);
        purchase_btn.setOnClickListener(this);
        about_txtView.setOnClickListener(this);
        route_txtView.setOnClickListener(this);
        review_txtview.setOnClickListener(this);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.placeholder_image1)        //	Display Stub Image
                .showImageForEmptyUri(R.drawable.placeholder_image1)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
        dialogWindow();
        singleEventMethod();

        if (getIntent().getExtras().getString("user").equalsIgnoreCase("user")) {
            signup_btn.setVisibility(View.GONE);
        } else {
            signup_btn.setVisibility(View.VISIBLE);
        }

        //------------- map object initilization------------
        buildGoogleApiClient();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        markers = new Hashtable<String, String>();
        mapFragment.getMapAsync(this);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_txtView:
                about_layout.setVisibility(View.VISIBLE);
                map_layout.setVisibility(View.GONE);
                review_layout.setVisibility(View.GONE);
                about_txtView.setTextColor(getResources().getColor(R.color.textcolor));
                route_txtView.setTextColor(getResources().getColor(R.color.White));
                review_txtview.setTextColor(getResources().getColor(R.color.White));


                break;
            case R.id.route_txtView:
                about_layout.setVisibility(View.GONE);
                map_layout.setVisibility(View.VISIBLE);
                review_layout.setVisibility(View.GONE);

                route_txtView.setTextColor(getResources().getColor(R.color.textcolor));
                about_txtView.setTextColor(getResources().getColor(R.color.White));
                review_txtview.setTextColor(getResources().getColor(R.color.White));

                break;

            case R.id.event_info_layout:

                rating_dialog();
                break;

            case R.id.review_txtView:
                about_layout.setVisibility(View.GONE);
                map_layout.setVisibility(View.GONE);
                review_layout.setVisibility(View.VISIBLE);
                if (review_list_array.size() == 0) {
                    getreviewlist();
                } else {
                    review_list.setAdapter(new ReviewList_Adapter(review_list_array, DetailsActivity.this));
                }

                review_txtview.setTextColor(getResources().getColor(R.color.textcolor));
                route_txtView.setTextColor(getResources().getColor(R.color.White));
                about_txtView.setTextColor(getResources().getColor(R.color.White));
                break;
            case R.id.purchase_btn:
                thingToBuy = new PayPalPayment(new BigDecimal("10"), "USD",
                        "HeadSet", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(DetailsActivity.this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                break;
            case R.id.signup_btn:
                Intent i = new Intent(DetailsActivity.this, ConfirmDetailsActivity.class);
                startActivity(i);
                break;

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void pagerAdapterMethod(ArrayList<String> list) {
        this.list = list;
        mAdapter = new ViewPagerAdapter(DetailsActivity.this, list);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
    }

    private void singleEventMethod() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {

                                JSONArray data = obj.getJSONArray("data");
                                for (int j = 0; j < data.length(); j++) {
                                    JSONObject objArry = data.getJSONObject(j);
                                    JSONObject adminobj = objArry.getJSONObject("event_admin");

                                    JSONArray images = objArry.getJSONArray(GlobalConstants.EVENT_IMAGES);
                                    for (int i = 0; i < images.length(); i++) {
                                        JSONObject imagObj = images.getJSONObject(i);
                                        list.add("http://worksdelight.com/envago/uploads/" + imagObj.getString(GlobalConstants.IMAGE));
                                    }

//----------------------------------Map-Loaction-variable-----------------------------
                                    meeting_loc = objArry.getString(GlobalConstants.EVENT_METTING_POINT);
                                    meeting_lat = objArry.getString("meeting_point_latitude");
                                    meeting_long = objArry.getString("meeting_point_longitude");


                                    ending_loc = objArry.getString("end_location");
                                    ending_lat = objArry.getString("end_latitude");
                                    ending_long = objArry.getString("end_longitude");
                                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                                    // Add a marker in Sydney and move the camera
                                    LatLng postion = new LatLng(Double.parseDouble(meeting_lat), Double.parseDouble(meeting_long));
                                    Marker mark = mMap.addMarker(new MarkerOptions().position(postion).title("Meeting Point").snippet(meeting_loc));
                                    markers.put(mark.getId(), list.get(0));




                                    LatLng postion_end = new LatLng(Double.parseDouble(ending_lat), Double.parseDouble(ending_long));
                                    Marker mark_end = mMap.addMarker(new MarkerOptions().position(postion_end).title("Ending Point").snippet(ending_loc));
                                    markers.put(mark_end.getId(), list.get(0));

                                    LatLng postion_start = new LatLng(Double.parseDouble(objArry.getString("latitude")), Double.parseDouble(objArry.getString("longitude")));
                                    Marker mark_start = mMap.addMarker(new MarkerOptions().position(postion_start).title("Starting Point").snippet(objArry.getString("location")));
                                    markers.put(mark_start.getId(), list.get(0));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postion_start, 12));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


                                    Location li = new Location("");
                                    li.setLatitude(Double.parseDouble(objArry.getString("latitude")));
                                    li.setLongitude(Double.parseDouble(objArry.getString("longitude")));
                                    Location l2 = new Location("");
                                    l2.setLatitude(Double.parseDouble(ending_lat));
                                    l2.setLongitude(Double.parseDouble(ending_long));
                                    listing.add(li);
                                    listing.add(l2);
                                    drawPrimaryLinePath(listing);

                                    //--------------------------------end-map-location-variable---------------------------------------------


                                    admin_name.setText(adminobj.getString(GlobalConstants.ADMIN_NAME));
                                    header_textview.setText(objArry.getString(GlobalConstants.EVENT_NAME));
                                    global.setEvent_name(objArry.getString(GlobalConstants.EVENT_NAME));


                                    String url = "http://worksdelight.com/envago/uploads/" + adminobj.getString(GlobalConstants.ADMIN_IMAGE);
                                    if (url != null && !url.equalsIgnoreCase("null")
                                            && !url.equalsIgnoreCase("")) {
                                        imageLoader.displayImage(url, orginiser_img, options,
                                                new SimpleImageLoadingListener() {
                                                    @Override
                                                    public void onLoadingComplete(String imageUri,
                                                                                  View view, Bitmap loadedImage) {
                                                        super.onLoadingComplete(imageUri, view,
                                                                loadedImage);

                                                    }
                                                });
                                    } else {
                                        orginiser_img.setImageResource(R.mipmap.ic_launcher);
                                    }

                                    admin_description.setText(adminobj.getString(GlobalConstants.ADMIN_ABOUT));
                                    if (objArry.getString(GlobalConstants.ADMIN_RATING).contains(".")) {
                                        rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING).split("0")[0].replace(".", ""));
                                    } else {
                                        rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING));
                                    }

                                    stars.setRating(Float.parseFloat(rating.getText().toString()));
                                    if (dateMatchMethod(objArry.getString(GlobalConstants.EVENT_START_DATE))) {
                                        status_text.setVisibility(View.VISIBLE);
                                    } else {
                                        status_text.setVisibility(View.GONE);
                                    }
                                    global.setEvent_time(objArry.getString("time"));
                                    String date_data=objArry.getString(GlobalConstants.EVENT_START_DATE);
                                    String split[] = date_data.split("-");
                                    String minth = split[1];
                                    String date=split[2];
                                    int mm = Integer.parseInt(minth);
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                                            "yyyy-MM-dd");
                                    String dateafter = dateFormat.format(c.getTime());
                                    startDate=new Date();
                                    endDate=new Date();
                                    try {
                                        startDate=dateFormat.parse(date_data);
                                        endDate=dateFormat.parse(dateafter);

                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }
                                    date_details.setText(date+" "+months[mm]+" "+split[0]);

                                    days_details.setText(String.valueOf(getDaysDifference(startDate,endDate))+" Days");
                                    global.setEvent_start_date(objArry.getString(GlobalConstants.EVENT_START_DATE));
                                    global.setEvent_end_date(objArry.getString(GlobalConstants.EVENT_END_DATE));
                                    location_name_txtView.setText(objArry.getString(GlobalConstants.LOCATION));
                                    global.setEvent_loc(objArry.getString(GlobalConstants.LOCATION));
                                    if (objArry.getString(GlobalConstants.EVENT_LEVEL).equalsIgnoreCase("1")) {
                                        level_no1.setVisibility(View.VISIBLE);
                                    } else if (objArry.getString(GlobalConstants.EVENT_LEVEL).equalsIgnoreCase("2")) {
                                        level_no2.setVisibility(View.VISIBLE);
                                    } else if (objArry.getString(GlobalConstants.EVENT_LEVEL).equalsIgnoreCase("3")) {
                                        level_no3.setVisibility(View.VISIBLE);
                                    } else {
                                        level_no4.setVisibility(View.VISIBLE);
                                    }
                                    meeting_desc.setText(objArry.getString(GlobalConstants.EVENT_METTING_POINT));
                                    time_txtVIew.setText(objArry.getString("time"));
                                    i = Integer.parseInt(objArry.getString("is_liked"));
                                    if (i == 1) {
                                        heart_img.setImageResource(R.drawable.heart_field);
                                    } else {
                                        heart_img.setImageResource(R.drawable.heart);
                                    }
                                    lower_description_txtView.setText(objArry.getString("description"));

                                    if (objArry.getString("transport").equalsIgnoreCase("0")) {
                                        transport_txtView.setImageResource(R.drawable.tansport_gray);
                                    } else {
                                        transport_txtView.setImageResource(R.drawable.transportation);
                                    }
                                    if (objArry.getString("meals").equalsIgnoreCase("0")) {
                                        meal_txtView.setImageResource(R.drawable.food_gray);
                                    } else {
                                        meal_txtView.setImageResource(R.drawable.meal);
                                    }
                                    if (objArry.getString("accomodation").equalsIgnoreCase("0")) {
                                        accomodation_txtView.setImageResource(R.drawable.accomodation_gray);
                                    } else {
                                        accomodation_txtView.setImageResource(R.drawable.accomodation);
                                    }
                                    if (objArry.getString("gear").equalsIgnoreCase("0")) {
                                        gear_txtView.setImageResource(R.drawable.gear_gray);
                                    } else {
                                        gear_txtView.setImageResource(R.drawable.gear);
                                    }
                                    if (objArry.getString("tent").equalsIgnoreCase("0")) {
                                        tent_txtView.setImageResource(R.drawable.tent_gray);
                                    } else {
                                        tent_txtView.setImageResource(R.drawable.tent);
                                    }
                                    places_txtView.setText(objArry.getString("total_no_of_places") + "Places");
                                    purchase_btn.setText("$" + objArry.getString(GlobalConstants.EVENT_PRICE));
                                    global.setEvent_price(objArry.getString(GlobalConstants.EVENT_PRICE));
                                }
                                pagerAdapterMethod(list);


                            } else {
                                Toast.makeText(DetailsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                            dialog2.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                        Toast.makeText(DetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(DetailsActivity.this));

                params.put(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));


                params.put("action", GlobalConstants.DETAILS_EVENT_ACTION);

                Log.e("Single Event Param", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //------------------------------------------------GET-Review-List-API--------------------------------------


    public void getreviewlist() {
        StringRequest listReq = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("rating data", s);
                try {
                    JSONObject obj = new JSONObject(s);

                    String response = obj.getString("success");

                    if (response.equalsIgnoreCase("1")) {
                        JSONArray array = obj.getJSONArray("reviews");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj1 = array.getJSONObject(i);

                            JSONObject mainobj = obj1.getJSONObject("user");

                            HashMap<String, String> data = new HashMap<>();

                            data.put(GlobalConstants.USERNAME, mainobj.getString(GlobalConstants.USERNAME));
                            data.put("text", obj1.getString("text"));
                            data.put("rating", obj1.getString("rating"));

                            review_list_array.add(data);


                        }
                        review_list.setAdapter(new ReviewList_Adapter(review_list_array, DetailsActivity.this));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                params.put(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));

                params.put("action", "get_reviews_list");
                Log.e("rating param", params.toString());
                return params;
            }
        };

        listReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(listReq);
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

    //---------------MEthod for match date--------------
    boolean dateMatchMethod(String selectedDate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String dateafter = dateFormat.format(c.getTime());

        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(dateafter);
            eventDate = dateFormat.parse(selectedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (eventDate.equals(currentDate) || eventDate.before(currentDate)) {
            return true;
        } else {
            return false;
        }


    }

    //-------------------------------Image-save-cache------------------------------


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
                DetailsActivity.this).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    //----------------------Map method---------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    public boolean onTouch(View v, MotionEvent event) {
        return false;
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    //-------------------------------Rating-Dailog--------------------------------


    public void rating_dialog() {
        rating_dialog = new Dialog(this);
        rating_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  rating_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.w));
        rating_dialog.setCanceledOnTouchOutside(true);
        rating_dialog.setContentView(R.layout.review_layout);
       /* AVLoadingIndicatorView loaderView = (AVLoadingIndicatorView) dialog2.findViewById(R.id.loader_view);
        loaderView.show();*/

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        rating_dialog.show();

        rating_save = (TextView) rating_dialog.findViewById(R.id.save_button);
        rating_cancel = (TextView) rating_dialog.findViewById(R.id.cancel_button);
        final RatingBar stars_dailog = (RatingBar) rating_dialog.findViewById(R.id.stars_dailog);
        final EditText rating_cmnt = (EditText) rating_dialog.findViewById(R.id.rating_edittext);


        rating_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rating_cmnt.getText().toString().length() == 0) {
                    rating_cmnt.setError("Please enter comment");
                } else if (String.valueOf(stars_dailog.getRating()).equalsIgnoreCase("0")) {
                    Toast.makeText(DetailsActivity.this, "Please select rating star", Toast.LENGTH_SHORT).show();
                } else {
                    dialogWindow();
                    ratingApiMethod(rating_cmnt.getText().toString(), String.valueOf(stars_dailog.getRating()).split(".")[0]);
                }
            }
        });

        rating_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_dialog.dismiss();
            }
        });
    }

    private void ratingApiMethod(final String text, final String rate) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();
                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                                rating_dialog.dismiss();
                                Toast.makeText(DetailsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(DetailsActivity.this));
                params.put(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                params.put("text", text);
                params.put("rating", rate);


                params.put("action", "add_reviews");
                Log.e("facebook login", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //---------------------------------------------Payment method----------------------------------
    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(DetailsActivity.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject().toString(4));

//                        Toast.makeText(getActivity(), "Registered", Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.e("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.e("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(DetailsActivity.this,
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(DetailsActivity.this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(DetailsActivity.this,
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(DetailsActivity.this, PayPalService.class));
        super.onDestroy();
    }

    //---------------------------route--------------------------------------
    private void drawPrimaryLinePath(ArrayList<Location> listLocsToDraw) {
        if (mMap == null) {
            return;
        }

        if (listLocsToDraw.size() < 2) {
            return;
        }

        PolylineOptions options = new PolylineOptions();

        options.color(Color.parseColor("#CC0000FF"));
        options.width(5);
        options.visible(true);

        for (Location locRecorded : listLocsToDraw) {
            options.add(new LatLng(locRecorded.getLatitude(),
                    locRecorded.getLongitude()));
        }

        mMap.addPolyline(options);

    }

    //------------------date-method-----------

    public static int getDaysDifference(Date fromDate, Date toDate)
    {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

}
