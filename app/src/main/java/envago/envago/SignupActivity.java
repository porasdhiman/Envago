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

/**
 * Created by jhang on 9/18/2016.
 */
public class SignupActivity extends Activity implements View.OnFocusChangeListener, View.OnClickListener, View.OnTouchListener {

    EditText username, email, password, cpassword;
    LinearLayout user, mail, pass, cpass;
    int flag = 0;
    TextView signin;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);

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
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }

        if (id == R.id.signup_button) {
            Intent intent = new Intent(SignupActivity.this, Tab_Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
}
