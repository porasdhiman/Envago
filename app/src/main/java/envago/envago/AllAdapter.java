package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikas on 28-12-2016.
 */

public class AllAdapter extends PagerAdapter {

    private Context mContext;
Global global;
    ArrayList<HashMap<String, String>> mResources = new ArrayList<>();
    /*  com.nostra13.universalimageloader.core.ImageLoader imageLoader;
      DisplayImageOptions options;*/

    int img[];
    String arr[] = {"Water", "Air", "Rock & Ice"};
    ArrayList<HashMap<String, String>> images;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    String url;
    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    public AllAdapter(Context mContext, ArrayList<HashMap<String, String>> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
global=(Global)mContext.getApplicationContext();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .showStubImage(0)        //	Display Stub Image
                .showImageForEmptyUri(0)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
        sp = mContext.getSharedPreferences("message", Context.MODE_PRIVATE);
        ed = sp.edit();
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.advanture_feature_pager_item, container, false);
        TextView view_text = (TextView) itemView.findViewById(R.id.view_txt);
        TextView view_price_text = (TextView) itemView.findViewById(R.id.view_price_txt);
        TextView view_date_text = (TextView) itemView.findViewById(R.id.view_advanture_date_txt);
        TextView view_location_txt = (TextView) itemView.findViewById(R.id.view_advanture_location_txt);
        ImageView view_img = (ImageView) itemView.findViewById(R.id.view_img);
        final LinearLayout main_layout = (LinearLayout) itemView.findViewById(R.id.main_layout);
        final ImageView heart_img = (ImageView) itemView.findViewById(R.id.heart_img);
        TextView start_event_txtView = (TextView) itemView.findViewById(R.id.start_event_txtView);
        Fonts.overrideFonts(mContext, main_layout);
        Fonts.overrideFonts1(mContext, view_text);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*main_layout.setDrawingCacheEnabled(true);

                Bitmap bitmap = main_layout.getDrawingCache();
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                try {
                    cachePath.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(cachePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                global.setF(cachePath);*/
                Intent j = new Intent(mContext, DetailsActivity.class);
                j.putExtra(GlobalConstants.EVENT_ID, mResources.get(i).get(GlobalConstants.EVENT_ID));
                j.putExtra("user", "no user");
                mContext.startActivity(j);

            }
        });


        url = GlobalConstants.IMAGE_URL + mResources.get(i).get(GlobalConstants.IMAGE);

        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            imageLoader.displayImage(url, view_img, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);

                        }
                    });
        } else {
            view_img.setImageResource(0);
        }


        view_text.setText(mResources.get(i).get(GlobalConstants.EVENT_NAME));
        view_price_text.setText("$" + mResources.get(i).get(GlobalConstants.EVENT_PRICE));
        String data = mResources.get(i).get(GlobalConstants.EVENT_START_DATE);
        String split[] = data.split("-");
        String minth = split[1];
        String date = split[2];
        int mm = Integer.parseInt(minth);

        view_date_text.setText(date + " " + months[mm]/* + " " + split[0]*/);
        view_location_txt.setText(mResources.get(i).get(GlobalConstants.EVENT_LOC));

        if (mResources.get(i).get(GlobalConstants.EVENT_FAV).equals("0")) {
            heart_img.setImageResource(R.drawable.heart);
        } else {
            heart_img.setImageResource(R.drawable.heart_field);

        }
        heart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResources.get(i).get(GlobalConstants.EVENT_FAV).equals("0")) {
                    mResources.get(i).put(GlobalConstants.EVENT_FAV, "1");
                    heart_img.setImageResource(R.drawable.heart_field);
                    favoriteMethod(mResources.get(i).get(GlobalConstants.EVENT_ID), "1");
                    ed.putString("message", "wish");
                    ed.commit();
                } else {
                    heart_img.setImageResource(R.drawable.heart);
                    mResources.get(i).put(GlobalConstants.EVENT_FAV, "0");
                    favoriteMethod(mResources.get(i).get(GlobalConstants.EVENT_ID), "0");
                    ed.putString("message", "not wish");
                    ed.commit();
                }

            }
        });
       /* Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd");
        String dateafter = dateFormat.format(c.getTime());

        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(dateafter);
            eventDate = dateFormat.parse(mResources.get(i).get(GlobalConstants.EVENT_START_DATE).replace("-", "/"));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (eventDate.equals(currentDate)||eventDate.before(currentDate)) {
            start_event_txtView.setVisibility(View.VISIBLE);
        } else {
            start_event_txtView.setVisibility(View.GONE);
        }*/

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    private void favoriteMethod(final String event_id, final String like_status) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {


                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(mContext));
                params.put(GlobalConstants.EVENT_ID, event_id);
                params.put(GlobalConstants.LIKE_STATUS, like_status);

                params.put("action", GlobalConstants.ACTION_LIKE_EVENT);
                Log.e("favorite param", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }


}

