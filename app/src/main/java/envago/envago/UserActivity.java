package envago.envago;

import android.os.Bundle;

/**
 * Created by vikas on 21-09-2016.
 */
public class UserActivity extends ActivityInTab {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        navigateTo(new UserFragment(),0);
    }
}
