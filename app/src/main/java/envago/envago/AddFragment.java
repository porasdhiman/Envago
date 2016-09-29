package envago.envago;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by vikas on 21-09-2016.
 */
public class AddFragment extends Fragment {

    RelativeLayout call_all, createAdventures;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.add_layout, container, false);


        call_all=(RelativeLayout)v.findViewById(R.id.call_all_layout);
        createAdventures=(RelativeLayout)v.findViewById(R.id.advanture_layout);

        createAdventures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CreateAdventuresActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

        call_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CallVolunteers.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

        return v;
    }

}
