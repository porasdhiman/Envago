package envago.envago;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static envago.envago.R.id.no_of_days_txtView;

/**
 * Created by vikas on 03-01-2017.
 */

public class FullSessionActivity extends Activity {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    EditText no_of_days;
    TextView session_spinner, year_spinner;

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
    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    String di,d2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.fill_fullstation_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp = getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed = sp.edit();
        global = (Global) getApplicationContext();
        back_button_create = (ImageView) findViewById(R.id.back_button_create);
       // calendarView=(MaterialCalendarView)findViewById(R.id.calendarView);



        submit_button = (Button) findViewById(R.id.submit_button);
        no_of_days = (EditText) findViewById(no_of_days_txtView);


        session_spinner = (TextView) findViewById(R.id.session_spinner);
        year_spinner = (TextView) findViewById(R.id.year_spinner);


        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(no_of_days.length()==0){
                    Toast.makeText(FullSessionActivity.this,"Please enter number of days adventure",Toast.LENGTH_SHORT).show();
                }else  if(session_spinner.length()==0){
                    Toast.makeText(FullSessionActivity.this,"Please enter start date",Toast.LENGTH_SHORT).show();

                }else  if(year_spinner.length()==0){
                    Toast.makeText(FullSessionActivity.this,"Please enter end date",Toast.LENGTH_SHORT).show();

                }else if(getDaysDifference(new Date(session_spinner.getText().toString()),new Date(year_spinner.getText().toString()))+1<Integer.parseInt(no_of_days.getText().toString().split(" ")[0])){
                    Toast.makeText(FullSessionActivity.this,"Please select date above number of days",Toast.LENGTH_SHORT).show();

                }
                else {
                    ed.putString(GlobalConstants.EVENT_START_DATE, formatdate(session_spinner.getText().toString()));
                    ed.putString(GlobalConstants.EVENT_END_DATE, formatdate(year_spinner.getText().toString()));
                    ed.putString(GlobalConstants.NUMBER_OF_DAY, no_of_days.getText().toString().split(" ")[0]);
                    global.setDateType("full_season");

                    ed.putString("date type", global.getDateType());
                    ed.commit();
                    finish();
                }
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        session_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(session_spinner);
            }
        });
        year_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate1(year_spinner);
            }
        });
        no_of_days.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {

                        case KeyEvent.KEYCODE_ENTER:


                            no_of_days.setText(no_of_days.getText()+" days adventure");
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        no_of_days.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                no_of_days.setText("");
                return false;
            }
        });
        if (sp.getString("date type", "").equalsIgnoreCase("full_season")) {

            no_of_days.setText(sp.getString(GlobalConstants.NUMBER_OF_DAY,"")+" days adventure");
            session_spinner.setText(formatdate3(sp.getString(GlobalConstants.EVENT_START_DATE,"")));
            year_spinner.setText(formatdate3(sp.getString(GlobalConstants.EVENT_END_DATE,"")));
di=session_spinner.getText().toString();
        }
        no_of_days.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();
                if(x.startsWith("0")) {
                    no_of_days.setText("1");
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
    public String formatdate3(String fdate)
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
    public String formatdate1(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat d= new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {

        }
        return  datetime;


    }
    public String formatdate2(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat d= new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {

        }
        return  datetime;


    }
    public static int getDaysDifference(Date fromDate, Date toDate)
    {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }
    @SuppressWarnings("deprecation")
    public void setDate1(View view) {
        showDialog(1000);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }   if (id == 1000) {
            return new DatePickerDialog(this,
                    myDateListener1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener1 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate1(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {



        di=new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year).toString();
        if(dateMatchMethod(di)){
            Toast.makeText(FullSessionActivity.this,"Please select date above current date",Toast.LENGTH_SHORT).show();
        }else{
            session_spinner.setText(formatdate1(di));
        }

    }
    private void showDate1(int year, int month, int day) {
        d2=new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year).toString();
        if(dateMatchMethod1(di,d2)){
            Toast.makeText(FullSessionActivity.this,"Please select date above start date",Toast.LENGTH_SHORT).show();
        }else{
            di=new StringBuilder().append(day).append("-")
                    .append(month).append("-").append(year).toString();
            year_spinner.setText(formatdate1(di));
        }

    }
    boolean dateMatchMethod(String selectedDate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy");
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
    boolean dateMatchMethod1(String selectedDate,String endDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy");


        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(endDate);
            eventDate = dateFormat.parse(selectedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (currentDate.before(eventDate)) {
            return true;
        } else {
            return false;
        }


    }

}
