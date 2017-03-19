package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

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

import static envago.envago.R.id.no_of_days_txtView;

/**
 * Created by worksdelight on 31/01/17.
 */

public class OpenFullsessionActivity extends Activity implements OnDateSelectedListener, OnMonthChangedListener{
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    TextView no_of_days;
    TextView session_spinner, year_spinner;
    String year;
    int j;
    ArrayList<String> list_date = new ArrayList<>();
    String startDate_arr[] = {" ", "01-01", "03-15", "06-15", "08-02", "10-02"};
    String endDate_arr[] = {" ", "03-14", "06-14", "08-01", "10-01", "12-31"};
    String catgory = "", yearType = "";
    Global global;
    int pos;
    Button submit_button;
    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};
    TextView clear;
MaterialCalendarView calendarView;
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
    String start_date="", end_date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.open_session_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        back_button_create = (ImageView) findViewById(R.id.back_button_create);
        calendarView=(MaterialCalendarView)findViewById(R.id.calendarView);


        if(dateMatchMethod(global.getEvent_start_date())){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");
            String dateafter = dateFormat.format(c.getTime());


            String formattedDate = dateafter;

            String formattedDate1 = global.getEvent_end_date();

            String date = formattedDate.split("-")[2];
            String month = formattedDate.split("-")[1];
            String year = formattedDate.split("-")[0];
            String date1 = formattedDate1.split("-")[2];
            String month1 = formattedDate1.split("-")[1];
            String year1 = formattedDate1.split("-")[0];
            // Toast.makeText(this,date+"-"+month+"-"+year,Toast.LENGTH_SHORT).show();
            calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
            calendarView.state().edit()

                    .setMinimumDate(CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date)))
                    .setMaximumDate(CalendarDay.from(Integer.parseInt(year1), Integer.parseInt(month1) - 1, Integer.parseInt(date1)))

                    .commit();
        }else{


            String formattedDate = global.getEvent_start_date();

            String formattedDate1 = global.getEvent_end_date();

            String date = formattedDate.split("-")[2];
            String month = formattedDate.split("-")[1];
            String year = formattedDate.split("-")[0];
            String date1 = formattedDate1.split("-")[2];
            String month1 = formattedDate1.split("-")[1];
            String year1 = formattedDate1.split("-")[0];
            // Toast.makeText(this,date+"-"+month+"-"+year,Toast.LENGTH_SHORT).show();
            calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
            calendarView.state().edit()

                    .setMinimumDate(CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date)))
                    .setMaximumDate(CalendarDay.from(Integer.parseInt(year1), Integer.parseInt(month1) - 1, Integer.parseInt(date1)))

                    .commit();
        }
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
      //  clear = (TextView) findViewById(R.id.clear);
        submit_button = (Button) findViewById(R.id.submit_button);
        no_of_days = (TextView) findViewById(no_of_days_txtView);


        session_spinner = (TextView) findViewById(R.id.session_spinner);
        year_spinner = (TextView) findViewById(R.id.year_spinner);
        session_spinner.setText(formatdate(global.getEvent_start_date()));
        year_spinner.setText(formatdate(global.getEvent_end_date()));
        no_of_days.setText(getIntent().getExtras().getString(GlobalConstants.NUMBER_OF_DAY)+" days");
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

       /* clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendarView.removeDecorators();
                list.clear();
                calendarDays.clear();
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
            }
        });*/

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!start_date.equalsIgnoreCase("") ) {
                    global.getBookdateArray().get(0).put(GlobalConstants.EVENT_START_DATE, start_date);
                    global.getBookdateArray().get(0).put(GlobalConstants.EVENT_END_DATE, end_date);
                    Intent i = new Intent(OpenFullsessionActivity.this, ConfirmDetailsActivity.class);
                    i.putExtra(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                    i.putExtra(GlobalConstants.remaining_places, global.getBookdateArray().get(0).get(GlobalConstants.NUMBER_OF_DAY));
                    i.putExtra("pos", String.valueOf(0));
                    startActivity(i);
                }
            }
        });


    }
    public String formatdate(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat d= new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {

        }
        return  datetime;


    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
       getSelectedDatesString();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        //Toast.makeText(OpenFullsessionActivity.this,formatdate(start_date),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
    private String getSelectedDatesString() {

        if (list.size() == 0) {
            date1 = calendarView.getSelectedDate();

            Log.e("share date array", date1.toString());
            int valye = Integer.parseInt(no_of_days.getText().toString().split(" ")[0]);
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
           // global.setEvent_start_date(start_date);
            end_date = list.get(valye - 1).toString().substring(12, list.get(valye - 1).toString().length() - 1);
            String year_end = end_date.split("-")[0];
            String months_end = String.valueOf(Integer.parseInt(end_date.split("-")[1]) + 1);
            String date_end = end_date.split("-")[2];
            end_date = year_end + "-" + months_end + "-" + date_end;
            Log.e("end date", end_date);
           // global.setEvent_end_date(end_date);

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
            int valye = Integer.parseInt(no_of_days.getText().toString().split(" ")[0]);
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
           // global.setEvent_start_date(start_date);
            end_date = list.get(valye - 1).toString().substring(12, list.get(valye - 1).toString().length() - 1);
            String year_end = end_date.split("-")[0];
            String months_end = String.valueOf(Integer.parseInt(end_date.split("-")[1]) + 1);
            String date_end = end_date.split("-")[2];
            end_date = year_end + "-" + months_end + "-" + date_end;
            Log.e("end date", end_date);
            //global.setEvent_end_date(end_date);

            if (date1 == null) {
                return "No Selection";
            }
            Log.e("calender day value1", calendarDays.toString());
        }
//end_date_txtView.setText(incrementDateByOne(new Date(FORMATTER.format(date1.getDate()).toString()), Integer.parseInt(of_days_txtView.getText().toString())).toString());
        return FORMATTER.format(date1.getDate());
    }
    boolean dateMatchMethod(String selectedDate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String dateafter = dateFormat.format(c.getTime());

        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(dateafter);
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
    public Date incrementDateByOne(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        Date nextDate = c.getTime();
        return nextDate;
    }


}