package envago.envago;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikas on 21-09-2016.
 */
public class AddFragment extends Fragment {

    RelativeLayout call_all, createAdventures, logout_layout;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    ProgressDialog pd;
    Dialog dialog2;
    Global global;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.add_layout, container, false);
        global = (Global) getActivity().getApplicationContext();
        sp = getActivity().getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        ed = sp.edit();
        call_all = (RelativeLayout) v.findViewById(R.id.call_all_layout);
        createAdventures = (RelativeLayout) v.findViewById(R.id.advanture_layout);
        logout_layout = (RelativeLayout) v.findViewById(R.id.logout_layout);
        createAdventures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(global.getIsVerified().equalsIgnoreCase("Approved")) {
                Intent intent = new Intent(getActivity(), AdventureForm.class);
                startActivity(intent);
                /*}else{
                    Intent intent = new Intent(getActivity(), CreateAdventuresActivity.class);
                    startActivity(intent);
                }*/

            }
        });

        call_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (global.getIsVerified().equalsIgnoreCase("Approved")) {
                    Intent intent = new Intent(getActivity(), CallVolunteerForm.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CallVolunteers.class);
                    startActivity(intent);
                }
            }
        });
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogWindow();
                logoutMethod();

            }
        });

        return v;
    }

    //-------------------------------Logout Api-----------------------------
    private void logoutMethod() {


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


                                ed.clear();
                                ed.commit();
                                Intent intent = new Intent(getActivity(), CallVolunteers.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                            } else {
                                Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(getActivity()));

                params.put("action", GlobalConstants.SINGOUT_ACTION);
                Log.e("Parameter for logout", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

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
}
