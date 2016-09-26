package envago.envago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jhang on 9/25/2016.
 */
public class Adventure_list_adapter extends BaseAdapter {

    int[] images;
    LayoutInflater inflater;
    Context applicationContext;
    Adventure_holder  holder = null;

    public Adventure_list_adapter(Context applicationContext, int[] images) {
        this.images = images;
        this.applicationContext = applicationContext;
        inflater = LayoutInflater.from(applicationContext);
    }

    @Override
    public int getCount() {
        return images.length;
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

        if (view==null) {
            holder = new Adventure_holder();

            view = inflater.inflate(R.layout.adventure_list_item, null);

            holder.backimg = (ImageView) view.findViewById(R.id.cat_img);

            view.setTag(holder);
        }
            else
            {
                holder=(Adventure_holder)view.getTag();
            }
            holder.backimg.setImageResource(images[i]);


        return view;
    }

    public class Adventure_holder
    {
        ImageView backimg;
        TextView price;
        TextView ad_name;
        TextView date;
        TextView location;
    }
}
