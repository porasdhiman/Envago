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
import android.os.Handler;
import android.os.Message;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import org.json.JSONObject;

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
        GoogleApiClient.ConnectionCallbacks, View.OnTouchListener {
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
    int i, j = 1, k;

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
    String trans_mString, tent_mString, meal_mString, gear_mString, acc_mString, main_id = "", sub_cat_id, level_mString = "";

    Button submit_event_btn;
    HttpEntity resEntity;
    String message;
    Dialog dialog2;
    String meeting_point_lat, meeting_point_long, start_lat, start_lng, end_lat, end_lng, loc1_location = "", loc2_location = "", loc3_location = "", loc4_location = "";
    String loc1_lat = "", loc1_lng = "", loc2_lat = "", loc2_lng = "", loc3_lat = "", loc3_lng = "", loc4_lat = "", loc4_lng = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }


        setContentView(R.layout.create_adventure_form);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        pager_image = (ViewPager) findViewById(R.id.viewpager_introduction);
        submit_event_btn = (Button) findViewById(R.id.submit_event_btn);
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
        submit_event_btn.setOnClickListener(this);
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
        start_date_textVeiw.setOnClickListener(this);
        end_date_textVeiw.setOnClickListener(this);
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
        autoSpinnerView();


        event_cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    main_id = "1";
                    sub_cat_id = String.valueOf(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        level_cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    level_mString = level_cat_spinner.getSelectedItem().toString();

                }
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
                        minus1_img.setVisibility(View.GONE);

                    } else if (j == 3) {
                        loc3.setVisibility(View.VISIBLE);
                        j++;
                        minus2_img.setVisibility(View.GONE);

                    } else if (j == 4) {
                        loc4.setVisibility(View.VISIBLE);
                        minus3_img.setVisibility(View.GONE);
                        add_loc.setVisibility(View.GONE);

                    }

                }


                break;
            case R.id.minus1_img:

                loc1.setVisibility(View.GONE);


                break;
            case R.id.minus2_img:

                loc2.setVisibility(View.GONE);
                minus1_img.setVisibility(View.VISIBLE);
                j--;


                break;
            case R.id.minus3_img:
                loc3.setVisibility(View.GONE);
                minus2_img.setVisibility(View.VISIBLE);
                j--;


                break;
            case R.id.minus4_img:
                minus3_img.setVisibility(View.VISIBLE);
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
            case R.id.submit_event_btn:
                dialogWindow();
                Log.e("arreay value", list_image.toString());
                new Thread(null, address_request, "")
                        .start();
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


    //-------------------------------Autolocation Method------------------------
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


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {

                Log.e("", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);

            final CharSequence thirdPartyAttribution = places.getAttributions();


            String latlong = place.getLatLng().toString().split(":")[1];
            String completeLatLng = latlong.substring(1, latlong.length() - 1);
            // Toast.makeText(MapsActivity.this,completeLatLng,Toast.LENGTH_SHORT).show();
            String lat = completeLatLng.split(",")[0];
            lat = lat.substring(1, lat.length());
            String lng = completeLatLng.split(",")[1];
            Toast.makeText(AdventureForm.this, lat + lng, Toast.LENGTH_SHORT).show();
            if (k == 1) {
                start_lat = lat;
                start_lng = lng;
            } else if (k == 2) {
                loc1_lat = lat;
                loc1_lat = lng;
                loc1_location = loc1_txtView.getText().toString();
            } else if (k == 3) {
                loc2_lat = lat;
                loc2_lat = lng;
                loc2_location = loc2_txtView.getText().toString();
            } else if (k == 4) {
                loc3_lat = lat;
                loc3_lat = lng;
                loc3_location = loc3_txtView.getText().toString();
            } else if (k == 5) {
                loc4_lat = lat;
                loc4_lat = lng;
                loc4_location = loc4_txtView.getText().toString();
            } else if (k == 6) {
                end_lat = lat;
                end_lng = lng;
            } else if (k == 7) {
                meeting_point_lat = lat;
                meeting_point_long = lng;
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
        loc1_txtView = (AutoCompleteTextView) findViewById(R.id.loc1_txtView);
        loc2_txtView = (AutoCompleteTextView) findViewById(R.id.loc2_txtView);

        loc3_txtView = (AutoCompleteTextView) findViewById(R.id.loc3_txtView);
        loc4_txtView = (AutoCompleteTextView) findViewById(R.id.loc4_txtView);
        end_point_txtView = (AutoCompleteTextView) findViewById(R.id.end_point_txtView);
        meeting_point_txtView = (AutoCompleteTextView) findViewById(R.id.meeting_point_txtView);
        start_point_loc.setOnItemClickListener(mAutocompleteClickListener);
        loc1_txtView.setOnItemClickListener(mAutocompleteClickListener);
        loc2_txtView.setOnItemClickListener(mAutocompleteClickListener);
        loc3_txtView.setOnItemClickListener(mAutocompleteClickListener);
        loc4_txtView.setOnItemClickListener(mAutocompleteClickListener);
        end_point_txtView.setOnItemClickListener(mAutocompleteClickListener);
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

        start_point_loc.setThreshold(1);

        start_point_loc.setAdapter(autoAdapter);
        start_point_loc.setOnTouchListener(this);


        loc1_txtView.setThreshold(1);

        loc1_txtView.setAdapter(autoAdapter);
        loc1_txtView.setOnTouchListener(this);


        loc2_txtView.setThreshold(1);

        loc2_txtView.setAdapter(autoAdapter);
        loc2_txtView.setOnTouchListener(this);


        loc3_txtView.setThreshold(1);

        loc3_txtView.setAdapter(autoAdapter);
        loc3_txtView.setOnTouchListener(this);


        loc4_txtView.setThreshold(1);

        loc4_txtView.setAdapter(autoAdapter);
        loc4_txtView.setOnTouchListener(this);


        end_point_txtView.setThreshold(1);

        end_point_txtView.setAdapter(autoAdapter);
        end_point_txtView.setOnTouchListener(this);


        meeting_point_txtView.setThreshold(1);

        meeting_point_txtView.setAdapter(autoAdapter);
        meeting_point_txtView.setOnTouchListener(this);

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
                Toast.makeText(AdventureForm.this, message,
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AdventureForm.this, message,
                        Toast.LENGTH_SHORT).show();
            }

        }

    };

    // ------------------------------------------------------upload
    // method---------------
    private String doFileUpload() {
        String success = "false";

        String urlString = GlobalConstants.URL;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();

            File file1 = new File(list_image.get(0));
            FileBody bin1 = new FileBody(file1, "image/png");
            reqEntity.addPart("image1", bin1);

            File file2 = new File(list_image.get(1));
            FileBody bin2 = new FileBody(file2, "image/png");
            reqEntity.addPart("image2", bin2);

            Log.e("image params", list_image.get(0) + " " + list_image.get(0));
            reqEntity.addPart(GlobalConstants.USERID, new StringBody(CommonUtils.UserID(this)));


            reqEntity.addPart(GlobalConstants.MAIN_CAT_ID, new StringBody(main_id));
            Log.e("main  id", main_id);
            reqEntity.addPart(GlobalConstants.EVENT_CAT_ID, new StringBody(sub_cat_id));
            Log.e("sub_cat_id", sub_cat_id);
            reqEntity.addPart(GlobalConstants.EVENT_NAME, new StringBody(advanture_editText.getText().toString()));
            Log.e("name", advanture_editText.getText().toString());
            reqEntity.addPart(GlobalConstants.EVENT_START_DATE, new StringBody(start_date_textVeiw.getText().toString()));
            Log.e(GlobalConstants.EVENT_START_DATE, start_date_textVeiw.getText().toString());
            reqEntity.addPart(GlobalConstants.EVENT_END_DATE, new StringBody(end_date_textVeiw.getText().toString()));
            Log.e(GlobalConstants.EVENT_END_DATE, end_date_textVeiw.getText().toString());
            reqEntity.addPart(GlobalConstants.EVENT_TIME, new StringBody(meeting_time_txtView.getText().toString()));
            Log.e(GlobalConstants.EVENT_TIME, meeting_time_txtView.getText().toString());
            reqEntity.addPart(GlobalConstants.EVENT_LEVEL, new StringBody(level_mString));
            Log.e(GlobalConstants.EVENT_LEVEL, level_mString);
            reqEntity.addPart(GlobalConstants.EVENT_METTING_POINT, new StringBody(meeting_point_txtView.getText().toString()));
            Log.e(GlobalConstants.EVENT_METTING_POINT, meeting_point_txtView.getText().toString());

            reqEntity.addPart("meeting_point_latitude", new StringBody(meeting_point_lat));
            Log.e("meeting_point_latitude", meeting_point_lat);

            reqEntity.addPart("meeting_point_longitude", new StringBody(meeting_point_long));
            Log.e("meeting_point_longitude", meeting_point_long);

            reqEntity.addPart("crireria_eligibilty", new StringBody(criteria_txtView.getText().toString()));
            Log.e("crireria_eligibilty", criteria_txtView.getText().toString());

            reqEntity.addPart(GlobalConstants.LOCATION, new StringBody(start_point_loc.getText().toString()));
            Log.e(GlobalConstants.LOCATION, start_point_loc.getText().toString());
            reqEntity.addPart(GlobalConstants.LATITUDE, new StringBody(start_lat));
            Log.e(GlobalConstants.LATITUDE, start_lat);

            reqEntity.addPart(GlobalConstants.LONGITUDE, new StringBody(start_lng));
            Log.e(GlobalConstants.LONGITUDE, start_lng);
            reqEntity.addPart("end_location", new StringBody(end_point_txtView.getText().toString()));
            Log.e("end_location", end_point_txtView.getText().toString());
            reqEntity.addPart("end_latitude", new StringBody(end_lat));
            Log.e("end_latitude", end_lat);

            reqEntity.addPart("end_longitude", new StringBody(end_lng));
            Log.e("end_longitude", end_lng);

            reqEntity.addPart("location_1", new StringBody(loc1_location));
            Log.e("location_1", loc1_location);
            reqEntity.addPart("loc_1_latitude", new StringBody(loc1_lat));
            Log.e("loc_1_latitude", loc1_lat);

            reqEntity.addPart("loc_1_longitude", new StringBody(loc1_lng));
            Log.e("loc_1_longitude", loc1_lng);
            reqEntity.addPart("location_2", new StringBody(loc2_location));
            Log.e("location_2", loc2_location);
            reqEntity.addPart("loc_2_latitude", new StringBody(loc2_lat));
            Log.e("loc_2_latitude", loc2_lat);

            reqEntity.addPart("loc_2_longitude", new StringBody(loc2_lng));
            Log.e("loc_2_longitude", loc2_lng);
            reqEntity.addPart("location_3", new StringBody(loc3_location));
            Log.e("location_3", loc3_location);
            reqEntity.addPart("loc_3_latitude", new StringBody(loc3_lat));
            Log.e("loc_3_latitude", loc3_lat);
            reqEntity.addPart("loc_3_longitude", new StringBody(loc3_lng));
            Log.e("loc_3_longitude", loc3_lng);
            reqEntity.addPart("location_4", new StringBody(loc4_location));
            Log.e("location_4", loc4_location);
            reqEntity.addPart("loc_4_latitude", new StringBody(loc4_lat));
            Log.e("loc_4_latitude", loc4_lat);
            reqEntity.addPart("loc_4_longitude", new StringBody(loc4_lng));
            Log.e("loc_4_longitude", loc4_lng);
            reqEntity.addPart("description", new StringBody(event_desc_txtView.getText().toString()));
            Log.e("description", event_desc_txtView.getText().toString());
            reqEntity.addPart("no_of_places", new StringBody(places_txtview.getText().toString()));
            Log.e("no_of_places", places_txtview.getText().toString());
            reqEntity.addPart("price", new StringBody(pcicing_txtview.getText().toString()));
            Log.e("price", pcicing_txtview.getText().toString());
            reqEntity.addPart("transport", new StringBody(trans_mString));
            Log.e("transport", trans_mString);
            reqEntity.addPart("meals", new StringBody(meal_mString));
            Log.e("meals", meal_mString);

            reqEntity.addPart("tent", new StringBody(tent_mString));
            Log.e("tent", tent_mString);
            reqEntity.addPart("accomodation", new StringBody(acc_mString));
            Log.e("accomodation", acc_mString);
            reqEntity.addPart("gear", new StringBody(gear_mString));
            Log.e("gear", gear_mString);
            reqEntity.addPart("disclaimer", new StringBody(disclaimer_txtView.getText().toString()));
            Log.e("disclaimer", disclaimer_txtView.getText().toString());


            reqEntity.addPart("action", new StringBody(GlobalConstants.CREATE_EVENT_ACTION));
            Log.e("action", GlobalConstants.CREATE_EVENT_ACTION);

            post.setEntity(reqEntity);


            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();

            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                JSONObject obj = new JSONObject(response_str);
                String status = obj.getString("success");
                if (status.equalsIgnoreCase("1")) {
                    success = "true";
                    message = obj.getString("msg");
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.start_point_loc:
                k = 1;

                break;
            case R.id.end_point_txtView:
                k = 6;

                break;
            case R.id.meeting_point_txtView:
                k = 7;

                break;
            case R.id.loc1_txtView:
                k = 2;

                break;
            case R.id.loc2_txtView:
                k = 3;

                break;
            case R.id.loc3_txtView:
                k = 4;

                break;
            case R.id.loc4_txtView:
                k = 5;

                break;
        }
        return false;
    }
}
