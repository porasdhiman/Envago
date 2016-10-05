package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
 * Created by jhang on 9/18/2016.
 */
public class SignupActivity extends Activity implements View.OnFocusChangeListener, View.OnClickListener, View.OnTouchListener {

    EditText username, email, password, cpassword;
    LinearLayout user, mail, pass, cpass;
    int flag = 0;
    TextView signin;
    Button signup;
    Global global;
    ProgressDialog pd;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
Dialog dialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);
        sp = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        ed = sp.edit();
        global = (Global) getApplicationContext();
        username = (EditText) findViewById(R.id.user);
        email = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        cpassword = (EditText) findViewById(R.id.cpassword);
        signin = (TextView) findViewById(R.id.signin_text);
        signup = (Button) findViewById(R.id.signup_button);

        user = (LinearLayout) findViewById(R.id.user_linear);
        mail = (LinearLayout) findViewById(R.id.email_linear);
        pass = (LinearLayout) findViewById(R.id.pass_linear);
        cpass = (LinearLayout) findViewById(R.id.confirmpass_linear);


        username.setOnTouchListener(this);
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        cpassword.setOnFocusChangeListener(this);

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);

    }


    @Override
    public void onFocusChange(View view, boolean hasfocus) {

        int id = view.getId();


        if (id == R.id.mail && hasfocus) {
            user.setBackground(getResources().getDrawable(R.drawable.rectangle));
            mail.setBackground(getResources().getDrawable(R.drawable.stroke));
            pass.setBackground(getResources().getDrawable(R.drawable.rectangle));
            cpass.setBackground(getResources().getDrawable(R.drawable.rectangle));
        } else if (id == R.id.password && hasfocus) {
            user.setBackground(getResources().getDrawable(R.drawable.rectangle));
            mail.setBackground(getResources().getDrawable(R.drawable.rectangle));
            pass.setBackground(getResources().getDrawable(R.drawable.stroke));
            cpass.setBackground(getResources().getDrawable(R.drawable.rectangle));
        } else if (id == R.id.cpassword && hasfocus) {
            user.setBackground(getResources().getDrawable(R.drawable.rectangle));
            mail.setBackground(getResources().getDrawable(R.drawable.rectangle));
            pass.setBackground(getResources().getDrawable(R.drawable.rectangle));
            cpass.setBackground(getResources().getDrawable(R.drawable.stroke));
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.signin_text) {


            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        }

        if (id == R.id.signup_button) {
            if (username.getText().length() == 0) {
                username.setError("Please enter username");

            } else if (email.getText().length() == 0) {
                username.setError("Please enter email");


            }
            if (password.getText().length() == 0) {
                username.setError("Please enter username");


            }
            if (password.getText().toString() != cpassword.getText().toString()) {
                Toast.makeText(SignupActivity.this, "password are not match", Toast.LENGTH_SHORT).show();
            } else {
                dialogWindow();
                registerMethod();
            }
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();


        if (id == R.id.user) {
            user.setBackground(getResources().getDrawable(R.drawable.stroke));
            mail.setBackground(getResources().getDrawable(R.drawable.rectangle));
            pass.setBackground(getResources().getDrawable(R.drawable.rectangle));
            cpass.setBackground(getResources().getDrawable(R.drawable.rectangle));

        }
        return false;
    }

    //------------------------------------Register Api Method

    private void registerMethod() {


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
                                ed.commit();

                                Intent intent = new Intent(SignupActivity.this, Tab_Activity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                            } else {
                                Toast.makeText(SignupActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERNAME, username.getText().toString());

                params.put(GlobalConstants.EMAIL, email.getText().toString());
                params.put(GlobalConstants.PASSWORD, password.getText().toString());

                params.put(GlobalConstants.LATITUDE, global.getLat());
                params.put(GlobalConstants.LONGITUDE, global.getLong());


                params.put(GlobalConstants.DEVICEID, global.getDeviceToken());
                params.put("action", GlobalConstants.REGISTER_ACTION);
                Log.e("Parameter for Register", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow(){
        dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.progrees_dialog);
        AVLoadingIndicatorView loaderView=(AVLoadingIndicatorView)dialog2.findViewById(R.id.loader_view);
        loaderView.show();

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        dialog2.show();
    }

}
