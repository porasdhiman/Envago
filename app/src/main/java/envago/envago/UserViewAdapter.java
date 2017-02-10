package envago.envago;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by worksdelight on 13/01/17.
 */

public class UserViewAdapter extends BaseAdapter {
    Context c;
    LayoutInflater inflator;
    int size;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    String url;
    int remeanningPlace;
    String type;

    UserViewAdapter(Context c, int size, ArrayList<HashMap<String, String>> list) {
        this.c = c;
        this.size = size;
        this.list = list;
        this.type = type;
        inflator = LayoutInflater.from(c);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.user_back)        //	Display Stub Image
                .showImageForEmptyUri(R.drawable.user_back)    //	If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        initImageLoader();
    }

    @Override
    public int getCount() {

            return size;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflator.inflate(R.layout.user_view_item, null);

        //TextView user_name=(TextView)convertView.findViewById(R.id.user_name);
        ImageView user_img = (ImageView) convertView.findViewById(R.id.user_img);

            if (list.size() > position) {
                ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
                int color1 = generator.getRandomColor();
                TextDrawable drawable2 = TextDrawable.builder()
                        .buildRound(list.get(position).get(GlobalConstants.USERNAME).substring(0, 1), color1);
                url = GlobalConstants.IMAGE_URL + list.get(position).get(GlobalConstants.IMAGE);
                if (url != null && !url.equalsIgnoreCase("null")
                        && !url.equalsIgnoreCase("")) {

                    Picasso.with(c).load(url).placeholder(drawable2).transform(new CircleTransform()).into(user_img);
                } else {
                    user_img.setImageDrawable(drawable2);
                }
            }

        return convertView;
    }

    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager)
                    c.getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                c).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    public String cap(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
