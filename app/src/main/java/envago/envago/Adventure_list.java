package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jhang on 9/25/2016.
 */
public class Adventure_list extends Activity {

    ListView ad_items;
    public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer, R.drawable.all};
    TextView headtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventure_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        headtext=(TextView)findViewById(R.id.header_text);

        headtext.setText("TITLE");
        ad_items = (ListView)findViewById(R.id.ad_list);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        ad_items.setAdapter(new Adventure_list_adapter(getApplicationContext(), images));

    }
}
