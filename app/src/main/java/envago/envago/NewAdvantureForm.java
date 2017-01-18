package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vikas on 03-01-2017.
 */

public class NewAdvantureForm extends Activity {
    ImageView booking_checkBox, route_checkbox, photo_checkBox, detail_checkBox;
    boolean is_booking, is_route, is_addPhoto, is_detail;
    Global global;
    Button submit_button;
    TextView preview_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.main_new_advanture_form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        global.setEvent_start_date("");
        booking_checkBox = (ImageView) findViewById(R.id.booking_checkBox);
        submit_button = (Button) findViewById(R.id.submit_button_create_advanture);
        preview_button = (TextView) findViewById(R.id.preview_button);
        booking_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, BookingDateActivity.class);
                startActivityForResult(i, 1);
            }
        });
        route_checkbox = (ImageView) findViewById(R.id.route_checkbox);
        route_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, StartingRouteActivity.class);
                startActivityForResult(i, 2);
            }
        });
        photo_checkBox = (ImageView) findViewById(R.id.add_photo_checkBox);
        photo_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, AddPhotoActivity.class);
                startActivityForResult(i, 3);
            }
        });
        detail_checkBox = (ImageView) findViewById(R.id.detail_checkBox);
        detail_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, CreateDetailActivity.class);
                startActivityForResult(i, 4);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!global.getEvent_start_date().equalsIgnoreCase("")) {
                booking_checkBox.setImageResource(R.drawable.selected);
            }
        }
        if (requestCode == 2) {
            if (!global.getStartingPoint().equalsIgnoreCase("")) {
                route_checkbox.setImageResource(R.drawable.selected);
            }
        }
        if (requestCode == 3) {
            if (global.getListImg().size() != 0) {
                photo_checkBox.setImageResource(R.drawable.selected);
            }
        }
        if (requestCode == 4) {
            if (global.getEvent_description().equalsIgnoreCase("true")) {
                detail_checkBox.setImageResource(R.drawable.selected);
            }

        }
        if (!global.getEvent_start_date().equalsIgnoreCase("") && !global.getStartingPoint().equalsIgnoreCase("")
                && global.getListImg().size() != 0 && global.getEvent_description().equalsIgnoreCase("true")) {
            submit_button.setBackgroundResource(R.drawable.red_button_back);
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            preview_button.setTextColor(Color.BLACK);
            preview_button.setBackgroundResource(R.drawable.red_border_button);
            preview_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(NewAdvantureForm.this, PreviewActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}
