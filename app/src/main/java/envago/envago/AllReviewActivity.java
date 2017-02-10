package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static envago.envago.R.id.stars_review_list;

/**
 * Created by worksdelight on 03/02/17.
 */

 class AllReviewActivity extends Activity {
    TextView no_review_txt;
    ImageView cancel_btn;
    ListView review_list;
    Global global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_review_layout);
        global=(Global)getApplicationContext();
        no_review_txt=(TextView)findViewById(R.id.no_review_txt);
        cancel_btn=(ImageView)findViewById(R.id.cancel_button);
        review_list=(ListView)findViewById(R.id.review_list);
        review_list.setAdapter(new FullReviewAdapter(this,global.getReviewList()));
        no_review_txt.setText(global.getReviewList().size()+"Reviews");
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

     class FullReviewAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;


        String url;
     Holder holder;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String months[] = { " ", "Jan", "Feb", "Mar", "Apr", "May",
                "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
                "Dec", };

        FullReviewAdapter(Context context, ArrayList<HashMap<String, String>> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);

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

            holder = new Holder();
            if (convertView == null) {


                convertView = inflater.inflate(R.layout.full_review_list_item, null);


                holder.event_img = (ImageView) convertView.findViewById(R.id.review_user_img);
                holder.event_name_txt = (TextView) convertView.findViewById(R.id.review_list_username);
              holder.star=(RatingBar)convertView.findViewById(stars_review_list) ;
                LayerDrawable star = (LayerDrawable) holder.star.getProgressDrawable();
                star.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.textcolor), PorterDuff.Mode.SRC_ATOP);
                star.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.textcolor), PorterDuff.Mode.SRC_ATOP);
                star.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.textcolor), PorterDuff.Mode.SRC_ATOP);
                holder.event_price_txt=(TextView)convertView.findViewById(R.id.comment);
                convertView.setTag(holder);


            } else {
                holder = (Holder) convertView.getTag();
            }


            url = GlobalConstants.IMAGE_URL + list.get(position).get(GlobalConstants.IMAGE);
            Log.e("urle",url);
            holder.event_price_txt.setText(list.get(position).get("text"));
            holder.event_name_txt.setText(cap(list.get(position).get("username")));
            holder.star.setRating(Float.parseFloat(list.get(position).get("rating")));
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
            int color1 = generator.getRandomColor();
            TextDrawable drawable2 = TextDrawable.builder()
                    .buildRound(holder.event_name_txt.getText().toString().substring(0,1).toUpperCase(), color1);

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {
                Picasso.with(context).load(url).placeholder(drawable2).centerCrop().resize(80,80).transform(new CircleTransform()).into(holder.event_img);
            } else {
                holder.event_img.setImageDrawable(drawable2);
            }
            return convertView;
        }

        class Holder {
            ImageView event_img;
            TextView event_name_txt, event_start_txt,event_price_txt;
            RatingBar star;

        }


        public String cap(String name){
            StringBuilder sb = new StringBuilder(name);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }
    }

}
