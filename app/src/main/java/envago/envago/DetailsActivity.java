package envago.envago;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jess.ui.TwoWayGridView;
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
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * Created by vikas on 15-10-2016.
 */
public class DetailsActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
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
    ArrayList<HashMap<String, String>> event_date_array = new ArrayList<>();
    ArrayList<HashMap<String, String>> review_list_array = new ArrayList<>();
    TextView status_text, lower_description_txtView, admin_name, places_txtView, level_no1, level_no2,
            level_no3, admin_description, level_no4, date_details, meeting_desc, time_txtVIew,
            location_name_txtView, rating, about_txtView, route_txtView, rating_save, rating_cancel, review_txtview, header_textview, Disclaimer_txtView;
    LinearLayout about_layout, map_layout, review_layout, desclaimer_layout;
    ImageView heart_img, accomodation_txtView, transport_txtView, meal_txtView, gear_txtView, tent_txtView, flight, back_button_create;
    ImageView orginiser_img;
    ArrayList<String> list = new ArrayList<>();
    Button purchase_btn;
    LinearLayout stars;
    String meeting_loc, meeting_lat, meeting_long, ending_loc, ending_lat, ending_long;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    ImageView back_button;
    ArrayList<HashMap<String, String>> mapLatLong = new ArrayList<>();
    Button signup_btn;
    int i = 0;
    //--------------------------------------MAp object-----------
    Marker marker;
    private GoogleMap mMap;
    private Hashtable<String, String> markers;

    private Location mLastLocation;
    ArrayList<Location> listing = new ArrayList<>();
    //--------------Google search api variable------------
    protected GoogleApiClient mGoogleApiClient;

    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};

    //-----------------------------------Paypal variable

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String CONFIG_CLIENT_ID = "AeVIdBo3Xom815-pzUShWlXQdr3c-SUyfQPvdTwycWJpzSSbD-Mu3DbkybQqmpnjfb7Cy0AwRSJq9o3G";

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
    Date startDate, endDate;

    TextView locatio_txt, desclaimer_txt_show, about_planner, level_txt, beginner_txt, price_btn, more_txtView, more_dis_txtView, who_txt, whts_txt, meeting_txt;
    ImageView dumy_imageview;
    TwoWayGridView user_grid;
    String id;
    ArrayList<HashMap<String, String>> eventUserList = new ArrayList<>();
    ArrayList<HashMap<String, String>> locationList = new ArrayList<>();
    String url1, dateType;
    float ratingValue;
    TextDrawable drawable;
    ScrollView scrollview_main;
    int r_value;
    LinearLayout main_layout;
    int total, t1;
    ImageView star1, star2, star3, star4, star5,share_img;
//---------------------- Facebook share -----------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        Fonts.overrideFonts(this, main_layout);

        // review_txtview = (TextView) findViewById(R.id.review_txtView);
        //  review_layout = (LinearLayout) findViewById(R.id.review_layout);
        // review_list = (ListView) findViewById(R.id.review_list);
        // pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        stars = (LinearLayout) findViewById(R.id.stars);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);

        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);

        star5 = (ImageView) findViewById(R.id.star5);
        scrollview_main = (ScrollView) findViewById(R.id.scrollview_main);
        locatio_txt = (TextView) findViewById(R.id.location_txt);
        level_txt = (TextView) findViewById(R.id.levl_txt);
        who_txt = (TextView) findViewById(R.id.who_txt);
        whts_txt = (TextView) findViewById(R.id.whts_txt);
        meeting_txt = (TextView) findViewById(R.id.meeting_text);
        places_txtView = (TextView) findViewById(R.id.places_count_txtView);

        desclaimer_txt_show = (TextView) findViewById(R.id.desclaimer_txt_show);
        share_img = (ImageView) findViewById(R.id.share_img);
        price_btn = (TextView) findViewById(R.id.price_btn);
        Fonts.overrideFonts1(this, locatio_txt);
        Fonts.overrideFonts1(this, meeting_txt);

        Fonts.overrideFonts1(this, level_txt);
        Fonts.overrideFonts1(this, who_txt);
        Fonts.overrideFonts1(this, whts_txt);
        Fonts.overrideFonts1(this, desclaimer_txt_show);
        Fonts.overrideFonts1(this, places_txtView);

        back_button_create = (ImageView) findViewById(R.id.back_button_create);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        about_layout = (LinearLayout) findViewById(R.id.about_layout);
        map_layout = (LinearLayout) findViewById(R.id.map_layout);
        status_text = (TextView) findViewById(R.id.status_text);
        admin_name = (TextView) findViewById(R.id.event_name);
        about_planner = (TextView) findViewById(R.id.about_planner);
        more_txtView = (TextView) findViewById(R.id.more_txtView);
        more_dis_txtView = (TextView) findViewById(R.id.more_dis_txtView);
        //route_txtView = (TextView) findViewById(R.id.route_txtView);
        // date_details = (TextView) findViewById(R.id.date_details);
        meeting_desc = (TextView) findViewById(R.id.meeting_desc);
        time_txtVIew = (TextView) findViewById(R.id.time_txtView);
        desclaimer_layout = (LinearLayout) findViewById(R.id.desclaimer_layout);
        //intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        dumy_imageview = (ImageView) findViewById(R.id.dumy_imageview);
      /*  level_no1 = (TextView) findViewById(R.id.level1);
        level_no2 = (TextView) findViewById(R.id.level2);
        level_no3 = (TextView) findViewById(R.id.level3);
        level_no4 = (TextView) findViewById(R.id.level4);*/
        header_textview = (TextView) findViewById(R.id.title);

        admin_description = (TextView) findViewById(R.id.upper_description);
        location_name_txtView = (TextView) findViewById(R.id.location_name);
        heart_img = (ImageView) findViewById(R.id.heart_img);
        heart_img.setOnClickListener(this);
        orginiser_img = (ImageView) findViewById(R.id.orginiser_img);
        //rating = (TextView) findViewById(R.id.counter);
        //lower_description_txtView = (TextView) findViewById(R.id.lower_description);
        beginner_txt = (TextView) findViewById(R.id.beginner_txt);


        Disclaimer_txtView = (TextView) findViewById(R.id.Disclaimer_txtView);

        accomodation_txtView = (ImageView) findViewById(R.id.accomodation);
        transport_txtView = (ImageView) findViewById(R.id.transport);
        meal_txtView = (ImageView) findViewById(R.id.meals);
        flight = (ImageView) findViewById(R.id.flight);
        gear_txtView = (ImageView) findViewById(R.id.gear);
        tent_txtView = (ImageView) findViewById(R.id.tent);
        back_button = (ImageView) findViewById(R.id.detail_back_button);
        user_grid = (TwoWayGridView) findViewById(R.id.user_view);
        //event_info_layout = (RelativeLayout) findViewById(R.id.event_info_layout);
        //event_info_layout.setOnClickListener(this);

        about_planner.setOnClickListener(this);
        more_txtView.setOnClickListener(this);
        more_dis_txtView.setOnClickListener(this);
        //route_txtView.setOnClickListener(this);
        // review_txtview.setOnClickListener(this);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(0)        //	Display Stub Image
                .showImageForEmptyUri(0)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
        dialogWindow();
        singleEventMethod();

      /*  if (getIntent().getExtras().getString("user").equalsIgnoreCase("user")) {
            signup_btn.setVisibility(View.GONE);
        } else {
            signup_btn.setVisibility(View.VISIBLE);
        }*/

        //------------- map object initilization------------
        more_txtView.setOnClickListener(this);
        buildGoogleApiClient();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        markers = new Hashtable<String, String>();
        mapFragment.getMapAsync(this);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        dumy_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, FullViewImage.class);
                i.putExtra("image_url", "1");
                startActivity(i);
            }
        });
        purchase_btn = (Button) findViewById(R.id.purchase_btn);

        share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm using Envago Adventures to go on \n" + header_textview.getText().toString()+ "\nhttps://play.google.com/store/apps/details?id=envago.envago&hl=ens");
                startActivity(shareIntent);
                /*Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image*//*");


                String urlToShare = header_textview.getText().toString();



                // See if official Facebook app is found

                List<ResolveInfo> matches = getPackageManager()
                        .queryIntentActivities(share, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase()
                            .startsWith("com.facebook.katana")) {
                        share.setPackage(info.activityInfo.packageName);
                        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        share.putExtra(Intent.EXTRA_TITLE, "Hey, download this app! https://itunes.apple.com/in/app/envago-adventures/id1140098064?mt=8 for join event with me");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(global.getF()));
                        startActivity(Intent.createChooser(share,"Share via"));
                        break;
                    }
                }
*/
                // As fallback, launch sharer.php in a browser





            }
        });

    }

    /*   private void setUiPageViewController() {

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
   */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_planner:
                Intent about = new Intent(DetailsActivity.this, AboutPlannerActivity.class);
                about.putExtra(GlobalConstants.USERID, id);
                about.putExtra(GlobalConstants.EVENT_NAME, admin_name.getText().toString());
                startActivity(about);

                break;
            case R.id.purchase_btn:

                if (dateType.equalsIgnoreCase("one_time")) {
                    Intent i = new Intent(DetailsActivity.this, ConfirmDetailsActivity.class);
                    i.putExtra(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                    i.putExtra(GlobalConstants.remaining_places, event_date_array.get(0).get(GlobalConstants.remaining_places));
                    i.putExtra("pos", String.valueOf(0));
                    startActivity(i);

                } else if (dateType.equalsIgnoreCase("full_season")) {
                    Intent i = new Intent(DetailsActivity.this, OpenFullsessionActivity.class);
                    i.putExtra(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                    i.putExtra(GlobalConstants.NUMBER_OF_DAY, String.valueOf(t1));
                    startActivity(i);
                } else {
                    Intent i = new Intent(DetailsActivity.this, BookDateActivity.class);
                    i.putExtra(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                    startActivity(i);
                }


                break;
            case R.id.more_txtView:
                if (more_txtView.getText().toString().equalsIgnoreCase("More...")) {
                    more_txtView.setText("Less...");
                    ViewGroup.LayoutParams params = admin_description.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    admin_description.setLayoutParams(params);
                } else {
                    more_txtView.setText("More...");

                    ViewGroup.LayoutParams params = admin_description.getLayoutParams();
                    params.height = 50;
                    admin_description.setLayoutParams(params);
                }
                break;
            case R.id.more_dis_txtView:
                if (more_dis_txtView.getText().toString().equalsIgnoreCase("More...")) {
                    more_dis_txtView.setText("Less...");
                    ViewGroup.LayoutParams params = Disclaimer_txtView.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    Disclaimer_txtView.setLayoutParams(params);
                } else {
                    more_dis_txtView.setText("More...");

                    ViewGroup.LayoutParams params = Disclaimer_txtView.getLayoutParams();
                    params.height = 50;
                    Disclaimer_txtView.setLayoutParams(params);
                }
                break;
            case R.id.heart_img:
                if (i == 0) {
                    favoriteMethod(getIntent().getExtras().getString(GlobalConstants.EVENT_ID), "1");
                    heart_img.setImageResource(R.drawable.heart_white);

                } else {
                    favoriteMethod(getIntent().getExtras().getString(GlobalConstants.EVENT_ID), "0");
                    heart_img.setImageResource(R.drawable.heart);

                }
                break;

        }

    }

    private void favoriteMethod(final String event_id, final String like_status) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {


                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(DetailsActivity.this));
                params.put(GlobalConstants.EVENT_ID, event_id);
                params.put(GlobalConstants.LIKE_STATUS, like_status);

                params.put("action", GlobalConstants.ACTION_LIKE_EVENT);

                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivity.this);
        requestQueue.add(stringRequest);
    }


    /*public void pagerAdapterMethod(ArrayList<String> list) {
        this.list = list;
        if(list.size()!=0) {
            dumy_imageview.setVisibility(View.GONE);
            intro_images.setVisibility(View.VISIBLE);
            mAdapter = new ViewPagerAdapter(DetailsActivity.this, list);
            intro_images.setAdapter(mAdapter);
            intro_images.setCurrentItem(0);
            intro_images.setOnPageChangeListener(this);
            setUiPageViewController();
        }
    }*/

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
                                    if (CommonUtils.UserID(DetailsActivity.this).equalsIgnoreCase(objArry.getString(GlobalConstants.USERID))) {
                                        purchase_btn.setVisibility(View.GONE);
                                        heart_img.setVisibility(View.VISIBLE);
                                        heart_img.setOnClickListener(null);
                                    } else {
                                        purchase_btn.setVisibility(View.VISIBLE);
                                        heart_img.setVisibility(View.VISIBLE);
                                    }
                                    /*if (getIntent().getExtras().getString("user").equalsIgnoreCase("user")) {
                                        purchase_btn.setVisibility(View.GONE);
                                        heart_img.setVisibility(View.INVISIBLE);
                                    } else*/ if (getIntent().getExtras().getString("user").equalsIgnoreCase("no user wish")) {
                                        heart_img.setVisibility(View.VISIBLE);
                                        heart_img.setOnClickListener(null);
                                        stars.setOnTouchListener(DetailsActivity.this);

                                        purchase_btn.setOnClickListener(DetailsActivity.this);
                                    } else {
                                        stars.setOnTouchListener(DetailsActivity.this);

                                        purchase_btn.setOnClickListener(DetailsActivity.this);
                                    }

                                    JSONArray images = objArry.getJSONArray(GlobalConstants.EVENT_IMAGES);
                                    for (int i = 0; i < images.length(); i++) {
                                        JSONObject imagObj = images.getJSONObject(i);
                                        list.add(GlobalConstants.IMAGE_URL + imagObj.getString(GlobalConstants.IMAGE));
                                    }
                                    global.setList(list);

//----------------------------------Map-Loaction-variable-----------------------------
                                    meeting_loc = objArry.getString(GlobalConstants.EVENT_METTING_POINT);
                                    meeting_lat = objArry.getString("meeting_point_latitude");
                                    meeting_long = objArry.getString("meeting_point_longitude");


                                    JSONArray location = objArry.getJSONArray("locations");

                                    if (location.length() == 0) {


                                        MarkerOptions options = new MarkerOptions();
                                        options.position(new LatLng(Double.parseDouble(objArry.getString("latitude")), Double.parseDouble(objArry.getString("longitude")))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");

                                        mMap.addMarker(options);
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(objArry.getString("latitude")), Double.parseDouble(objArry.getString("longitude"))), 7));
                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);
                                        dialog2.dismiss();

                                    } else {
                                        if (location.length() == 1) {
                                            JSONObject locObj = location.getJSONObject(0);
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("location_1", locObj.getString("location_1"));
                                            map.put("loc_1_latitude", locObj.getString("loc_1_latitude"));
                                            map.put("loc_1_longitude", locObj.getString("loc_1_longitude"));
                                            locationList.add(map);
                                            Log.e("location_list", locationList.toString());
                                            String locationUrl = getMapsApiDirectionsUrl2(objArry.getString("latitude"), objArry.getString("longitude"), locationList);
                                            ReadTask downloadTask = new ReadTask();
                                            Log.e("locationUrl", locationUrl);
                                            downloadTask.execute(locationUrl);

                                            MarkerOptions options = new MarkerOptions();
                                            MarkerOptions options1 = new MarkerOptions();
                                            options.position(new LatLng(Double.parseDouble(objArry.getString("latitude")), Double.parseDouble(objArry.getString("longitude")))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");

                                            options1.position(new LatLng(Double.parseDouble(locObj.getString("loc_1_latitude")), Double.parseDouble(locObj.getString("loc_1_longitude"))));
                                            options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.a)).title("First Point");


                                            mMap.addMarker(options);
                                            mMap.addMarker(options1);
                                            LatLng pos = new LatLng(Double.parseDouble(objArry.getString("latitude")), Double.parseDouble(objArry.getString("longitude")));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                                        } else if (location.length() == 2) {
                                            JSONObject locObj = location.getJSONObject(0);
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("location_1", locObj.getString("location_1"));
                                            map.put("loc_1_latitude", locObj.getString("loc_1_latitude"));
                                            map.put("loc_1_longitude", locObj.getString("loc_1_longitude"));
                                            locationList.add(map);
                                            JSONObject locObj1 = location.getJSONObject(1);
                                            HashMap<String, String> map1 = new HashMap<>();
                                            map1.put("location_2", locObj1.getString("location_2"));
                                            map1.put("loc_2_latitude", locObj1.getString("loc_2_latitude"));
                                            map1.put("loc_2_longitude", locObj1.getString("loc_2_longitude"));
                                            locationList.add(map1);

                                            Log.e("location_list", locationList.toString());
                                            String locationUrl = getMapsApiDirectionsUrl2(objArry.getString("latitude"), objArry.getString("longitude"), locationList);
                                            ReadTask downloadTask = new ReadTask();
                                            Log.e("locationUrl", locationUrl);
                                            downloadTask.execute(locationUrl);

                                            MarkerOptions options = new MarkerOptions();
                                            MarkerOptions options1 = new MarkerOptions();
                                            MarkerOptions options2 = new MarkerOptions();

                                            options.position(new LatLng(Double.parseDouble(objArry.getString("latitude")), Double.parseDouble(objArry.getString("longitude")))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");

                                            options1.position(new LatLng(Double.parseDouble(locObj.getString("loc_1_latitude")), Double.parseDouble(locObj.getString("loc_1_longitude"))));
                                            options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.a)).title("First Point");
                                            options2.position(new LatLng(Double.parseDouble(locObj1.getString("loc_2_latitude")), Double.parseDouble(locObj1.getString("loc_2_longitude"))));
                                            options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.b)).title("Second Point");


                                            mMap.addMarker(options);
                                            mMap.addMarker(options1);
                                            mMap.addMarker(options2);
                                            LatLng pos = new LatLng(Double.parseDouble(locObj1.getString("loc_2_latitude")), Double.parseDouble(locObj1.getString("loc_2_longitude")));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                                        } else {
                                            JSONObject locObj = location.getJSONObject(0);
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("location_1", locObj.getString("location_1"));
                                            map.put("loc_1_latitude", locObj.getString("loc_1_latitude"));
                                            map.put("loc_1_longitude", locObj.getString("loc_1_longitude"));
                                            locationList.add(map);
                                            JSONObject locObj1 = location.getJSONObject(1);
                                            HashMap<String, String> map1 = new HashMap<>();
                                            map1.put("location_2", locObj1.getString("location_2"));
                                            map1.put("loc_2_latitude", locObj1.getString("loc_2_latitude"));
                                            map1.put("loc_2_longitude", locObj1.getString("loc_2_longitude"));
                                            locationList.add(map1);
                                            JSONObject locObj2 = location.getJSONObject(2);
                                            HashMap<String, String> map2 = new HashMap<>();
                                            map2.put("location_3", locObj2.getString("location_3"));
                                            map2.put("loc_3_latitude", locObj2.getString("loc_3_latitude"));
                                            map2.put("loc_3_longitude", locObj2.getString("loc_3_longitude"));
                                            locationList.add(map2);

                                            Log.e("location_list", locationList.toString());
                                            String locationUrl = getMapsApiDirectionsUrl2(objArry.getString("latitude"), objArry.getString("longitude"), locationList);
                                            ReadTask downloadTask = new ReadTask();
                                            Log.e("locationUrl", locationUrl);
                                            downloadTask.execute(locationUrl);


                                            MarkerOptions options = new MarkerOptions();
                                            MarkerOptions options1 = new MarkerOptions();
                                            MarkerOptions options2 = new MarkerOptions();
                                            MarkerOptions options3 = new MarkerOptions();
                                            options.position(new LatLng(Double.parseDouble(objArry.getString("latitude")), Double.parseDouble(objArry.getString("longitude")))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");

                                            options1.position(new LatLng(Double.parseDouble(locObj.getString("loc_1_latitude")), Double.parseDouble(locObj.getString("loc_1_longitude"))));
                                            options1.icon(BitmapDescriptorFactory.fromResource(R.drawable.a)).title("First Point");
                                            options2.position(new LatLng(Double.parseDouble(locObj1.getString("loc_2_latitude")), Double.parseDouble(locObj1.getString("loc_2_longitude"))));
                                            options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.b)).title("Second Point");

                                            options3.position(new LatLng(Double.parseDouble(locObj2.getString("loc_3_latitude")), Double.parseDouble(locObj2.getString("loc_3_longitude"))));
                                            options3.icon(BitmapDescriptorFactory.fromResource(R.drawable.c)).title("Third Point");

                                            mMap.addMarker(options);
                                            mMap.addMarker(options1);
                                            mMap.addMarker(options2);
                                            mMap.addMarker(options3);
                                            LatLng pos = new LatLng(Double.parseDouble(locObj1.getString("loc_2_latitude")), Double.parseDouble(locObj1.getString("loc_2_longitude")));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));
                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                                        }


                                    }


                                    //--------------------------------end-map-location-variable---------------------------------------------


                                    admin_name.setText(cap(adminobj.getString(GlobalConstants.ADMIN_NAME)));
                                    global.setAdminName(cap(adminobj.getString(GlobalConstants.ADMIN_NAME)));
                                    header_textview.setText(cap(objArry.getString(GlobalConstants.EVENT_NAME)));
                                    global.setEvent_name(objArry.getString(GlobalConstants.EVENT_NAME));
                                    id = objArry.getString(GlobalConstants.USERID);

                                    admin_description.setText(objArry.getString("description"));
                                    if (objArry.getString("description").length() > 80) {

                                        more_txtView.setVisibility(View.VISIBLE);
                                    } else if (objArry.getString("disclaimer").length() < 30) {
                                        ViewGroup.LayoutParams params = more_txtView.getLayoutParams();
                                        params.height = 10;
                                    }
                                    JSONArray event_users = objArry.getJSONArray("event_users");
                                    for (int i = 0; i < event_users.length(); i++) {
                                        JSONObject event_obj = event_users.getJSONObject(i);
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put(GlobalConstants.ID, event_obj.getString(GlobalConstants.ID));

                                        map.put(GlobalConstants.USERNAME, event_obj.getString(GlobalConstants.USERNAME));
                                        map.put(GlobalConstants.IMAGE, event_obj.getString(GlobalConstants.IMAGE));
                                        eventUserList.add(map);
                                    }
                                    Log.e("Event user", eventUserList.toString());
                                    String url = GlobalConstants.IMAGE_URL + adminobj.getString(GlobalConstants.ADMIN_IMAGE);

                                    global.setAdminUrl(url);

                                    char a = admin_name.getText().toString().charAt(0);

                                    drawable = TextDrawable.builder()
                                            .buildRound(String.valueOf(a), Color.parseColor("#F94444"));
                                    if (url != null && !url.equalsIgnoreCase("null")
                                            && !url.equalsIgnoreCase("")) {

                                        Picasso.with(DetailsActivity.this).load(url).placeholder(drawable).transform(new CircleTransform()).into(orginiser_img);
                                    } else {
                                        orginiser_img.setImageDrawable(drawable);
                                    }
                                   /* if (url != null && !url.equalsIgnoreCase("null")
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
                                        orginiser_img.setImageDrawable(drawable);
                                    }*/

                                    if (global.getList().get(0) != null && !global.getList().get(0).equalsIgnoreCase("null")
                                            && !url.equalsIgnoreCase("")) {
                                        imageLoader.displayImage(global.getList().get(0), dumy_imageview, options,
                                                new SimpleImageLoadingListener() {
                                                    @Override
                                                    public void onLoadingComplete(String imageUri,
                                                                                  View view, Bitmap loadedImage) {
                                                        super.onLoadingComplete(imageUri, view,
                                                                loadedImage);

                                                    }
                                                });
                                    } else {
                                        dumy_imageview.setImageResource(0);
                                    }
                                    global.setAdminRating(objArry.getString(GlobalConstants.ADMIN_RATING));

/*
                                    if (objArry.getString(GlobalConstants.ADMIN_RATING).contains(".")) {
                                        // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING).split("0")[0].replace(".", ""));
                                        stars.setRating(Float.parseFloat(objArry.getString(GlobalConstants.ADMIN_RATING).split("0")[0].replace(".", "")));
                                    } else {*/
                                    // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING));
                                    //stars.setRating(Float.parseFloat(objArry.getString(GlobalConstants.ADMIN_RATING)));
                                    //       }
                                    String starValue = objArry.getString(GlobalConstants.ADMIN_RATING);
                                    if (starValue.contains("1")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star_blank);
                                        star3.setImageResource(R.drawable.star_blank);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (starValue.contains("2")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star_blank);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (starValue.contains("3")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (starValue.contains("4")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star);
                                        star4.setImageResource(R.drawable.star);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (starValue.contains("5")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star);
                                        star4.setImageResource(R.drawable.star);
                                        star5.setImageResource(R.drawable.star);
                                    } else {
                                        star1.setImageResource(R.drawable.star_blank);
                                        star2.setImageResource(R.drawable.star_blank);
                                        star3.setImageResource(R.drawable.star_blank);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    }
                                    location_name_txtView.setText(objArry.getString(GlobalConstants.LOCATION));
                                    global.setEvent_loc(objArry.getString(GlobalConstants.LOCATION));
                                    if (objArry.getString(GlobalConstants.EVENT_LEVEL).equalsIgnoreCase("1")) {
                                        beginner_txt.setText("Easy");
                                    } else if (objArry.getString(GlobalConstants.EVENT_LEVEL).equalsIgnoreCase("2")) {
                                        beginner_txt.setText("Moderate");
                                    } else if (objArry.getString(GlobalConstants.EVENT_LEVEL).equalsIgnoreCase("3")) {
                                        beginner_txt.setText("Difficult");
                                    } else {
                                        beginner_txt.setText("Extreme");
                                    }
                                    meeting_desc.setText(objArry.getString(GlobalConstants.EVENT_METTING_POINT));
                                    global.setEvent_meetin_point(objArry.getString(GlobalConstants.EVENT_METTING_POINT));
                                    time_txtVIew.setText(objArry.getString("time"));
                                    i = Integer.parseInt(objArry.getString("is_liked"));
                                    if (i == 1) {
                                        heart_img.setImageResource(R.drawable.heart_white);
                                    } else {
                                        heart_img.setImageResource(R.drawable.heart);
                                    }
                                    //   lower_description_txtView.setText(objArry.getString("description"));

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
                                    if (objArry.getString("flight").equalsIgnoreCase("0")) {
                                        flight.setImageResource(R.drawable.flight_gray);
                                    } else {
                                        flight.setImageResource(R.drawable.flight);
                                    }
                                    if (objArry.getString("disclaimer").equalsIgnoreCase(null) || objArry.getString("disclaimer").equalsIgnoreCase("") || objArry.getString("disclaimer").equalsIgnoreCase(" ")) {
                                        desclaimer_layout.setVisibility(view.GONE);
                                    } else {
                                        desclaimer_layout.setVisibility(view.VISIBLE);

                                        Disclaimer_txtView.setText(objArry.getString("disclaimer"));
                                        if (objArry.getString("disclaimer").length() > 80) {

                                            more_dis_txtView.setVisibility(View.VISIBLE);
                                        } else if (objArry.getString("disclaimer").length() < 30) {
                                            ViewGroup.LayoutParams params = Disclaimer_txtView.getLayoutParams();
                                            params.height = 10;
                                        }
                                    }


                                    price_btn.setText("$" + objArry.getString("price") + " per person");
                                    Fonts.overrideFonts1(DetailsActivity.this, price_btn);
                                    purchase_btn.setText("Book");
                                    global.setEvent_price(objArry.getString(GlobalConstants.EVENT_PRICE));
                                    scrollview_main.smoothScrollTo(0, 0);
                                    dateType = objArry.getString("event_type");
                                    global.setEvent_no_of_days(objArry.getString("event_no_of_days"));
                                    global.setEventSession(objArry.getString("event_season"));

                                    JSONArray event_dates = objArry.getJSONArray("event_dates");
                                    for (int i = 0; i < event_dates.length(); i++) {
                                        JSONObject event_datesObj = event_dates.getJSONObject(i);
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put(GlobalConstants.ID, event_datesObj.getString(GlobalConstants.ID));
                                        map.put(GlobalConstants.EVENT_START_DATE, event_datesObj.getString(GlobalConstants.EVENT_START_DATE));
                                        map.put(GlobalConstants.EVENT_END_DATE, event_datesObj.getString(GlobalConstants.EVENT_END_DATE));
                                        map.put(GlobalConstants.remaining_places, event_datesObj.getString(GlobalConstants.remaining_places));
                                        map.put(GlobalConstants.NUMBER_OF_DAY, objArry.getString("total_no_of_places"));

                                        event_date_array.add(map);
                                    }
                                    Log.e("event_date_array", event_date_array.toString());
                                    global.setBookdateArray(event_date_array);


                                    if (dateType.equalsIgnoreCase("one_time")) {
                                        total = Integer.parseInt(objArry.getString("total_no_of_places"));

                                        if (Integer.parseInt(event_date_array.get(0).get(GlobalConstants.remaining_places)) == 0) {
                                            total = eventUserList.size();
                                            purchase_btn.setText("Sold out");
                                            purchase_btn.setOnClickListener(null);

                                        } else {
                                            if (total != Integer.parseInt(event_date_array.get(0).get(GlobalConstants.remaining_places))) {

                                                total = Integer.parseInt(event_date_array.get(0).get(GlobalConstants.remaining_places)) + eventUserList.size();

                                            }

                                        }
                                        if (dateMatchMethod(event_date_array.get(0).get(GlobalConstants.EVENT_START_DATE))) {
                                            purchase_btn.setText("Closed");
                                            purchase_btn.setOnClickListener(null);
                                        }

                                        user_grid.setAdapter(new UserViewAdapter(DetailsActivity.this, total, eventUserList, header_textview.getText().toString()));
                                        if (eventUserList.size() > 0) {
                                            places_txtView.setText(objArry.getString("total_no_of_places") + " Places ");
                                        } else {
                                            places_txtView.setText(String.valueOf(total) + " Places ");
                                        }

                                        global.setEvent_start_date(event_date_array.get(0).get(GlobalConstants.EVENT_START_DATE));
                                        global.setEvent_end_date(event_date_array.get(0).get(GlobalConstants.EVENT_END_DATE));
                                    } else if (dateType.equalsIgnoreCase("full_season")) {
                                        t1 = Integer.parseInt(objArry.getString("event_no_of_days"));
                                        total = Integer.parseInt(objArry.getString("total_no_of_places")) * event_date_array.size();
                                        int l = 0, p = 0;
                                        for (int k = 0; k < event_date_array.size(); k++) {
                                            l = Integer.parseInt(event_date_array.get(k).get(GlobalConstants.remaining_places)) + l;
                                        }
                                        if (l == 0) {
                                            total = eventUserList.size();
                                            l = total;
                                            purchase_btn.setText("Sold out");
                                            purchase_btn.setOnClickListener(null);
                                        } else {
                                            if (l != total) {
                                                p = l;
                                                l = l + eventUserList.size();
                                            } else {
                                                p = total;
                                            }
                                        }

                                        if (dateMatchMethod(event_date_array.get(event_date_array.size() - 1).get(GlobalConstants.EVENT_START_DATE))) {
                                            purchase_btn.setText("Closed");
                                            purchase_btn.setOnClickListener(null);
                                        }

                                        user_grid.setAdapter(new UserViewAdapter(DetailsActivity.this, l, eventUserList, header_textview.getText().toString()));
                                        if (eventUserList.size() > 0) {
                                            places_txtView.setText(/*String.valueOf(p) + "/" + String.valueOf(total) + */" Places (" + objArry.getString("total_no_of_places") + " per trip)");
                                        } else {
                                            Log.e("total", String.valueOf(p));
                                            places_txtView.setText(/*String.valueOf(p) +*/ " Places (" + objArry.getString("total_no_of_places") + " per trip)");
                                        }
                                        global.setEvent_start_date(event_date_array.get(0).get(GlobalConstants.EVENT_START_DATE));
                                        global.setEvent_end_date(event_date_array.get(event_date_array.size() - 1).get(GlobalConstants.EVENT_END_DATE));
                                    } else {
                                        total = Integer.parseInt(objArry.getString("total_no_of_places")) * event_date_array.size();
                                        int l = 0, p = 0;
                                        for (int k = 0; k < event_date_array.size(); k++) {
                                            l = Integer.parseInt(event_date_array.get(k).get(GlobalConstants.remaining_places)) + l;
                                        }
                                        if (l == 0) {
                                            total = eventUserList.size();
                                            l = total;
                                            purchase_btn.setText("Sold out");
                                            purchase_btn.setOnClickListener(null);
                                        } else {
                                            if (l != total) {
                                                p = l;
                                                l = l + eventUserList.size();
                                            } else {
                                                p = total;
                                            }
                                        }


                                        user_grid.setAdapter(new UserViewAdapter(DetailsActivity.this, l, eventUserList, header_textview.getText().toString()));
                                        if (eventUserList.size() > 0) {
                                            places_txtView.setText(/*String.valueOf(p) + "/" + String.valueOf(total) */ " Places (" + objArry.getString("total_no_of_places") + " per trip)");
                                        } else {
                                            Log.e("total", String.valueOf(p));
                                            places_txtView.setText(/*String.valueOf(p) + */" Places (" + objArry.getString("total_no_of_places") + " per trip)");
                                        }

                                    }


                                  /*  if (dateMatchMethod(event_date_array.get(0).get(GlobalConstants.EVENT_START_DATE))) {
                                        status_text.setVisibility(View.VISIBLE);
                                    } else {
                                        status_text.setVisibility(View.GONE);
                                    }*/
                                    global.setEvent_time(objArry.getString("time"));
                                    String date_data = event_date_array.get(0).get(GlobalConstants.EVENT_START_DATE);
                                    String split[] = date_data.split("-");
                                    String minth = split[1];
                                    String date = split[2];
                                    int mm = Integer.parseInt(minth);
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                                            "yyyy-MM-dd");
                                    String dateafter = dateFormat.format(c.getTime());
                                    startDate = new Date();
                                    endDate = new Date();
                                    try {
                                        startDate = dateFormat.parse(date_data);
                                        endDate = dateFormat.parse(dateafter);

                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }
                                    // date_details.setText(date+" "+months[mm]+" "+split[0]);

                                    //days_details.setText(String.valueOf(getDaysDifference(startDate,endDate))+" Days");


                                }


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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String cap(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    private String getMapsApiDirectionsUrl2(String startLat, String startlng, ArrayList<HashMap<String, String>> list) {
        if (locationList.size() == 1) {
            url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLat + "," + startlng + "&waypoints=" + list.get(0).get("loc_1_latitude") + "," + list.get(0).get("loc_1_longitude") + "&destination=" + list.get(0).get("loc_1_latitude") + "," + list.get(0).get("loc_1_longitude") + "&sensor=true&mode=walking";
        } else if (locationList.size() == 2) {
            url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLat + "," + startlng + "&waypoints=" + list.get(0).get("loc_1_latitude") + "," + list.get(0).get("loc_1_longitude") + "%7C" + list.get(1).get("loc_2_latitude") + "," + list.get(1).get("loc_2_longitude") + "&destination=" + list.get(1).get("loc_2_latitude") + "," + list.get(1).get("loc_2_longitude") + "&sensor=true&mode=walking";
        } else {
            url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLat + "," + startlng + "&waypoints=" + list.get(0).get("loc_1_latitude") + "," + list.get(0).get("loc_1_longitude") + "%7C" + list.get(1).get("loc_2_latitude") + "," + list.get(1).get("loc_2_longitude") + "%7C" + list.get(2).get("loc_3_latitude") + "," + list.get(2).get("loc_3_longitude") + "&destination=" + list.get(2).get("loc_3_latitude") + "," + list.get(2).get("loc_3_longitude") + "&sensor=true&mode=walking";

        }
        return url1;
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
                polyLineOptions.width(8);
                polyLineOptions.color(Color.BLUE);
            }
            if (polyLineOptions != null) {
                mMap.addPolyline(polyLineOptions);

            }
            dialog2.dismiss();

        }

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

        listReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
        loaderView.setVisibility(View.GONE);
        // loaderView.show();

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
        rating_dialog();
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
        //rating_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.w));
        rating_dialog.setCanceledOnTouchOutside(true);
        rating_dialog.setContentView(R.layout.review_layout);
       /* AVLoadingIndicatorView loaderView = (AVLoadingIndicatorView) dialog2.findViewById(R.id.loader_view);
        loaderView.show();*/

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        rating_dialog.show();

        rating_save = (TextView) rating_dialog.findViewById(R.id.save_button);
        rating_cancel = (TextView) rating_dialog.findViewById(R.id.cancel_button);
        final RatingBar stars_dailog = (RatingBar) rating_dialog.findViewById(R.id.stars_dailog);
        LayerDrawable stars = (LayerDrawable) stars_dailog.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.textcolor), PorterDuff.Mode.SRC_ATOP);
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
                    ratingApiMethod(rating_cmnt.getText().toString(), String.valueOf(stars_dailog.getRating()));
                    ratingValue = stars_dailog.getRating();
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
                                String value = String.valueOf(ratingValue);
                                Log.e("value", value);

                                if (value.contains("1")) {
                                    star1.setImageResource(R.drawable.star);
                                    star2.setImageResource(R.drawable.star_blank);
                                    star3.setImageResource(R.drawable.star_blank);
                                    star4.setImageResource(R.drawable.star_blank);
                                    star5.setImageResource(R.drawable.star_blank);
                                } else if (value.contains("2")) {
                                    star1.setImageResource(R.drawable.star);
                                    star2.setImageResource(R.drawable.star);
                                    star3.setImageResource(R.drawable.star_blank);
                                    star4.setImageResource(R.drawable.star_blank);
                                    star5.setImageResource(R.drawable.star_blank);
                                } else if (value.contains("3")) {
                                    star1.setImageResource(R.drawable.star);
                                    star2.setImageResource(R.drawable.star);
                                    star3.setImageResource(R.drawable.star);
                                    star4.setImageResource(R.drawable.star_blank);
                                    star5.setImageResource(R.drawable.star_blank);
                                } else if (value.contains("4")) {
                                    star1.setImageResource(R.drawable.star);
                                    star2.setImageResource(R.drawable.star);
                                    star3.setImageResource(R.drawable.star);
                                    star4.setImageResource(R.drawable.star);
                                    star5.setImageResource(R.drawable.star_blank);
                                } else if (value.contains("5")) {
                                    star1.setImageResource(R.drawable.star);
                                    star2.setImageResource(R.drawable.star);
                                    star3.setImageResource(R.drawable.star);
                                    star4.setImageResource(R.drawable.star);
                                    star5.setImageResource(R.drawable.star);
                                } else {
                                    star1.setImageResource(R.drawable.star_blank);
                                    star2.setImageResource(R.drawable.star_blank);
                                    star3.setImageResource(R.drawable.star_blank);
                                    star4.setImageResource(R.drawable.star_blank);
                                    star5.setImageResource(R.drawable.star_blank);
                                }

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

    public static int getDaysDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;

        return (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    //-------------------------------------More textView method-----------------
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        Log.e("text data", tv.getText().toString());

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });
    }


    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {

        String str = strSpanned.toString();
        Log.e("Spanned text data ", str);
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);


        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Less", false);

                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "More..", true);

                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }

        return ssb;

    }


}
