package envago.envago;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by vikas on 11-01-2017.
 */

public class SearchByLocationActivity extends Activity {
    public String loc[]={"Albania","Sri Lanka","China","American Samoa","Tonga","Tuvalu","Namibia","Jamaica"};
    ListView search_listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_by_location_layout);
        search_listView=(ListView)findViewById(R.id.search_list);
        search_listView.setAdapter(new ArrayAdapter<String>(this,R.layout.search_list_item,R.id.product_name,loc));
    }
}
