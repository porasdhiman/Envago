package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jhang on 9/27/2016.
 */
public class EditProfileActivity extends Activity {


    LinearLayout change_pass;
    EditText username_edit, email_edit, name_edit, phone_edit, paypal_edit, document_edit, about_edit;
    ImageView user_tick, email_tick, name_tick, phone_tick, paypal_tick, document_tick, about_tick, back_button;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CircleImageView profileimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        preferences = getSharedPreferences(GlobalConstants.PREFNAME, MODE_PRIVATE);
        editor = preferences.edit();

        profileimg = (CircleImageView) findViewById(R.id.profile_img);
        change_pass = (LinearLayout) findViewById(R.id.body_phone_changepass);
        username_edit = (EditText) findViewById(R.id.username_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);
        name_edit = (EditText) findViewById(R.id.name_edit);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        paypal_edit = (EditText) findViewById(R.id.paypal_edit);
        document_edit = (EditText) findViewById(R.id.document_edit);
        about_edit = (EditText) findViewById(R.id.about_edit);

        user_tick = (ImageView) findViewById(R.id.right_img);
        email_tick = (ImageView) findViewById(R.id.right_img_email);
        name_tick = (ImageView) findViewById(R.id.right_img_name);
        phone_tick = (ImageView) findViewById(R.id.right_img_phone);
        paypal_tick = (ImageView) findViewById(R.id.right_img_paypal);
        document_tick = (ImageView) findViewById(R.id.right_img_id);
        about_tick = (ImageView) findViewById(R.id.right_img_about);
        back_button = (ImageView) findViewById(R.id.back_editprofile);

        getinfo();
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditProfileActivity.this, ChangePassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
            }
        });

    }
    //------------------------profile-info-method---------------------------------------------

    public void getinfo() {


        String username = preferences.getString(GlobalConstants.USERNAME, "");
        String email = preferences.getString(GlobalConstants.EMAIL, "");
        String name = preferences.getString(GlobalConstants.FIRST_NAME.concat(GlobalConstants.LAST_NAME), "");
        String phone = preferences.getString(GlobalConstants.CONTACT, "");
        String paypal = preferences.getString(GlobalConstants.PAYPAL, "");
        String document = preferences.getString(GlobalConstants.DOCUMENT, "");
        String about = preferences.getString(GlobalConstants.ABOUT, "");

        if (preferences.getString(GlobalConstants.IMAGE, "").length() == 0) {

        } else {

            if (preferences.getString(GlobalConstants.IMAGE,"").contains("http")) {
                Picasso.with(this).load(preferences.getString(GlobalConstants.IMAGE, ""));
            }
            else
            {
                profileimg.setImageURI(Uri.fromFile(new File(preferences.getString(GlobalConstants.IMAGE,""))));
            }
        }

        if (username.length() == 0) {

        } else {
            username_edit.setText(username);
            user_tick.setImageResource(R.drawable.right_red);
        }

        if (email.length() == 0) {

        } else {
            email_edit.setText(email);
            email_tick.setImageResource(R.drawable.right_red);
        }

        if (name.length() == 0) {

        } else {
            name_edit.setText(name);
            name_tick.setImageResource(R.drawable.right_red);
        }

        if (phone.length() == 0) {

        } else {
            phone_edit.setText(phone);
            phone_tick.setImageResource(R.drawable.right_red);
        }

        if (paypal.length() == 0) {

        } else {
            paypal_edit.setText(paypal);
            paypal_tick.setImageResource(R.drawable.right_red);
        }

        if (document.length() == 0) {

        } else {
            document_edit.setText(document);
            document_tick.setImageResource(R.drawable.right_red);
        }
        if (about.length() == 0) {

        } else {
            about_edit.setText(about);
            about_tick.setImageResource(R.drawable.right_red);
        }


    }

}
