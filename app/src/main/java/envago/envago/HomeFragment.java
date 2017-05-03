package envago.envago;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.rampo.updatechecker.UpdateChecker;
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
public class HomeFragment extends Fragment {
    LinearLayout featured_planners_linear_layout, suggested_linear_layout, all_linear_layout, featured_linear_layout, main_layout;
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
    Dialog dialog2;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int measuredWidth = 0;
    int measuredHeight = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.homepage_activity, container, false);

        global = (Global) getActivity().getApplicationContext();
        preferences = getActivity().getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        UpdateChecker checker = new UpdateChecker(getActivity()); // If you are in a Activity or a FragmentActivity
        checker.setSuccessfulChecksRequired(5);
        checker.start();
        map_button = (ImageView) v.findViewById(R.id.map_button);

        WindowManager w = getActivity().getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
            measuredHeight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
            measuredHeight = d.getHeight();
        }
        global.setHeight(measuredHeight);
        global.setWidth(measuredWidth);
        planner_list_cat = (ListView) v.findViewById(R.id.main_list);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        shimmer_view_container = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_container);
        shimmer_view_container.setVisibility(View.GONE);
        main_layout = (LinearLayout) v.findViewById(R.id.main_layout);
        Fonts.overrideFonts1(getActivity(), main_layout);
        plus_button = (ImageView) v.findViewById(R.id.plus_button);
        featured_planners_linear_layout = (LinearLayout) v.findViewById(R.id.featured_planners_linear_layout);
        suggested_linear_layout = (LinearLayout) v.findViewById(R.id.suggested_linear_layout);
        featured_linear_layout = (LinearLayout) v.findViewById(R.id.featured_linear_layout);
        all_linear_layout = (LinearLayout) v.findViewById(R.id.all_linear_layout);
        view_item_pager1 = (ViewPager) v.findViewById(R.id.view_item_pager1);
        view_item_pager2 = (ViewPager) v.findViewById(R.id.view_item_pager2);
        cat_pager = (ViewPager) v.findViewById(R.id.cat_pager);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_api();

                Intent intent = new Intent(getActivity(), NewCreateAdvantureVideoForm.class);
                startActivity(intent);

            }
        });
        dialogWindow();
        // get_list();
        all_list();

        planner_list_cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent about = new Intent(getActivity(), AboutPlannerActivity.class);
                about.putExtra(GlobalConstants.USERID, featured_planner_list.get(position).get(GlobalConstants.ID));
                about.putExtra(GlobalConstants.EVENT_NAME, featured_planner_list.get(position).get(GlobalConstants.EVENT_CAT_NAME));
                startActivity(about);
            }
        });

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        return v;

    }


    //----------------------------------------Get list on map-------------------------------
    public void get_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


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
                            details.put(GlobalConstants.LATITUDE, arrobj.getString(GlobalConstants.LATITUDE));
                            details.put(GlobalConstants.EVENT_FAV, arrobj.getString(GlobalConstants.EVENT_FAV));
                            details.put(GlobalConstants.EVENT_IMAGES, arrobj.getString(GlobalConstants.EVENT_IMAGES));
                            JSONArray arr = arrobj.getJSONArray("event_dates");
                            JSONObject objArr = arr.getJSONObject(0);
                            details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                            details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                            event_list.add(details);

                        }

                        global.setEvent_list(event_list);


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
                dialog2.dismiss();
                Log.e("ratinf", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        // JSONObject data = obj.getJSONObject("data");
                        JSONObject data = obj.getJSONObject("data");
                        if (data.has("countries")) {
                            JSONArray events = data.getJSONArray("countries");

                            for (int i = 0; i < events.length(); i++) {
                                JSONObject arrobj = events.getJSONObject(i);

                                HashMap<String, String> details = new HashMap<>();

                                details.put(GlobalConstants.EVENT_ID, arrobj.getString(GlobalConstants.EVENT_ID));
                                details.put(GlobalConstants.country_id, arrobj.getString(GlobalConstants.country_id));
                                details.put(GlobalConstants.NAME, arrobj.getString(GlobalConstants.NAME));
                                details.put(GlobalConstants.IMAGE, arrobj.getString(GlobalConstants.IMAGE));
                                details.put(GlobalConstants.EVENT_CAT_COUNT, arrobj.getString(GlobalConstants.EVENT_CAT_COUNT));


                                suggested_event_list.add(details);

                            }
                        }

                        if (data.has("featured_events")) {
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
                                JSONArray arr = arrobj.getJSONArray("event_dates");
                                JSONObject objArr = arr.getJSONObject(0);
                                details.put(GlobalConstants.EVENT_START_DATE, objArr.getString(GlobalConstants.EVENT_START_DATE));
                                // details.put(GlobalConstants.Is_SUGGESTED, arrobj.getString(GlobalConstants.Is_SUGGESTED));
                                //  details.put(GlobalConstants.LONGITUDE, arrobj.getString(GlobalConstants.LONGITUDE));


                                featured_event_list.add(details);

                            }
                        }

                        if (data.has("featured_users")) {
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

                        if (data.has("categories")) {
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

                        if (catgory_list.size() != 0) {
                            all_linear_layout.setVisibility(View.VISIBLE);
                            shimmer_view_container.setVisibility(View.GONE);
                            cat_pager.setAdapter(new CatPagerAdapter(getActivity(), catgory_list));

                            if (measuredWidth >= 1440) {
                                cat_pager.setClipToPadding(false);
                                cat_pager.setPadding(0, 0, 70, 0);


                            } else {
                                cat_pager.setClipToPadding(false);
                                cat_pager.setPadding(0, 0, 40, 0);


                            }
                            catgoryPagerMethod();
                        }
                        if (suggested_event_list.size() != 0) {
                            suggested_linear_layout.setVisibility(View.VISIBLE);
                            view_item_pager2.setAdapter(new AccordingToCountryAdapter(getActivity(), suggested_event_list));
                            if (suggested_event_list.size() > 1) {
                                if (measuredWidth >= 1440) {
                                    view_item_pager2.setClipToPadding(false);
                                    view_item_pager2.setPadding(0, 0, 70, 0);

                                } else {
                                    view_item_pager2.setClipToPadding(false);
                                    view_item_pager2.setPadding(0, 0, 40, 0);

                                }

                                suggestedMethod();
                            }
                        }
                        if (featured_planner_list.size() != 0) {

                            featured_planners_linear_layout.setVisibility(View.VISIBLE);
                            planner_list_cat.setAdapter(new FeaturedPlannerAdapter(getActivity(), featured_planner_list));
                            CommonUtils.getListViewSize(planner_list_cat);
                        }
                        if (featured_event_list.size() != 0) {
                            featured_linear_layout.setVisibility(View.VISIBLE);
                            view_item_pager1.setAdapter(new AllAdapter(getActivity(), featured_event_list));
                            if (featured_event_list.size() > 1) {
                                if (measuredWidth >= 1440) {
                                    view_item_pager1.setClipToPadding(false);
                                    view_item_pager1.setPadding(0, 0, 70, 0);

                                } else {
                                    view_item_pager1.setClipToPadding(false);
                                    view_item_pager1.setPadding(0, 0, 40, 0);

                                }

                                featuredMethod();
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

                params.put(GlobalConstants.USERID, CommonUtils.UserID(getActivity()));


                params.put("action","get_featured_entities_replica");
                Log.e("all param", params.toString());

                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(cat_request);
    }

    public void catgoryPagerMethod() {
        cat_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == catgory_list.size() - 1) {
                    if (measuredWidth >= 1440) {
                        cat_pager.setClipToPadding(false);
                        cat_pager.setPadding(70, 0, 0, 0);

                    } else {
                        cat_pager.setClipToPadding(false);
                        cat_pager.setPadding(40, 0, 0, 0);

                    }

                } else {
                    if (measuredWidth >= 1440) {
                        cat_pager.setClipToPadding(false);
                        cat_pager.setPadding(0, 0, 70, 0);


                    } else {
                        cat_pager.setClipToPadding(false);
                        cat_pager.setPadding(0, 0, 40, 0);

                    }

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void featuredMethod() {
        view_item_pager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == featured_event_list.size() - 1) {
                    if (measuredWidth >= 1440) {

                        view_item_pager1.setClipToPadding(false);
                        view_item_pager1.setPadding(70, 0, 0, 0);

                    } else {
                        view_item_pager1.setClipToPadding(false);
                        view_item_pager1.setPadding(40, 0, 0, 0);

                    }

                } else {
                    if (measuredWidth >= 1440) {

                        view_item_pager1.setClipToPadding(false);
                        view_item_pager1.setPadding(0, 0, 70, 0);

                    } else {
                        view_item_pager1.setClipToPadding(false);
                        view_item_pager1.setPadding(0, 0, 40, 0);

                    }

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void suggestedMethod() {
        view_item_pager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == suggested_event_list.size() - 1) {
                    if (measuredWidth >= 1440) {
                        view_item_pager2.setClipToPadding(false);
                        view_item_pager2.setPadding(70, 0, 0, 0);

                    } else {
                        view_item_pager2.setClipToPadding(false);
                        view_item_pager2.setPadding(40, 0, 0, 0);

                    }

                } else {
                    if (measuredWidth >= 1440) {
                        view_item_pager2.setClipToPadding(false);
                        view_item_pager2.setPadding(0, 0, 70, 0);

                    } else {
                        view_item_pager2.setClipToPadding(false);
                        view_item_pager2.setPadding(0, 0, 40, 0);

                    }

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void dialogWindow() {
        dialog2 = new Dialog(getActivity());
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

    public void profile_api() {
        StringRequest request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    JSONObject respose_obj = new JSONObject(s);
                    String obj = respose_obj.getString("success");
                    if (obj.equalsIgnoreCase("1")) {
                        JSONObject json_data = respose_obj.getJSONObject("data");

                        editor.putString(GlobalConstants.EMAIL, json_data.getString(GlobalConstants.EMAIL));
                        editor.putString(GlobalConstants.CONTACT, json_data.getString(GlobalConstants.CONTACT));
                        editor.putString(GlobalConstants.FIRST_NAME, json_data.getString(GlobalConstants.FIRST_NAME));
                        editor.putString(GlobalConstants.LAST_NAME, json_data.getString(GlobalConstants.LAST_NAME));
                        editor.putString(GlobalConstants.USERNAME, json_data.getString(GlobalConstants.USERNAME));
                        editor.putString(GlobalConstants.ADDRESS, json_data.getString(GlobalConstants.ADDRESS));
                        editor.putString(GlobalConstants.PAYPAL, json_data.getString(GlobalConstants.PAYPAL));
                        editor.putString(GlobalConstants.ABOUT, json_data.getString(GlobalConstants.ABOUT));
                        editor.putString(GlobalConstants.DOCUMENT, GlobalConstants.IMAGE_URL + json_data.getString(GlobalConstants.DOCUMENT));
                        editor.putString(GlobalConstants.IMAGE, GlobalConstants.IMAGE_URL + json_data.getString(GlobalConstants.IMAGE));

                        String img_url = "";
                        if (!json_data.getString(GlobalConstants.IMAGE).equalsIgnoreCase("")) {
                            img_url = json_data.getString(GlobalConstants.IMAGE);
                        }
                        editor.commit();


                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), respose_obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("action", GlobalConstants.GETPROFILE_ACTION);


                return params;


            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

}
