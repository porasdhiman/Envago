package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
public class ChangePassword extends Activity {

    EditText password, new_password, re_new_password;
    Button change_pass;
    ImageView back;
    Dialog dialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        setContentView(R.layout.change_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        back = (ImageView) findViewById(R.id.changepass_back);
        change_pass = (Button) findViewById(R.id.change_pass_button);
        password = (EditText) findViewById(R.id.pass_edittext);
        new_password = (EditText) findViewById(R.id.new_pass_edit);
        re_new_password = (EditText) findViewById(R.id.new_pass_re_edit);

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.length() == 0) {
                    password.setError("Please Enter Password");
                } else if (new_password.length() == 0 && password.length() != 0) {
                    new_password.setError("Please Enter Your New Password");
                } else if (re_new_password.length() == 0 && password.length() != 0 && new_password.length() != 0) {
                    re_new_password.setError("Please Re-Enter Your New Password");
                }

                if (re_new_password.getText().toString().equalsIgnoreCase(new_password.getText().toString())) {
                    dialogWindow();
                    password_change();

                } else {
                    Toast.makeText(getApplicationContext(), "Password Do Not Match", Toast.LENGTH_SHORT).show();
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
    }
//--------------------------Change=pass_APi------------------------------------


    public void password_change() {
        StringRequest change_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();
                Log.e("hulalalalalala", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String response = obj.getString("success");

                    if (response.equalsIgnoreCase("1")) {
                        password.setText("");
                        new_password.setText("");
                        re_new_password.setText("");

                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();

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
                params.put(GlobalConstants.USERID, CommonUtils.UserID(getApplicationContext()));
                params.put("action", GlobalConstants.CHANGE_PASS_ACTION);
                params.put("old_password", password.getText().toString());
                params.put("password", re_new_password.getText().toString());

                Log.e("change password", params.toString());
                return params;
            }
        };

        change_request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(change_request);
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
