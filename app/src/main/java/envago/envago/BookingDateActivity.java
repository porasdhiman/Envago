package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by vikas on 03-01-2017.
 */

public class BookingDateActivity extends Activity implements View.OnClickListener {
    RelativeLayout one_time_advanture_layout,full_session_layout,repeated_dates_adventure_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.booking_date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        one_time_advanture_layout=(RelativeLayout)findViewById(R.id.one_time_advanture_layout);
        full_session_layout=(RelativeLayout)findViewById(R.id.full_season_layout);
        repeated_dates_adventure_layout=(RelativeLayout)findViewById(R.id.repeated_dates_adventure_layout);
        one_time_advanture_layout.setOnClickListener(this);
        full_session_layout.setOnClickListener(this);
        repeated_dates_adventure_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.one_time_advanture_layout:
                Intent i=new Intent(this,OneTimeAdvantureActivity.class);
                startActivity(i);
                break;
            case R.id.full_season_layout:
                Intent j=new Intent(this,FullSessionActivity.class);
                startActivity(j);
                break;
            case R.id.repeated_dates_adventure_layout:
                Intent k=new Intent(this,RequestedActivity.class);
                startActivity(k);
                break;
        }
    }
}
