package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vikas on 03-01-2017.
 */

public class NewAdvantureForm extends Activity implements View.OnClickListener {
    ImageView booking_checkBox, route_checkbox, photo_checkBox, detail_checkBox,back_button_create;
    boolean is_booking, is_route, is_addPhoto, is_detail;
    Global global;
    Button submit_button;
    TextView preview_button;
    HttpEntity resEntity;
    String message;
    Dialog dialog2;
SharedPreferences sp;
    RelativeLayout date_layout,detail_layout,add_layout,route_layout;
    LinearLayout main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.main_new_advanture_form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp=getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        global = (Global) getApplicationContext();
        global.setEvent_start_date("");
        main_layout=(LinearLayout)findViewById(R.id.main_layout);
        Fonts.overrideFonts(this,main_layout);
        back_button_create=(ImageView) findViewById(R.id.back_button_create);
        booking_checkBox = (ImageView) findViewById(R.id.booking_checkBox);
        date_layout=(RelativeLayout)findViewById(R.id.date_layout);
        detail_layout=(RelativeLayout)findViewById(R.id.details_layout);
        add_layout=(RelativeLayout)findViewById(R.id.photo_layout);
        route_layout=(RelativeLayout)findViewById(R.id.route_layout);
        date_layout.setOnClickListener(this);
        detail_layout.setOnClickListener(this);
        add_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);


        //submit_button = (Button) findViewById(R.id.submit_button_create_advanture);
        preview_button = (TextView) findViewById(R.id.preview_button);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        booking_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, BookingDateActivity.class);
                startActivityForResult(i, 1);
            }
        });
        route_checkbox = (ImageView) findViewById(R.id.route_checkbox);
        route_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, StartingRouteActivity.class);
                startActivityForResult(i, 2);
            }
        });
        photo_checkBox = (ImageView) findViewById(R.id.add_photo_checkBox);
        photo_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, AddPhotoActivity.class);
                startActivityForResult(i, 3);
            }
        });
        detail_checkBox = (ImageView) findViewById(R.id.detail_checkBox);
        detail_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewAdvantureForm.this, CreateDetailActivity.class);
                startActivityForResult(i, 4);
            }
        });
        if(!sp.getString(GlobalConstants.EVENT_START_DATE,"").equalsIgnoreCase("")){
            booking_checkBox.setImageResource(R.drawable.selected);
        }

        if(!sp.getString(GlobalConstants.VALUE,"").equalsIgnoreCase("")){
            detail_checkBox.setImageResource(R.drawable.selected);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!sp.getString(GlobalConstants.EVENT_START_DATE,"").equalsIgnoreCase("")) {
                booking_checkBox.setImageResource(R.drawable.selected);
            }
        }
        if (requestCode == 2) {
            if (!global.getStartingPoint().equalsIgnoreCase("")) {
                route_checkbox.setImageResource(R.drawable.selected);
            }
        }
        if (requestCode == 3) {
            if (global.getListImg().size() != 0) {
                photo_checkBox.setImageResource(R.drawable.selected);
            }
        }
        if (requestCode == 4) {
            if (sp.getString(GlobalConstants.VALUE,"").equalsIgnoreCase("true")) {
                detail_checkBox.setImageResource(R.drawable.selected);
            }

        }
        if (!sp.getString(GlobalConstants.EVENT_START_DATE,"").equalsIgnoreCase("") && !global.getStartingPoint().equalsIgnoreCase("")
                && global.getListImg().size() != 0 && sp.getString(GlobalConstants.VALUE,"").equalsIgnoreCase("true")) {
            /*submit_button.setBackgroundResource(R.drawable.red_button_back);
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogWindow();
                    new Thread(null, address_request, "")
                            .start();
                }
            });*/
            preview_button.setTextColor(Color.BLACK);
            preview_button.setBackgroundResource(R.drawable.red_border_button);
            preview_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(NewAdvantureForm.this, PreviewActivity.class);
                    startActivity(i);
                }
            });
        }
    }
    //-------------------------------------------------Api method-----------------------

    Runnable address_request = new Runnable() {
        String res = "false";


        @Override
        public void run() {
            try {

                res = doFileUpload();
            } catch (Exception e) {

            }
            Message msg = new Message();
            msg.obj = res;
            address_request_Handler.sendMessage(msg);
        }
    };

    Handler address_request_Handler = new Handler() {
        public void handleMessage(Message msg) {
            String res = (String) msg.obj;
            dialog2.dismiss();
            if (res.equalsIgnoreCase("true")) {
                // terms_dialog.dismiss();
                Toast.makeText(NewAdvantureForm.this, message,
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(NewAdvantureForm.this, message,
                        Toast.LENGTH_SHORT).show();
            }

        }

    };
    public ArrayList<HashMap<String,String>> loadSharedPreferencesLogList() {
        ArrayList<HashMap<String,String>> callLog = new ArrayList<HashMap<String,String>>();

        Gson gson = new Gson();
        String json = sp.getString(GlobalConstants.DATE_DATA, "");
        if (json.isEmpty()) {
            callLog = new ArrayList<HashMap<String,String>>();
        } else {
            Type type = new TypeToken<List<HashMap<String,String>>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }
    // ------------------------------------------------------upload
    // method---------------
    private String doFileUpload() {
        String success = "false";

        String urlString = GlobalConstants.URL;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();

            File file1 = new File(global.getListImg().get(0));
            FileBody bin1 = new FileBody(file1, "image/png");
            reqEntity.addPart("image1", bin1);

            File file2 = new File(global.getListImg().get(1));
            FileBody bin2 = new FileBody(file2, "image/png");
            reqEntity.addPart("image2", bin2);

            Log.e("image params", global.getListImg().get(0) + " " + global.getListImg().get(0));
            reqEntity.addPart(GlobalConstants.USERID, new StringBody(CommonUtils.UserID(this)));


            reqEntity.addPart(GlobalConstants.MAIN_CAT_ID, new StringBody("1"));
            Log.e("main  id", "1");
            reqEntity.addPart(GlobalConstants.EVENT_CAT_ID, new StringBody(sp.getString(GlobalConstants.EVENT_CAT_ID,"")));
            Log.e("sub_cat_id", sp.getString(GlobalConstants.EVENT_CAT_ID,""));
            reqEntity.addPart(GlobalConstants.EVENT_NAME, new StringBody(sp.getString(GlobalConstants.EVENT_NAME,"")));
            Log.e("name", sp.getString(GlobalConstants.EVENT_NAME,""));
            if (global.getDateType().equalsIgnoreCase("one_time")) {
                reqEntity.addPart("event_type", new StringBody(sp.getString("date type","")));
                Log.e("event_type", sp.getString("date type",""));
                reqEntity.addPart("event_no_of_days", new StringBody(sp.getString(GlobalConstants.NUMBER_OF_DAY,"")));
                Log.e("event_no_of_days", sp.getString(GlobalConstants.NUMBER_OF_DAY,""));
                JSONArray installedList = new JSONArray();



                    try {
                        JSONObject installedPackage = new JSONObject();
                        installedPackage.put(GlobalConstants.EVENT_START_DATE, sp.getString(GlobalConstants.EVENT_START_DATE,""));
                        installedPackage.put(GlobalConstants.EVENT_END_DATE, sp.getString(GlobalConstants.EVENT_END_DATE,""));
                        installedList.put(installedPackage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
            } else if (global.getDateType().equalsIgnoreCase("full_season")) {
                reqEntity.addPart("event_type", new StringBody(sp.getString("date type","")));
                Log.e("event_type", sp.getString("date type",""));
                reqEntity.addPart("event_no_of_days", new StringBody(sp.getString(GlobalConstants.NUMBER_OF_DAY,"")));
                Log.e("event_no_of_days", sp.getString(GlobalConstants.NUMBER_OF_DAY,""));

                reqEntity.addPart("event_season", new StringBody(""));
                Log.e("event_season","");
                JSONArray installedList = new JSONArray();



                try {
                    JSONObject installedPackage = new JSONObject();
                    installedPackage.put(GlobalConstants.EVENT_START_DATE, sp.getString(GlobalConstants.EVENT_START_DATE,""));
                    installedPackage.put(GlobalConstants.EVENT_END_DATE, sp.getString(GlobalConstants.EVENT_END_DATE,""));
                    installedList.put(installedPackage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
            } else {
                reqEntity.addPart("event_type", new StringBody(sp.getString("date type","")));
                Log.e("event_type", sp.getString("date type",""));
                reqEntity.addPart("event_no_of_days", new StringBody(sp.getString(GlobalConstants.NUMBER_OF_DAY,"")));
                Log.e("event_no_of_days", sp.getString(GlobalConstants.NUMBER_OF_DAY,""));
global.setDateArray(loadSharedPreferencesLogList());
                JSONArray installedList = new JSONArray();


                for (int i = 0; i < global.getDateArray().size(); i++)
                {
                    try {
                        JSONObject installedPackage = new JSONObject();
                        installedPackage.put(GlobalConstants.EVENT_START_DATE, global.getDateArray().get(i).get(GlobalConstants.EVENT_START_DATE));
                        installedPackage.put(GlobalConstants.EVENT_END_DATE, global.getDateArray().get(i).get(GlobalConstants.EVENT_END_DATE));
                        installedList.put(installedPackage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
               /* reqEntity.addPart("event_dates[0][event_end_date]", new StringBody("2017-10-3"));
                Log.e("event_dates[0][event_end_date]", "2017-10-3");*/
            }


            reqEntity.addPart(GlobalConstants.EVENT_TIME, new StringBody(sp.getString(GlobalConstants.EVENT_TIME,"")));
            Log.e(GlobalConstants.EVENT_TIME, sp.getString(GlobalConstants.EVENT_TIME,""));
            reqEntity.addPart(GlobalConstants.EVENT_LEVEL, new StringBody(sp.getString(GlobalConstants.EVENT_LEVEL,"")));
            Log.e(GlobalConstants.EVENT_LEVEL, sp.getString(GlobalConstants.EVENT_LEVEL,""));
            reqEntity.addPart(GlobalConstants.EVENT_METTING_POINT, new StringBody(sp.getString(GlobalConstants.EVENT_METTING_POINT,"")));
            Log.e(GlobalConstants.EVENT_METTING_POINT, sp.getString(GlobalConstants.EVENT_METTING_POINT,""));
            reqEntity.addPart("meeting_point_latitude", new StringBody(sp.getString(GlobalConstants.EVENT_MEETING_LAT,"")));
            Log.e("meeting_point_latitude", sp.getString(GlobalConstants.EVENT_MEETING_LAT,""));
            reqEntity.addPart("meeting_point_longitude", new StringBody(sp.getString(GlobalConstants.EVENT_MEETING_LNG,"")));
            Log.e("meeting_point_longitude",sp.getString(GlobalConstants.EVENT_MEETING_LNG,""));
            /*reqEntity.addPart("crireria_eligibilty", new StringBody(global.getEvent_criteria()));
            Log.e("crireria_eligibilty", global.getEvent_criteria());*/
            reqEntity.addPart(GlobalConstants.LOCATION, new StringBody(global.getStartingPoint()));
            Log.e(GlobalConstants.LOCATION, global.getStartingPoint());
            reqEntity.addPart(GlobalConstants.LATITUDE, new StringBody(global.getStarting_lat()));
            Log.e(GlobalConstants.LATITUDE, global.getStarting_lat());
            reqEntity.addPart(GlobalConstants.LONGITUDE, new StringBody(global.getStarting_lng()));
            Log.e(GlobalConstants.LONGITUDE, global.getStarting_lng());
            reqEntity.addPart("end_location", new StringBody(global.getwPoint()));
            Log.e("end_location", global.getwPoint());
            reqEntity.addPart("end_latitude", new StringBody(global.getW_lat()));
            Log.e("end_latitude", global.getW_lat());
            reqEntity.addPart("end_longitude", new StringBody(global.getW_lng()));
            Log.e("end_longitude", global.getW_lng());
            reqEntity.addPart("location_1", new StringBody(global.getaPoint()));
            Log.e("location_1", global.getaPoint());
            reqEntity.addPart("loc_1_latitude", new StringBody(global.getA_lat()));
            Log.e("loc_1_latitude", global.getA_lat());
            reqEntity.addPart("loc_1_longitude", new StringBody(global.getA_lng()));
            Log.e("loc_1_longitude", global.getA_lng());
            reqEntity.addPart("location_2", new StringBody(global.getbPoint()));
            Log.e("location_2", global.getbPoint());
            reqEntity.addPart("loc_2_latitude", new StringBody(global.getB_lat()));
            Log.e("loc_2_latitude", global.getB_lat());
            reqEntity.addPart("loc_2_longitude", new StringBody(global.getB_lng()));
            Log.e("loc_2_longitude", global.getB_lng());
            reqEntity.addPart("location_3", new StringBody(global.getcPoint()));
            Log.e("location_3", global.getcPoint());
            reqEntity.addPart("loc_3_latitude", new StringBody(global.getC_lat()));
            Log.e("loc_3_latitude", global.getC_lat());
            reqEntity.addPart("loc_3_longitude", new StringBody(global.getC_lng()));
            Log.e("loc_3_longitude", global.getC_lng());
           /* reqEntity.addPart("location_4", new StringBody(loc4_location));
            Log.e("location_4", loc4_location);
            reqEntity.addPart("loc_4_latitude", new StringBody(loc4_lat));
            Log.e("loc_4_latitude", loc4_lat);
            reqEntity.addPart("loc_4_longitude", new StringBody(loc4_lng));
            Log.e("loc_4_longitude", loc4_lng);*/
            reqEntity.addPart("description", new StringBody(sp.getString(GlobalConstants.EVENT_DESCRIPTION,"")));
            Log.e("description", sp.getString(GlobalConstants.EVENT_DESCRIPTION,""));
            reqEntity.addPart("no_of_places", new StringBody(sp.getString(GlobalConstants.EVENT_PLACE,"")));
            reqEntity.addPart("price", new StringBody(sp.getString(GlobalConstants.EVENT_PRICE,"")));
            Log.e("no_of_places", sp.getString(GlobalConstants.EVENT_PRICE,""));
            Log.e("price", sp.getString(GlobalConstants.EVENT_PRICE,""));
            reqEntity.addPart("whats_included", new StringBody("dddd"));
            Log.e("whats_included", "dddd");
            reqEntity.addPart("meals", new StringBody(sp.getString("meal","0")));
            Log.e("meals", sp.getString("meal","0"));
            reqEntity.addPart("transport", new StringBody(sp.getString("trans","0")));
            Log.e("transport", sp.getString("trans","0"));
            reqEntity.addPart("tent", new StringBody(sp.getString("tent","0")));
            Log.e("tent", String.valueOf(sp.getString("tent","0")));
            reqEntity.addPart("accomodation", new StringBody(sp.getString("Accomodation","0")));
            Log.e("accomodation", sp.getString("Accomodation","0"));
            reqEntity.addPart("gear", new StringBody(sp.getString("gear","0")));
            Log.e("gear", sp.getString("gear","0"));
            reqEntity.addPart("disclaimer", new StringBody(sp.getString(GlobalConstants.EVENT_DISCLAIMER,"")));
            Log.e("disclaimer", sp.getString(GlobalConstants.EVENT_DISCLAIMER,""));
            reqEntity.addPart("flight", new StringBody(sp.getString("flight","0")));
            Log.e("flight", sp.getString("flight","0"));
            reqEntity.addPart("action", new StringBody(GlobalConstants.CREATE_EVENT_ACTION));
            Log.e("action", GlobalConstants.CREATE_EVENT_ACTION);


            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            Log.e("response_str",response_str);
            if (resEntity != null) {
                JSONObject obj = new JSONObject(response_str);
                String status = obj.getString("success");
                if (status.equalsIgnoreCase("1")) {
                    success = "true";
                    message = obj.getString("msg");
                } else {
                    success = "false";
                    message = obj.getString("msg");
                }

                /*JSONObject resp = new JSONObject(response_str);
                String status = resp.getString("status");
                String message = resp.getString("message");
                if (status.equals("1")) {

                    success = "true";

                } else {

                    success = "false";

                }
*/
            }
        } catch (Exception ex) {
        }
        return success;
    }

    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow() {
        dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.progrees_dialog);
        AVLoadingIndicatorView loaderView = (AVLoadingIndicatorView) dialog2.findViewById(R.id.loader_view);
        loaderView.show();

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        dialog2.show();
    }
    private String convertToString(ArrayList<String> list) {

        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (String s : list)
        {
            sb.append(delim);
            sb.append(s);;
            delim = ",";
        }
        return sb.toString();
    }

    private ArrayList<String> convertToArray(String string) {

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(",")));
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_layout:
                Intent i = new Intent(NewAdvantureForm.this, BookingDateActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.photo_layout:
                Intent p = new Intent(NewAdvantureForm.this, AddPhotoActivity.class);
                startActivityForResult(p, 3);
                break;
            case R.id.route_layout:
                Intent r = new Intent(NewAdvantureForm.this, StartingRouteActivity.class);
                startActivityForResult(r, 2);
                break;
            case R.id.details_layout:
                Intent d = new Intent(NewAdvantureForm.this, CreateDetailActivity.class);
                startActivityForResult(d, 4);
                break;
        }
    }
}
