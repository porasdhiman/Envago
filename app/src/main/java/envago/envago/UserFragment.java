package envago.envago;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by vikas on 21-09-2016.
 */
public class UserFragment extends Fragment {

    ImageView map_button;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.profile, container, false);
        map_button = (ImageView)v.findViewById(R.id.map_button);

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.card_flip_right_out, R.anim.card_flip_left_in);
            }
        });


        return v;
    }
}
