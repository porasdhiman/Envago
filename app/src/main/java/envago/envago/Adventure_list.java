package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhang on 9/25/2016.
 */
public class Adventure_list extends Activity {

    ListView ad_items;
    //  public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer, R.drawable.all};
    TextView headtext;
    ArrayList<HashMap<String, String>> event_list = new ArrayList<>();
    String main_id, cat_id;
    Global global;
    Dialog dialog2;

    ImageView back_img;
    ShimmerFrameLayout  shimmer_view_container;
ImageView search_button,list_back_img;
    boolean isLoading=false;
    int j = 1, page_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.new_advanture_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        global = (Global) getApplicationContext();
        search_button=(ImageView)findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headtext = (TextView) findViewById(R.id.header_text_adv);
       /* shimmer_view_container=(ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmer_view_container.startShimmerAnimation();*/
        list_back_img=(ImageView)findViewById(R.id.list_back_img);
        ad_items = (ListView) findViewById(R.id.ad_list);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
       // Log.e("Status", getIntent().getExtras().getString("status"));

        String category_name = getIntent().getExtras().getString(GlobalConstants.EVENT_CAT_NAME);


            headtext.setText(category_name);

        cat_id=getIntent().getExtras().getString(GlobalConstants.CAT_ID);

if(headtext.getText().toString().equalsIgnoreCase("Air"))
{
    list_back_img.setImageResource(R.drawable.air_back);
}else if(headtext.getText().toString().equalsIgnoreCase("Earth"))
{
    list_back_img.setImageResource(R.drawable.earth_back);
}else if(headtext.getText().toString().equalsIgnoreCase("Water"))
{
    list_back_img.setImageResource(R.drawable.watering_back);
}
else if(headtext.getText().toString().equalsIgnoreCase("Rock & Ice"))
{
    list_back_img.setImageResource(R.drawable.rock_back);
}else{
    list_back_img.setImageResource(R.drawable.favourites_back);
}
        dialogWindow();
        get_list();
        ad_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(Adventure_list.this,DetailsActivity.class);
                i.putExtra(GlobalConstants.EVENT_ID,event_list.get(position).get(GlobalConstants.EVENT_ID));
                i.putExtra("user","non user");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        ad_items.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                        get_list();
                    }


                }

            }

        });

    }
    //--------------------------------List-API----------------------------

    public void get_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
dialog2.dismiss();
                Log.e("Categoryyyy", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        // JSONObject data = obj.getJSONObject("data");
                        JSONArray events = obj.getJSONArray("data");
                        if (events.length() == 0) {

                        } else {
                            j = j + 1;

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
                            Log.e("event list", event_list.toString());
                            if(page_value>=j){
                                isLoading = false;
                                if (event_list.size() > 0) {
                                    ad_items.setVisibility(View.VISIBLE);

                                    ad_items.setAdapter(new Adventure_list_adapter(getApplicationContext(), event_list));

                                    list_back_img.setVisibility(View.GONE);
                                }

                            }else{

                                if (event_list.size() > 0) {
                                    ad_items.setVisibility(View.VISIBLE);

                                    ad_items.setAdapter(new Adventure_list_adapter(getApplicationContext(), event_list));

                                    list_back_img.setVisibility(View.GONE);
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

                params.put(GlobalConstants.USERID, CommonUtils.UserID(Adventure_list.this));


                    params.put(GlobalConstants.MAIN_CAT_ID, "1");
                    params.put(GlobalConstants.EVENT_CAT_ID, cat_id);


                params.put(GlobalConstants.LATITUDE, global.getLat());
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.RESPONSE_TYPE, "list");
                params.put("page", String.valueOf(j));
                params.put("perpage", "20");
                params.put("action", GlobalConstants.GET_EVENT_FILTER);

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(Adventure_list.this);
        requestQueue.add(cat_request);
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
        //loaderView.show();

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        dialog2.show();
    }
}
