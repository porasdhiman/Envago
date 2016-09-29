package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by jhang on 9/30/2016.
 */
public class CallVolunteers extends Activity implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {

    EditText name, address, about, paypal;
    CheckBox individual, group, licensed, nlicensed;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.call_volunteers);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }


        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        about = (EditText) findViewById(R.id.about);
        paypal = (EditText) findViewById(R.id.paypal);

        individual = (CheckBox) findViewById(R.id.chk_individual);
        group = (CheckBox) findViewById(R.id.chk_group);
        licensed = (CheckBox) findViewById(R.id.chk_licensed);
        nlicensed = (CheckBox) findViewById(R.id.chk_notlicensed);
        back_button=(ImageView)findViewById(R.id.back_button_volunteers);

        individual.setOnClickListener(this);
        group.setOnClickListener(this);
        licensed.setOnClickListener(this);
        nlicensed.setOnClickListener(this);
        back_button.setOnClickListener(this);


        name.setOnTouchListener(this);
        address.setOnFocusChangeListener(this);
        about.setOnFocusChangeListener(this);
        paypal.setOnFocusChangeListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();


        if (id == R.id.name) {
            name.setBackground(getResources().getDrawable(R.drawable.stroke));
            address.setBackground(getResources().getDrawable(R.drawable.rectangle));
            about.setBackground(getResources().getDrawable(R.drawable.rectangle));
            paypal.setBackground(getResources().getDrawable(R.drawable.rectangle));

        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasfocus) {
        int id = view.getId();


        if (id == R.id.address && hasfocus) {
            name.setBackground(getResources().getDrawable(R.drawable.rectangle));
            address.setBackground(getResources().getDrawable(R.drawable.stroke));
            about.setBackground(getResources().getDrawable(R.drawable.rectangle));
            paypal.setBackground(getResources().getDrawable(R.drawable.rectangle));
        } else if (id == R.id.about && hasfocus) {
            name.setBackground(getResources().getDrawable(R.drawable.rectangle));
            address.setBackground(getResources().getDrawable(R.drawable.rectangle));
            about.setBackground(getResources().getDrawable(R.drawable.stroke));
            paypal.setBackground(getResources().getDrawable(R.drawable.rectangle));
        } else if (id == R.id.paypal && hasfocus) {
            name.setBackground(getResources().getDrawable(R.drawable.rectangle));
            address.setBackground(getResources().getDrawable(R.drawable.rectangle));
            about.setBackground(getResources().getDrawable(R.drawable.rectangle));
            paypal.setBackground(getResources().getDrawable(R.drawable.stroke));
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.chk_individual) {
            individual.setChecked(true);
            group.setChecked(false);
        }

        if (id == R.id.chk_group) {
            individual.setChecked(false);
            group.setChecked(true);
        }

        if (id == R.id.chk_licensed) {
            licensed.setChecked(true);
            nlicensed.setChecked(false);
        }
        if (id == R.id.chk_notlicensed) {
            licensed.setChecked(false);
            nlicensed.setChecked(true);
        }

        if (id==R.id.back_button_volunteers)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        }

    }
}

