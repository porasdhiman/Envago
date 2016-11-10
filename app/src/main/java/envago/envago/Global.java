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

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    String event_name;

    public String getEvent_start_date() {
        return event_start_date;
    }

    public void setEvent_start_date(String event_start_date) {
        this.event_start_date = event_start_date;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public void setEvent_end_date(String event_end_date) {
        this.event_end_date = event_end_date;
    }

    public String getEvent_loc() {
        return event_loc;
    }

    public void setEvent_loc(String event_loc) {
        this.event_loc = event_loc;
    }

    String event_start_date;
    String event_end_date;
    String event_loc;

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    String event_time;

    public String getEvent_price() {
        return event_price;
    }

    public void setEvent_price(String event_price) {
        this.event_price = event_price;
    }

    String event_price;
    @Override
    public void onCreate() {
        super.onCreate();

        Font.overrideFont(getApplicationContext(), "SERIF", "fonts/avenir_book.ttf");

    }

}
