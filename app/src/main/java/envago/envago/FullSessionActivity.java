package envago.envago;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vikas on 03-01-2017.
 */

public class FullSessionActivity extends Activity {
    TextView no_of_days;
    TextView session_spinner, year_spinner;

    int j;
    ArrayList<String> list = new ArrayList<>();
    StringBuilder startDate;
    StringBuilder endDate;
    String catgory = "", yearType = "";
    Global global;
    int pos;
    Button submit_button;
    ImageView back_button_create;
    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day, i = 0;
    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.full_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        sp = getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed = sp.edit();
        global = (Global) getApplicationContext();
        back_button_create = (ImageView) findViewById(R.id.back_button_create);
        submit_button = (Button) findViewById(R.id.submit_button);
        no_of_days = (TextView) findViewById(R.id.no_of_days_txtView);
        // year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        session_spinner = (TextView) findViewById(R.id.session_spinner);
        year_spinner = (TextView) findViewById(R.id.year_spinner);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        session_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(year, month + 1, day);
                showDialog(999);
                i = 0;
            }
        });
        year_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate1(year, month + 1, day);
                showDialog(1111);
                i = 1;
            }
        });
        /*j = Integer.parseInt(year);
        list.add("year");
        for (int i = 0; i < 17; i++) {

            list.add(String.valueOf(j));
            j = ++j;

        }*/

        /*ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(this,
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
        });*/
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (session_spinner.getText().length()==0) {
                    Toast.makeText(FullSessionActivity.this, "Please enter start date", Toast.LENGTH_SHORT).show();
                } else  if (year_spinner.getText().length()==0) {
                     Toast.makeText(FullSessionActivity.this, "Please enter end date", Toast.LENGTH_SHORT).show();
                } else {
                    global.setEvent_start_date(startDate.toString());
                    global.setEvent_end_date(endDate.toString());
                    global.setSessionType(catgory);
                    global.setDateType("full_season");
                    global.setNumberOfDay(no_of_days.getText().toString());
                    ed.putString("date type", global.getDateType());
                    ed.putString(GlobalConstants.EVENT_START_DATE, startDate.toString());
                    ed.putString(GlobalConstants.EVENT_END_DATE, endDate.toString());
                     ed.commit();
                    finish();
                }
            }
        });
        if (sp.getString("date type", "").equalsIgnoreCase("full_season")) {


            session_spinner.setText(formatdate2(sp.getString(GlobalConstants.EVENT_START_DATE,"")));
            year_spinner.setText(formatdate2(sp.getString(GlobalConstants.EVENT_END_DATE,"")));

        }

    }
    public void select(String date){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String dateafter = dateFormat.format(c.getTime());

        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(dateafter);
            eventDate = dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (eventDate.before(currentDate)) {
            Toast.makeText(FullSessionActivity.this,"Date is not valid",Toast.LENGTH_SHORT).show();
        } else {

            session_spinner.setText(formatdate2(date));
        }
    }
    public void select1(String stardate,String enddate){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String dateafter = dateFormat.format(c.getTime());

        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(stardate);
            eventDate = dateFormat.parse(enddate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (eventDate.before(currentDate)) {
            Toast.makeText(FullSessionActivity.this,"Date is not valid",Toast.LENGTH_SHORT).show();
        } else {
            year_spinner.setText(formatdate2(enddate));
        }
    }
    public String formatdate2(String fdate)
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
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 1111) {
            return new DatePickerDialog(this,
                    myDateListener1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener1 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate1(arg1, arg2 + 1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    public void showDate(int year, int month, int day) {


        startDate = new StringBuilder().append(year).append("-").append(month).append("-").append(day);
        select(startDate.toString());

    }

    public void showDate1(int year, int month, int day) {


        endDate = new StringBuilder().append(year).append("-").append(month).append("-").append(day);
        select1(session_spinner.getText().toString(),endDate.toString());
    }
}
