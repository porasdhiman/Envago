package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
    String start_date, end_date;
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

        String formattedDate = global.getEvent_start_date();
        String formattedDate1 = global.getEvent_end_date();

        String date=formattedDate.split("-")[2];
        String month=formattedDate.split("-")[1];
        String year=formattedDate.split("-")[0];
        String date1=formattedDate1.split("-")[2];
        String month1=formattedDate1.split("-")[1];
        String year1=formattedDate1.split("-")[0];
        // Toast.makeText(this,date+"-"+month+"-"+year,Toast.LENGTH_SHORT).show();
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.state().edit()

                .setMinimumDate(CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(date)))
                .setMaximumDate(CalendarDay.from(Integer.parseInt(year1), Integer.parseInt(month1)-1, Integer.parseInt(date1)))

                .commit();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
      //  clear = (TextView) findViewById(R.id.clear);
        submit_button = (Button) findViewById(R.id.submit_button);
        no_of_days = (TextView) findViewById(no_of_days_txtView);


        session_spinner = (TextView) findViewById(R.id.session_spinner);
        year_spinner = (TextView) findViewById(R.id.year_spinner);
        session_spinner.setText(date+" "+months[Integer.parseInt(month)]+" "+year);
        year_spinner.setText(date1+" "+months[Integer.parseInt(month1)]+" "+year1);
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
                global.getBookdateArray().get(0).put(GlobalConstants.EVENT_START_DATE,formatdate(start_date));
                global.getBookdateArray().get(0).put(GlobalConstants.EVENT_END_DATE,formatdate(start_date));
                Intent i = new Intent(OpenFullsessionActivity.this, ConfirmDetailsActivity.class);
                i.putExtra(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                i.putExtra(GlobalConstants.remaining_places,global.getBookdateArray().get(0).get(GlobalConstants.remaining_places));
                i.putExtra("pos", String.valueOf(0));
                startActivity(i);
            }
        });


    }
    public String formatdate(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");

        SimpleDateFormat d= new SimpleDateFormat("yyyy-MM-dd");
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
       start_date=getSelectedDatesString();
        //Toast.makeText(OpenFullsessionActivity.this,formatdate(start_date),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
    private String getSelectedDatesString() {

            date1 = calendarView.getSelectedDate();

        return FORMATTER.format(date1.getDate());
    }


}