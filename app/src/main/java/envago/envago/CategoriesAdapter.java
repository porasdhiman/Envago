package envago.envago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by jhang on 9/19/2016.
 */
public class CategoriesAdapter extends BaseAdapter {

    int [] images ;
    LayoutInflater inflater;
    Context applicationContext;



    public CategoriesAdapter(Context applicationContext, int[] images) {
        this.images = images;
        this.applicationContext =applicationContext;
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

        view = inflater.inflate(R.layout.list_item, null);


        ImageView category = (ImageView)view.findViewById(R.id.cat_img);

        category.setImageResource(images[i]);

        return view;
    }
}
