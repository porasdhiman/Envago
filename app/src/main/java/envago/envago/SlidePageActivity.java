package envago.envago;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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


/**
 * Created by poras on 17-06-2016.
 */
public class SlidePageActivity extends FragmentActivity implements View.OnClickListener {

    ViewPager pager;
    TextView bottom_txt;
    ImageView img1, img2, img3;
    String slider_upper_txt[];
    String slider_bottom_txt[];
    VideoView videoView1;
    TextView slider_sign_up_btn, slider_fb_btn;
    LinearLayout slider_sign_in_layout;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    //--------------facebook variable--------------
    CallbackManager callbackManager;
    LoginButton Login_TV;
    String token;
    Button facebook_btn;
    String username_mString, email_mString, id_mString;
    Dialog dialog2;
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.slider_pager_layout);
        sp = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        ed = sp.edit();
        // bottom_txt = (TextView) findViewById(R.id.bottom_txt);
        global = (Global) getApplicationContext();

        //---------------------Facebook variable init------------------
        callbackManager = CallbackManager.Factory.create();
        slider_fb_btn = (TextView) findViewById(R.id.slider_fb_btn);
        Login_TV = (LoginButton) findViewById(R.id.Fb_Login);
        Login_TV.setReadPermissions(Arrays.asList("public_profile, email"));
        fbMethod();
        //---------------------------------
        pager = (ViewPager) findViewById(R.id.viewpager);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        videoView1 = (VideoView) findViewById(R.id.videoView1);
        slider_sign_up_btn = (TextView) findViewById(R.id.slider_sign_up_btn);
        slider_sign_in_layout = (LinearLayout) findViewById(R.id.slider_sign_in_layout);
        String uriPath = "android.resource://envago.envago/" + R.raw.envagowalk;
        slider_sign_in_layout.setOnClickListener(this);
        slider_sign_up_btn.setOnClickListener(this);
        Uri uri = Uri.parse(uriPath);
        videoView1.setVideoURI(uri);
        videoView1.requestFocus();
        videoView1.start();
        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        slider_upper_txt = getResources().getStringArray(R.array.slide1_txt);
        slider_bottom_txt = getResources().getStringArray(R.array.slide1_bottom_txt);

        pager.setAdapter(new CutomePagerAdapter(SlidePageActivity.this, slider_upper_txt, slider_bottom_txt));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                Log.e("vale", "" + arg0);
                if (arg0 == 2) {

                    img3.setImageResource(R.drawable.not_filled);
                    img2.setImageResource(R.drawable.filled);
                    img1.setImageResource(R.drawable.filled);
                } else if (arg0 == 1) {
                    img3.setImageResource(R.drawable.filled);
                    img2.setImageResource(R.drawable.not_filled);
                    img1.setImageResource(R.drawable.filled);

                } else {
                    img3.setImageResource(R.drawable.filled);
                    img2.setImageResource(R.drawable.filled);
                    img1.setImageResource(R.drawable.not_filled);


                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        slider_fb_btn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slider_sign_up_btn:
                Intent j = new Intent(SlidePageActivity.this, ActivityLogin.class);
                startActivity(j);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
finish();

                break;
            case R.id.slider_sign_in_layout:

                Intent i = new Intent(SlidePageActivity.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;
            case R.id.slider_fb_btn:
                Login_TV.performClick();

                break;
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
                                JSONObject data = obj.getJSONObject("data");
                                ed.putString(GlobalConstants.USERID, data.getString(GlobalConstants.USERID));
                                ed.commit();
                                Intent intent = new Intent(SlidePageActivity.this, Tab_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            } else {
                                Toast.makeText(SlidePageActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("device_type", "android");

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
