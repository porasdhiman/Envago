package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * Created by vikas on 03-01-2017.
 */

public class NewAdvantureForm extends Activity {
    ImageView booking_checkBox,route_checkbox,photo_checkBox,detail_checkBox;
    boolean is_booking,is_route,is_addPhoto,is_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.main_new_advanture_form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        booking_checkBox=(ImageView)findViewById(R.id.booking_checkBox);
        booking_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking_checkBox.setImageResource(R.drawable.selected);
                Intent i=new Intent(NewAdvantureForm.this,BookingDateActivity.class);
                startActivity(i);
            }
        });
        route_checkbox=(ImageView)findViewById(R.id.route_checkbox);
        route_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route_checkbox.setImageResource(R.drawable.selected);
                Intent i=new Intent(NewAdvantureForm.this,StartingRouteActivity.class);
                startActivity(i);
            }
        });
        photo_checkBox=(ImageView)findViewById(R.id.add_photo_checkBox);
        photo_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_checkBox.setImageResource(R.drawable.selected);
                Intent i=new Intent(NewAdvantureForm.this,AddPhotoActivity.class);
                startActivity(i);
            }
        });
        detail_checkBox=(ImageView)findViewById(R.id.detail_checkBox);
        detail_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail_checkBox.setImageResource(R.drawable.selected);
                Intent i=new Intent(NewAdvantureForm.this,StartingRouteActivity.class);
                startActivity(i);
            }
        });
    }
}
