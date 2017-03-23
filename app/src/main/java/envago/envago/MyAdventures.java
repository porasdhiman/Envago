package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhang on 11/4/2016.
 */

public class MyAdventures extends Activity implements View.OnTouchListener {

    TextView planning, goingto;
    LinearLayout planning_layout, goingtolayout;
    ListView planning_list;
    ArrayList<HashMap<String, String>> event_list = new ArrayList<>();
    Dialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.myadventures);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        planning_list = (ListView) findViewById(R.id.planning_list);


        planning = (TextView) findViewById(R.id.planning_text);
        goingto = (TextView) findViewById(R.id.goingto_text);

        planning_layout = (LinearLayout) findViewById(R.id.planning_layout);
        goingtolayout = (LinearLayout) findViewById(R.id.goingto_layout);

        dialogWindow();
        get_list();


        planning.setOnTouchListener(this);
        goingto.setOnTouchListener(this);
        planning_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MyAdventures.this,DetailsActivity.class);
                i.putExtra(GlobalConstants.EVENT_ID,event_list.get(position).get(GlobalConstants.EVENT_ID));
                i.putExtra("user","user");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int id = v.getId();

        if (id == R.id.planning_text) {
            planning.setBackgroundColor(getResources().getColor(R.color.White));
            planning_layout.setBackgroundColor(getResources().getColor(R.color.White));
            planning.setTextColor(getResources().getColor(R.color.textcolor));

            goingto.setBackgroundColor(getResources().getColor(R.color.transparent));
            goingtolayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            goingto.setTextColor(getResources().getColor(R.color.White));

            event_list.clear();
            dialogWindow();
            get_list();
        }

        if (id == R.id.goingto_text) {
            planning.setBackgroundColor(getResources().getColor(R.color.transparent));
            planning_layout.setBackgroundColor(getResources().getColor(R.color.transparent));
            planning.setTextColor(getResources().getColor(R.color.White));

            goingto.setBackgroundColor(getResources().getColor(R.color.White));
            goingtolayout.setBackgroundColor(getResources().getColor(R.color.White));
            goingto.setTextColor(getResources().getColor(R.color.textcolor));

            event_list.clear();
            dialogWindow();
            get_list_goingto();
        }

        return false;
    }


    //--------------------------------List-API-Planning----------------------------

    public void get_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        // JSONObject data = obj.getJSONObject("data");

                        JSONArray events = obj.getJSONArray("data");
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
                            details.put(GlobalConstants.EVENT_START_DATE, arrobj.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            event_list.add(details);

                        }
                        if (event_list.size() > 0) {
                            //planning_list.setAdapter(new Favorite_list_Adapter(getApplicationContext(), event_list));
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

                params.put(GlobalConstants.USERID, CommonUtils.UserID(getApplication()));

               /* if (getActivity().getIntent().getExtras().getString("status").equalsIgnoreCase("single"))
                {
                    params.put(GlobalConstants.MAIN_CAT_ID, main_id);
                    params.put(GlobalConstants.EVENT_CAT_ID, sub_id);
                }*/

                /*params.put(GlobalConstants.LATITUDE,global.getLat() );
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.RESPONSE_TYPE, "list");*/
                params.put("page", "1");
                params.put("perpage", "20");
                params.put("action", "get_my_events");

                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(cat_request);
    }


    //--------------------------------List-API-goingto----------------------------

    public void get_list_goingto() {
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
                            details.put(GlobalConstants.EVENT_START_DATE, arrobj.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            event_list.add(details);

                        }
                        if (event_list.size() > 0) {
                           // planning_list.setAdapter(new Favorite_list_Adapter(getApplicationContext(), event_list));
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

                params.put(GlobalConstants.USERID, CommonUtils.UserID(getApplication()));

               /* if (getActivity().getIntent().getExtras().getString("status").equalsIgnoreCase("single"))
                {
                    params.put(GlobalConstants.MAIN_CAT_ID, main_id);
                    params.put(GlobalConstants.EVENT_CAT_ID, sub_id);
                }*/

                /*params.put(GlobalConstants.LATITUDE,global.getLat() );
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.RESPONSE_TYPE, "list");*/
                params.put("page", "1");
                params.put("perpage", "20");
                params.put("action", "get_events_joined_by_me");

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(cat_request);
    }

    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow() {
        dialog2 = new Dialog(MyAdventures.this);
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
