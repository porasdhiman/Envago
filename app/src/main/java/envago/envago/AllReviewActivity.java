package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by worksdelight on 03/02/17.
 */

public class AllReviewActivity extends Activity {
    TextView no_review_txt;
    ImageView cancel_btn;
    ListView review_list;
    Global global;
    LinearLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_review_layout);
        global = (Global) getApplicationContext();
        no_review_txt = (TextView) findViewById(R.id.no_review_txt);
        cancel_btn = (ImageView) findViewById(R.id.cancel_button);
        review_list = (ListView) findViewById(R.id.review_list);
        review_list.setAdapter(new FullReviewAdapter(this, global.getReviewList()));
        no_review_txt.setText(global.getReviewList().size() + "Reviews");
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        Fonts.overrideFonts(this, main_layout);
    }

    class FullReviewAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;


        String url;
        Holder holder;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
                "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
                "Dec",};

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


           /*   holder.star=(RatingBar)convertView.findViewById(stars_review_list) ;
                LayerDrawable star_draw = (LayerDrawable)holder.star.getProgressDrawable();
                star_draw.getDrawable(2).setColorFilter(getResources().getColor(R.color.textcolor), PorterDuff.Mode.SRC_ATOP);*/
                holder.event_price_txt = (TextView) convertView.findViewById(R.id.comment);
                holder.main_layout = (LinearLayout) convertView.findViewById(R.id.main_layout);
                holder.star1 = (ImageView) convertView.findViewById(R.id.star1);
                holder.star2 = (ImageView) convertView.findViewById(R.id.star2);

                holder.star3 = (ImageView) convertView.findViewById(R.id.star3);
                holder.star4 = (ImageView) convertView.findViewById(R.id.star4);

                holder.star5 = (ImageView) convertView.findViewById(R.id.star5);
                convertView.setTag(holder);


            } else {
                holder = (Holder) convertView.getTag();
            }
            Fonts.overrideFonts(context, holder.main_layout);

            url = GlobalConstants.IMAGE_URL + list.get(position).get(GlobalConstants.IMAGE);
            Log.e("urle", url);
            holder.event_price_txt.setText(list.get(position).get("text"));
            holder.event_name_txt.setText(cap(list.get(position).get("username")));
            //holder.star.setRating(Float.parseFloat(list.get(position).get("rating")));
            if (list.get(position).get("rating").contains("1")) {
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star_blank);
                holder.star3.setImageResource(R.drawable.star_blank);
                holder.star4.setImageResource(R.drawable.star_blank);
                holder.star5.setImageResource(R.drawable.star_blank);
            } else if (list.get(position).get("rating").contains("2")) {
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star_blank);
                holder.star4.setImageResource(R.drawable.star_blank);
                holder.star5.setImageResource(R.drawable.star_blank);
            } else if (list.get(position).get("rating").contains("3")) {
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star);
                holder.star4.setImageResource(R.drawable.star_blank);
                holder.star5.setImageResource(R.drawable.star_blank);
            } else if (list.get(position).get("rating").contains("4")) {
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star);
                holder.star4.setImageResource(R.drawable.star);
                holder.star5.setImageResource(R.drawable.star_blank);
            } else if (list.get(position).get("rating").contains("5")) {
                holder.star1.setImageResource(R.drawable.star);
                holder.star2.setImageResource(R.drawable.star);
                holder.star3.setImageResource(R.drawable.star);
                holder.star4.setImageResource(R.drawable.star);
                holder.star5.setImageResource(R.drawable.star);
            } else {
                holder.star1.setImageResource(R.drawable.star_blank);
                holder.star2.setImageResource(R.drawable.star_blank);
                holder.star3.setImageResource(R.drawable.star_blank);
                holder.star4.setImageResource(R.drawable.star_blank);
                holder.star5.setImageResource(R.drawable.star_blank);
            }


            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
            int color1 = generator.getRandomColor();
            TextDrawable drawable2 = TextDrawable.builder()
                    .buildRound(holder.event_name_txt.getText().toString().substring(0, 1).toUpperCase(), color1);

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {
                Picasso.with(context).load(url).placeholder(drawable2).centerCrop().resize(80, 80).transform(new CircleTransform()).into(holder.event_img);
            } else {
                holder.event_img.setImageDrawable(drawable2);
            }
            return convertView;
        }

        class Holder {
            ImageView event_img;
            TextView event_name_txt, event_start_txt, event_price_txt;
            RatingBar star;
            LinearLayout main_layout;
            ImageView star1, star2, star3, star4, star5;

        }


        public String cap(String name) {
            StringBuilder sb = new StringBuilder(name);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }
    }

}
