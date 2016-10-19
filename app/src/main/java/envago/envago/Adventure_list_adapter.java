package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.plus.model.people.Person;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.apache.james.mime4j.field.datetime.parser.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhang on 9/25/2016.
 */
public class Adventure_list_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> images;
    LayoutInflater inflater;
    Context applicationContext;
    Adventure_holder holder = null;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    String url;

    public Adventure_list_adapter(Context applicationContext, ArrayList<HashMap<String, String>> images) {
        this.images = images;
        this.applicationContext = applicationContext;
        inflater = LayoutInflater.from(applicationContext);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)        //	Display Stub Image
                .showImageForEmptyUri(R.mipmap.ic_launcher)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
    }

    @Override
    public int getViewTypeCount() {
        return images.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        holder = new Adventure_holder();
        if (view == null) {


            view = inflater.inflate(R.layout.adventure_list_item, null);


            holder.backimg = (ImageView) view.findViewById(R.id.adv_img);
            holder.ad_name = (TextView) view.findViewById(R.id.adname);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.date = (TextView) view.findViewById(R.id.start_date);
            holder.location = (TextView) view.findViewById(R.id.location_adv);
            holder.heart_img = (ImageView) view.findViewById(R.id.heart_img);
            holder.start_event_txtView = (TextView) view.findViewById(R.id.start_event_txtView);
            view.setTag(holder);
            holder.heart_img.setTag(holder);


        } else {
            holder = (Adventure_holder) view.getTag();
        }

        url = "http://envagoapp.com/uploads/" + images.get(i).get(GlobalConstants.EVENT_IMAGES);

        holder.ad_name.setText(images.get(i).get(GlobalConstants.EVENT_NAME));
        holder.price.setText(images.get(i).get(GlobalConstants.EVENT_PRICE));
        holder.date.setText(images.get(i).get(GlobalConstants.EVENT_START_DATE));
        holder.location.setText(images.get(i).get(GlobalConstants.EVENT_LOC));
        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            imageLoader.displayImage(url, holder.backimg, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);

                        }
                    });
        } else {
            holder.backimg.setImageResource(R.mipmap.ic_launcher);
        }
        if (images.get(i).get(GlobalConstants.EVENT_FAV).equals("0")) {
            holder.heart_img.setImageResource(R.drawable.heart);
        } else {
            holder.heart_img.setImageResource(R.drawable.heart_field);

        }

        holder.heart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder = (Adventure_holder) v.getTag();
                if (images.get(i).get(GlobalConstants.EVENT_FAV).equals("0")) {
                    images.get(i).put(GlobalConstants.EVENT_FAV, "1");
                    holder.heart_img.setImageResource(R.drawable.heart_field);
                    favoriteMethod(images.get(i).get(GlobalConstants.EVENT_ID), "1");
                } else {
                    holder.heart_img.setImageResource(R.drawable.heart);
                    images.get(i).put(GlobalConstants.EVENT_FAV, "0");
                    favoriteMethod(images.get(i).get(GlobalConstants.EVENT_ID), "0");
                }

            }
        });


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd");
        String dateafter = dateFormat.format(c.getTime());

        Date currentDate = new Date();
        Date eventDate = new Date();

        try {
            currentDate = dateFormat.parse(dateafter);
            eventDate = dateFormat.parse(images.get(i).get(GlobalConstants.EVENT_START_DATE).replace("-", "/"));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (eventDate.equals(currentDate)||eventDate.before(currentDate)) {
            holder.start_event_txtView.setVisibility(View.VISIBLE);
        } else {
            holder.start_event_txtView.setVisibility(View.GONE);
        }

        return view;
    }

    public class Adventure_holder {
        ImageView backimg, heart_img;
        TextView price;
        TextView ad_name;
        TextView date, start_event_txtView;
        TextView location;

    }

    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager)
                    applicationContext.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                applicationContext).threadPoolSize(5)
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

                        Log.e("response", response);
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

                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(applicationContext));
                params.put(GlobalConstants.EVENT_ID, event_id);
                params.put(GlobalConstants.LIKE_STATUS, like_status);

                params.put("action", GlobalConstants.ACTION_LIKE_EVENT);
                Log.e("favorite param", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(stringRequest);
    }


}
