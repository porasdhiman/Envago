package envago.envago;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
 * Created by vikas on 21-09-2016.
 */
public class FavoriteFragment extends Fragment {
    ListView ad_items;
    LinearLayout ging_to_linear_layout, planning_linear_layout, wish_linear_layout,main_layout,message_linear_layout;
    public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer, R.drawable.all};
    TextView headtext;
    ArrayList<HashMap<String, String>> event_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> goint_to_event_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> planning_event_list = new ArrayList<>();
    Dialog dialog2;
    ImageView back_img;
    ViewPager view_item_pager1, view_item_pager2, cat_pager;

    ShimmerFrameLayout shimmer_container;
    ScrollView scroll_view;
TextView start_btn;
SharedPreferences sp;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View v = inflater.inflate(R.layout.adventure_list, container, false);
        main_layout= (LinearLayout) v.findViewById(R.id.main_layout);
        Fonts.overrideFonts(getActivity(),main_layout);
        headtext = (TextView) v.findViewById(R.id.header_text_adv);

        headtext.setText("Wish");
        sp=getActivity().getSharedPreferences("message", Context.MODE_PRIVATE);
        message_linear_layout = (LinearLayout) v.findViewById(R.id.message_linear_layout);
        scroll_view=(ScrollView)v.findViewById(R.id.scroll_view);
       if(!sp.getString("message","not wish").equalsIgnoreCase("not wish")){
           message_linear_layout.setVisibility(View.GONE);
           scroll_view.setVisibility(View.VISIBLE);
       }
        start_btn=(TextView)v.findViewById(R.id.start_btn);
        ging_to_linear_layout = (LinearLayout) v.findViewById(R.id.goint_to_linear_layout);
        planning_linear_layout = (LinearLayout) v.findViewById(R.id.planning_linear_layout);
        wish_linear_layout = (LinearLayout) v.findViewById(R.id.wish_linear_layout);
        view_item_pager1 = (ViewPager) v.findViewById(R.id.view_item_pager1);
        view_item_pager2 = (ViewPager) v.findViewById(R.id.view_item_pager2);
        cat_pager = (ViewPager) v.findViewById(R.id.cat_pager);
        shimmer_container = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_container);
        shimmer_container.startShimmerAnimation();

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tab=new Intent(getActivity(),Tab_Activity.class);
                startActivity(tab);
                getActivity().finish();

            }
        });
        // back_img=(ImageView)v.findViewById(R.id.back_img);
        // ad_items = (ListView)v.findViewById(R.id.ad_list);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        // ad_items.setAdapter(new FavoriteAdvantureAdapter(getActivity()));


        get_list();
        //get_list_goingto();
        //get_planning_list();
        // back_img.setImageResource(R.drawable.favourites_back);

      /*  ad_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getActivity(),DetailsActivity.class);
                i.putExtra(GlobalConstants.EVENT_ID,event_list.get(position).get(GlobalConstants.EVENT_ID));
                i.putExtra("user","non user");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });*/

        return v;
    }

    //--------------------------------List-API----------------------------

    public void get_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("wish list", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                         JSONObject data = obj.getJSONObject("data");

                        JSONArray liked_events = data.getJSONArray("liked_events");
                        for (int i = 0; i < liked_events.length(); i++) {
                            JSONObject arrobj = liked_events.getJSONObject(i);

                            HashMap<String, String> details = new HashMap<>();

                            details.put(GlobalConstants.EVENT_ID, arrobj.getString(GlobalConstants.ID));
                            details.put(GlobalConstants.EVENT_NAME, arrobj.getString(GlobalConstants.EVENT_NAME));
                            details.put(GlobalConstants.EVENT_LOC, arrobj.getString(GlobalConstants.EVENT_LOC));
                            details.put(GlobalConstants.EVENT_PRICE, arrobj.getString(GlobalConstants.EVENT_PRICE));
                            details.put(GlobalConstants.LATITUDE, arrobj.getString(GlobalConstants.LATITUDE));
                            details.put(GlobalConstants.EVENT_FAV, arrobj.getString(GlobalConstants.EVENT_FAV));
                            details.put(GlobalConstants.EVENT_IMAGES, arrobj.getString(GlobalConstants.EVENT_IMAGES));
                            JSONArray arr=arrobj.getJSONArray("event_dates");
                            JSONObject objArr=arr.getJSONObject(0);
                            details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            event_list.add(details);

                        }
                        if (event_list.size() > 0) {
                            wish_linear_layout.setVisibility(View.VISIBLE);
                            shimmer_container.setVisibility(View.GONE);
                            view_item_pager2.setAdapter(new Favorite_list_Adapter(getActivity(), event_list));


                            /*view_item_pager2.setClipToPadding(false);
                            view_item_pager2.setPadding(0, 0, 40, 0);*/

                        }



                        JSONArray my_events = data.getJSONArray("my_events");
                        for (int i = 0; i < my_events.length(); i++) {
                            JSONObject arrobj = my_events.getJSONObject(i);

                            HashMap<String, String> details = new HashMap<>();

                            details.put(GlobalConstants.EVENT_ID, arrobj.getString(GlobalConstants.ID));
                            details.put(GlobalConstants.EVENT_NAME, arrobj.getString(GlobalConstants.EVENT_NAME));
                            details.put(GlobalConstants.EVENT_LOC, arrobj.getString(GlobalConstants.EVENT_LOC));
                            details.put(GlobalConstants.EVENT_PRICE, arrobj.getString(GlobalConstants.EVENT_PRICE));
                            details.put(GlobalConstants.LATITUDE, arrobj.getString(GlobalConstants.LONGITUDE));
                            details.put(GlobalConstants.EVENT_FAV, arrobj.getString(GlobalConstants.EVENT_FAV));
                            details.put(GlobalConstants.EVENT_IMAGES, arrobj.getString(GlobalConstants.EVENT_IMAGES));
                            JSONArray arr=arrobj.getJSONArray("event_dates");
                            JSONObject objArr=arr.getJSONObject(0);
                            details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            planning_event_list.add(details);

                        }
                        if (planning_event_list.size() > 0) {
                            // view_item_pager1.setAdapter(new AdvantureFeatureAdapter(getActivity(),event_list));
                            planning_linear_layout.setVisibility(View.VISIBLE);
                            shimmer_container.setVisibility(View.GONE);
                            cat_pager.setAdapter(new AdvantureFeatureAdapter(getActivity(), planning_event_list));
                            /*cat_pager.setClipToPadding(false);
                            cat_pager.setPadding(0, 0, 40, 0);
*/

                            //view_item_pager1.setClipToPadding(false);
                            // view_item_pager1.setPadding(0,0,40,0);

                        }
                        JSONArray joined_events = data.getJSONArray("joined_events");
                        for (int i = 0; i < joined_events.length(); i++) {
                            JSONObject arrobj = joined_events.getJSONObject(i);

                            HashMap<String, String> details = new HashMap<>();

                            details.put(GlobalConstants.EVENT_ID, arrobj.getString(GlobalConstants.ID));
                            details.put(GlobalConstants.EVENT_NAME, arrobj.getString(GlobalConstants.EVENT_NAME));
                            details.put(GlobalConstants.EVENT_LOC, arrobj.getString(GlobalConstants.EVENT_LOC));
                            details.put(GlobalConstants.EVENT_PRICE, arrobj.getString(GlobalConstants.EVENT_PRICE));
                            details.put(GlobalConstants.LATITUDE, arrobj.getString(GlobalConstants.LONGITUDE));
                            details.put(GlobalConstants.EVENT_FAV, arrobj.getString(GlobalConstants.EVENT_FAV));
                            details.put(GlobalConstants.EVENT_IMAGES, arrobj.getString(GlobalConstants.EVENT_IMAGES));
                            JSONArray arr=arrobj.getJSONArray("event_dates");
                            JSONObject objArr=arr.getJSONObject(0);
                            details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            goint_to_event_list.add(details);

                        }
                        if (goint_to_event_list.size() > 0) {
                            ging_to_linear_layout.setVisibility(View.VISIBLE);
                            shimmer_container.setVisibility(View.GONE);
                            view_item_pager1.setAdapter(new AdvantureFeatureAdapter(getActivity(), goint_to_event_list));


                          /*  view_item_pager1.setClipToPadding(false);
                            view_item_pager1.setPadding(0, 0, 40, 0);*/
                        }


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

                Map<String, String> params = new HashMap<>();

                params.put(GlobalConstants.USERID, CommonUtils.UserID(getActivity()));


                params.put("action", "get_entities");

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(cat_request);
    }

    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow() {
        dialog2 = new Dialog(getActivity());
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

    //--------------------------------List-API-Planning----------------------------

    public void get_planning_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("planning list", s);
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
                            JSONArray arr=arrobj.getJSONArray("event_dates");
                            JSONObject objArr=arr.getJSONObject(0);
                            details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            planning_event_list.add(details);

                        }
                        if (planning_event_list.size() > 0) {
                            // view_item_pager1.setAdapter(new AdvantureFeatureAdapter(getActivity(),event_list));
                            planning_linear_layout.setVisibility(View.VISIBLE);
                            shimmer_container.setVisibility(View.GONE);
                            cat_pager.setAdapter(new AdvantureFeatureAdapter(getActivity(), planning_event_list));
                            /*cat_pager.setClipToPadding(false);
                            cat_pager.setPadding(0, 0, 40, 0);
*/

                            //view_item_pager1.setClipToPadding(false);
                            // view_item_pager1.setPadding(0,0,40,0);

                        }

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

                Map<String, String> params = new HashMap<>();

                params.put(GlobalConstants.USERID, CommonUtils.UserID(getActivity()));

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

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(cat_request);
    }


    //--------------------------------List-API-goingto----------------------------

    public void get_list_goingto() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("list going to", s);
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
                            JSONArray arr=arrobj.getJSONArray("event_dates");
                            JSONObject objArr=arr.getJSONObject(0);
                            details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            goint_to_event_list.add(details);

                        }
                        if (goint_to_event_list.size() > 0) {
                            ging_to_linear_layout.setVisibility(View.VISIBLE);
                            shimmer_container.setVisibility(View.GONE);
                            view_item_pager1.setAdapter(new AdvantureFeatureAdapter(getActivity(), event_list));


                          /*  view_item_pager1.setClipToPadding(false);
                            view_item_pager1.setPadding(0, 0, 40, 0);*/
                        }

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

                Map<String, String> params = new HashMap<>();

                params.put(GlobalConstants.USERID, CommonUtils.UserID(getActivity()));

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

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(cat_request);
    }

}
