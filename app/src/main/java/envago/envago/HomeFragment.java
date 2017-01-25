package envago.envago;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikas on 21-09-2016.
 */
public class HomeFragment extends Fragment {
    LinearLayout featured_planners_linear_layout, suggested_linear_layout, all_linear_layout, featured_linear_layout,main_layout;
    ListView planner_list_cat;
    public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer};
    ImageView map_button, plus_button;
    Global global;
    ArrayList<HashMap<String, String>> event_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> suggested_event_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> catgory_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> featured_planner_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> featured_event_list = new ArrayList<>();
    ViewPager view_item_pager1, view_item_pager2, cat_pager;
ShimmerFrameLayout shimmer_view_container;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.homepage_activity, container, false);

        global = (Global) getActivity().getApplicationContext();

        map_button = (ImageView) v.findViewById(R.id.map_button);

        planner_list_cat = (ListView) v.findViewById(R.id.main_list);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        shimmer_view_container=(ShimmerFrameLayout)v.findViewById(R.id.shimmer_view_container);
        shimmer_view_container.startShimmerAnimation();
        main_layout= (LinearLayout) v.findViewById(R.id.main_layout);
        Fonts.overrideFonts(getActivity(),main_layout);
        plus_button = (ImageView) v.findViewById(R.id.plus_button);
        featured_planners_linear_layout = (LinearLayout) v.findViewById(R.id.featured_planners_linear_layout);
        suggested_linear_layout = (LinearLayout) v.findViewById(R.id.suggested_linear_layout);
        featured_linear_layout = (LinearLayout) v.findViewById(R.id.featured_linear_layout);
        all_linear_layout=(LinearLayout) v.findViewById(R.id.all_linear_layout);
        view_item_pager1 = (ViewPager) v.findViewById(R.id.view_item_pager1);
        view_item_pager2 = (ViewPager) v.findViewById(R.id.view_item_pager2);
        cat_pager = (ViewPager) v.findViewById(R.id.cat_pager);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (global.getIsVerified().equalsIgnoreCase("Approved")) {
                    Intent intent = new Intent(getActivity(), NewCreateAdvantureVideoForm.class);
                    startActivity(intent);
                /*} else {
                    Intent intent = new Intent(getActivity(), CreateAdventuresActivity.class);
                    startActivity(intent);
                }*/
            }
        });

        get_list();
        all_list();

        planner_list_cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Intent intent = new Intent(getActivity(), Adventure_list.class);

                if (position == 0 || position == 1 || position == 2 || position == 3) {
                    intent.putExtra("status", "single");
                    intent.putExtra("main_id", "1");
                    intent.putExtra("sub_id", String.valueOf(position + 1));
                    intent.putExtra("cat", String.valueOf(position));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                } else if (position == 4) {

                    intent.putExtra("cat", String.valueOf(position));
                    Intent intent1 = new Intent(getActivity(), Volunteers_list.class);
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                } else {
                    intent.putExtra("status", "all");
                    intent.putExtra("cat", String.valueOf(position));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }*/
            }
        });


        return v;

    }

  /*  Bundle args = new Bundle();
    Adventure_list fragment=new Adventure_list();

    if (position == 0 || position == 1 || position == 2 || position == 3) {
        args.putString("status", "single");
        args.putString("main_id", "1");
        args.putString("sub_id", String.valueOf(position + 1));
        args.putString("cat", String.valueOf(position));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction anim_frag = fragmentManager.beginTransaction();


        anim_frag.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);


        anim_frag.replace(R.id.contentintab, fragment).addToBackStack(null).commit();

    } else if (position == 4) {

        args.putString("cat", String.valueOf(position));


    } else {
        args.putString("status", "all");
        args.putString("cat", String.valueOf(position));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction anim_frag = fragmentManager.beginTransaction();


        anim_frag.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);


        anim_frag.replace(R.id.contentintab, fragment).addToBackStack(null).commit();

    }
}
});*/


    //----------------------------------------Get list on map-------------------------------
    public void get_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("Categoryyyy", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        // JSONObject data = obj.getJSONObject("data");
                        JSONObject data = obj.getJSONObject("data");
                        JSONArray events = data.getJSONArray("events");
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


                            event_list.add(details);

                        }

                        global.setEvent_list(event_list);
                        map_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getActivity(), MapsActivity.class);
                                startActivity(intent);
                            }
                        });


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


                params.put(GlobalConstants.LATITUDE, global.getLat());
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.RESPONSE_TYPE, "map");

                params.put("action", GlobalConstants.GET_EVENT_FILTER);

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(cat_request);
    }


    //-----------------------------------------------All advanture----------------------
//----------------------------------------Get list on map-------------------------------
    public void all_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("Categoryyyy", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        // JSONObject data = obj.getJSONObject("data");
                        JSONObject data = obj.getJSONObject("data");
                        if(data.has("suggested_events")) {
                            JSONArray events = data.getJSONArray("suggested_events");

                            for (int i = 0; i < events.length(); i++) {
                                JSONObject arrobj = events.getJSONObject(i);

                                HashMap<String, String> details = new HashMap<>();

                                details.put(GlobalConstants.EVENT_ID, arrobj.getString(GlobalConstants.ID));
                                details.put(GlobalConstants.EVENT_NAME, arrobj.getString(GlobalConstants.EVENT_NAME));
                                details.put(GlobalConstants.EVENT_LOC, arrobj.getString(GlobalConstants.EVENT_LOC));
                                details.put(GlobalConstants.EVENT_PRICE, arrobj.getString(GlobalConstants.EVENT_PRICE));
                                //details.put(GlobalConstants.LATITUDE, arrobj.getString(GlobalConstants.LONGITUDE));
                                details.put(GlobalConstants.EVENT_FAV, arrobj.getString(GlobalConstants.EVENT_FAV));
                                details.put(GlobalConstants.IMAGE, arrobj.getString(GlobalConstants.IMAGE));
                                JSONArray arr=arrobj.getJSONArray("event_dates");
                                JSONObject objArr=arr.getJSONObject(0);
                                details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                                details.put(GlobalConstants.Is_SUGGESTED, arrobj.getString(GlobalConstants.Is_SUGGESTED));
                                //  details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                                suggested_event_list.add(details);

                            }
                        }
                        Log.e("suggested_event_list", suggested_event_list.toString());
                        if(data.has("featured_events")) {
                            JSONArray fet_events = data.getJSONArray("featured_events");
                            for (int i = 0; i < fet_events.length(); i++) {
                                JSONObject arrobj = fet_events.getJSONObject(i);

                                HashMap<String, String> details = new HashMap<>();

                                details.put(GlobalConstants.EVENT_ID, arrobj.getString(GlobalConstants.ID));
                                details.put(GlobalConstants.EVENT_NAME, arrobj.getString(GlobalConstants.EVENT_NAME));
                                details.put(GlobalConstants.EVENT_LOC, arrobj.getString(GlobalConstants.EVENT_LOC));
                                details.put(GlobalConstants.EVENT_PRICE, arrobj.getString(GlobalConstants.EVENT_PRICE));
                                //details.put(GlobalConstants.LATITUDE, arrobj.getString(GlobalConstants.LONGITUDE));
                                details.put(GlobalConstants.EVENT_FAV, arrobj.getString(GlobalConstants.EVENT_FAV));
                                details.put(GlobalConstants.IMAGE, arrobj.getString(GlobalConstants.IMAGE));
                                JSONArray arr=arrobj.getJSONArray("event_dates");
                                JSONObject objArr=arr.getJSONObject(0);
                                details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                               // details.put(GlobalConstants.Is_SUGGESTED, arrobj.getString(GlobalConstants.Is_SUGGESTED));
                                //  details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                                featured_event_list.add(details);

                            }
                        }
                        Log.e("featured event",featured_event_list.toString());
                        if(data.has("featured_users")) {
                            JSONArray featured_users = data.getJSONArray("featured_users");
                            for (int i = 0; i < featured_users.length(); i++) {
                                JSONObject arrobj = featured_users.getJSONObject(i);

                                HashMap<String, String> details = new HashMap<>();

                                details.put(GlobalConstants.ID, arrobj.getString(GlobalConstants.ID));
                                details.put(GlobalConstants.USERNAME, arrobj.getString(GlobalConstants.USERNAME));
                                details.put(GlobalConstants.ADDRESS, arrobj.getString(GlobalConstants.ADDRESS));
                                details.put(GlobalConstants.IMAGE, arrobj.getString(GlobalConstants.IMAGE));
                                details.put("rating", arrobj.getString("rating"));


                                featured_planner_list.add(details);

                            }
                        }
                        Log.e("featured_planner_list", featured_planner_list.toString());
                        if(data.has("categories")) {
                            JSONArray categories = data.getJSONArray("categories");
                            for (int i = 0; i < categories.length(); i++) {
                                JSONObject arrobj = categories.getJSONObject(i);

                                HashMap<String, String> details = new HashMap<>();

                                details.put(GlobalConstants.CAT_ID, arrobj.getString(GlobalConstants.CAT_ID));
                                details.put(GlobalConstants.EVENT_CAT_NAME, arrobj.getString(GlobalConstants.EVENT_CAT_NAME));
                                details.put(GlobalConstants.EVENT_CAT_COUNT, arrobj.getString(GlobalConstants.EVENT_CAT_COUNT));





                                catgory_list.add(details);

                            }
                        }
                        Log.e("catgory_list", catgory_list.toString());
                        if(catgory_list.size()!=0) {
                            all_linear_layout.setVisibility(View.VISIBLE);
                            shimmer_view_container.setVisibility(View.GONE);
                            cat_pager.setAdapter(new CatPagerAdapter(getActivity(), catgory_list));
                            /*cat_pager.setClipToPadding(false);
                            cat_pager.setPadding(0, 0, 40, 0);*/
                        }
                        if (suggested_event_list.size() != 0) {
                            suggested_linear_layout.setVisibility(View.VISIBLE);
                            view_item_pager2.setAdapter(new AllAdapter(getActivity(), suggested_event_list));
                            //view_item_pager2.setAdapter(asvantureAdapter);
                /*   view_item_pager2.setClipToPadding(false);
        view_item_pager2.setPadding(0,0,40,0);*/
                            /*view_item_pager2.setClipToPadding(false);
                            view_item_pager2.setPadding(0, 0, 40, 0);*/
                        }
                        if (featured_planner_list.size() != 0) {
                            featured_planners_linear_layout.setVisibility(View.VISIBLE);
                            planner_list_cat.setAdapter(new FeaturedPlannerAdapter(getActivity(), featured_planner_list));
                            CommonUtils.getListViewSize(planner_list_cat);
                        }
                        if(featured_event_list.size()!=0){
                            featured_linear_layout.setVisibility(View.VISIBLE);
                            view_item_pager1.setAdapter(new AllAdapter(getActivity(), suggested_event_list));
                            //view_item_pager2.setAdapter(asvantureAdapter);
                /*   view_item_pager2.setClipToPadding(false);
        view_item_pager2.setPadding(0,0,40,0);*/
                            /*view_item_pager1.setClipToPadding(false);
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


                params.put("action", "get_featured_entities");

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(cat_request);
    }
}
