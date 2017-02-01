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
 * Created by worksdelight on 19/01/17.
 */

public class AboutEventAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    String url;
    AboutEventAdapter.Holder holder;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    String months[] = { " ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec", };

    AboutEventAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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

        holder = new AboutEventAdapter.Holder();
        if (convertView == null) {


            convertView = inflater.inflate(R.layout.about_event_item, null);


            holder.event_img = (ImageView) convertView.findViewById(R.id.event_img);
            holder.event_name_txt = (TextView) convertView.findViewById(R.id.event_name_txt);
            holder.event_start_txt = (TextView) convertView.findViewById(R.id.event_date_txt);

            holder.event_price_txt=(TextView)convertView.findViewById(R.id.event_price_txt);
            convertView.setTag(holder);


        } else {
            holder = (AboutEventAdapter.Holder) convertView.getTag();
        }


        url = "http://worksdelight.com/envago/uploads/" + list.get(position).get(GlobalConstants.IMAGE);
        Log.e("urle",url);
        holder.event_price_txt.setText(list.get(position).get(GlobalConstants.EVENT_PRICE));
        holder.event_name_txt.setText(cap(list.get(position).get(GlobalConstants.EVENT_NAME)));

        String data = list.get(position).get(GlobalConstants.EVENT_START_DATE);
        String split[] = data.split("-");
        String minth = split[1];
        String date = split[2];
        int mm = Integer.parseInt(minth);

        holder.event_start_txt.setText(date + " " + months[mm] + " " + split[0]);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRect(holder.event_name_txt.getText().toString().substring(0,1).toUpperCase(), color1);

        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            Picasso.with(context).load(url).placeholder(drawable2).into(holder.event_img);
        } else {
            holder.event_img.setImageDrawable(drawable2);
        }
        return convertView;
    }

    class Holder {
        ImageView event_img;
        TextView event_name_txt, event_start_txt,event_price_txt;

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
    public String cap(String name){
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}