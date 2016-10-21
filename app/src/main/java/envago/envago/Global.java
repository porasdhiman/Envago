package envago.envago;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

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

    String Lat="";

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    String Long="";

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    String isVerified;

    public ArrayList<HashMap<String, String>> getEvent_list() {
        return event_list;
    }

    public void setEvent_list(ArrayList<HashMap<String, String>> event_list) {
        this.event_list = event_list;
    }

    ArrayList<HashMap<String,String>> event_list=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        Font.overrideFont(getApplicationContext(), "SERIF", "fonts/avenir_book.ttf");

    }

}
