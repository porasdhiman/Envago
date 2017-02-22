package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vikas on 03-01-2017.
 */

public class OneTimeAdvantureActivity extends Activity implements OnDateSelectedListener, OnMonthChangedListener, View.OnTouchListener {
    MaterialCalendarView calendarView;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    TextView select_date_txtView, end_date_txtView;
    Button submit_button;
    Global global;
    List<CalendarDay> list = new ArrayList<CalendarDay>();
    CalendarDay date1;
    String start_date, end_date;
    ArrayList<String> sharedData;

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
    ImageView back_button_create;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
RelativeLayout start_end_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.one_time_advanture_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp = getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed = sp.edit();
        global = (Global) getApplicationContext();
        back_button_create = (ImageView) findViewById(R.id.back_button_create);
        of_days_txtView = (EditText) findViewById(R.id.no_of_days_txtView);
        start_end_layout=(RelativeLayout)findViewById(R.id.start_end_layout);
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


        select_date_txtView = (TextView) findViewById(R.id.select_date_txtView);
        end_date_txtView = (TextView) findViewById(R.id.end_date_view);
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (of_days_txtView.getText().toString().length() == 0) {
                    Toast.makeText(OneTimeAdvantureActivity.this, "Please enter number of days", Toast.LENGTH_SHORT).show();
                }
                else if (select_date_txtView.getText().toString().length() == 0) {
                    Toast.makeText(OneTimeAdvantureActivity.this, "Please enter select date", Toast.LENGTH_SHORT).show();
                }
               else {
                    global.setDateType("one_time");
                    global.setNumberOfDay(of_days_txtView.getText().toString().split(" ")[0]);
                    ed.putString("date type", global.getDateType());
                    ed.putString(GlobalConstants.EVENT_START_DATE, start_date);
                    ed.putString(GlobalConstants.EVENT_END_DATE, end_date);
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    ed.putString(GlobalConstants.DATE_DATA, json);
                    ed.putString(GlobalConstants.NUMBER_OF_DAY, global.getNumberOfDay());
                    ed.commit();

                    finish();
                }

            }
        });
        of_days_txtView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {

                        case KeyEvent.KEYCODE_ENTER:
                            calendarView.setVisibility(View.VISIBLE);
                            submit_button.setVisibility(View.VISIBLE);
                            start_end_layout.setVisibility(View.VISIBLE);
                            of_days_txtView.setText(of_days_txtView.getText()+" days");
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        of_days_txtView.setOnTouchListener(this);
        if (sp.getString("date type", "").equalsIgnoreCase("one_time")) {


            of_days_txtView.setText(sp.getString(GlobalConstants.NUMBER_OF_DAY, "")+" days");

            list = loadSharedPreferencesLogList();


            calendarDays = list;
            calendarView.addDecorators(new EventDecorator(getResources().getColor(R.color.textcolor), calendarDays));

            select_date_txtView.setText(formatdate2(sp.getString(GlobalConstants.EVENT_START_DATE, "")));
            end_date_txtView.setText(formatdate2(sp.getString(GlobalConstants.EVENT_END_DATE, "")));
            DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");


            Date sDate=null;

            try {
                sDate = inputFormat.parse(select_date_txtView.getText().toString());



            } catch (ParseException e) {

            }

            calendarView.setCurrentDate(sDate);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        if (of_days_txtView.getText().length() != 0) {
            calendarView.setVisibility(View.VISIBLE);
            submit_button.setVisibility(View.VISIBLE);
            start_end_layout.setVisibility(View.VISIBLE);

        }
        of_days_txtView.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();
                if(x.startsWith("0")) {
                    of_days_txtView.setText("1");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        // Toast.makeText(OneTimeAdvantureActivity.this,getSelectedDatesString(),Toast.LENGTH_SHORT).show();
        if (of_days_txtView.getText().length() > 0) {
            select_date_txtView.setText(getSelectedDatesString());


            Log.e("select Date", formatdate2(end_date));




            end_date_txtView.setText(formatdate2(end_date));
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        } else {
            Toast.makeText(OneTimeAdvantureActivity.this, "Please enter number of days", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private String getSelectedDatesString() {
        if (list.size() == 0) {
            date1 = calendarView.getSelectedDate();

            Log.e("share date array", date1.toString());
            int valye = Integer.parseInt(of_days_txtView.getText().toString().split(" ")[0]);
            //calendarView.setSelectedDate(incrementDateByOne(new Date(FORMATTER.format(date.getDate()).toString())));
            for (int i = 0; i < valye; i++) {

                CalendarDay date = new CalendarDay(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), i));
                list.add(date);
            }
            calendarDays = list;
            Log.e("share date array", list.toString());

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
            int valye = Integer.parseInt(of_days_txtView.getText().toString().split(" ")[0]);
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
//end_date_txtView.setText(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), Integer.parseInt(of_days_txtView.getText().toString())).toString());
        return FORMATTER.format(date1.getDate());
    }

    public ArrayList<CalendarDay> loadSharedPreferencesLogList() {
        ArrayList<CalendarDay> callLog = new ArrayList<CalendarDay>();

        Gson gson = new Gson();
        String json = sp.getString(GlobalConstants.DATE_DATA, "");
        if (json.isEmpty()) {
            callLog = new ArrayList<CalendarDay>();
        } else {
            Type type = new TypeToken<List<CalendarDay>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    public Date incrementDateByOne(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        Date nextDate = c.getTime();
        return nextDate;
    }

    public Date DateByOne(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 0);
        Date nextDate = c.getTime();
        return nextDate;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        of_days_txtView.setText("");
        calendarView.removeDecorators();
        select_date_txtView.setText("");
        end_date_txtView.setText("");
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("OneTimeAdvanture Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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

