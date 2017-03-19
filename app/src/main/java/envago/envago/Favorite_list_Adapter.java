package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Favorite_list_Adapter extends PagerAdapter {

    private Context mContext;

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

    public Favorite_list_Adapter(Context mContext, ArrayList<HashMap<String, String>> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;

        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(0)        //	Display Stub Image
                .showImageForEmptyUri(0)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
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
        ImageView heart_img = (ImageView) itemView.findViewById(R.id.heart_img);
        TextView start_event_txtView = (TextView) itemView.findViewById(R.id.start_event_txtView);
        LinearLayout main_layout=(LinearLayout)itemView.findViewById(R.id.main_layout);
        Fonts.overrideFonts(mContext,main_layout);
        Fonts.overrideFonts1(mContext,view_text);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(mContext,DetailsActivity.class);
                j.putExtra(GlobalConstants.EVENT_ID,mResources.get(i).get(GlobalConstants.EVENT_ID));
                j.putExtra("user","no user wish");
                mContext.startActivity(j);

            }
        });

        if (mResources.get(i).get(GlobalConstants.EVENT_IMAGES).equalsIgnoreCase("")||mResources.get(i).get(GlobalConstants.EVENT_IMAGES).equalsIgnoreCase(null)) {

        } else {
            url = GlobalConstants.IMAGE_URL + mResources.get(i).get(GlobalConstants.EVENT_IMAGES);
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
        heart_img.setVisibility(View.GONE);
       /* if (mResources.get(i).get(GlobalConstants.EVENT_FAV).equals("0")) {
            heart_img.setImageResource(R.drawable.heart);
        } else {
            heart_img.setImageResource(R.drawable.heart_field);

        }*/

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
            int memClass = ((ActivityManager)
                    mContext.getSystemService(Context.ACTIVITY_SERVICE))
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
}

