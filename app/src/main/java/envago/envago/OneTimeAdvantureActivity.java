package envago.envago;

import android.app.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static envago.envago.R.drawable.calendar;

/**
 * Created by vikas on 03-01-2017.
 */

public class OneTimeAdvantureActivity extends Activity implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView calendarView;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    TextView select_date_txtView;
    Button submit_button;
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.one_time_advanture_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        select_date_txtView = (TextView) findViewById(R.id.select_date_txtView);
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!select_date_txtView.getText().toString().equalsIgnoreCase("Select a date")) {
                    global.setEvent_start_date(select_date_txtView.getText().toString());
                    finish();

                }
            }
        });

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        // Toast.makeText(OneTimeAdvantureActivity.this,getSelectedDatesString(),Toast.LENGTH_SHORT).show();
        select_date_txtView.setText(getSelectedDatesString());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private String getSelectedDatesString() {
        CalendarDay date = calendarView.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
}

