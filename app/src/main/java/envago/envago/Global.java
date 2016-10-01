package envago.envago;

import android.app.Application;

/**
 * Created by jhang on 9/27/2016.
 */
public class Global extends Application {

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    String deviceToken;

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    String Lat;

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    String Long;
    @Override
    public void onCreate() {
        super.onCreate();

        Font.overrideFont(getApplicationContext(), "SERIF", "fonts/avenir_book.ttf");

    }

}
