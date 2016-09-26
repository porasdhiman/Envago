package envago.envago;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by vikas on 21-09-2016.
 */
public class FavoriteFragment extends Fragment {
    ListView ad_items;
    public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer, R.drawable.all};
    TextView headtext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View v = inflater.inflate(R.layout.adventure_list, container, false);

        headtext=(TextView)v.findViewById( R.id.header_text);

        headtext.setText("FAVORITE");

                ad_items = (ListView)v.findViewById(R.id.ad_list);
        //  tabs = (ScrollingTabContainerView)findViewById(R.id.tabs);
        ad_items.setAdapter(new Adventure_list_adapter(getActivity(), images));


        return v;
    }
}
