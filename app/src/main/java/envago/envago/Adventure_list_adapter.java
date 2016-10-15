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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.plus.model.people.Person;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.apache.james.mime4j.field.datetime.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        holder = new Adventure_holder();
        if (view == null) {


            view = inflater.inflate(R.layout.adventure_list_item, null);



            holder.backimg = (ImageView) view.findViewById(R.id.adv_img);
            holder.ad_name = (TextView) view.findViewById(R.id.adname);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.date = (TextView) view.findViewById(R.id.start_date);
            holder.location = (TextView) view.findViewById(R.id.location_adv);

            view.setTag(holder);
        } else {
            holder = (Adventure_holder) view.getTag();
        }

url="http://envagoapp.com/uploads/" + images.get(i).get(GlobalConstants.EVENT_IMAGES);

        holder.ad_name.setText(images.get(i).get(GlobalConstants.EVENT_NAME));
        holder.price.setText(images.get(i).get(GlobalConstants.EVENT_PRICE));
        holder.date.setText(images.get(i).get(GlobalConstants.EVENT_START_DATE).replace("-","/"));
        holder.location.setText(images.get(i).get(GlobalConstants.EVENT_LOC));
        if (url!= null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            imageLoader.displayImage(url, holder.backimg , options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);

                        }
                    });
        } else {
            holder.backimg .setImageResource(R.mipmap.ic_launcher);
        }

        Calendar c=Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/mm/dd");
        String dateafter=dateFormat.format(c.getTime());
        Date convertedDate = new Date();
        Date convertedDate2 = new Date();

            try {
                convertedDate = dateFormat.parse(holder.date.getText().toString());
                convertedDate2 = dateFormat.parse(dateafter);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            if (convertedDate2.equals(convertedDate)) {
                Log.e("match",convertedDate2+" "+convertedDate);
            } else {
                Log.e("match",convertedDate2+" "+convertedDate);
            }


        return view;
    }

    public class Adventure_holder {
        ImageView backimg;
        TextView price;
        TextView ad_name;
        TextView date;
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
}
