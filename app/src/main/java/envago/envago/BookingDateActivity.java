package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vikas on 03-01-2017.
 */

public class BookingDateActivity extends Activity implements View.OnClickListener {
    RelativeLayout one_time_advanture_layout,full_session_layout,repeated_dates_adventure_layout;
    Global global;
    ImageView back_button_create;
    TextView repete_txt,daily_txt,one_time_txt;
    SharedPreferences sp;
    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};
    LinearLayout main_layout;
TextView date_txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.booking_date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp=getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        global=(Global)getApplicationContext();
        main_layout=(LinearLayout)findViewById(R.id.main_layout);
        Fonts.overrideFonts(this,main_layout);
        date_txtView=(TextView)findViewById(R.id.date_txtView);
        Fonts.overrideFonts1(this,date_txtView);
        one_time_advanture_layout=(RelativeLayout)findViewById(R.id.one_time_advanture_layout);
        full_session_layout=(RelativeLayout)findViewById(R.id.full_season_layout);
        repeated_dates_adventure_layout=(RelativeLayout)findViewById(R.id.repeated_dates_adventure_layout);
        one_time_advanture_layout.setOnClickListener(this);
        full_session_layout.setOnClickListener(this);
        repeated_dates_adventure_layout.setOnClickListener(this);
        back_button_create=(ImageView)findViewById(R.id.back_button_create);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        repete_txt=(TextView)findViewById(R.id.repeate_txt);
        daily_txt=(TextView)findViewById(R.id.daily_txt);
        one_time_txt=(TextView)findViewById(R.id.one_time_txt);
        if(sp.getString("date type","").equalsIgnoreCase("one_time")){
            one_time_txt.setVisibility(View.VISIBLE);

            one_time_txt.setText(formatdate2(sp.getString(GlobalConstants.EVENT_START_DATE,""))+" to "+formatdate2(sp.getString(GlobalConstants.EVENT_END_DATE,""))+" ("+sp.getString(GlobalConstants.NUMBER_OF_DAY,"")+" days)");
        }
        if(sp.getString("date type","").equalsIgnoreCase("repeated")){
            repete_txt.setVisibility(View.VISIBLE);

            repete_txt.setText(sp.getString(GlobalConstants.EVENT_START_DATE,"")+" to "+sp.getString(GlobalConstants.EVENT_END_DATE,"")+" ("+sp.getString(GlobalConstants.NUMBER_OF_DAY,"")+" days)");
        }
        if(sp.getString("date type","").equalsIgnoreCase("full_season")){
            daily_txt.setVisibility(View.VISIBLE);

            daily_txt.setText(formatdate2(sp.getString(GlobalConstants.EVENT_START_DATE,""))+" to "+formatdate2(sp.getString(GlobalConstants.EVENT_END_DATE,"")));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.one_time_advanture_layout:
                Intent i=new Intent(this,OneTimeAdvantureActivity.class);
                startActivity(i);

                finish();
                break;
            case R.id.full_season_layout:
                Intent j=new Intent(this,FullSessionActivity.class);
                startActivity(j);

                finish();
                break;
            case R.id.repeated_dates_adventure_layout:
                Intent k=new Intent(this,RequestedActivity.class);
                startActivity(k);

                finish();
                break;
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
}
