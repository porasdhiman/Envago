package envago.envago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vikas on 20-10-2016.
 */
public class ReviewList_Adapter extends BaseAdapter {

    ArrayList<HashMap<String,String>> review_list_array;
    Context detailsActivity;
    LayoutInflater inflater;
    review_list_holder holder = null;

    public ReviewList_Adapter(ArrayList<HashMap<String, String>> review_list_array, DetailsActivity detailsActivity) {

        this.detailsActivity = detailsActivity;
        this.review_list_array = review_list_array;
        inflater = LayoutInflater.from(detailsActivity);


    }

    @Override
    public int getCount() {
        return review_list_array.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return review_list_array.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        holder = new review_list_holder();
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.review_list_item,null);


            holder.review_list_comment = (TextView)convertView.findViewById(R.id.review_list_comment);
            holder.review_list_username = (TextView)convertView.findViewById(R.id.review_list_username);
            holder.review_list_stars=(RatingBar)convertView.findViewById(R.id.stars_review_list);

            convertView.setTag(holder);

        }
        else
        {
            holder =(review_list_holder) convertView.getTag();
        }

        holder.review_list_username.setText(review_list_array.get(position).get(GlobalConstants.USERNAME));
        holder.review_list_comment.setText(review_list_array.get(position).get("text"));
        holder.review_list_stars.setRating(Integer.parseInt(review_list_array.get(position).get("rating")));

        return convertView;
    }

    public class review_list_holder
    {
        RatingBar review_list_stars;
        TextView review_list_username;
        TextView review_list_comment;
    }
}
