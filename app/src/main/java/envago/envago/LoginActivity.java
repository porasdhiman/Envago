package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    LinearLayout email, password;
    EditText mail, pass;
    TextView signup;
    int flag = 0;
    Button signin;

    //--------------facebook variable--------------
    CallbackManager callbackManager;
    LoginButton Login_TV;
    String token;
Button facebook_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        facebook_btn=(Button)findViewById(R.id.facebook_btn);
        Login_TV = (LoginButton) findViewById(R.id.Fb_Login);
        Login_TV.setReadPermissions(Arrays.asList("public_profile, email"));
        fbMethod();
        email = (LinearLayout) findViewById(R.id.email_linear);
        password = (LinearLayout) findViewById(R.id.pass_linear);
        mail = (EditText) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup_txt);

        signin = (Button)findViewById(R.id.login_button);


        mail.setOnTouchListener(this);
        pass.setOnTouchListener(this);
        facebook_btn.setOnClickListener(this);
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);


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
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);



        }
        if (id == R.id.facebook_btn) {
            Login_TV.performClick();

        }

        if (id == R.id.login_button)
        {
            Intent intent = new Intent(LoginActivity.this, Tab_Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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

                      //  try {


                            // name = object.getString("name");
                           // global.setUsername(object.getString("name"));
                            if (object.has("email")) {

                                //global.setEmail(object.getString("email"));
                            } else {
                                //  email = "";
                            }
                            // id = object.getString("id");

                       /* if (object.has("gender"))


                            if (object.has("birthday")) {
                                BIRTHDAY = object.getString("birthday");
                                Log.e("BIRTHDAY", BIRTHDAY);
                            }

                        if (object.has("quotes")) {
                            fbquotes = object.getString("quotes");
                            Log.e("quotesss", fbquotes);
                        } else {
                            fbquotes = "";
                        }
                       */
                            //global.setImageUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                            // getFacebookFrdList();


                            Intent sign = new Intent(LoginActivity.this, Tab_Activity.class);

                            startActivity(sign);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            finish();

                       /* } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/
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
}
