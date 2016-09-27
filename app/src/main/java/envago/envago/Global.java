package envago.envago;

import android.app.Application;

/**
 * Created by jhang on 9/27/2016.
 */
public class Global extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Font.overrideFont(getApplicationContext(), "SERIF", "assets/fonts/avenir_book.ttf");

    }

}
