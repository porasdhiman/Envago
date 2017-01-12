package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vikas on 28-12-2016.
 */

public class CatPagerAdapter extends PagerAdapter {

    private Context mContext;
    int img[];
    /*  com.nostra13.universalimageloader.core.ImageLoader imageLoader;
      DisplayImageOptions options;*/
    String url;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    String arr[] = {"Water", "Air", "Rock & Ice"};
    public int[] images = {R.drawable.air, R.drawable.earth, R.drawable.water, R.drawable.rockice, R.drawable.volunteer};
    public CatPagerAdapter(Context mContext, ArrayList<HashMap<String, String>> list) {
        this.mContext = mContext;
        this.list = list;
      /*  imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)        //	Display Stub Image
                .showImageForEmptyUri(R.mipmap.ic_launcher)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();*/
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.advanture_pager_item_for_cat, container, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext, Adventure_list.class);
                intent.putExtra(GlobalConstants.CAT_ID,list.get(position).get(GlobalConstants.CAT_ID));
                intent.putExtra(GlobalConstants.EVENT_CAT_NAME,list.get(position).get(GlobalConstants.EVENT_CAT_NAME));
                mContext.startActivity(intent);


            }
        });
        TextView cat_name_txtView = (TextView) itemView.findViewById(R.id.cat_name_txtView);
        TextView count_value = (TextView) itemView.findViewById(R.id.count_value);
        ImageView cat_pager_img = (ImageView) itemView.findViewById(R.id.cat_pager_img);

        cat_pager_img.setBackgroundResource(images[position]);
        cat_name_txtView.setText(list.get(position).get(GlobalConstants.EVENT_CAT_NAME));
        count_value.setText(list.get(position).get(GlobalConstants.EVENT_CAT_COUNT));

       /* url = mResources.get(position);
        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            imageLoader.displayImage(url, imageView, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);

                        }
                    });
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
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

