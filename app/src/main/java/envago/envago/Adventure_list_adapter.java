package envago.envago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jhang on 9/25/2016.
 */
public class Adventure_list_adapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> images;
    LayoutInflater inflater;
    Context applicationContext;
    Adventure_holder holder = null;
    ImageLoader loader;

    public Adventure_list_adapter(Context applicationContext, ArrayList<HashMap<String, String>> images) {
        this.images = images;
        this.applicationContext = applicationContext;
        inflater = LayoutInflater.from(applicationContext);
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

        if (view == null) {
            holder = new Adventure_holder();

            view = inflater.inflate(R.layout.adventure_list_item, null);

            loader = CustomVolleyRequestQueue.getInstance(applicationContext).getImageLoader();

            holder.backimg = (NetworkImageView) view.findViewById(R.id.adv_img);
            holder.ad_name = (TextView) view.findViewById(R.id.adname);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.date = (TextView) view.findViewById(R.id.start_date);
            holder.location = (TextView) view.findViewById(R.id.location_adv);

            view.setTag(holder);
        } else {
            holder = (Adventure_holder) view.getTag();
        }

        loader.get(images.get(i).get(GlobalConstants.EVENT_IMAGES), ImageLoader.getImageListener(holder.backimg, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        holder.backimg.setImageUrl("http://envagoapp.com/uploads/" + images.get(i).get(GlobalConstants.EVENT_IMAGES), loader);


        holder.ad_name.setText(images.get(i).get(GlobalConstants.EVENT_NAME));
        holder.price.setText(images.get(i).get(GlobalConstants.EVENT_PRICE));
        holder.date.setText(images.get(i).get(GlobalConstants.EVENT_START_DATE));
        holder.location.setText(images.get(i).get(GlobalConstants.EVENT_LOC));

        return view;
    }

    public class Adventure_holder {
        NetworkImageView backimg;
        TextView price;
        TextView ad_name;
        TextView date;
        TextView location;
    }
}
