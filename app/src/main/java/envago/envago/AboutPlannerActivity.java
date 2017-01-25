package envago.envago;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static envago.envago.R.id.stars;

/**
 * Created by worksdelight on 19/01/17.
 */

public class AboutPlannerActivity extends Activity {
    ImageView cancel_button,review_user_img;
    TextView admin_name,review_txt,textWithUserName,review_user_name,comment,show_all_txtView;
    RatingBar star;
    ListView main_list;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    Global global;
    CircleImageView admin_image;
    Dialog dialog2;
    ArrayList<HashMap<String,String>> reviewList=new ArrayList<>();
    ArrayList<HashMap<String,String>> eventList=new ArrayList<>();
    RatingBar stars_review_list;
    RelativeLayout review_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.about_planner_layout);
        global=(Global)getApplicationContext();
        review_layout=(RelativeLayout)findViewById(R.id.review_layout);
        cancel_button=(ImageView)findViewById(R.id.cancel_button);
        admin_name=(TextView)findViewById(R.id.admin_name);

        textWithUserName=(TextView)findViewById(R.id.textWithUserName);
        star=(RatingBar)findViewById(stars);
        admin_image=(CircleImageView)findViewById(R.id.admin_img);
        stars_review_list=(RatingBar)findViewById(R.id.stars_review_list);
        review_user_name=(TextView)findViewById(R.id.review_list_username);
        show_all_txtView=(TextView)findViewById(R.id.show_all_txtView);
        comment=(TextView)findViewById(R.id.comment) ;
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        main_list=(ListView)findViewById(R.id.main_list);

        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.placeholder_image1)        //	Display Stub Image
                .showImageForEmptyUri(R.drawable.placeholder_image1)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
        getValueFromGlobal();
        show_all_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
public void getValueFromGlobal(){
    admin_name.setText(global.getAdminName());
    textWithUserName.setText("Adventure by "+global.getAdminName());
    if (global.getAdminUrl() != null && !global.getAdminUrl() .equalsIgnoreCase("null")
            && !global.getAdminUrl() .equalsIgnoreCase("")) {
        imageLoader.displayImage(global.getAdminUrl() , admin_image, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri,
                                                  View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view,
                                loadedImage);

                    }
                });
    } else {
        admin_image.setImageResource(R.drawable.user_back);
    }




      /*  if (global.getAdminRating().contains(".")) {
            // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING).split("0")[0].replace(".", ""));
            star.setRating(Float.parseFloat(global.getAdminRating().split("0")[0].replace(".", "")));
        } else {*/
            // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING));
            star.setRating(Float.parseFloat(global.getAdminRating()));
        //}

    dialogWindow();
    getEventMethod();
}
    //-------------------------------Image-save-cache------------------------------


    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager)
                    getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                AboutPlannerActivity.this).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    private void getEventMethod() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();

                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                                JSONObject data = obj.getJSONObject("data");
                                JSONArray review=data.getJSONArray("reviews");

                                for (int i = 0; i < review.length(); i++) {
                                    JSONObject reviewObj = review.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();

                                    JSONObject user = reviewObj.getJSONObject("user");
                                    map.put("id", user.getString("id"));
                                    map.put("username", user.getString("username"));
                                    map.put("image", user.getString("image"));
                                    map.put("text", reviewObj.getString("text"));
                                    map.put("rating", user.getString("rating"));
                                    map.put("created", user.getString("created"));
                                    reviewList.add(map);
                                }
                                if(reviewList.size()>0){
                                    review_layout.setVisibility(View.VISIBLE);
                                    review_user_name.setText(reviewList.get(0).get("username"));
                                    comment.setText(reviewList.get(0).get("text"));


                                     /*   if (reviewList.get(0).get("rating").contains(".")) {
                                            // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING).split("0")[0].replace(".", ""));
                                            stars_review_list.setRating(Float.parseFloat(reviewList.get(0).get("rating").split("0")[0].replace(".", "")));
                                        } else {*/
                                            // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING));
                                            stars_review_list.setRating(Float.parseFloat(reviewList.get(0).get("rating")));
                                        //}
                                        String url = "http://worksdelight.com/envago/uploads/" + reviewList.get(0).get("image");
                                        if (url != null && !url .equalsIgnoreCase("null")
                                            && !url .equalsIgnoreCase("")) {
                                        imageLoader.displayImage(url , review_user_img, options,
                                                new SimpleImageLoadingListener() {
                                                    @Override
                                                    public void onLoadingComplete(String imageUri,
                                                                                  View view, Bitmap loadedImage) {
                                                        super.onLoadingComplete(imageUri, view,
                                                                loadedImage);

                                                    }
                                                });
                                    } else {
                                        review_user_img.setImageResource(R.drawable.user_back);
                                    }

                                }

                                JSONArray events=data.getJSONArray("events");
                                for(int i=0;i<events.length();i++){
                                    JSONObject eventObj=events.getJSONObject(i);
                                    HashMap<String,String> map=new HashMap<>();
                                    map.put(GlobalConstants.EVENT_NAME,eventObj.getString(GlobalConstants.EVENT_NAME));
                                    map.put(GlobalConstants.IMAGE,eventObj.getString(GlobalConstants.IMAGE));
                                    map.put(GlobalConstants.EVENT_PRICE,eventObj.getString(GlobalConstants.EVENT_PRICE));
                                    map.put(GlobalConstants.EVENT_START_DATE,eventObj.getString(GlobalConstants.EVENT_START_DATE));
                                    eventList.add(map);
                                }
                                if(eventList.size()!=0) {
                                    main_list.setAdapter(new AboutEventAdapter(AboutPlannerActivity.this,eventList));
                                }
                            } else {
                                Toast.makeText(AboutPlannerActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, getIntent().getExtras().getString(GlobalConstants.USERID));
                params.put("with_detail", "yes");

                params.put("action", "get_profile");
                Log.e("Parameter for prifile", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
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
