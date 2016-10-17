package envago.envago;

import android.*;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jhang on 10/16/2016.
 */
public class AdventureForm extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    ViewPager pager_image;
    ImageView add_imageView;
    ArrayList<String> list_image = new ArrayList<>();
    ArrayList<Bitmap> list_bitmap = new ArrayList<>();
    Dialog camgllry;
    String selectedImagePath = "";
    LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    MultipleImageAdapter mAdapter;

    EditText advanture_editText, event_desc_txtView, places_txtview, pcicing_txtview, criteria_txtView, disclaimer_txtView;
    Spinner level_cat_spinner, event_cat_spinner;
    TextView start_date_textVeiw, end_date_textVeiw, meeting_time_txtView;
    private Calendar calendar;
    private int hour;
    private int minute;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    int i, j = 1;

    boolean tran = true, acc = true, tent = true, meal = true, gear = true;
    ImageView add_loc, minus1_img, minus2_img, minus3_img, minus4_img;
    //-----------------------location----------------------

    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter autoAdapter;

    private AutoCompleteTextView start_point_loc, loc1_txtView, loc2_txtView, loc3_txtView, loc4_txtView, end_point_txtView, meeting_point_txtView;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    RelativeLayout loc1, loc2, loc3, loc4;

    private int mYear, mMonth, mDay, mHour, mMinute;
    CheckBox chk_transport, chk_meal, chk_accomodation, chk_gear, chk_tent;
    String trans_mString, tent_mString, meal_mString, gear_mString, acc_mString, main_id, sub_cat_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }


        setContentView(R.layout.create_adventure_form);
        pager_image = (ViewPager) findViewById(R.id.viewpager_introduction);
        loc1 = (RelativeLayout) findViewById(R.id.loc1);
        loc2 = (RelativeLayout) findViewById(R.id.loc2);
        loc3 = (RelativeLayout) findViewById(R.id.loc3);
        loc4 = (RelativeLayout) findViewById(R.id.loc4);
        add_imageView = (ImageView) findViewById(R.id.add_image_adventure);
        add_imageView.setOnClickListener(this);
        add_loc = (ImageView) findViewById(R.id.add_loc);
        add_loc.setOnClickListener(this);
        minus1_img = (ImageView) findViewById(R.id.minus1_img);
        minus2_img = (ImageView) findViewById(R.id.minus2_img);
        minus3_img = (ImageView) findViewById(R.id.minus3_img);
        minus4_img = (ImageView) findViewById(R.id.minus4_img);
        minus1_img.setOnClickListener(this);
        minus2_img.setOnClickListener(this);
        minus3_img.setOnClickListener(this);
        minus4_img.setOnClickListener(this);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots_layout);
        advanture_editText = (EditText) findViewById(R.id.name_of_advanture_editText);
        places_txtview = (EditText) findViewById(R.id.places_txtview);
        pcicing_txtview = (EditText) findViewById(R.id.pricing_txtView);
        criteria_txtView = (EditText) findViewById(R.id.criteria_txtView);
        level_cat_spinner = (Spinner) findViewById(R.id.level_cat_spinner);
        event_cat_spinner = (Spinner) findViewById(R.id.event_cat_spinner);
        event_desc_txtView = (EditText) findViewById(R.id.event_desc_txtView);
        disclaimer_txtView = (EditText) findViewById(R.id.disclaimer_txtView);
        start_date_textVeiw = (TextView) findViewById(R.id.start_date_textVeiw);
        end_date_textVeiw = (TextView) findViewById(R.id.end_date_textView);
        meeting_time_txtView = (TextView) findViewById(R.id.meeting_time_txtView);
        meeting_time_txtView.setOnClickListener(this);
        chk_transport = (CheckBox) findViewById(R.id.chk_transport);
        chk_accomodation = (CheckBox) findViewById(R.id.chk_accomodation);
        chk_gear = (CheckBox) findViewById(R.id.chk_gear);
        chk_meal = (CheckBox) findViewById(R.id.chk_meal);
        chk_tent = (CheckBox) findViewById(R.id.chk_meal);
        chk_transport.setOnClickListener(this);
        chk_accomodation.setOnClickListener(this);
        chk_gear.setOnClickListener(this);
        chk_meal.setOnClickListener(this);
        chk_tent.setOnClickListener(this);

        calendar = Calendar.getInstance();


        hour = calendar.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = calendar.get(Calendar.MINUTE);

        //-------------------------------Call AutocompleteTxtView-----------------
        buildGoogleApiClient();
        autoSpinnerView();
        event_cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                main_id = "1";
                sub_cat_id = String.valueOf(position + 1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void removePagerIndicatoreMethod() {
        //  dotsCount = mAdapter.getCount();
        if (list_bitmap.size() > 0) {

            pager_indicator.removeAllViews();


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

    public void pagerAdapterMethod(ArrayList<Bitmap> list) {
        list = list_bitmap;
        mAdapter = new MultipleImageAdapter(AdventureForm.this, list);
        pager_image.setAdapter(mAdapter);
        pager_image.setCurrentItem(0);
        pager_image.setOnPageChangeListener(this);
        setUiPageViewController();
    }

    public void dailog() {
        camgllry = new Dialog(AdventureForm.this);
        camgllry.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camgllry.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        camgllry.setContentView(R.layout.camera_dialog);

        camgllry.show();

        onclick();

    }

    public void onclick() {
        LinearLayout camera, gallery;

        camera = (LinearLayout) camgllry.findViewById(R.id.camera_layout);
        gallery = (LinearLayout) camgllry.findViewById(R.id.gallery_layout);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 1);
                camgllry.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
                camgllry.dismiss();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {


                    onSelectFromGalleryResult(data);


                }
            }
        } else if (requestCode == 1) {
            onCaptureImageResult(data);

        }
    }

    //------------------------------------Image Upload----------------------
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        list_bitmap.add(bm);

        pagerAdapterMethod(list_bitmap);
        Uri uri = getImageUri(AdventureForm.this, bm);
        try {
            selectedImagePath = getFilePath(AdventureForm.this, uri);
            list_image.add(selectedImagePath);
            Log.e("image Array", "" + list_image.size());
            Log.e("bitmap Array", "" + list_bitmap.size());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private void onCaptureImageResult(Intent data) {
        // Uri uri=data.getData();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list_bitmap.add(thumbnail);

        pagerAdapterMethod(list_bitmap);
        Uri uri = getImageUri(AdventureForm.this, thumbnail);
        try {
            selectedImagePath = getFilePath(AdventureForm.this, uri);
            list_image.add(selectedImagePath);
            Log.e("image Array", "" + list_image.size());
            Log.e("bitmap Array", "" + list_bitmap.size());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //----------------------------------date----------------------------------
    public void datePicker() {

        fromDatePickerDialog = new DatePickerDialog(AdventureForm.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String getdate = dateFormatter.format(newDate.getTime());
                String first[] = getdate.split("-");

                String datetchange = first[0];
                String mntchange = first[1];
                String yearf = first[2];

                if (i == 0) {
                    if (dateMatchMethod(yearf + "-" + mntchange + "-" + datetchange)) {
                        Toast.makeText(AdventureForm.this, "Date is not valid", Toast.LENGTH_SHORT).show();
                    } else {
                        start_date_textVeiw.setText(yearf + "-" + mntchange + "-" + datetchange);
                    }

                } else {
                    if (dateMatchMethod(yearf + "-" + mntchange + "-" + datetchange)) {
                        Toast.makeText(AdventureForm.this, "Date is not valid", Toast.LENGTH_SHORT).show();
                    } else {
                        end_date_textVeiw.setText(yearf + "-" + mntchange + "-" + datetchange);
                    }

                }


            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDatePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_image_adventure:
                if (list_bitmap.size() == 2) {
                    Toast.makeText(AdventureForm.this, "Only upload two image", Toast.LENGTH_SHORT).show();
                } else {
                    dailog();
                    removePagerIndicatoreMethod();

                }
                break;
            case R.id.start_date_textVeiw:
                i = 0;
                datePicker();

                break;
            case R.id.end_date_textView:

                i = 1;
                datePicker();

                break;
            case R.id.add_loc:

                if (j < 5) {
                    if (j == 1) {
                        loc1.setVisibility(View.VISIBLE);
                        j++;

                    } else if (j == 2) {
                        loc2.setVisibility(View.VISIBLE);
                        j++;

                    } else if (j == 3) {
                        loc3.setVisibility(View.VISIBLE);
                        j++;

                    } else if (j == 4) {
                        loc4.setVisibility(View.VISIBLE);
                        add_loc.setVisibility(View.GONE);

                    }

                }


                break;
            case R.id.minus1_img:

                loc1.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),String.valueOf(j),Toast.LENGTH_SHORT).show();

                break;
            case R.id.minus2_img:

                loc2.setVisibility(View.GONE);
                j--;
                Toast.makeText(getApplicationContext(),String.valueOf(j),Toast.LENGTH_SHORT).show();

                break;
            case R.id.minus3_img:
                loc3.setVisibility(View.GONE);
                j--;
                Toast.makeText(getApplicationContext(),String.valueOf(j),Toast.LENGTH_SHORT).show();

                break;
            case R.id.minus4_img:
                loc4.setVisibility(View.GONE);
                j--;
                add_loc.setVisibility(View.VISIBLE);
                break;
            case R.id.meeting_time_txtView:
                timePicker();

                break;
            case R.id.chk_accomodation:
                if (acc) {
                    acc = false;
                    acc_mString = "1";
                } else {
                    acc = true;
                    acc_mString = "0";
                }

                break;
            case R.id.chk_gear:
                if (gear) {
                    gear = false;
                    gear_mString = "1";
                } else {
                    gear = true;
                    gear_mString = "0";
                }

                break;
            case R.id.chk_transport:
                if (tran) {
                    tran = false;
                    trans_mString = "1";
                } else {
                    tran = true;
                    trans_mString = "0";
                }

                break;
            case R.id.chk_tent:
                if (tent) {
                    tent = false;
                    tent_mString = "1";
                } else {
                    tent = true;
                    tent_mString = "0";
                }


                break;
            case R.id.chk_meal:

                if (meal) {
                    meal = false;
                    meal_mString = "1";
                } else {
                    meal = true;
                    meal_mString = "0";
                }
                break;
        }
    }

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

    //----------------------------------location name method--------------
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)

                .addApi(Places.GEO_DATA_API)

                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();
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

            final PlaceAutocompleteAdapter.PlaceAutocomplete item = autoAdapter.getItem(position);
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
            Toast.makeText(AdventureForm.this, lat + lng, Toast.LENGTH_SHORT).show();


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
        autoAdapter.setGoogleApiClient(mGoogleApiClient);

        Log.i("search", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        autoAdapter.setGoogleApiClient(null);
        Log.e("search", "Google Places API connection suspended.");
    }

    public void autoSpinnerView() {
        start_point_loc = (AutoCompleteTextView) findViewById(R.id.start_point_loc);

        start_point_loc.setOnItemClickListener(mAutocompleteClickListener);
        autoAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
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

        start_point_loc.setThreshold(1);

        start_point_loc.setAdapter(autoAdapter);

        loc1_txtView = (AutoCompleteTextView) findViewById(R.id.loc1_txtView);

        loc1_txtView.setOnItemClickListener(mAutocompleteClickListener);
        autoAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
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

        loc1_txtView.setThreshold(1);

        loc1_txtView.setAdapter(autoAdapter);

        loc2_txtView = (AutoCompleteTextView) findViewById(R.id.loc2_txtView);

        loc2_txtView.setOnItemClickListener(mAutocompleteClickListener);
        autoAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
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

        loc2_txtView.setThreshold(1);

        loc2_txtView.setAdapter(autoAdapter);
        loc3_txtView = (AutoCompleteTextView) findViewById(R.id.loc2_txtView);

        loc3_txtView.setOnItemClickListener(mAutocompleteClickListener);
        autoAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
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

        loc3_txtView.setThreshold(1);

        loc3_txtView.setAdapter(autoAdapter);
        loc4_txtView = (AutoCompleteTextView) findViewById(R.id.loc4_txtView);

        loc4_txtView.setOnItemClickListener(mAutocompleteClickListener);
        autoAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
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

        loc4_txtView.setThreshold(1);

        loc4_txtView.setAdapter(autoAdapter);

        end_point_txtView = (AutoCompleteTextView) findViewById(R.id.end_point_txtView);

        end_point_txtView.setOnItemClickListener(mAutocompleteClickListener);
        autoAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
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

        end_point_txtView.setThreshold(1);

        end_point_txtView.setAdapter(autoAdapter);

        meeting_point_txtView = (AutoCompleteTextView) findViewById(R.id.meeting_point_txtView);

        meeting_point_txtView.setOnItemClickListener(mAutocompleteClickListener);
        autoAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
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

        meeting_point_txtView.setThreshold(1);

        meeting_point_txtView.setAdapter(autoAdapter);
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

                        meeting_time_txtView.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
