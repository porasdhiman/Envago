package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vikas on 28-12-2016.
 */

public class FeaturedPlannerAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    String url;
    Holder holder;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();

    FeaturedPlannerAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.placeholder_image1)        //	Display Stub Image
                .showImageForEmptyUri(R.drawable.placeholder_image1)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        holder = new Holder();
        if (convertView == null) {


            convertView = inflater.inflate(R.layout.advanture_view_item, null);


            holder.planner_img = (ImageView) convertView.findViewById(R.id.planer_img);
            holder.Planner_name = (TextView) convertView.findViewById(R.id.planner_txt);
            holder.planner_address = (TextView) convertView.findViewById(R.id.planner_address_txt);
holder.planer_stars=(RatingBar)convertView.findViewById(R.id.planer_stars) ;

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }


            /*if (list.get(position).get("rating").contains(".")) {
                // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING).split("0")[0].replace(".", ""));
                holder.planer_stars.setRating(Float.parseFloat(list.get(position).get("rating").split("0")[1].replace(".", "")));
            } else {*/
                // rating.setText(objArry.getString(GlobalConstants.ADMIN_RATING));
                holder.planer_stars.setRating(Float.parseFloat(list.get(position).get("rating")));
            //}

        url = "http://worksdelight.com/envago/uploads/" + list.get(position).get(GlobalConstants.IMAGE);
        Log.e("urle",url);
        holder.Planner_name.setText(list.get(position).get(GlobalConstants.USERNAME));
        holder.planner_address.setText(list.get(position).get(GlobalConstants.ADDRESS));
        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            imageLoader.displayImage(url, holder.planner_img, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);

                        }
                    });
        } else {
            holder.planner_img.setImageResource(R.mipmap.ic_launcher);
        }
        return convertView;
    }

    class Holder {
        ImageView planner_img;
        TextView Planner_name, planner_address;
        RatingBar planer_stars;
    }

    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager)
                    context.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }
}
