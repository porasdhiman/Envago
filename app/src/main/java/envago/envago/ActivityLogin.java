package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikas on 27-12-2016.
 */

public class ActivityLogin extends Activity implements View.OnTouchListener,View.OnClickListener {


    TextView show_txt;
    EditText password_editView,email_editText;
    LinearLayout sign_in_layout;
    TextView login_button;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    Dialog dialog2;
    Global global;
    RelativeLayout main_layout;
    TextView mail_error_txtView,password_error_txtView,forgot_txtView;
ImageView back_from_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        global=(Global)getApplicationContext();
        sp = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        ed = sp.edit();
        show_txt=(TextView)findViewById(R.id.show_txt);
        password_editView=(EditText)findViewById(R.id.password);
        email_editText=(EditText)findViewById(R.id.mail_editText);
        main_layout=(RelativeLayout)findViewById(R.id.main_layout);
        sign_in_layout=(LinearLayout)findViewById(R.id.sign_in_layout);
        login_button=(TextView)findViewById(R.id.login_button);
        mail_error_txtView=(TextView)findViewById(R.id.mail_error_txtView) ;
        password_error_txtView=(TextView)findViewById(R.id.password_error_txtView);
        back_from_login=(ImageView)findViewById(R.id.back_from_login);
        forgot_txtView=(TextView)findViewById(R.id.forgot_txtView);
        forgot_txtView.setOnClickListener(this);
        login_button.setOnClickListener(this);
        sign_in_layout.setOnClickListener(this);
        show_txt.setOnClickListener(this);
        email_editText.setOnTouchListener(this);
        password_editView.setOnClickListener(this);
        back_from_login.setOnClickListener(this);

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();


        if (id == R.id.mail_editText) {
            mail_error_txtView.setVisibility(View.GONE);

        } else if (id == R.id.password) {
            password_error_txtView.setVisibility(View.GONE);


        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:

                if (email_editText.length() == 0) {
                    mail_error_txtView.setVisibility(View.VISIBLE);
                    mail_error_txtView.setText("Please enter Email");

                } else if (password_editView.length() == 0) {
                    password_error_txtView.setVisibility(View.VISIBLE);
                    password_error_txtView.setText("Please enter Password");

                } else if (!CommonUtils.isEmailValid(email_editText.getText().toString())) {
                    mail_error_txtView.setVisibility(View.VISIBLE);
                    mail_error_txtView.setText("Please enter Valid Email");
                } else {
                    if (CommonUtils.getConnectivityStatus(ActivityLogin.this)) {
                        dialogWindow();
                        loginMethod();
                    } else {
                        CommonUtils.openInternetDialog(ActivityLogin.this);
                    }
                }

                break;
            case R.id.sign_in_layout:
                Intent j=new Intent(ActivityLogin.this,RegisterActivity.class);
                startActivity(j);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);



                break;
            case R.id.back_from_login:
                Intent k = new Intent(ActivityLogin.this, SlidePageActivity.class);
                startActivity(k);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();


                break;

            case R.id.forgot_txtView:
                Intent f=new Intent(ActivityLogin.this,ForgotPassword.class);
                startActivity(f);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);



                break;
            case R.id.show_txt:
                if(show_txt.getText().toString().equalsIgnoreCase("SHOW")){
                    password_editView.setInputType(InputType.TYPE_CLASS_TEXT);
                    password_editView.refreshDrawableState();
                    show_txt.setText("HIDE");
                }else{
                    password_editView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    show_txt.setText("SHOW");
                }

                break;
        }
    }

    private void loginMethod() {


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
                                JSONObject data = obj.getJSONObject("data");

                                ed.putString(GlobalConstants.USERID, data.getString(GlobalConstants.USERID));
                                ed.putString("login type","app");
                                ed.commit();
                                Intent i=new Intent(ActivityLogin.this,Tab_Activity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                Toast.makeText(ActivityLogin.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.EMAIL, email_editText.getText().toString());
                params.put(GlobalConstants.PASSWORD, password_editView.getText().toString());
                Log.e("lat long", global.getLat());
                params.put(GlobalConstants.LATITUDE, global.getLat());
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.DEVICEID, global.getDeviceToken());
                params.put("device_type", "android");
                params.put("action", GlobalConstants.LOGIN_ACTION);
                Log.e("Parameter for Login", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
}
