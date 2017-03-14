package envago.envago;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import java.sql.Time;

/**
 * Created by vikas on 05-01-2017.
 */

public class CreateDetailActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener, View.OnTouchListener {
    EditText name_editText, place_editText, pricing_editText, crtieria_editText;
    TextView name_error_txtView, meeting_time_error_txtView, meeting_error_txtView, desc_error_txtView, place_error_txtView, pricing_error_txtView,
            crtieria_error_txtView, disclaimer_error_txtView, whts_error_txtView, whts_editText, desc_editText, meeting_time_editText, cat_error_txtView, disclaimer_editText;
    Spinner cat_editText;
    private Calendar calendar;
    private int hour;
    private int minute;
    private DatePickerDialog fromDatePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    //----------------search location api variable-------
    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    Global global;
    Button submit_button;
    String catgory = "";
    SeekBar seekBar;
    ImageView back_button_create;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    String[] catArray = {"Select", "Air", "Earth", "Water", "Rock &amp; Ice ", "Go Volunteer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        buildGoogleApiClient();
        sp = getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed = sp.edit();
        global = (Global) getApplicationContext();
        //-------------------------------Call AutocompleteTxtView-----------------
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.meeting_editText);
        back_button_create = (ImageView) findViewById(R.id.back_button_create);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        init();
        cat_editText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (position == 0) {

                } else {

                    global.setEvent_cat_id(String.valueOf(position));
                    catgory = cat_editText.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        calendar = Calendar.getInstance();


        hour = calendar.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = calendar.get(Calendar.MINUTE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                global.setEvent_level(String.valueOf(progress + 1));
                ed.putString(GlobalConstants.EVENT_LEVEL, global.getEvent_level());
                ed.commit();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void init() {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        name_editText = (EditText) findViewById(R.id.name_editText);
        meeting_time_editText = (TextView) findViewById(R.id.meeting_time_editText);
        desc_editText = (TextView) findViewById(R.id.desc_editText);
        place_editText = (EditText) findViewById(R.id.place_editText);
        pricing_editText = (EditText) findViewById(R.id.pricing_editText);
        //crtieria_editText = (EditText) findViewById(R.id.crtieria_editText);
        disclaimer_editText = (TextView) findViewById(R.id.disclaimer_editText);
        whts_editText = (TextView) findViewById(R.id.whts_editText);
        name_error_txtView = (TextView) findViewById(R.id.name_error_txtView);
        meeting_time_error_txtView = (TextView) findViewById(R.id.meeting_time_error_txtView);
        meeting_error_txtView = (TextView) findViewById(R.id.meeting_error_txtView);
        desc_error_txtView = (TextView) findViewById(R.id.desc_error_txtView);
        place_error_txtView = (TextView) findViewById(R.id.place_error_txtView);
        pricing_error_txtView = (TextView) findViewById(R.id.pricing_error_txtView);
        // crtieria_error_txtView = (TextView) findViewById(R.id.crtieria_error_txtView);
        disclaimer_error_txtView = (TextView) findViewById(R.id.disclaimer_error_txtView);
        whts_error_txtView = (TextView) findViewById(R.id.whts_error_txtView);
        cat_error_txtView = (TextView) findViewById(R.id.cat_error_txtView);
        cat_editText = (Spinner) findViewById(R.id.cat_editText);
        submit_button = (Button) findViewById(R.id.submit_button);
        meeting_time_editText.setOnClickListener(this);
        desc_editText.setOnClickListener(this);
        // crtieria_editText.setOnClickListener(this);
        disclaimer_editText.setOnClickListener(this);
        whts_editText.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        name_editText.setOnTouchListener(this);
        meeting_time_editText.setOnTouchListener(this);
        desc_editText.setOnTouchListener(this);
        place_editText.setOnTouchListener(this);
        pricing_editText.setOnTouchListener(this);
        // crtieria_editText.setOnTouchListener(this);
        disclaimer_editText.setOnTouchListener(this);
        whts_editText.setOnTouchListener(this);
        cat_editText.setOnTouchListener(this);
        mAutocompleteView.setOnTouchListener(this);
        pricing_editText.setText("$");
        pricing_editText.setSelection(1);

        if (!sp.getString(GlobalConstants.EVENT_NAME, "").equalsIgnoreCase("")) {
            name_editText.setText(sp.getString(GlobalConstants.EVENT_NAME, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_TIME, "").equalsIgnoreCase("")) {
            meeting_time_editText.setText(sp.getString(GlobalConstants.EVENT_TIME, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_PLACE, "").equalsIgnoreCase("")) {
            place_editText.setText(sp.getString(GlobalConstants.EVENT_PLACE, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_PRICE, "").equalsIgnoreCase("")) {
            pricing_editText.setText("$"+sp.getString(GlobalConstants.EVENT_PRICE, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_METTING_POINT, "").equalsIgnoreCase("")) {
            mAutocompleteView.setText(sp.getString(GlobalConstants.EVENT_METTING_POINT, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_DESCRIPTION, "").equalsIgnoreCase("")) {
            desc_editText.setText(sp.getString(GlobalConstants.EVENT_DESCRIPTION, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_DISCLAIMER, "").equalsIgnoreCase("")) {
            disclaimer_editText.setText("$" + sp.getString(GlobalConstants.EVENT_DISCLAIMER, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_CAT_NAME, "").equalsIgnoreCase("")) {
            selectSpinnerValue(cat_editText, sp.getString(GlobalConstants.EVENT_CAT_NAME, ""));
        }
        if (!sp.getString(GlobalConstants.EVENT_LEVEL, "").equalsIgnoreCase("")) {

            seekBar.setProgress(Integer.parseInt(sp.getString(GlobalConstants.EVENT_LEVEL, ""))-1);
        }
        if (!sp.getString(GlobalConstants.WHATS_INCLUDED, "").equalsIgnoreCase("")) {

            whts_editText.setText(sp.getString(GlobalConstants.WHATS_INCLUDED, ""));
        }

        place_editText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();
                if(x.startsWith("0"))
                {
                    place_editText.setText("1");
                    place_editText.setSelection(x.length());

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });
        pricing_editText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();
                if(x.startsWith("0"))
                {
                    pricing_editText.setText("1");
                    pricing_editText.setSelection(x.length());

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });
        pricing_editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {

                        case KeyEvent.KEYCODE_ENTER:
                            if(!pricing_editText.getText().toString().contains("$")){
                                pricing_editText.setText("$"+pricing_editText.getText().toString());


                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    public void selectSpinnerValue(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.name_editText:
                name_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.meeting_time_editText:
                meeting_time_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.desc_editText:
                desc_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.place_editText:
                place_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.pricing_editText:
                pricing_error_txtView.setVisibility(View.GONE);

                break;
            /*case R.id.crtieria_editText:
                crtieria_error_txtView.setVisibility(View.GONE);
                break;*/
            case R.id.disclaimer_editText:
                disclaimer_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.whts_editText:
                whts_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.cat_editText:
                cat_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.meeting_editText:
                meeting_error_txtView.setVisibility(View.GONE);
                break;


        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meeting_time_editText:
                timePicker();
                break;
            case R.id.desc_editText:
                Intent i = new Intent(CreateDetailActivity.this, AdvantureDescriptionActivity.class);
                startActivityForResult(i, 1);
                break;
           /* case R.id.crtieria_editText:

                break;*/
            case R.id.disclaimer_editText:
                Intent k = new Intent(CreateDetailActivity.this, AdaventureDisclamierActivity.class);
                startActivityForResult(k, 3);
                break;
            case R.id.whts_editText:
                Intent j = new Intent(CreateDetailActivity.this, WhatsIncludedActivity.class);
                startActivityForResult(j, 2);
                break;
            case R.id.submit_button:
                if (name_editText.getText().toString().length() == 0) {
                    name_error_txtView.setText("Please enter name of advaenture");
                    name_error_txtView.setVisibility(View.VISIBLE);
                } else if (meeting_time_editText.getText().toString().equalsIgnoreCase("Please Select")||meeting_time_editText.getText().toString().length()==0) {
                    meeting_time_error_txtView.setText("Please enter time");
                    meeting_time_error_txtView.setVisibility(View.VISIBLE);
                } else if (desc_editText.getText().toString().equalsIgnoreCase("Write a description")||desc_editText.getText().toString().length()==0) {
                    desc_error_txtView.setText("Please enter decription");
                    desc_error_txtView.setVisibility(View.VISIBLE);
                } else if (place_editText.getText().toString().length() == 0) {
                    place_error_txtView.setText("Please enter place");
                    place_error_txtView.setVisibility(View.VISIBLE);
                } else if (pricing_editText.getText().toString().length() == 0) {
                    pricing_error_txtView.setText("Please enter price");
                    pricing_error_txtView.setVisibility(View.VISIBLE);
                } /*else if (crtieria_editText.getText().toString().length() == 0) {
                    crtieria_error_txtView.setText("Please enter Criteria/eligibility");
                    crtieria_error_txtView.setVisibility(View.VISIBLE);
                }*/ else if (disclaimer_editText.getText().toString().equalsIgnoreCase("Write something")||disclaimer_editText.getText().toString().length()==0) {
                    disclaimer_error_txtView.setText("Please enter disclaimer");
                    disclaimer_error_txtView.setVisibility(View.VISIBLE);
                } else if (whts_editText.getText().toString().equalsIgnoreCase("Please select")||whts_editText.getText().toString().length()==0) {
                    whts_error_txtView.setText("Please enter what's included");
                    whts_error_txtView.setVisibility(View.VISIBLE);
                } else if (catgory.length() == 0) {
                    cat_error_txtView.setText("Please enter what's catgory");
                    cat_error_txtView.setVisibility(View.VISIBLE);
                } else if (mAutocompleteView.getText().toString().length() == 0) {
                    meeting_error_txtView.setText("Please enter meeting place");
                    meeting_error_txtView.setVisibility(View.VISIBLE);
                }
                else {
                    global.setEvent_name(name_editText.getText().toString());
                    global.setEvent_time(meeting_time_editText.getText().toString());
                    global.setEvent_place(place_editText.getText().toString());
                    global.setEvent_price(pricing_editText.getText().toString().replace("$", "").trim());
                    //global.setEvent_criteria(crtieria_editText.getText().toString());

                    global.setEvent_meetin_point(mAutocompleteView.getText().toString());
                    global.setEvent_description("true");
                    ed.putString(GlobalConstants.EVENT_NAME, global.getEvent_name());
                    ed.putString(GlobalConstants.EVENT_TIME, global.getEvent_time());
                    ed.putString(GlobalConstants.EVENT_PLACE, global.getEvent_place());
                    ed.putString(GlobalConstants.EVENT_PRICE, global.getEvent_price());
                    ed.putString(GlobalConstants.EVENT_PRICE, global.getEvent_price());
                    ed.putString(GlobalConstants.EVENT_METTING_POINT, global.getEvent_meetin_point());
                    ed.putString(GlobalConstants.VALUE, "true");
                    ed.putString(GlobalConstants.EVENT_DESCRIPTION, desc_editText.getText().toString());
                    ed.putString(GlobalConstants.EVENT_DISCLAIMER, disclaimer_editText.getText().toString());
                    ed.putString(GlobalConstants.EVENT_CAT_NAME, catgory);

                    ed.putString(GlobalConstants.EVENT_CAT_ID,global.getEvent_cat_id());
                    ed.commit();
                    finish();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!sp.getString(GlobalConstants.EVENT_DESCRIPTION,"").equalsIgnoreCase("")) {
                desc_editText.setText(sp.getString(GlobalConstants.EVENT_DESCRIPTION,""));
            }else{
                desc_editText.setText("");
            }
        } else if (requestCode == 2) {
            if (!sp.getString(GlobalConstants.WHATS_INCLUDED,"").equalsIgnoreCase("")) {
                whts_editText.setText(sp.getString(GlobalConstants.WHATS_INCLUDED,""));
            }else{
                whts_editText.setText("");

            }
        } else if (requestCode == 3) {
            if (!sp.getString(GlobalConstants.EVENT_DISCLAIMER,"").equalsIgnoreCase("")) {
                disclaimer_editText.setText(sp.getString(GlobalConstants.EVENT_DISCLAIMER,""));
            }else{
                disclaimer_editText.setText("");

            }
        }
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
            global.setEvent_meeting_lat(lat);
            global.setEvent_meeting_lng(lng);
            ed.putString(GlobalConstants.EVENT_MEETING_LAT, lat);
            ed.putString(GlobalConstants.EVENT_MEETING_LNG, lng);
            ed.commit();

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

    public void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        meeting_time_editText.setText(getTime(hourOfDay, minute));


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }
}
