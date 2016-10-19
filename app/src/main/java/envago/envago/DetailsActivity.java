package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by vikas on 15-10-2016.
 */
public class DetailsActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    protected View view;
    private ImageButton btnNext, btnFinish;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    Dialog dialog2;
    TextView status_text,lower_description_txtView, event_name,places_txtView, level_no1, level_no2, level_no3, level_no4, date_details, meeting_desc, time_txtVIew, location_name_txtView;
    ImageView heart_img,accomodation_txtView,transport_txtView,meal_txtView,gear_txtView,tent_txtView;
    ArrayList<String> list = new ArrayList<>();
    Button purchase_btn;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);


        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        status_text = (TextView) findViewById(R.id.status_text);
        event_name = (TextView) findViewById(R.id.event_name);
        date_details = (TextView) findViewById(R.id.date_details);
        meeting_desc = (TextView) findViewById(R.id.meeting_desc);
        time_txtVIew = (TextView) findViewById(R.id.time_txtView);
        level_no1 = (TextView) findViewById(R.id.level1);
        level_no2 = (TextView) findViewById(R.id.level2);
        level_no3 = (TextView) findViewById(R.id.level3);
        level_no4 = (TextView) findViewById(R.id.level4);
        location_name_txtView = (TextView) findViewById(R.id.location_name);
        heart_img = (ImageView) findViewById(R.id.heart_img);
        heart_img.setOnClickListener(this);

        lower_description_txtView=(TextView)findViewById(R.id.lower_description);
        accomodation_txtView=(ImageView)findViewById(R.id.accomodation);
        transport_txtView=(ImageView)findViewById(R.id.transport);
        meal_txtView=(ImageView)findViewById(R.id.meals);
        gear_txtView=(ImageView)findViewById(R.id.gear);
        tent_txtView=(ImageView)findViewById(R.id.tent);
        places_txtView=(TextView)findViewById(R.id.places_count_txtView);
        purchase_btn=(Button)findViewById(R.id.purchase_btn);
        purchase_btn.setOnClickListener(this);
        dialogWindow();
        singleEventMethod();

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
                        dialog2.dismiss();
                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray data = obj.getJSONArray("data");
                                for (int j = 0; j < data.length(); j++) {
                                    JSONObject objArry = data.getJSONObject(j);
                                    JSONArray images = objArry.getJSONArray(GlobalConstants.EVENT_IMAGES);
                                    for (int i = 0; i < images.length(); i++) {
                                        JSONObject imagObj = images.getJSONObject(i);
                                        list.add("http://envagoapp.com/uploads/" + imagObj.getString(GlobalConstants.IMAGE));
                                    }
                                    event_name.setText(objArry.getString(GlobalConstants.EVENT_NAME));
                                    if (dateMatchMethod(objArry.getString(GlobalConstants.EVENT_START_DATE))) {
                                        status_text.setVisibility(View.VISIBLE);
                                    } else {
                                        status_text.setVisibility(View.GONE);
                                    }
                                    date_details.setText(objArry.getString(GlobalConstants.EVENT_START_DATE));
                                    location_name_txtView.setText(objArry.getString(GlobalConstants.LOCATION));
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
                                    i=Integer.parseInt(objArry.getString("is_liked"));
                                    if(i==1){
                                        heart_img.setImageResource(R.drawable.heart_field);
                                    }else{
                                        heart_img.setImageResource(R.drawable.heart);
                                    }
                                    lower_description_txtView.setText(objArry.getString("description"));

                                    if(objArry.getString("transport").equalsIgnoreCase("0")){
                                        transport_txtView.setImageResource(R.drawable.tansport_gray);
                                    }else{
                                        transport_txtView.setImageResource(R.drawable.transportation);
                                    }
                                    if(objArry.getString("meals").equalsIgnoreCase("0")){
                                        meal_txtView.setImageResource(R.drawable.food_gray);
                                    }else{
                                        meal_txtView.setImageResource(R.drawable.meal);
                                    }
                                    if(objArry.getString("accomodation").equalsIgnoreCase("0")){
                                        accomodation_txtView.setImageResource(R.drawable.accomodation_gray);
                                    }else{
                                        accomodation_txtView.setImageResource(R.drawable.accomodation);
                                    }
                                    if(objArry.getString("gear").equalsIgnoreCase("0")){
                                        gear_txtView.setImageResource(R.drawable.gear_gray);
                                    }else{
                                        gear_txtView.setImageResource(R.drawable.gear);
                                    }
                                    if(objArry.getString("tent").equalsIgnoreCase("0")){
                                        tent_txtView.setImageResource(R.drawable.tent_gray);
                                    }else{
                                        tent_txtView.setImageResource(R.drawable.tent);
                                    }
                                    places_txtView.setText(objArry.getString("total_no_of_places")+"Places");
                                    purchase_btn.setText("$"+objArry.getString(GlobalConstants.EVENT_PRICE));
                                }
                                pagerAdapterMethod(list);

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

}
