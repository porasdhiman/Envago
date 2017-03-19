package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by jhang on 9/27/2016.
 */
public class ForgotPassword extends Activity {

    Button sumbit_mail;
    EditText email;
    Dialog dialog2;
    TextView mail_error_txtView,email_txtView;
ImageView back;
RelativeLayout main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgot_password);
        main_layout=(RelativeLayout)findViewById(R.id.main_layout);
        Fonts.overrideFonts(this,main_layout);
        email_txtView=(TextView)findViewById(R.id.mail_txtView);
        Fonts.overrideFontHeavy(this,email_txtView);
        sumbit_mail = (Button) findViewById(R.id.forget_sumbit);
        email = (EditText) findViewById(R.id.mail_forgot);
        mail_error_txtView=(TextView)findViewById(R.id.mail_error_txtView);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sumbit_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length() == 0) {
                    mail_error_txtView.setVisibility(View.VISIBLE);
                    mail_error_txtView.setText("Please enter Your Email");
                } else if (!CommonUtils.isEmailValid(email.getText().toString())) {
                    mail_error_txtView.setVisibility(View.VISIBLE);
                    mail_error_txtView.setText("Please enter a Valid Email");
                } else {
                    dialogWindow();
                    forgot();
                }
            }
        });
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mail_error_txtView.setVisibility(View.GONE);
                return false;
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sumbit_mail.setBackgroundResource(R.drawable.red_button_back);
            }
        });
    }

    //-------------------------------Forgot-Api--------------

    private void forgot() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();
                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            //  JSONObject data = obj.getJSONObject("data");

                            //   ed.putString(GlobalConstants.USERID, data.getString(GlobalConstants.USERID));
                            //  ed.commit();
                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {


                                finish();
                                Toast.makeText(ForgotPassword.this, obj.getString("A resent link has been sent to your email"), Toast.LENGTH_SHORT).show();


                            } else {
                                mail_error_txtView.setVisibility(View.VISIBLE);
                                mail_error_txtView.setText(obj.getString("msg"));
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
                        Toast.makeText(ForgotPassword.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.EMAIL, email.getText().toString());

                params.put("action", GlobalConstants.FORGOT_ACTION);
                Log.e("Parameter for Login", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //----------progress--------------------

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
