package envago.envago;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
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
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static envago.envago.R.id.stars;

/**
 * Created by worksdelight on 19/01/17.
 */

public class AboutPlannerActivity extends Activity {
    ImageView cancel_button, review_user_img;
    TextView admin_name, review_txt, textWithUserName, review_user_name, comment, show_all_txtView;
    TextView star;
    ListView main_list;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    Global global;
    ImageView admin_image;
    Dialog dialog2;
    ArrayList<HashMap<String, String>> reviewList = new ArrayList<>();
    ArrayList<HashMap<String, String>> eventList = new ArrayList<>();
    RatingBar stars_review_list;
    RelativeLayout review_layout;
    TextView no_review_txt, About_me_txt;
    ScrollView main_scrollview;
    LinearLayout main_layout;
    ImageView star1, star2, star3, star4, star5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.about_planner_layout);
        global = (Global) getApplicationContext();
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        Fonts.overrideFonts(this, main_layout);
        review_layout = (RelativeLayout) findViewById(R.id.review_layout);
        cancel_button = (ImageView) findViewById(R.id.cancel_button);
        admin_name = (TextView) findViewById(R.id.admin_name);
        Fonts.overrideFonts1(this, admin_name);
        main_scrollview = (ScrollView) findViewById(R.id.main_scrollview);
        textWithUserName = (TextView) findViewById(R.id.textWithUserName);
        Fonts.overrideFonts1(this, textWithUserName);
        star = (TextView) findViewById(stars);
        //star.setVisibility(View.GONE);
        admin_image = (ImageView) findViewById(R.id.admin_img);
       // stars_review_list = (RatingBar) findViewById(R.id.stars_review_list);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView)findViewById(R.id.star2);

       star3 = (ImageView)findViewById(R.id.star3);
        star4 = (ImageView)findViewById(R.id.star4);

        star5 = (ImageView)findViewById(R.id.star5);

        review_user_name = (TextView) findViewById(R.id.review_list_username);
        review_user_img = (ImageView) findViewById(R.id.review_user_img);
        show_all_txtView = (TextView) findViewById(R.id.show_all_txtView);
        About_me_txt = (TextView) findViewById(R.id.About_me_txt);
        comment = (TextView) findViewById(R.id.comment);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        main_list = (ListView) findViewById(R.id.main_list);
        no_review_txt = (TextView) findViewById(R.id.no_review_txt);
        Fonts.overrideFonts1(this, no_review_txt);
        Fonts.overrideFonts1(this, star);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(0)        //	Display Stub Image
                .showImageForEmptyUri(0)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
        getValueFromGlobal();
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AboutPlannerActivity.this, DetailsActivity.class);
                i.putExtra(GlobalConstants.EVENT_ID, eventList.get(position).get(GlobalConstants.EVENT_ID));
                i.putExtra("user", "non user");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    public void getValueFromGlobal() {


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

                                About_me_txt.setText(data.getString("about_me"));

                                admin_name.setText(data.getString("username"));
                                TextDrawable drawable2 = TextDrawable.builder()
                                        .buildRound(admin_name.getText().toString().substring(0, 1).toUpperCase(), Color.parseColor("#F94444"));

                                String user_url = GlobalConstants.IMAGE_URL + data.getString("image");
                                if (user_url != null && !user_url.equalsIgnoreCase("null")
                                        && !user_url.equalsIgnoreCase("")) {
                                    Picasso.with(AboutPlannerActivity.this).load(global.getAdminUrl()).placeholder(drawable2).transform(new CircleTransform()).into(admin_image);
                                } else {
                                    admin_image.setImageDrawable(drawable2);
                                }


                                JSONArray review = data.getJSONArray("reviews");

                                for (int i = 0; i < review.length(); i++) {
                                    JSONObject reviewObj = review.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();

                                    JSONObject user = reviewObj.getJSONObject("user");
                                    map.put("id", user.getString("id"));
                                    map.put("username", user.getString("username"));
                                    map.put("image", user.getString("image"));
                                    map.put("text", reviewObj.getString("text"));
                                    map.put("rating", reviewObj.getString("rating"));
                                    map.put("created", reviewObj.getString("created"));
                                    reviewList.add(map);
                                }

                                if (reviewList.size() > 0) {
                                    show_all_txtView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(AboutPlannerActivity.this, AllReviewActivity.class);
                                            startActivity(i);

                                        }
                                    });
                                    global.setReviewList(reviewList);
                                    if (reviewList.size() == 0) {
                                        star.setText("Reviews");
                                    } else {
                                        star.setText(reviewList.size() + " Reviews");
                                    }

                                    no_review_txt.setText(reviewList.size() + " Reviews");
                                    review_layout.setVisibility(View.VISIBLE);
                                    review_user_name.setText(cap(reviewList.get(0).get("username")));
                                    comment.setText(reviewList.get(0).get("text"));

                                     /*   if (reviewList.get(0).get("rating").contains(".")) {
                                            // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING).split("0")[0].replace(".", ""));
                                            stars_review_list.setRating(Float.parseFloat(reviewList.get(0).get("rating").split("0")[0].replace(".", "")));
                                        } else {*/
                                    // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING));
                                   // stars_review_list.setRating(Float.parseFloat(reviewList.get(0).get("rating")));
                                    Log.e("value",reviewList.get(0).get("rating"));
                                    String value=reviewList.get(0).get("rating");
                                    if (value.contains("1")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star_blank);
                                        star3.setImageResource(R.drawable.star_blank);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (value.contains("2")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star_blank);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (value.contains("3")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (value.contains("4")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star);
                                        star4.setImageResource(R.drawable.star);
                                        star5.setImageResource(R.drawable.star_blank);
                                    } else if (value.contains("5")) {
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star);
                                        star4.setImageResource(R.drawable.star);
                                        star5.setImageResource(R.drawable.star);
                                    }else{
                                       star1.setImageResource(R.drawable.star_blank);
                                        star2.setImageResource(R.drawable.star_blank);
                                        star3.setImageResource(R.drawable.star_blank);
                                        star4.setImageResource(R.drawable.star_blank);
                                        star5.setImageResource(R.drawable.star_blank);
                                    }
                                    //}
                                    TextDrawable drawable = TextDrawable.builder()
                                            .buildRound(review_user_name.getText().toString().substring(0, 1).toUpperCase(), Color.parseColor("#d1d1d1"));

                                    String url = GlobalConstants.IMAGE_URL + reviewList.get(0).get("image");
                                    if (url != null && !url.equalsIgnoreCase("null")
                                            && !url.equalsIgnoreCase("")) {
                                        Picasso.with(AboutPlannerActivity.this).load(url).placeholder(drawable).transform(new CircleTransform()).into(review_user_img);
                                    } else {
                                        review_user_img.setImageDrawable(drawable);
                                    }

                                }

                                JSONArray events = data.getJSONArray("events");
                                for (int i = 0; i < events.length(); i++) {
                                    JSONObject eventObj = events.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put(GlobalConstants.EVENT_ID, eventObj.getString("id"));

                                    map.put(GlobalConstants.EVENT_NAME, eventObj.getString(GlobalConstants.EVENT_NAME));
                                    map.put(GlobalConstants.IMAGE, eventObj.getString(GlobalConstants.IMAGE));
                                    map.put(GlobalConstants.EVENT_PRICE, eventObj.getString(GlobalConstants.EVENT_PRICE));
                                    JSONArray event_dates = eventObj.getJSONArray("event_dates");
                                    JSONObject event_datesObj = event_dates.getJSONObject(0);

                                    map.put(GlobalConstants.EVENT_START_DATE, event_datesObj.getString(GlobalConstants.EVENT_START_DATE));
                                    eventList.add(map);
                                }
                                if (eventList.size() != 0) {
                                    textWithUserName.setVisibility(View.VISIBLE);
                                    textWithUserName.setText("Adventures by " + admin_name.getText().toString());

                                    main_list.setAdapter(new AboutEventAdapter(AboutPlannerActivity.this, eventList));
                                    CommonUtils.getListViewSize(main_list);

                                } else {
                                    textWithUserName.setVisibility(View.GONE);
                                }
                                main_scrollview.smoothScrollTo(0, 0);


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

        loaderView.setVisibility(View.GONE);
        //loaderView.show();

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        dialog2.show();
    }

    public String cap(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
