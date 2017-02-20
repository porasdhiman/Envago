package envago.envago;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class Tab_Activity extends TabActivity {

    TabHost tabHost;
    SharedPreferences sharedPreferences;
    Editor mEditor;
    ProgressDialog progressDialog;
    Global global;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.tab_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        VerifiedMethod();
        tabHost = getTabHost();

        setTabs();
        tabHost.setCurrentTab(0);
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            // tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.action_back);


            ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);
            //vg.setBackgroundColor(Color.TRANSPARENT);

            //TextView tv1 = (TextView) vg.getChildAt(1);
            @SuppressWarnings("unused")
            ImageView img = (ImageView) vg.getChildAt(0);

			/*if (i == 0) {
                tv1.setTextColor(Color.parseColor("#0076be"));

			} else {
				tv1.setTextColor(Color.parseColor("#999999"));
			}*/
        }

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                int index = tabHost.getCurrentTab();

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);

                    vg.setBackgroundColor(Color.TRANSPARENT);

                    //TextView tv1 = (TextView) vg.getChildAt(1);
                    @SuppressWarnings("unused")
                    ImageView img = (ImageView) vg.getChildAt(0);
                    /*if (i == index) {

						tv1.setTextColor(Color.parseColor("#0076be"));

					}

					else {

						tv1.setTextColor(Color.parseColor("#999999"));

					}*/

                }

            }

        });

    }
    public void callHome(){
        tabHost.setCurrentTab(0);
    }

    private void setTabs() {


        addTab(R.drawable.tab_home, HomeActivity.class);
        addTab(R.drawable.tab_favorite, FavoriteActivity.class);
        addTab(R.drawable.tab_chat, MyChatActivity.class);

        addTab(R.drawable.tab_search, SearchByLocationActivity.class);


        addTab(R.drawable.tab_user, UserActivity.class);

    }

    private void addTab(int drawableId, Class<?> c) {

        Intent intent = new Intent(this, c);

        TabHost.TabSpec spec = tabHost.newTabSpec("tab");

        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, getTabWidget(), false);

		/*TextView title = (TextView) v.findViewById(R.id.title);

		title.setText(labelId);
*/
        ImageView icon = (ImageView) v.findViewById(R.id.icon);

        icon.setImageResource(drawableId);

        spec.setIndicator(v);

        spec.setContent(intent);

        tabHost.addTab(spec);

    }

    private void VerifiedMethod() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                                if (obj.getString("doc_status").equalsIgnoreCase("0")) {
                                    global.setIsVerified("0");
                                } else if (obj.getString("doc_status").equalsIgnoreCase("1")) {
                                    global.setIsVerified("1");
                                } else if (obj.getString("doc_status").equalsIgnoreCase("2")) {
                                    global.setIsVerified("2");
                                } else {
                                    global.setIsVerified("3");
                                }


                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Tab_Activity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(Tab_Activity.this));

                params.put("action", GlobalConstants.VERIFIED_ACTION);
                Log.e("facebook login", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}