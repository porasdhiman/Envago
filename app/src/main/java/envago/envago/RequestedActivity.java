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

/**
 * Created by vikas on 03-01-2017.
 */

public class RequestedActivity extends Activity implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView calendarView;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    int i=0;
    TextView start_txtView,end_txtView,clear;
Button submit_button;
    Global global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.requested_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global=(Global)getApplicationContext();
        start_txtView=(TextView)findViewById(R.id.start_date_txtView) ;
        end_txtView=(TextView)findViewById(R.id.end_date_txtView) ;
        clear=(TextView)findViewById(R.id.clear);
        calendarView=(MaterialCalendarView)findViewById(R.id.calendarView);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_txtView.setText("Start");
                end_txtView.setText("End");
                i=0;
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
            }
        });
        submit_button=(Button)findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_txtView.getText().toString().equalsIgnoreCase("Start")){
                    Toast.makeText(RequestedActivity.this,"please select start date",Toast.LENGTH_SHORT).show();

                }else if(start_txtView.getText().toString().equalsIgnoreCase("End")){
                    Toast.makeText(RequestedActivity.this,"please select end date",Toast.LENGTH_SHORT).show();
                }else{
                    global.setEvent_start_date(start_txtView.getText().toString());
                    global.setEvent_end_date(start_txtView.getText().toString());
                    finish();
                }
            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

       if(i==0){
           start_txtView.setText(getSelectedDatesString());
           i=i+1;
       }else{
           end_txtView.setText(getSelectedDatesString());
       }
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
    //---------------MEthod for match date--------------
    boolean dateMatchMethod(String selectedDate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");



        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(start_txtView.getText().toString());
            eventDate = dateFormat.parse(selectedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (eventDate.equals(currentDate) || eventDate.before(currentDate)) {
            return true;
        } else {
            return false;
        }


    }
}
