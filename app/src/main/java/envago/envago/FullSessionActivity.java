package envago.envago;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by vikas on 03-01-2017.
 */

public class FullSessionActivity extends Activity {
    EditText no_of_days;
    Spinner session_spinner, year_spinner;
    String year;
    int j;
    ArrayList<String> list = new ArrayList<>();
    String startDate_arr[] = {" ", "01-01", "03-15", "06-15", "08-02", "10-02"};
    String endDate_arr[] = {" ", "03-14", "06-14", "08-01", "10-01", "12-31"};
    String catgory = "", yearType = "";
    Global global;
    int pos;
    Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.full_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        submit_button = (Button) findViewById(R.id.submit_button);
        no_of_days = (EditText) findViewById(R.id.no_of_days_txtView);
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        session_spinner = (Spinner) findViewById(R.id.session_spinner);
        year_spinner = (Spinner) findViewById(R.id.year_spinner);
        j = Integer.parseInt(year);
        list.add("year");
        for (int i = 0; i < 17; i++) {

            list.add(String.valueOf(j));
            j = ++j;

        }

        ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        karant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(karant_adapter);

        session_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                ((TextView) parent.getChildAt(0)).setTextSize(14);
                if (position == 0) {

                } else {

                    pos = position;
                    catgory = session_spinner.getSelectedItem().toString();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                ((TextView) parent.getChildAt(0)).setTextSize(14);
                if (position == 0) {

                } else {


                    yearType = year_spinner.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (no_of_days.getText().length() == 0) {
                    Toast.makeText(FullSessionActivity.this, "Please enter number of days", Toast.LENGTH_SHORT).show();

                } else if (yearType.equalsIgnoreCase("")) {
                    Toast.makeText(FullSessionActivity.this, "Please enter year", Toast.LENGTH_SHORT).show();
                } else if (catgory.equalsIgnoreCase("")) {
                    Toast.makeText(FullSessionActivity.this, "Please enter season", Toast.LENGTH_SHORT).show();
                } else {
                    global.setEvent_start_date(yearType + "-" + startDate_arr[pos]);
                    global.setEvent_end_date(yearType + "-" + endDate_arr[pos]);
                    global.setSessionType(catgory);
                    global.setDateType("full_season");
                    global.setNumberOfDay(no_of_days.getText().toString());
                    finish();
                }
            }
        });

    }
}
