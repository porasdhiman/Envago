package envago.envago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by worksdelight on 13/01/17.
 */

public class UserViewAdapter extends BaseAdapter {
Context c;
    LayoutInflater inflator;
    int size;

    UserViewAdapter(Context c){
        this.c=c;

        inflator=LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return 10;
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

        convertView=inflator.inflate(R.layout.user_view_item,null);
        return convertView;
    }
}
