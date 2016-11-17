package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    LinearLayout email, password;
    EditText mail, pass;
    TextView signup;
    int flag = 0;
    Button signin;
    TextView forgot;
    Global global;
    //--------------facebook variable--------------
    CallbackManager callbackManager;
    LoginButton Login_TV;
    String token;
    Button facebook_btn;
    ProgressDialog pd;
    String username_mString, email_mString, id_mString;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    Dialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        ed = sp.edit();

        global = (Global) getApplicationContext();
        callbackManager = CallbackManager.Factory.create();
        facebook_btn = (Button) findViewById(R.id.facebook_btn);
        Login_TV = (LoginButton) findViewById(R.id.Fb_Login);
        Login_TV.setReadPermissions(Arrays.asList("public_profile, email"));
        fbMethod();
        email = (LinearLayout) findViewById(R.id.email_linear);
        password = (LinearLayout) findViewById(R.id.pass_linear);
        mail = (EditText) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup_txt);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");
        //Font.overrideFont(getApplicationContext(), "SERIF", "fonts/avenir_book.ttf");


        forgot = (TextView) findViewById(R.id.forgot_pass);
        forgot.setTypeface(font);
        signin = (Button) findViewById(R.id.login_button);


        mail.setOnTouchListener(this);
        pass.setOnTouchListener(this);
        facebook_btn.setOnClickListener(this);
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        forgot.setOnClickListener(this);


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();


        if (id == R.id.mail) {
            flag = 1;
            email.setBackground(getResources().getDrawable(R.drawable.stroke));
            password.setBackground(getResources().getDrawable(R.drawable.rectangle));
        } else if (id == R.id.password) {
            flag = 0;
            password.setBackground(getResources().getDrawable(R.drawable.stroke));
            email.setBackground(getResources().getDrawable(R.drawable.rectangle));

        }
        return false;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.signup_txt) {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


        }
        if (id == R.id.facebook_btn) {
            Login_TV.performClick();

        }

        if (id == R.id.login_button) {
            if (mail.length() == 0) {
                mail.setError("Please enter Email");
            } else if (pass.length() == 0) {
                mail.setError("Please enter Password");
            } else if (!CommonUtils.isEmailValid(mail.getText().toString())) {
                mail.setError("Please enter Valid Email");
            } else {
                if (CommonUtils.getConnectivityStatus(LoginActivity.this)) {
                    dialogWindow();
                    loginMethod();
                } else {
                    CommonUtils.openInternetDialog(LoginActivity.this);
                }
            }

        }

        if (id == R.id.forgot_pass) {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    //---------------------------facebook method------------------------------
    public void fbMethod() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                token = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        // Application code


                        try {
                            username_mString = object.getString("name");
                            if (object.has("email")) {
                                email_mString = object.getString("email");
                            } else {
                                //  email = "";
                            }
                            id_mString = object.getString("id");
                            dialogWindow();
                            facebookApiMethod();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "picture.type(large),bio,id,name,link,gender,email, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void loginMethod() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject data = obj.getJSONObject("data");

                            ed.putString(GlobalConstants.USERID, data.getString(GlobalConstants.USERID));
                            ed.commit();
                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {

                                Intent intent = new Intent(LoginActivity.this, Tab_Activity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                            dialog2.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.EMAIL, mail.getText().toString());
                params.put(GlobalConstants.PASSWORD, pass.getText().toString());

                params.put(GlobalConstants.LATITUDE, global.getLat());
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.DEVICEID, global.getDeviceToken());
                params.put("action", GlobalConstants.LOGIN_ACTION);
                Log.e("Parameter for Login", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//-------------------------------Facebook api method------------------------------


    private void facebookApiMethod() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {

                                Intent intent = new Intent(LoginActivity.this, Tab_Activity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            } else {
                                Toast.makeText(LoginActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                            dialog2.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.FB_ID, id_mString);
                params.put(GlobalConstants.EMAIL, email_mString);
                params.put(GlobalConstants.USERNAME, username_mString);


                params.put(GlobalConstants.LATITUDE, global.getLat());
                params.put(GlobalConstants.LONGITUDE, global.getLong());


                params.put(GlobalConstants.DEVICEID, global.getDeviceToken());
                params.put("action", GlobalConstants.FACEBOOK_REGISTER_ACTION);
                Log.e("facebook login", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
