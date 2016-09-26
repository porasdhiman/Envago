package envago.envago;

import android.os.Bundle;

/**
 * Created by vikas on 21-09-2016.
 */
public class HomeActivity extends ActivityInTab {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        navigateTo(new HomeFragment(),0);
    }
}

