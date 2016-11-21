package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Volunteers_list extends Activity {

    ListView vol_categories;
    public int[] images = {R.drawable.disaster_pic, R.drawable.refugee_pic, R.drawable.health_pic, R.drawable.education_pic, R.drawable.youth_pic, R.drawable.environmental_pic, R.drawable.all_pic};
    ImageView map_button, back_button;
    TextView main_heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_volunteers_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        back_button = (ImageView) findViewById(R.id.back_button_vol);

        main_heading = (TextView) findViewById(R.id.title_vol);
        main_heading.setText("Go Volunteers");
        map_button = (ImageView) findViewById(R.id.map_button_vol);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Volunteers_list.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.stay, R.anim.rotate);
            }
        });

        vol_categories = (ListView) findViewById(R.id.volunteers_listview);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        vol_categories.setAdapter(new CategoriesAdapter(getApplicationContext(), images));


        vol_categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(Volunteers_list.this,Volunteer_lists.class);


                if (position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5)
                {
                    intent.putExtra("status_vol", "single");

                }
            }
        });


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
