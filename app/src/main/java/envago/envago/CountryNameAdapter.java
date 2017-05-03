package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by worksdelight on 03/05/17.
 */

public class CountryNameAdapter extends BaseAdapter{
    ArrayList<HashMap<String, String>> images = new ArrayList<>();
    LayoutInflater inflater;
    Context applicationContext;
    Holder holder= null;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    String url;

    SharedPreferences sp;
    SharedPreferences.Editor ed;
    Global global;
    LayoutInflater inflatore;
    CountryNameAdapter(Context applicationContext, ArrayList<HashMap<String, String>> images){
        this.applicationContext=applicationContext;
        this.images=images;
        inflatore=LayoutInflater.from(applicationContext);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)

                .showStubImage(0)        //	Display Stub Image
                .showImageForEmptyUri(0)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
        sp = applicationContext.getSharedPreferences("message", Context.MODE_PRIVATE);
        ed = sp.edit();
    }
    @Override
    public int getCount() {
        return images.size();
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


            convertView = inflatore.inflate(R.layout.dialog_country_list_item, null);


            holder.flag_img = (ImageView) convertView.findViewById(R.id.flag_img);
            holder.country_name = (TextView) convertView.findViewById(R.id.country_name);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.country_name.setText(images.get(position).get(GlobalConstants.NAME));
        url=GlobalConstants.FLAG_URL+images.get(position).get(GlobalConstants.flag);
        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            imageLoader.displayImage(url, holder.flag_img, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);

                        }
                    });
        } else {
            holder.flag_img.setImageResource(0);
        }
        return convertView;
    }
    public class Holder {
        TextView country_name;
        ImageView flag_img;
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

}
