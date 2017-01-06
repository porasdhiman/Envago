package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * Created by vikas on 06-01-2017.
 */

public class WhatsIncludedActivity extends Activity implements View.OnClickListener {
    Global global;
    ImageView trans_checkBox, flight_checkbox, acc_checkBox, tent_checkBox, gear_checkBox, meals_checkBox;
    Button submit_button;
    String value = "";
    boolean is_trans = false, is_flight = false, is_acc = false, is_tent = false, is_gear = false, is_meals = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whts_included_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        init();
    }

    public void init() {
        trans_checkBox = (ImageView) findViewById(R.id.trans_checkBox);
        flight_checkbox = (ImageView) findViewById(R.id.flight_checkbox);
        acc_checkBox = (ImageView) findViewById(R.id.acc_checkBox);
        tent_checkBox = (ImageView) findViewById(R.id.tent_checkBox);
        gear_checkBox = (ImageView) findViewById(R.id.gear_checkBox);
        meals_checkBox = (ImageView) findViewById(R.id.meals_checkBox);
        submit_button = (Button) findViewById(R.id.submit_button);
        trans_checkBox.setOnClickListener(this);
        flight_checkbox.setOnClickListener(this);
        acc_checkBox.setOnClickListener(this);
        tent_checkBox.setOnClickListener(this);
        gear_checkBox.setOnClickListener(this);
        meals_checkBox.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trans_checkBox:
                if (is_trans == false) {
                    is_trans = true;
                    global.setTransportataion(1);
                    trans_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Transportation";
                    } else {
                        value = value + ",Transportation";
                    }
                } else {
                    global.setTransportataion(0);
                    is_trans = false;
                    trans_checkBox.setImageResource(R.drawable.unselected);
                    value.replace("Transportation", "");
                }
                break;
            case R.id.flight_checkbox:
                if (is_flight == false) {
                    is_flight = true;
                    global.setFlight(1);
                    flight_checkbox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Flight";
                    } else {
                        value = value + ",Flight";
                    }
                } else {
                    is_flight = false;
                    global.setFlight(0);
                    flight_checkbox.setImageResource(R.drawable.unselected);
                    value.replace("Flight", "");
                }
                break;
            case R.id.acc_checkBox:
                if (is_acc == false) {
                    is_acc = true;
                    global.setAccomodation(1);
                    acc_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Accomodation";
                    } else {
                        value = value + ",Accomodation";
                    }
                } else {
                    global.setAccomodation(0);
                    is_acc = false;
                    acc_checkBox.setImageResource(R.drawable.unselected);
                    value.replace("Accomodation", "");
                }
                break;
            case R.id.tent_checkBox:
                if (is_tent == false) {
                    is_tent = true;
                    global.setTent(1);
                    tent_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Tent";
                    } else {
                        value = value + ",Tent";
                    }
                } else {
                    global.setTent(0);
                    is_tent = false;
                    tent_checkBox.setImageResource(R.drawable.unselected);
                    value.replace("Tent", "");
                }
                break;
            case R.id.gear_checkBox:
                if (is_gear == false) {
                    global.setGear(1);
                    is_gear = true;
                    gear_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Gear";
                    } else {
                        value = value + ",Gear";
                    }
                } else {
                    global.setGear(0);
                    is_gear = false;
                    gear_checkBox.setImageResource(R.drawable.unselected);
                    value.replace("Tent", "");
                }
                break;
            case R.id.meals_checkBox:
                if (is_meals == false) {
                    global.setMeal(1);
                    is_meals = true;
                    meals_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Meal";
                    } else {
                        value = value + ",Meal";
                    }
                } else {
                    global.setMeal(0);
                    is_meals = false;
                    meals_checkBox.setImageResource(R.drawable.unselected);
                    value.replace("Meal", "");
                }
                break;
            case R.id.submit_button:
                global.setWhtsicludedString(value);
                finish();
                break;
        }
    }
}
