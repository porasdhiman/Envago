package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by vikas on 03-01-2017.
 */

public class NewAdvantureForm extends Activity {
    ImageView booking_checkBox, route_checkbox, photo_checkBox, detail_checkBox,back_button_create;
    boolean is_booking, is_route, is_addPhoto, is_detail;
    Global global;
    Button submit_button;
    TextView preview_button;
    HttpEntity resEntity;
    String message;
    Dialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.main_new_advanture_form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        global.setEvent_start_date("");
        back_button_create=(ImageView) findViewById(R.id.back_button_create);
        booking_checkBox = (ImageView) findViewById(R.id.booking_checkBox);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!global.getEvent_start_date().equalsIgnoreCase("")) {
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
            if (global.getEvent_description().equalsIgnoreCase("true")) {
                detail_checkBox.setImageResource(R.drawable.selected);
            }

        }
        if (!global.getEvent_start_date().equalsIgnoreCase("") && !global.getStartingPoint().equalsIgnoreCase("")
                && global.getListImg().size() != 0 && global.getEvent_description().equalsIgnoreCase("true")) {
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
            reqEntity.addPart(GlobalConstants.EVENT_CAT_ID, new StringBody(global.getEvent_cat_id()));
            Log.e("sub_cat_id", global.getEvent_cat_id());
            reqEntity.addPart(GlobalConstants.EVENT_NAME, new StringBody(global.getEvent_name()));
            Log.e("name", global.getEvent_name());
            if (global.getDateType().equalsIgnoreCase("one_time")) {
                reqEntity.addPart("event_type", new StringBody(global.getDateType()));
                Log.e("event_type", global.getDateType());
                reqEntity.addPart("event_no_of_days", new StringBody(global.getNumberOfDay()));
                Log.e("event_no_of_days", global.getNumberOfDay());
                JSONArray installedList = new JSONArray();



                    try {
                        JSONObject installedPackage = new JSONObject();
                        installedPackage.put(GlobalConstants.EVENT_START_DATE, global.getEvent_start_date());
                        installedPackage.put(GlobalConstants.EVENT_END_DATE, global.getEvent_end_date());
                        installedList.put(installedPackage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
            } else if (global.getDateType().equalsIgnoreCase("full_season")) {
                reqEntity.addPart("event_type", new StringBody(global.getDateType()));
                Log.e("event_type", global.getDateType());
                reqEntity.addPart("event_no_of_days", new StringBody(global.getNumberOfDay()));
                Log.e("event_no_of_days", global.getNumberOfDay());

                reqEntity.addPart("event_season", new StringBody(global.getSessionType()));
                Log.e("event_season", global.getSessionType());
                JSONArray installedList = new JSONArray();



                try {
                    JSONObject installedPackage = new JSONObject();
                    installedPackage.put(GlobalConstants.EVENT_START_DATE, global.getEvent_start_date());
                    installedPackage.put(GlobalConstants.EVENT_END_DATE, global.getEvent_end_date());
                    installedList.put(installedPackage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                String dataToSend = installedList.toString();
                reqEntity.addPart("event_dates", new StringBody(dataToSend));
                Log.e("event_datesjjjjjj", dataToSend);
            } else {
                reqEntity.addPart("event_type", new StringBody(global.getDateType()));
                Log.e("event_type", global.getDateType());
                reqEntity.addPart("event_no_of_days", new StringBody(global.getNumberOfDay()));
                Log.e("event_no_of_days", global.getNumberOfDay());

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


            reqEntity.addPart(GlobalConstants.EVENT_TIME, new StringBody(global.getEvent_time()));
            Log.e(GlobalConstants.EVENT_TIME, global.getEvent_time());
            reqEntity.addPart(GlobalConstants.EVENT_LEVEL, new StringBody(global.getEvent_level()));
            Log.e(GlobalConstants.EVENT_LEVEL, global.getEvent_level());
            reqEntity.addPart(GlobalConstants.EVENT_METTING_POINT, new StringBody(global.getEvent_meetin_point()));
            Log.e(GlobalConstants.EVENT_METTING_POINT, global.getEvent_meetin_point());
            reqEntity.addPart("meeting_point_latitude", new StringBody(global.getEvent_meeting_lat()));
            Log.e("meeting_point_latitude", global.getEvent_meeting_lat());
            reqEntity.addPart("meeting_point_longitude", new StringBody(global.getEvent_meeting_lng()));
            Log.e("meeting_point_longitude", global.getEvent_meeting_lng());
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
            reqEntity.addPart("description", new StringBody(global.getDescriptionString()));
            Log.e("description", global.getDescriptionString());
            reqEntity.addPart("no_of_places", new StringBody(global.getEvent_place()));
            reqEntity.addPart("price", new StringBody(global.getEvent_price()));
            Log.e("no_of_places", global.getEvent_place());
            Log.e("price", global.getEvent_price());
            reqEntity.addPart("whats_included", new StringBody("dddd"));
            Log.e("whats_included", "dddd");
            reqEntity.addPart("meals", new StringBody(String.valueOf(global.getMeal())));
            Log.e("meals", String.valueOf(global.getMeal()));
            reqEntity.addPart("transport", new StringBody(String.valueOf(global.getTransportataion())));
            Log.e("transport", String.valueOf(global.getTransportataion()));
            reqEntity.addPart("tent", new StringBody(String.valueOf(global.getTent())));
            Log.e("tent", String.valueOf(global.getTent()));
            reqEntity.addPart("accomodation", new StringBody(String.valueOf(global.getAccomodation())));
            Log.e("accomodation", String.valueOf(global.getAccomodation()));
            reqEntity.addPart("gear", new StringBody(String.valueOf(global.getGear())));
            Log.e("gear", String.valueOf(global.getGear()));
            reqEntity.addPart("disclaimer", new StringBody(global.getEvent_disclaimer()));
            Log.e("disclaimer", global.getEvent_disclaimer());
            reqEntity.addPart("flight", new StringBody(String.valueOf(global.getFlight())));
            Log.e("flight", String.valueOf(global.getFlight()));
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
}
