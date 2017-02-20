package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by jhang on 9/25/2016.
 */
public class Adventure_list_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> images=new ArrayList<>();
    LayoutInflater inflater;
    Context applicationContext;
    Adventure_holder holder = null;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    String url;
    String months[] = { " ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec", };
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    public Adventure_list_adapter(Context applicationContext, ArrayList<HashMap<String, String>> images) {
        this.images = images;
        this.applicationContext = applicationContext;
        inflater = LayoutInflater.from(applicationContext);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)

                .showStubImage(0)        //	Display Stub Image
                .showImageForEmptyUri(0)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
        sp=applicationContext.getSharedPreferences("message",Context.MODE_PRIVATE);
        ed=sp.edit();
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


            view = inflater.inflate(R.layout.advanture_feature_pager_item, null);


            holder.backimg = (ImageView) view.findViewById(R.id.view_img);
            holder.ad_name = (TextView) view.findViewById(R.id.view_txt);
            holder.price = (TextView) view.findViewById(R.id.view_price_txt);
            holder.advanture_date = (TextView) view.findViewById(R.id.view_advanture_date_txt);
            holder.advanture_location = (TextView) view.findViewById(R.id.view_advanture_location_txt);
            holder.heart_img = (ImageView) view.findViewById(R.id.heart_img);
            holder.start_event_txtView = (TextView) view.findViewById(R.id.start_event_txtView);
            holder.main_layout=(LinearLayout)view.findViewById(R.id.main_layout);
            view.setTag(holder);
            holder.heart_img.setTag(holder);


        } else {
            holder = (Adventure_holder) view.getTag();
        }
Fonts.overrideFonts(applicationContext,holder.main_layout);
        url = GlobalConstants.IMAGE_URL + images.get(i).get(GlobalConstants.EVENT_IMAGES);
        holder.ad_name.setText(images.get(i).get(GlobalConstants.EVENT_NAME));
        holder.price .setText("$" + images.get(i).get(GlobalConstants.EVENT_PRICE));
        String data = images.get(i).get(GlobalConstants.EVENT_START_DATE);
        String split[] = data.split("-");
        String minth = split[1];
        String date = split[2];
        int mm = Integer.parseInt(minth);

        holder.advanture_date.setText(date + " " + months[mm]/* + " " + split[0]*/);
        holder.advanture_date.setTextColor(Color.GRAY);

        holder.advanture_location.setText(images.get(i).get(GlobalConstants.EVENT_LOC));
        holder.advanture_location.setTextColor(Color.GRAY);
        Log.e("location text ",holder.advanture_location.getText().toString());

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
            holder.backimg.setImageResource(0);
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
                    ed.putString("message","wish");
                    ed.commit();
                } else {
                    holder.heart_img.setImageResource(R.drawable.heart);
                    images.get(i).put(GlobalConstants.EVENT_FAV, "0");
                    favoriteMethod(images.get(i).get(GlobalConstants.EVENT_ID), "0");
                    ed.putString("message","not wish");
                    ed.commit();
                }

            }
        });


      /*  Calendar c = Calendar.getInstance();
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
        }*/

        return view;
    }

    public class Adventure_holder {
        ImageView backimg, heart_img;
        TextView price;
        TextView ad_name;
        TextView advanture_date, start_event_txtView;
        TextView advanture_location;
        LinearLayout main_layout;

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
