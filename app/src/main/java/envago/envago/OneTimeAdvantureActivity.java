package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vikas on 03-01-2017.
 */

public class OneTimeAdvantureActivity extends Activity implements OnDateSelectedListener, OnMonthChangedListener, View.OnTouchListener {
    MaterialCalendarView calendarView;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    TextView select_date_txtView;
    Button submit_button;
    Global global;
    List<CalendarDay> list = new ArrayList<CalendarDay>();
    CalendarDay date1;
    String start_date, end_date;

    private Collection<CalendarDay> calendarDays = new Collection<CalendarDay>() {
        @Override
        public boolean add(CalendarDay object) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends CalendarDay> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean contains(Object object) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @NonNull
        @Override
        public Iterator<CalendarDay> iterator() {
            return null;
        }

        @Override
        public boolean remove(Object object) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(T[] array) {
            return null;
        }
    };
    EditText of_days_txtView;

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
        of_days_txtView = (EditText) findViewById(R.id.no_of_days_txtView);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        select_date_txtView = (TextView) findViewById(R.id.select_date_txtView);
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!select_date_txtView.getText().toString().equalsIgnoreCase("Select a date")) {
                    global.setDateType("one_time");
                    global.setNumberOfDay(of_days_txtView.getText().toString());
                    finish();

                }
            }
        });

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        of_days_txtView.setOnTouchListener(this);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        // Toast.makeText(OneTimeAdvantureActivity.this,getSelectedDatesString(),Toast.LENGTH_SHORT).show();
        if (of_days_txtView.getText().length() > 0) {
            select_date_txtView.setText(getSelectedDatesString());
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        } else {
            Toast.makeText(OneTimeAdvantureActivity.this, "Please enter number of days", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private String getSelectedDatesString() {
        if (list.size() == 0) {
            date1 = calendarView.getSelectedDate();
            int valye = Integer.parseInt(of_days_txtView.getText().toString());
            //calendarView.setSelectedDate(incrementDateByOne(new Date(FORMATTER.format(date.getDate()).toString())));
            for (int i = 0; i < valye; i++) {
                CalendarDay date = new CalendarDay(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), i));
                list.add(date);
            }
            calendarDays = list;
            calendarView.addDecorators(new EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));
            start_date = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            String year = start_date.split("-")[0];
            String months = String.valueOf(Integer.parseInt(start_date.split("-")[1]) + 1);
            String date = start_date.split("-")[2];
            start_date = year + "-" + months + "-" + date;
            Log.e("start date", start_date);
            global.setEvent_start_date(start_date);
            end_date = list.get(valye - 1).toString().substring(12, list.get(valye - 1).toString().length() - 1);
            String year_end = end_date.split("-")[0];
            String months_end = String.valueOf(Integer.parseInt(end_date.split("-")[1]) + 1);
            String date_end = end_date.split("-")[2];
            end_date = year_end + "-" + months_end + "-" + date_end;
            Log.e("end date", end_date);
            global.setEvent_end_date(end_date);
            if (date1 == null) {
                return "No Selection";
            }
            Log.e("calender day value", calendarDays.toString());
        } else {


            calendarView.removeDecorators();
            list.clear();
            calendarDays.clear();
            date1 = calendarView.getSelectedDate();
            //calendarView.setSelectedDate(incrementDateByOne(new Date(FORMATTER.format(date.getDate()).toString())));
            int valye = Integer.parseInt(of_days_txtView.getText().toString());
            for (int i = 0; i < valye; i++) {
                CalendarDay date = new CalendarDay(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), i));
                list.add(date);
            }
            calendarDays = list;
            calendarView.addDecorators(new EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));
            start_date = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            String year = start_date.split("-")[0];
            String months = String.valueOf(Integer.parseInt(start_date.split("-")[1]) + 1);
            String date = start_date.split("-")[2];
            start_date = year + "-" + months + "-" + date;
            Log.e("start date", start_date);
            global.setEvent_start_date(start_date);
            end_date = list.get(valye - 1).toString().substring(12, list.get(valye - 1).toString().length() - 1);
            String year_end = end_date.split("-")[0];
            String months_end = String.valueOf(Integer.parseInt(end_date.split("-")[1]) + 1);
            String date_end = end_date.split("-")[2];
            end_date = year_end + "-" + months_end + "-" + date_end;
            Log.e("end date", end_date);
            global.setEvent_end_date(end_date);
            if (date1 == null) {
                return "No Selection";
            }
            Log.e("calender day value1", calendarDays.toString());
        }

        return FORMATTER.format(date1.getDate());
    }

    public Date incrementDateByOne(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        Date nextDate = c.getTime();
        return nextDate;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        return false;
    }

    class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(7, color));
        }
    }
}

