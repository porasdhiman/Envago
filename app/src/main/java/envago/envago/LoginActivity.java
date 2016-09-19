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

public class LoginActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    LinearLayout email, password;
    EditText mail, pass;
    TextView signup;
    int flag = 0;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (LinearLayout) findViewById(R.id.email_linear);
        password = (LinearLayout) findViewById(R.id.pass_linear);
        mail = (EditText) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup_txt);

        signin = (Button)findViewById(R.id.login_button);


        mail.setOnTouchListener(this);
        pass.setOnTouchListener(this);

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

        if (id == R.id.login_button)
        {
            Intent intent = new Intent(LoginActivity.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }
}
