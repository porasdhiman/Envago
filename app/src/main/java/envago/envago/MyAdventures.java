package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jhang on 11/4/2016.
 */

public class MyAdventures extends Activity implements View.OnTouchListener {

    TextView planning,goingto;
    LinearLayout planning_layout, goingtolayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.myadventures);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        planning=(TextView)findViewById(R.id.planning_text);
        goingto=(TextView)findViewById(R.id.goingto_text);

        planning_layout = (LinearLayout)findViewById(R.id.planning_layout);
        goingtolayout = (LinearLayout)findViewById(R.id.goingto_layout);



        planning.setOnTouchListener(this);
        goingto.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int id = v.getId();

        if (id==R.id.planning_text)
        {
            planning.setBackgroundColor(getResources().getColor(R.color.White));
            planning_layout.setBackgroundColor(getResources().getColor(R.color.White));
            planning.setTextColor(getResources().getColor(R.color.textcolor));

            goingto.setBackgroundColor(getResources().getColor(R.color.transparent));
            goingtolayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            goingto.setTextColor(getResources().getColor(R.color.White));
        }

        if (id==R.id.goingto_text)
        {
            planning.setBackgroundColor(getResources().getColor(R.color.transparent));
            planning_layout.setBackgroundColor(getResources().getColor(R.color.transparent));
            planning.setTextColor(getResources().getColor(R.color.White));

            goingto.setBackgroundColor(getResources().getColor(R.color.White));
            goingtolayout.setBackgroundColor(getResources().getColor(R.color.White));
            goingto.setTextColor(getResources().getColor(R.color.textcolor));
        }

        return false;
    }
}
