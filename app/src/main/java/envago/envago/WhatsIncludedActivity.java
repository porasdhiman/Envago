package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vikas on 06-01-2017.
 */

public class WhatsIncludedActivity extends Activity implements View.OnClickListener {
    Global global;
    ImageView trans_checkBox, flight_checkbox, acc_checkBox, tent_checkBox, gear_checkBox, meals_checkBox;
    Button submit_button;
    String value = "";
    boolean is_trans = false, is_flight = false, is_acc = false, is_tent = false, is_gear = false, is_meals = false;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    RelativeLayout trans_layout, flight_layout, accom_layout, tent_layout, gear_layout, meal_layout;
    TextView tran_txtView, flight_txtView, acco_txtView, tent_txtView, gear_txtView, meal_txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whts_included_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp = getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed = sp.edit();
        global = (Global) getApplicationContext();
        init();
    }

    public void init() {
        tran_txtView = (TextView) findViewById(R.id.tran_txtView);
        flight_txtView = (TextView) findViewById(R.id.flight_txtView);
        tent_txtView = (TextView) findViewById(R.id.tent_txtView);
        acco_txtView = (TextView) findViewById(R.id.acco_txtView);
        gear_txtView = (TextView) findViewById(R.id.gear_txtView);
        meal_txtView = (TextView) findViewById(R.id.meal_txtView);
        Fonts.overrideFonts1(this, tran_txtView);
        Fonts.overrideFonts1(this, tran_txtView);
        Fonts.overrideFonts1(this, flight_txtView);
        Fonts.overrideFonts1(this, tent_txtView);
        Fonts.overrideFonts1(this, acco_txtView);
        Fonts.overrideFonts1(this, gear_txtView);
        Fonts.overrideFonts1(this, meal_txtView);
        trans_layout = (RelativeLayout) findViewById(R.id.trans_layout);
        flight_layout = (RelativeLayout) findViewById(R.id.flight_layout);
        accom_layout = (RelativeLayout) findViewById(R.id.accom_layout);
        tent_layout = (RelativeLayout) findViewById(R.id.tent_layout);
        gear_layout = (RelativeLayout) findViewById(R.id.gear_layout);
        meal_layout = (RelativeLayout) findViewById(R.id.meal_layout);
        trans_checkBox = (ImageView) findViewById(R.id.trans_checkBox);
        flight_checkbox = (ImageView) findViewById(R.id.flight_checkbox);
        acc_checkBox = (ImageView) findViewById(R.id.acc_checkBox);
        tent_checkBox = (ImageView) findViewById(R.id.tent_checkBox);
        gear_checkBox = (ImageView) findViewById(R.id.gear_checkBox);
        meals_checkBox = (ImageView) findViewById(R.id.meals_checkBox);
        submit_button = (Button) findViewById(R.id.submit_button);
        trans_layout.setOnClickListener(this);
        flight_layout.setOnClickListener(this);
        accom_layout.setOnClickListener(this);
        tent_layout.setOnClickListener(this);
        gear_layout.setOnClickListener(this);
        meal_layout.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        if (!sp.getString("trans", "0").equalsIgnoreCase("0")) {
            is_trans = true;


            trans_checkBox.setImageResource(R.drawable.selected);
            if (value.equalsIgnoreCase("")) {
                value = "Transportation";
            } else {
                value = value + " Transportation";
            }
        }
        if (!sp.getString("flight", "0").equalsIgnoreCase("0")) {
            is_flight = true;

            flight_checkbox.setImageResource(R.drawable.selected);
            if (value.equalsIgnoreCase("")) {
                value = "Flight";
            } else {
                value = value + " Flight";
            }
        }
        if (!sp.getString("Accomodation", "0").equalsIgnoreCase("0")) {
            is_acc = true;

            acc_checkBox.setImageResource(R.drawable.selected);
            if (value.equalsIgnoreCase("")) {
                value = "Accomodation";
            } else {
                value = value + " Accomodation";
            }
        }
        if (!sp.getString("tent", "0").equalsIgnoreCase("0")) {
            is_tent = true;

            tent_checkBox.setImageResource(R.drawable.selected);
            if (value.equalsIgnoreCase("")) {
                value = "Tent";
            } else {
                value = value + " Tent";
            }
        }
        if (!sp.getString("gear", "0").equalsIgnoreCase("0")) {

            is_gear = true;
            gear_checkBox.setImageResource(R.drawable.selected);
            if (value.equalsIgnoreCase("")) {
                value = "Gear";
            } else {
                value = value + " Gear";
            }
        }
        if (!sp.getString("meal", "0").equalsIgnoreCase("0")) {

            is_meals = true;
            meals_checkBox.setImageResource(R.drawable.selected);
            if (value.equalsIgnoreCase("")) {
                value = "Meal";
            } else {
                value = value + " Meal";
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trans_layout:
                if (is_trans == false) {
                    is_trans = true;
                    global.setTransportataion(1);


                    trans_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Transportation";
                    } else {
                        value = value + " Transportation";
                    }
                } else {
                    global.setTransportataion(0);


                    is_trans = false;
                    trans_checkBox.setImageResource(R.drawable.unselected);
                    value = value.replace("Transportation", "");
                }
                break;
            case R.id.flight_layout:
                if (is_flight == false) {
                    is_flight = true;
                    global.setFlight(1);


                    flight_checkbox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Flight";
                    } else {
                        value = value + " Flight";
                    }
                } else {
                    is_flight = false;
                    global.setFlight(0);

                    flight_checkbox.setImageResource(R.drawable.unselected);
                    value = value.replace("Flight", "");
                }
                break;
            case R.id.accom_layout:
                if (is_acc == false) {
                    is_acc = true;
                    global.setAccomodation(1);


                    acc_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Accomodation";
                    } else {
                        value = value + " Accomodation";
                    }
                } else {
                    global.setAccomodation(0);

                    is_acc = false;
                    acc_checkBox.setImageResource(R.drawable.unselected);
                    value = value.replace("Accomodation", "");
                }
                break;
            case R.id.tent_layout:
                if (is_tent == false) {
                    is_tent = true;
                    global.setTent(1);


                    tent_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Tent";
                    } else {
                        value = value + " Tent";
                    }
                } else {
                    global.setTent(0);

                    is_tent = false;
                    tent_checkBox.setImageResource(R.drawable.unselected);
                    value = value.replace("Tent", "");
                }
                break;
            case R.id.gear_layout:
                if (is_gear == false) {
                    global.setGear(1);


                    is_gear = true;
                    gear_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Gear";
                    } else {
                        value = value + " Gear";
                    }
                } else {
                    global.setGear(0);

                    is_gear = false;
                    gear_checkBox.setImageResource(R.drawable.unselected);
                    value = value.replace("Gear", "");
                }
                break;
            case R.id.meal_layout:
                if (is_meals == false) {
                    global.setMeal(1);


                    is_meals = true;
                    meals_checkBox.setImageResource(R.drawable.selected);
                    if (value.equalsIgnoreCase("")) {
                        value = "Meal";
                    } else {
                        value = value + " Meal";
                    }
                } else {
                    global.setMeal(0);

                    is_meals = false;
                    meals_checkBox.setImageResource(R.drawable.unselected);
                    value = value.replace("Meal", "");
                }
                break;
            case R.id.submit_button:
                if (value.startsWith("")) {
                    value = value.trim();
                    global.setWhtsicludedString(value);
                } else {
                    global.setWhtsicludedString(value);
                }

                ed.putString("trans", String.valueOf(global.getTransportataion()));
                ed.putString("flight", String.valueOf(global.getFlight()));
                ed.putString("Accomodation", String.valueOf(global.getAccomodation()));
                ed.putString("tent", String.valueOf(global.getTent()));
                ed.putString("gear", String.valueOf(global.getGear()));
                ed.putString("meal", String.valueOf(global.getMeal()));
                ed.putString(GlobalConstants.WHATS_INCLUDED, global.getWhtsicludedString());
                ed.commit();
                finish();
                break;
        }
    }
}
