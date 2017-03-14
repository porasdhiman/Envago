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

public class RegisterActivity extends Activity implements View.OnTouchListener, View.OnClickListener {
    LinearLayout sign_up_layout;
    TextView sign_up_button;
    TextView show_txt;
    EditText password_editText, mail_editText, name_editText;
    TextView password_error_txtView, mail_error_txtView, name_error_txtView;

    Global global;

    SharedPreferences sp;
    SharedPreferences.Editor ed;
    Dialog dialog2;
    ImageView back_from_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        sp = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        ed = sp.edit();
        global = (Global) getApplicationContext();
        sign_up_layout = (LinearLayout) findViewById(R.id.sign_up_layout);
        sign_up_button = (TextView) findViewById(R.id.sign_up_button);
        show_txt = (TextView) findViewById(R.id.show_txt);
        password_editText = (EditText) findViewById(R.id.password);
        mail_editText = (EditText) findViewById(R.id.mail);
        name_editText = (EditText) findViewById(R.id.name_edit_TxtView);
        password_error_txtView = (TextView) findViewById(R.id.password_error_txtView);
        name_error_txtView = (TextView) findViewById(R.id.name_error_txtView);
        mail_error_txtView = (TextView) findViewById(R.id.mail_error_txtView);
        back_from_login = (ImageView) findViewById(R.id.back_from_login);
        show_txt.setOnClickListener(this);
        sign_up_button.setOnClickListener(this);
        sign_up_layout.setOnClickListener(this);
        password_editText.setOnTouchListener(this);
        name_editText.setOnTouchListener(this);
        mail_editText.setOnTouchListener(this);
        back_from_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_layout:
                Intent k = new Intent(RegisterActivity.this, ActivityLogin.class);
                startActivity(k);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.back_from_login:
                Intent a = new Intent(RegisterActivity.this, ActivityLogin.class);
                startActivity(a);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

                break;

            case R.id.sign_up_button:
                if (name_editText.getText().length() == 0) {
                    name_error_txtView.setVisibility(View.VISIBLE);
                    name_error_txtView.setText("Please enter username");

                } else if (mail_editText.getText().length() == 0) {
                    mail_error_txtView.setVisibility(View.VISIBLE);
                    mail_error_txtView.setText("Please enter email");


                } else if (password_editText.getText().length() == 0) {
                    password_error_txtView.setVisibility(View.VISIBLE);
                    password_error_txtView.setText("Please enter password");


                } else if (!CommonUtils.isEmailValid(mail_editText.getText().toString())) {
                    mail_error_txtView.setVisibility(View.VISIBLE);
                    mail_error_txtView.setText("Please enter Valid Email");
                } else {
                    registerMethod();
                    dialogWindow();


                }

                break;
            case R.id.show_txt:
                if (show_txt.getText().toString().equalsIgnoreCase("SHOW")) {
                    password_editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    password_editText.refreshDrawableState();
                    show_txt.setText("HIDE");
                } else {
                    password_editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    show_txt.setText("SHOW");
                }

                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.mail:
                mail_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.password:
                password_error_txtView.setVisibility(View.GONE);
                break;
            case R.id.name_edit_TxtView:
                name_error_txtView.setVisibility(View.GONE);
                break;
        }


        return false;
    }
    //------------------------------------Register Api Method------------------------------

    private void registerMethod() {


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
                                ed.putString("login type", "app");
                                ed.commit();

                                Intent j = new Intent(RegisterActivity.this, Tab_Activity.class);
                                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(j);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                finish();

                            } else {
                                Toast.makeText(RegisterActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put(GlobalConstants.USERNAME, name_editText.getText().toString());

                params.put(GlobalConstants.EMAIL, mail_editText.getText().toString());
                params.put(GlobalConstants.PASSWORD, password_editText.getText().toString());

                params.put(GlobalConstants.LATITUDE, global.getLat());
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put("device_type", "android");


                params.put(GlobalConstants.DEVICEID, global.getDeviceToken());
                params.put("action", GlobalConstants.REGISTER_ACTION);
                Log.e("Parameter for Register", params.toString());
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
