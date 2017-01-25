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
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vikas on 03-01-2017.
 */

public class RequestedActivity extends Activity implements OnDateSelectedListener, OnMonthChangedListener, View.OnTouchListener {
    MaterialCalendarView calendarView;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    int i = 0;
    TextView start_txtView, end_txtView, clear;
    Button submit_button;
    Global global;
    EditText no_of_days_txtView;
    List<CalendarDay> list = new ArrayList<CalendarDay>();
    CalendarDay date1;
    ArrayList<HashMap<String, String>> dateArray = new ArrayList<>();
    HashMap<String, String> map;
    ImageView back_button_create;
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
    String start_date, end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.requested_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        back_button_create=(ImageView)findViewById(R.id.back_button_create);
        start_txtView = (TextView) findViewById(R.id.start_date_txtView);
        no_of_days_txtView = (EditText) findViewById(R.id.no_of_days_txtView);
        end_txtView = (TextView) findViewById(R.id.end_date_txtView);
        clear = (TextView) findViewById(R.id.clear);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_txtView.setText("Start");
                end_txtView.setText("End");
                i = 0;
                calendarView.removeDecorators();
                list.clear();
                calendarDays.clear();
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
            }
        });
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_txtView.getText().toString().equalsIgnoreCase("Start")) {
                    Toast.makeText(RequestedActivity.this, "please select start date", Toast.LENGTH_SHORT).show();

                } else if (start_txtView.getText().toString().equalsIgnoreCase("End")) {
                    Toast.makeText(RequestedActivity.this, "please select end date", Toast.LENGTH_SHORT).show();
                } else {
                    global.setNumberOfDay(no_of_days_txtView.getText().toString());
                    global.setDateType("repeated");
                    finish();
                }
            }
        });
        no_of_days_txtView.setOnTouchListener(this);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if (no_of_days_txtView.getText().length() > 0) {
            if (i == 0) {
                start_txtView.setText(getSelectedDatesString());
                i = i + 1;
            } else {
                end_txtView.setText(getSelectedDatesString());
            }
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        } else {
            Toast.makeText(RequestedActivity.this, "Please enter number of days", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private String getSelectedDatesString() {
        if (list.size() == 0) {
            date1 = calendarView.getSelectedDate();
            int valye = Integer.parseInt(no_of_days_txtView.getText().toString());
            //calendarView.setSelectedDate(incrementDateByOne(new Date(FORMATTER.format(date.getDate()).toString())));
            for (int i = 0; i < valye; i++) {
                CalendarDay date = new CalendarDay(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), i));
                list.add(date);
            }
            calendarDays = list;
            calendarView.addDecorators(new RequestedActivity.EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));

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


            map = new HashMap<>();
            map.put(GlobalConstants.EVENT_START_DATE, start_date);
            map.put(GlobalConstants.EVENT_END_DATE, end_date);
            dateArray.add(map);
            Log.e("value array", dateArray.toString());
            if (date1 == null) {
                return "No Selection";
            }
            Log.e("calender day value", calendarDays.toString());
            global.setDateArray(dateArray);
        } else {


            list.clear();
            calendarDays.clear();
            date1 = calendarView.getSelectedDate();
            //calendarView.setSelectedDate(incrementDateByOne(new Date(FORMATTER.format(date.getDate()).toString())));
            int valye = Integer.parseInt(no_of_days_txtView.getText().toString());
            for (int i = 0; i < valye; i++) {
                CalendarDay date = new CalendarDay(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), i));
                list.add(date);
            }
            calendarDays = list;
            calendarView.addDecorators(new RequestedActivity.EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));

            start_date = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            String year = start_date.split("-")[0];
            String months = String.valueOf(Integer.parseInt(start_date.split("-")[1]) + 1);
            String date = start_date.split("-")[2];
            start_date = year + "-" + months + "-" + date;
            Log.e("start date", start_date);

            end_date = list.get(valye - 1).toString().substring(12, list.get(valye - 1).toString().length() - 1);
            String year_end = end_date.split("-")[0];
            String months_end = String.valueOf(Integer.parseInt(end_date.split("-")[1]) + 1);
            String date_end = end_date.split("-")[2];
            end_date = year_end + "-" + months_end + "-" + date_end;
            Log.e("end date", end_date);
            map = new HashMap<>();
            map.put(GlobalConstants.EVENT_START_DATE, start_date);
            map.put(GlobalConstants.EVENT_END_DATE, end_date);
            dateArray.add(map);
            Log.e("value array", dateArray.toString());
            if (date1 == null) {
                return "No Selection";
            }
            Log.e("calender day value1", calendarDays.toString());
            global.setDateArray(dateArray);
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
