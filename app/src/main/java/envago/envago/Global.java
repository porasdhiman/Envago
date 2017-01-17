package envago.envago;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

    String event_start_date="";
    String event_end_date="";
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

    public ArrayList<HashMap<String, String>> getListing() {
        return listing;
    }

    public void setListing(ArrayList<HashMap<String, String>> listing) {
        this.listing = listing;
    }

    ArrayList<HashMap<String, String>> listing=new ArrayList<HashMap<String,String>>();

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    String event_id;

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getStarting_lat() {
        return starting_lat;
    }

    public void setStarting_lat(String starting_lat) {
        this.starting_lat = starting_lat;
    }

    public String getStarting_lng() {
        return starting_lng;
    }

    public void setStarting_lng(String starting_lng) {
        this.starting_lng = starting_lng;
    }

    String startingPoint="",starting_lat="",starting_lng="";

    public String getaPoint() {
        return aPoint;
    }

    public void setaPoint(String aPoint) {
        this.aPoint = aPoint;
    }

    public String getA_lat() {
        return a_lat;
    }

    public void setA_lat(String a_lat) {
        this.a_lat = a_lat;
    }

    public String getA_lng() {
        return a_lng;
    }

    public void setA_lng(String a_lng) {
        this.a_lng = a_lng;
    }
    String wPoint,w_lat,w_lng;
    String aPoint="",a_lat="",a_lng="";

    public String getwPoint() {
        return wPoint;
    }

    public void setwPoint(String wPoint) {
        this.wPoint = wPoint;
    }

    public String getW_lat() {
        return w_lat;
    }

    public void setW_lat(String w_lat) {
        this.w_lat = w_lat;
    }

    public String getW_lng() {
        return w_lng;
    }

    public void setW_lng(String w_lng) {
        this.w_lng = w_lng;
    }

    public String getbPoint() {
        return bPoint;
    }

    public void setbPoint(String bPoint) {
        this.bPoint = bPoint;
    }

    public String getB_lat() {
        return b_lat;
    }

    public void setB_lat(String b_lat) {
        this.b_lat = b_lat;
    }

    public String getB_lng() {
        return b_lng;
    }

    public void setB_lng(String b_lng) {
        this.b_lng = b_lng;
    }

    public String getcPoint() {
        return cPoint;
    }

    public void setcPoint(String cPoint) {
        this.cPoint = cPoint;
    }

    public String getC_lat() {
        return c_lat;
    }

    public void setC_lat(String c_lat) {
        this.c_lat = c_lat;
    }

    public String getC_lng() {
        return c_lng;
    }

    public void setC_lng(String c_lng) {
        this.c_lng = c_lng;
    }

    String bPoint="",b_lat="",b_lng="";
    String cPoint="",c_lat="",c_lng="";

    public ArrayList<String> getListImg() {
        return listImg;
    }

    public void setListImg(ArrayList<String> listImg) {
        this.listImg = listImg;
    }

    ArrayList<String> listImg=new ArrayList<>();
    String descriptionString;

    public String getWhtsicludedString() {
        return whtsicludedString;
    }

    public void setWhtsicludedString(String whtsicludedString) {
        this.whtsicludedString = whtsicludedString;
    }

    public String getDescriptionString() {
        return descriptionString;
    }

    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    String whtsicludedString;

    public String getEvent_level() {
        return event_level;
    }

    public void setEvent_level(String event_level) {
        this.event_level = event_level;
    }

    String event_level;

    public String getEvent_cat_id() {
        return event_cat_id;
    }

    public void setEvent_cat_id(String event_cat_id) {
        this.event_cat_id = event_cat_id;
    }

    String event_cat_id;

    public String getEvent_meetin_point() {
        return event_meetin_point;
    }

    public void setEvent_meetin_point(String event_meetin_point) {
        this.event_meetin_point = event_meetin_point;
    }

    String event_meetin_point;

    public String getEvent_meeting_lat() {
        return event_meeting_lat;
    }

    public void setEvent_meeting_lat(String event_meeting_lat) {
        this.event_meeting_lat = event_meeting_lat;
    }

    public String getEvent_meeting_lng() {
        return event_meeting_lng;
    }

    public void setEvent_meeting_lng(String event_meeting_lng) {
        this.event_meeting_lng = event_meeting_lng;
    }

    String event_meeting_lat;
    String event_meeting_lng;

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    String event_description="false";

    public String getEvent_place() {
        return event_place;
    }

    public void setEvent_place(String event_place) {
        this.event_place = event_place;
    }

    String event_place;

    public String getEvent_criteria() {
        return event_criteria;
    }

    public void setEvent_criteria(String event_criteria) {
        this.event_criteria = event_criteria;
    }

    String event_criteria;

    public String getEvent_disclaimer() {
        return event_disclaimer;
    }

    public void setEvent_disclaimer(String event_disclaimer) {
        this.event_disclaimer = event_disclaimer;
    }

    String event_disclaimer;

    public int getTransportataion() {
        return transportataion;
    }

    public void setTransportataion(int transportataion) {
        this.transportataion = transportataion;
    }

    public int getFlight() {
        return flight;
    }

    public void setFlight(int flight) {
        this.flight = flight;
    }

    public int getAccomodation() {
        return accomodation;
    }

    public void setAccomodation(int accomodation) {
        this.accomodation = accomodation;
    }

    public int getTent() {
        return tent;
    }

    public void setTent(int tent) {
        this.tent = tent;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

    int transportataion=0,flight=0,accomodation=0,tent=0,gear=0,meal=0;

    public ArrayList<HashMap<String, String>> getCountryList() {
        return countryList;
    }

    public void setCountryList(ArrayList<HashMap<String, String>> countryList) {
        this.countryList = countryList;
    }

    ArrayList<HashMap<String,String>> countryList=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();

        Font.overrideFont(getApplicationContext(), "SERIF", "fonts/avenir_book.ttf");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(1500000) // 1.5 Mb
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .enableLogging() // Not necessary in common
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

}
