package envago.envago;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


/**
 * Created by vikas on 15-10-2016.
 */
public class PreviewActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
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
            location_name_txtView, rating, about_txtView, route_txtView, whts_txt, who_txt, level_txt, header_textview, Disclaimer_txtView, locatio_txt;
    LinearLayout about_layout, map_layout, review_layout, desclaimer_layout;
    ImageView heart_img, accomodation_txtView, transport_txtView, meal_txtView, gear_txtView, tent_txtView, flight_img;
    ImageView orginiser_img;
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

    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};

    //-----------------------------------Paypal variable

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "ATu7TrT4HhoSprVHhzQYVhVoI_QrBo_-vUDqSMPWnrGJqvOtSyo4rJ-3mAVn-iaW5EyN7oeI3OjG09Jt";

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
    LinearLayout main_layout;
    TextView days_details, desclaimer_txt_show, price_btn, beginner_txt, more_txtView, more_dis_txtView, meeting_txt;
    ImageView dumy_imageview;
    TwoWayGridView user_grid;
    ArrayList<HashMap<String, String>> eventUserList = new ArrayList<>();
    String message;
    HttpEntity resEntity;
    TextDrawable drawable;
    SharedPreferences preferences, sp;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        preferences = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        sp = getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed = sp.edit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        markers = new Hashtable<String, String>();
        mapFragment.getMapAsync(this);
        global = (Global) getApplicationContext();
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        Fonts.overrideFonts(this, main_layout);

        desclaimer_layout = (LinearLayout) findViewById(R.id.desclaimer_layout);
        //intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        dumy_imageview = (ImageView) findViewById(R.id.dumy_imageview);
        // review_txtview = (TextView) findViewById(R.id.review_txtView);
        // review_layout = (LinearLayout) findViewById(R.id.review_layout);
        // review_list = (ListView) findViewById(R.id.review_list);
        //pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        stars = (RatingBar) findViewById(R.id.stars);
        LayerDrawable star = (LayerDrawable) stars.getProgressDrawable();
        star.getDrawable(2).setColorFilter(getResources().getColor(R.color.textcolor), PorterDuff.Mode.SRC_ATOP);

        stars.setOnTouchListener(null);
        more_txtView = (TextView) findViewById(R.id.more_txtView);
        more_dis_txtView = (TextView) findViewById(R.id.more_dis_txtView);
        more_txtView.setOnClickListener(this);
        more_dis_txtView.setOnClickListener(this);
        price_btn = (TextView) findViewById(R.id.price_btn);
        beginner_txt = (TextView) findViewById(R.id.beginner_txt);
        about_layout = (LinearLayout) findViewById(R.id.about_layout);
        map_layout = (LinearLayout) findViewById(R.id.map_layout);
        status_text = (TextView) findViewById(R.id.status_text);
        admin_name = (TextView) findViewById(R.id.event_name);
        user_grid = (TwoWayGridView) findViewById(R.id.user_view);
        // about_txtView = (TextView) findViewById(R.id.about_txtView);
        //route_txtView = (TextView) findViewById(R.id.route_txtView);
        //date_details = (TextView) findViewById(R.id.date_details);
        meeting_desc = (TextView) findViewById(R.id.meeting_desc);
        time_txtVIew = (TextView) findViewById(R.id.time_txtView);
      /*  level_no1 = (TextView) findViewById(R.id.level1);
        level_no2 = (TextView) findViewById(R.id.level2);
        level_no3 = (TextView) findViewById(R.id.level3);
        level_no4 = (TextView) findViewById(R.id.level4);*/
        header_textview = (TextView) findViewById(R.id.header_text);
        admin_description = (TextView) findViewById(R.id.upper_description);
        location_name_txtView = (TextView) findViewById(R.id.location_name);
        heart_img = (ImageView) findViewById(R.id.heart_img);
        heart_img.setOnClickListener(this);
        orginiser_img = (ImageView) findViewById(R.id.orginiser_img);
        //rating = (TextView) findViewById(R.id.counter);
        //lower_description_txtView = (TextView) findViewById(R.id.lower_description);
        Disclaimer_txtView = (TextView) findViewById(R.id.Disclaimer_txtView);
        desclaimer_txt_show = (TextView) findViewById(R.id.desclaimer_txt_show);
        accomodation_txtView = (ImageView) findViewById(R.id.accomodation);
        transport_txtView = (ImageView) findViewById(R.id.transport);
        meal_txtView = (ImageView) findViewById(R.id.meals);
        gear_txtView = (ImageView) findViewById(R.id.gear);
        tent_txtView = (ImageView) findViewById(R.id.tent);
        flight_img = (ImageView) findViewById(R.id.flight);
        back_button = (ImageView) findViewById(R.id.detail_back_button);
        places_txtView = (TextView) findViewById(R.id.places_count_txtView);
        //event_info_layout = (RelativeLayout) findViewById(R.id.event_info_layout);
        //event_info_layout.setOnClickListener(this);
        purchase_btn = (Button) findViewById(R.id.purchase_btn);
        // signup_btn = (Button) findViewById(R.id.signup_btn);
        //days_details = (TextView)findViewById(R.id.days_details);
        //signup_btn.setOnClickListener(this);
        purchase_btn.setOnClickListener(this);
        // about_txtView.setOnClickListener(this);
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
        locatio_txt = (TextView) findViewById(R.id.location_txt);
        level_txt = (TextView) findViewById(R.id.levl_txt);
        who_txt = (TextView) findViewById(R.id.who_txt);
        whts_txt = (TextView) findViewById(R.id.whts_txt);
        places_txtView = (TextView) findViewById(R.id.places_count_txtView);
        meeting_txt = (TextView) findViewById(R.id.meeting_text);

        desclaimer_txt_show = (TextView) findViewById(R.id.desclaimer_txt_show);
        Fonts.overrideFonts1(this, locatio_txt);
        Fonts.overrideFonts1(this, meeting_txt);
        Fonts.overrideFonts1(this, level_txt);
        Fonts.overrideFonts1(this, who_txt);
        Fonts.overrideFonts1(this, whts_txt);
        Fonts.overrideFonts1(this, desclaimer_txt_show);
        Fonts.overrideFonts1(this, places_txtView);
       /* dialogWindow();
        singleEventMethod();
*/
      /*  if (getIntent().getExtras().getString("user").equalsIgnoreCase("user")) {
            signup_btn.setVisibility(View.GONE);
        } else {
            signup_btn.setVisibility(View.VISIBLE);
        }*/

        //------------- map object initilization------------


        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        dumy_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PreviewActivity.this, FullViewImage.class);
                i.putExtra("image_url", "0");
                startActivity(i);
            }
        });
        allValueShow();
        buildGoogleApiClient();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.purchase_btn:
                dialogWindow();
                new Thread(null, address_request, "")
                        .start();

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
        }

    }


    public void allValueShow() {
        meeting_loc = global.getStartingPoint();
        meeting_lat = global.getStarting_lat();
        meeting_long = global.getStarting_lng();
        Log.e("lat long value", meeting_lat + meeting_long);


        //--------------------------------end-map-location-variable---------------------------------------------


        admin_name.setText(preferences.getString(GlobalConstants.USERNAME, ""));
        header_textview.setText(sp.getString(GlobalConstants.EVENT_NAME, ""));


        admin_description.setText(sp.getString(GlobalConstants.EVENT_DESCRIPTION, ""));

        if (sp.getString(GlobalConstants.EVENT_DESCRIPTION, "").length() > 200) {
            more_txtView.setVisibility(View.VISIBLE);

        }


        global.setEvent_time(sp.getString(GlobalConstants.EVENT_TIME, ""));


        location_name_txtView.setText(global.getStartingPoint());

        meeting_desc.setText(sp.getString(GlobalConstants.EVENT_METTING_POINT, ""));
        char a = admin_name.getText().toString().charAt(0);

        drawable = TextDrawable.builder()
                .buildRound(cap(String.valueOf(a)), Color.parseColor("#F94444"));


        if (preferences.getString(GlobalConstants.IMAGE, "").length() == 0) {

        } else {
            drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(a), Color.parseColor("#F94444"));
            if (preferences.getString(GlobalConstants.IMAGE, "").contains("http")) {
                Picasso.with(PreviewActivity.this).load(preferences.getString(GlobalConstants.IMAGE, "")).placeholder(drawable).transform(new CircleTransform()).into(orginiser_img);
            } else {
                if (!preferences.getString(GlobalConstants.IMAGE, "").equalsIgnoreCase("")) {
                    Picasso.with(PreviewActivity.this).load(new File(preferences.getString(GlobalConstants.IMAGE, ""))).placeholder(drawable).transform(new CircleTransform()).into(orginiser_img);

                    //profilepic.setImageURI(Uri.fromFile(new File(preferences.getString(GlobalConstants.IMAGE, ""))));
                } else {
                    orginiser_img.setImageDrawable(drawable);
                }
            }
        }
        time_txtVIew.setText(sp.getString(GlobalConstants.EVENT_TIME, ""));
        i = 0;

        if (sp.getString("trans", "0").equalsIgnoreCase("0")) {
            transport_txtView.setImageResource(R.drawable.tansport_gray);
        } else {
            transport_txtView.setImageResource(R.drawable.transportation);
        }
        if (sp.getString("meal", "0").equalsIgnoreCase("0")) {
            meal_txtView.setImageResource(R.drawable.food_gray);
        } else {
            meal_txtView.setImageResource(R.drawable.meal);
        }
        if (sp.getString("Accomodation", "0").equalsIgnoreCase("0")) {
            accomodation_txtView.setImageResource(R.drawable.accomodation_gray);
        } else {
            accomodation_txtView.setImageResource(R.drawable.accomodation);
        }
        if (sp.getString("gear", "0").equalsIgnoreCase("0")) {
            gear_txtView.setImageResource(R.drawable.gear_gray);
        } else {
            gear_txtView.setImageResource(R.drawable.gear);
        }
        if (sp.getString("tent", "0").equalsIgnoreCase("0")) {
            tent_txtView.setImageResource(R.drawable.tent_gray);
        } else {
            tent_txtView.setImageResource(R.drawable.tent);
        }
        if (sp.getString("flight", "0").equalsIgnoreCase("0")) {
            flight_img.setImageResource(R.drawable.flight_gray);
        } else {
            flight_img.setImageResource(R.drawable.flight);
        }

        Disclaimer_txtView.setText(sp.getString(GlobalConstants.EVENT_DISCLAIMER, ""));


        if (sp.getString(GlobalConstants.EVENT_DISCLAIMER, "").length() > 200) {

            more_dis_txtView.setVisibility(View.VISIBLE);
        }
        places_txtView.setText(sp.getString(GlobalConstants.EVENT_PLACE, "") + " places");
        purchase_btn.setText("Submit");

        user_grid.setAdapter(new UserViewAdapter(PreviewActivity.this, Integer.parseInt(sp.getString(GlobalConstants.EVENT_PLACE, "")), eventUserList, header_textview.getText().toString()));
        dumy_imageview.setImageURI(Uri.fromFile(new File(global.getListImg().get(0))));
        price_btn.setText("$" + sp.getString(GlobalConstants.EVENT_PRICE, ""));
        if (sp.getString(GlobalConstants.EVENT_LEVEL, "").equalsIgnoreCase("1")) {
            beginner_txt.setText("Easy");
        } else if (sp.getString(GlobalConstants.EVENT_LEVEL, "").equalsIgnoreCase("2")) {
            beginner_txt.setText("Moderate");
        } else if (sp.getString(GlobalConstants.EVENT_LEVEL, "").equalsIgnoreCase("3")) {
            beginner_txt.setText("Difficult");
        } else {
            beginner_txt.setText("Extreme");
        }

    }

    public String cap(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    private String getMapsApiDirectionsUrl(String startLat, String startlng, String meetingLat, String meetingLng) {

        String url1 = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLat + "," + startlng + "&waypoints=" + meetingLat + "," + meetingLng + "&destination=" + meetingLat + "," + meetingLng + "&sensor=true&mode=walking";

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
                polyLineOptions.width(5);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
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
                PreviewActivity.this).threadPoolSize(5)
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
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(Double.parseDouble(meeting_lat), Double.parseDouble(meeting_long))).icon(BitmapDescriptorFactory.fromResource(R.drawable.oval)).title("starting Point");

        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(meeting_lat), Double.parseDouble(meeting_long)), 7));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);
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


    //-------------------------------------------------Api method-----------------------

    Runnable address_request = new Runnable() {
        String res = "false";


        @Override
        public void run() {
            try {

                res = doFileUpload();
            } catch (Exception e) {

            }
            Message msg = new Message();
            msg.obj = res;
            address_request_Handler.sendMessage(msg);
        }
    };

    Handler address_request_Handler = new Handler() {
        public void handleMessage(Message msg) {
            String res = (String) msg.obj;
            dialog2.dismiss();
            if (res.equalsIgnoreCase("true")) {
                // terms_dialog.dismiss();
                Toast.makeText(PreviewActivity.this, message,
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(PreviewActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }

        }

    };

    public ArrayList<HashMap<String, String>> loadSharedPreferencesLogList() {
        ArrayList<HashMap<String, String>> callLog = new ArrayList<HashMap<String, String>>();

        Gson gson = new Gson();
        String json = sp.getString(GlobalConstants.DATE_DATA, "");
        if (json.isEmpty()) {
            callLog = new ArrayList<HashMap<String, String>>();
        } else {
            Type type = new TypeToken<List<HashMap<String, String>>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    // ------------------------------------------------------upload
    // method---------------
    private String doFileUpload() {
        String success = "false";

        String urlString = GlobalConstants.URL;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();
            for (int i = 0; i < global.getListImg().size(); i++) {
                File file1 = new File(global.getListImg().get(i));
                FileBody bin1 = new FileBody(file1, "image/jpg");
                reqEntity.addPart("file" + i, bin1);
                Log.e("file" + i, global.getListImg().get(i).toString());

            }
           /* File file1 = new File(global.getListImg().get(0));
            FileBody bin1 = new FileBody(file1, "image/jpg");
            reqEntity.addPart("image1", bin1);
            Log.e("image1", global.getListImg().get(i).toString());
            File file2 = new File(global.getListImg().get(1));
            FileBody bin2 = new FileBody(file2, "image/jpg");
            reqEntity.addPart("image2", bin2);
            Log.e("image2", global.getListImg().get(i).toString());
*/



            reqEntity.addPart(GlobalConstants.USERID, new StringBody(CommonUtils.UserID(this)));


            reqEntity.addPart(GlobalConstants.MAIN_CAT_ID, new StringBody("1"));
            Log.e("main  id", "1");
            reqEntity.addPart(GlobalConstants.EVENT_CAT_ID, new StringBody(sp.getString(GlobalConstants.EVENT_CAT_ID, "")));
            Log.e("sub_cat_id", sp.getString(GlobalConstants.EVENT_CAT_ID, ""));
            reqEntity.addPart(GlobalConstants.EVENT_NAME, new StringBody(sp.getString(GlobalConstants.EVENT_NAME, "")));
            Log.e("name", sp.getString(GlobalConstants.EVENT_NAME, ""));
            if (sp.getString("date type", "").equalsIgnoreCase("one_time")) {
                reqEntity.addPart("event_type", new StringBody(sp.getString("date type", "")));
                Log.e("event_type", sp.getString("date type", ""));
                reqEntity.addPart("event_no_of_days", new StringBody(sp.getString(GlobalConstants.NUMBER_OF_DAY, "")));
                Log.e("event_no_of_days", sp.getString(GlobalConstants.NUMBER_OF_DAY, ""));
                JSONArray installedList = new JSONArray();


                try {
                    JSONObject installedPackage = new JSONObject();
                    installedPackage.put(GlobalConstants.EVENT_START_DATE, sp.getString(GlobalConstants.EVENT_START_DATE, ""));
                    installedPackage.put(GlobalConstants.EVENT_END_DATE, sp.getString(GlobalConstants.EVENT_END_DATE, ""));
                    installedList.put(installedPackage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
            } else if (sp.getString("date type", "").equalsIgnoreCase("full_season")) {
                reqEntity.addPart("event_type", new StringBody(sp.getString("date type", "")));
                Log.e("event_type", sp.getString("date type", ""));
                reqEntity.addPart("event_no_of_days", new StringBody(sp.getString(GlobalConstants.NUMBER_OF_DAY, "")));
                Log.e("event_no_of_days", sp.getString(GlobalConstants.NUMBER_OF_DAY, ""));

                reqEntity.addPart("event_season", new StringBody(""));
                Log.e("event_season", "");
                JSONArray installedList = new JSONArray();


                try {
                    JSONObject installedPackage = new JSONObject();
                    installedPackage.put(GlobalConstants.EVENT_START_DATE, sp.getString(GlobalConstants.EVENT_START_DATE, ""));
                    installedPackage.put(GlobalConstants.EVENT_END_DATE, sp.getString(GlobalConstants.EVENT_END_DATE, ""));
                    installedList.put(installedPackage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
            } else {
                reqEntity.addPart("event_type", new StringBody(sp.getString("date type", "")));
                Log.e("event_type", sp.getString("date type", ""));
                reqEntity.addPart("event_no_of_days", new StringBody(sp.getString(GlobalConstants.NUMBER_OF_DAY, "")));
                Log.e("event_no_of_days", sp.getString(GlobalConstants.NUMBER_OF_DAY, ""));
                global.setDateArray(loadSharedPreferencesLogList());
                JSONArray installedList = new JSONArray();


                for (int i = 0; i < global.getDateArray().size(); i++) {
                    try {
                        JSONObject installedPackage = new JSONObject();
                        installedPackage.put(GlobalConstants.EVENT_START_DATE, global.getDateArray().get(i).get(GlobalConstants.EVENT_START_DATE));
                        installedPackage.put(GlobalConstants.EVENT_END_DATE, global.getDateArray().get(i).get(GlobalConstants.EVENT_END_DATE));
                        installedList.put(installedPackage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
               /* reqEntity.addPart("event_dates[0][event_end_date]", new StringBody("2017-10-3"));
                Log.e("event_dates[0][event_end_date]", "2017-10-3");*/
            }


            reqEntity.addPart(GlobalConstants.EVENT_TIME, new StringBody(sp.getString(GlobalConstants.EVENT_TIME, "")));
            Log.e(GlobalConstants.EVENT_TIME, sp.getString(GlobalConstants.EVENT_TIME, ""));
            reqEntity.addPart(GlobalConstants.EVENT_LEVEL, new StringBody(sp.getString(GlobalConstants.EVENT_LEVEL, "")));
            Log.e(GlobalConstants.EVENT_LEVEL, sp.getString(GlobalConstants.EVENT_LEVEL, ""));
            reqEntity.addPart(GlobalConstants.EVENT_METTING_POINT, new StringBody(sp.getString(GlobalConstants.EVENT_METTING_POINT, "")));
            Log.e(GlobalConstants.EVENT_METTING_POINT, sp.getString(GlobalConstants.EVENT_METTING_POINT, ""));
            reqEntity.addPart("meeting_point_latitude", new StringBody(sp.getString(GlobalConstants.EVENT_MEETING_LAT, "")));
            Log.e("meeting_point_latitude", sp.getString(GlobalConstants.EVENT_MEETING_LAT, ""));
            reqEntity.addPart("meeting_point_longitude", new StringBody(sp.getString(GlobalConstants.EVENT_MEETING_LNG, "")));
            Log.e("meeting_point_longitude", sp.getString(GlobalConstants.EVENT_MEETING_LNG, ""));
            /*reqEntity.addPart("crireria_eligibilty", new StringBody(global.getEvent_criteria()));
            Log.e("crireria_eligibilty", global.getEvent_criteria());*/
            reqEntity.addPart(GlobalConstants.LOCATION, new StringBody(global.getStartingPoint()));
            Log.e(GlobalConstants.LOCATION, global.getStartingPoint());
            reqEntity.addPart(GlobalConstants.LATITUDE, new StringBody(global.getStarting_lat()));
            Log.e(GlobalConstants.LATITUDE, global.getStarting_lat());
            reqEntity.addPart(GlobalConstants.LONGITUDE, new StringBody(global.getStarting_lng()));
            Log.e(GlobalConstants.LONGITUDE, global.getStarting_lng());
            reqEntity.addPart("end_location", new StringBody(global.getwPoint()));
            Log.e("end_location", global.getwPoint());
            reqEntity.addPart("end_latitude", new StringBody(global.getW_lat()));
            Log.e("end_latitude", global.getW_lat());
            reqEntity.addPart("end_longitude", new StringBody(global.getW_lng()));
            Log.e("end_longitude", global.getW_lng());
            reqEntity.addPart("location_1", new StringBody(global.getaPoint()));
            Log.e("location_1", global.getaPoint());
            reqEntity.addPart("loc_1_latitude", new StringBody(global.getA_lat()));
            Log.e("loc_1_latitude", global.getA_lat());
            reqEntity.addPart("loc_1_longitude", new StringBody(global.getA_lng()));
            Log.e("loc_1_longitude", global.getA_lng());
            reqEntity.addPart("location_2", new StringBody(global.getbPoint()));
            Log.e("location_2", global.getbPoint());
            reqEntity.addPart("loc_2_latitude", new StringBody(global.getB_lat()));
            Log.e("loc_2_latitude", global.getB_lat());
            reqEntity.addPart("loc_2_longitude", new StringBody(global.getB_lng()));
            Log.e("loc_2_longitude", global.getB_lng());
            reqEntity.addPart("location_3", new StringBody(global.getcPoint()));
            Log.e("location_3", global.getcPoint());
            reqEntity.addPart("loc_3_latitude", new StringBody(global.getC_lat()));
            Log.e("loc_3_latitude", global.getC_lat());
            reqEntity.addPart("loc_3_longitude", new StringBody(global.getC_lng()));
            Log.e("loc_3_longitude", global.getC_lng());
           /* reqEntity.addPart("location_4", new StringBody(loc4_location));
            Log.e("location_4", loc4_location);
            reqEntity.addPart("loc_4_latitude", new StringBody(loc4_lat));
            Log.e("loc_4_latitude", loc4_lat);
            reqEntity.addPart("loc_4_longitude", new StringBody(loc4_lng));
            Log.e("loc_4_longitude", loc4_lng);*/
            reqEntity.addPart("description", new StringBody(sp.getString(GlobalConstants.EVENT_DESCRIPTION, "")));
            Log.e("description", sp.getString(GlobalConstants.EVENT_DESCRIPTION, ""));
            reqEntity.addPart("no_of_places", new StringBody(sp.getString(GlobalConstants.EVENT_PLACE, "")));
            reqEntity.addPart("price", new StringBody(sp.getString(GlobalConstants.EVENT_PRICE, "")));
            Log.e("no_of_places", sp.getString(GlobalConstants.EVENT_PRICE, ""));
            Log.e("price", sp.getString(GlobalConstants.EVENT_PRICE, ""));
            reqEntity.addPart("whats_included", new StringBody("dddd"));
            Log.e("whats_included", "dddd");
            reqEntity.addPart("meals", new StringBody(sp.getString("meal", "0")));
            Log.e("meals", sp.getString("meal", "0"));
            reqEntity.addPart("transport", new StringBody(sp.getString("trans", "0")));
            Log.e("transport", sp.getString("trans", "0"));
            reqEntity.addPart("tent", new StringBody(sp.getString("tent", "0")));
            Log.e("tent", String.valueOf(sp.getString("tent", "0")));
            reqEntity.addPart("accomodation", new StringBody(sp.getString("Accomodation", "0")));
            Log.e("accomodation", sp.getString("Accomodation", "0"));
            reqEntity.addPart("gear", new StringBody(sp.getString("gear", "0")));
            Log.e("gear", sp.getString("gear", "0"));
            reqEntity.addPart("disclaimer", new StringBody(sp.getString(GlobalConstants.EVENT_DISCLAIMER, "")));
            Log.e("disclaimer", sp.getString(GlobalConstants.EVENT_DISCLAIMER, ""));
            reqEntity.addPart("flight", new StringBody(sp.getString("flight", "0")));
            Log.e("flight", sp.getString("flight", "0"));
            reqEntity.addPart("action", new StringBody(GlobalConstants.CREATE_EVENT_ACTION));
            Log.e("action", GlobalConstants.CREATE_EVENT_ACTION);

            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            Log.e("response_str", response_str);
            if (resEntity != null) {
                JSONObject obj = new JSONObject(response_str);
                String status = obj.getString("success");
                if (status.equalsIgnoreCase("1")) {
                    success = "true";
                    message = obj.getString("msg");
                    ed.clear();
                    ed.commit();

                    Intent i = new Intent(PreviewActivity.this, Tab_Activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else {
                    success = "false";
                    message = obj.getString("msg");
                }

                /*JSONObject resp = new JSONObject(response_str);
                String status = resp.getString("status");
                String message = resp.getString("message");
                if (status.equals("1")) {

                    success = "true";

                } else {

                    success = "false";

                }
*/
            }
        } catch (Exception ex) {
        }
        return success;
    }


    //---------------------------------------------Payment method----------------------------------
    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(PreviewActivity.this,
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
                        Toast.makeText(PreviewActivity.this,
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
                .getApplicationCorrelationId(PreviewActivity.this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(PreviewActivity.this,
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(PreviewActivity.this, PayPalService.class));
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
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);


        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Read Less", false);

                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "Read More", true);

                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }

        return ssb;

    }

}
