package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static envago.envago.R.id.end_date_txtView;

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
    List<CalendarDay> demolist = new ArrayList<CalendarDay>();
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
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    RelativeLayout start_end_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.requested_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp = getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed = sp.edit();

        global = (Global) getApplicationContext();
        start_end_layout = (RelativeLayout) findViewById(R.id.start_end_layout);
        back_button_create = (ImageView) findViewById(R.id.back_button_create);
        start_txtView = (TextView) findViewById(R.id.start_date_txtView);
        no_of_days_txtView = (EditText) findViewById(R.id.no_of_days_txtView);
        end_txtView = (TextView) findViewById(end_date_txtView);
        clear = (TextView) findViewById(R.id.clear);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        String date = formattedDate.split("-")[0];
        String month = formattedDate.split("-")[1];
        String year = formattedDate.split("-")[2];
        // Toast.makeText(this,date+"-"+month+"-"+year,Toast.LENGTH_SHORT).show();
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.state().edit()

                .setMinimumDate(CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date)))
                .setMaximumDate(CalendarDay.from(2023, 12, 31))

                .commit();
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
                demolist.clear();
                dateArray.clear();
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
               /* ed.clear();
                ed.commit();*/
                ed.putString(GlobalConstants.NUMBER_OF_DAY, "");
                ed.putString("date type", "");
                Gson gson = new Gson();
                String json = gson.toJson(dateArray);
                ed.putString(GlobalConstants.DATE_DATA, "");
                Gson gson1 = new Gson();
                String json1 = gson1.toJson(demolist);
                ed.putString("demoList", "");
                ed.putString(GlobalConstants.EVENT_START_DATE, "");
                ed.putString(GlobalConstants.EVENT_END_DATE, "");
                ed.commit();

            }
        });
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_txtView.getText().toString().equalsIgnoreCase("Start")) {
                    Toast.makeText(RequestedActivity.this, "please select start date", Toast.LENGTH_SHORT).show();

                } else if (no_of_days_txtView.getText().toString().length() == 0) {
                    Toast.makeText(RequestedActivity.this, "please enter number of days", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("dada arry", dateArray.toString());
                    Log.e("dada arry", demolist.toString());
                    Log.e("dada arry", list.toString());

                    global.setNumberOfDay(no_of_days_txtView.getText().toString().split(" ")[0]);
                    global.setDateType("repeated");
                    ed.putString(GlobalConstants.NUMBER_OF_DAY, global.getNumberOfDay());
                    ed.putString("date type", global.getDateType());
                    Gson gson = new Gson();
                    String json = gson.toJson(dateArray);
                    ed.putString(GlobalConstants.DATE_DATA, json);
                    Gson gson1 = new Gson();
                    String json1 = gson1.toJson(demolist);
                    ed.putString("demoList", json1);

                    ed.putString(GlobalConstants.EVENT_START_DATE, start_txtView.getText().toString());
                    ed.putString(GlobalConstants.EVENT_END_DATE, end_txtView.getText().toString());
                    ed.commit();
                    finish();
                }
            }
        });
        no_of_days_txtView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {

                        case KeyEvent.KEYCODE_ENTER:
                            if (no_of_days_txtView.getText().toString().length() > 0) {
                                calendarView.setVisibility(View.VISIBLE);
                                submit_button.setVisibility(View.VISIBLE);
                                clear.setVisibility(View.VISIBLE);
                                start_end_layout.setVisibility(View.VISIBLE);
                                no_of_days_txtView.setText(no_of_days_txtView.getText() + " days");
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        no_of_days_txtView.setOnTouchListener(this);
        if (sp.getString("date type", "").equalsIgnoreCase("repeated")) {
            if (!sp.getString(GlobalConstants.DATE_DATA, "").equalsIgnoreCase("")) {
                no_of_days_txtView.setText(sp.getString(GlobalConstants.NUMBER_OF_DAY, "") + " days");
                start_txtView.setText(sp.getString(GlobalConstants.EVENT_START_DATE, ""));
                end_txtView.setText(sp.getString(GlobalConstants.EVENT_END_DATE, ""));

                demolist = loadSharedPreferencesLogList();
                list = loadSharedPreferencesLogList();
                dateArray=dateList();
                calendarDays = list;

                calendarView.addDecorators(new RequestedActivity.EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));
                DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");


                Date sDate = null;

                try {
                    sDate = inputFormat.parse(start_txtView.getText().toString());


                } catch (ParseException e) {

                }

                calendarView.setCurrentDate(sDate);
                i = i + 1;
                no_of_days_txtView.setSelection(sp.getString(GlobalConstants.NUMBER_OF_DAY, "").length() + 5);

            }
        }
        if (no_of_days_txtView.getText().length() != 0) {
            calendarView.setVisibility(View.VISIBLE);
            submit_button.setVisibility(View.VISIBLE);
            start_end_layout.setVisibility(View.VISIBLE);
            clear.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<CalendarDay> loadSharedPreferencesLogList() {
        ArrayList<CalendarDay> callLog = new ArrayList<CalendarDay>();

        Gson gson = new Gson();
        String json = sp.getString("demoList", "");
        if (json.isEmpty()) {
            callLog = new ArrayList<CalendarDay>();
        } else {
            Type type = new TypeToken<List<CalendarDay>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }
    public ArrayList<HashMap<String, String>> dateList() {
        ArrayList<HashMap<String, String>> callLog = new ArrayList<HashMap<String, String>>();

        Gson gson = new Gson();
        String json = sp.getString(GlobalConstants.DATE_DATA, "");
        if (json.isEmpty()) {
            callLog = new ArrayList<HashMap<String, String>>();
        } else {
            Type type = new TypeToken<List<HashMap<String, String>>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if (no_of_days_txtView.getText().length() > 0) {
            if (i == 0) {
                start_txtView.setText(getSelectedDatesString());


                end_txtView.setText(formatdate2(end_date));

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

    public String formatdate2(String fdate) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat d = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }

    private String getSelectedDatesString() {
        if (list.size() == 0) {
            Log.e("date type", "0");
            date1 = calendarView.getSelectedDate();
            int valye = Integer.parseInt(no_of_days_txtView.getText().toString().split(" ")[0]);
            //calendarView.setSelectedDate(incrementDateByOne(new Date(FORMATTER.format(date.getDate()).toString())));
            for (int i = 0; i < valye; i++) {
                CalendarDay date = new CalendarDay(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), i));
                list.add(date);
            }
            calendarDays = list;
            demolist.addAll(list);
            calendarView.addDecorators(new RequestedActivity.EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));

            start_date = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            String year = start_date.split("-")[0];
            String months = String.valueOf(Integer.parseInt(start_date.split("-")[1]) + 1);
            String date = start_date.split("-")[2];
            start_date = year + "-" + months + "-" + date;
            global.setEvent_start_date(start_date);
            end_date = list.get(valye - 1).toString().substring(12, list.get(valye - 1).toString().length() - 1);
            String year_end = end_date.split("-")[0];
            String months_end = String.valueOf(Integer.parseInt(end_date.split("-")[1]) + 1);
            String date_end = end_date.split("-")[2];
            end_date = year_end + "-" + months_end + "-" + date_end;


            map = new HashMap<>();
            map.put(GlobalConstants.EVENT_START_DATE, start_date);
            map.put(GlobalConstants.EVENT_END_DATE, end_date);
            dateArray.add(map);
            if (date1 == null) {
                return "No Selection";
            }
            global.setDateArray(dateArray);
        } else {
            Log.e("date type", "1");

            list.clear();
            calendarDays.clear();
            date1 = calendarView.getSelectedDate();
            //calendarView.setSelectedDate(incrementDateByOne(new Date(FORMATTER.format(date.getDate()).toString())));
            int valye = Integer.parseInt(no_of_days_txtView.getText().toString().split(" ")[0]);
            for (int i = 0; i < valye; i++) {
                CalendarDay date = new CalendarDay(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), i));
                list.add(date);
            }
            calendarDays = list;
            demolist.addAll(list);
            calendarView.addDecorators(new RequestedActivity.EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));

            start_date = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            String year = start_date.split("-")[0];
            String months = String.valueOf(Integer.parseInt(start_date.split("-")[1]) + 1);
            String date = start_date.split("-")[2];
            start_date = year + "-" + months + "-" + date;

            end_date = list.get(valye - 1).toString().substring(12, list.get(valye - 1).toString().length() - 1);
            String year_end = end_date.split("-")[0];
            String months_end = String.valueOf(Integer.parseInt(end_date.split("-")[1]) + 1);
            String date_end = end_date.split("-")[2];
            end_date = year_end + "-" + months_end + "-" + date_end;
            map = new HashMap<>();
            map.put(GlobalConstants.EVENT_START_DATE, start_date);
            map.put(GlobalConstants.EVENT_END_DATE, end_date);
            dateArray.add(map);
            if (date1 == null) {
                return "No Selection";
            }
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
        start_txtView.setText("Start");
        end_txtView.setText("End");
        i = 0;
        calendarView.removeDecorators();
        list.clear();
        calendarDays.clear();
        demolist.clear();
        no_of_days_txtView.setText("");
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

    @Override
    public void onBackPressed() {

    }
}
