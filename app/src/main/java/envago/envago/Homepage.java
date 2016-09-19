package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

/**
 * Created by jhang on 9/19/2016.
 */
public class Homepage extends Activity {

   // ScrollingTabContainerView tabs;
    ListView categories;
    public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer, R.drawable.all};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.homepage_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }


        categories = (ListView) findViewById(R.id.main_list);
      //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        categories.setAdapter(new CategoriesAdapter(getApplicationContext(), images));




    }
}