package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by worksdelight on 25/01/17.
 */

public class BookDateActivity extends Activity {
    ImageView cancel;
    ListView date_listView;
    String[] days = new String[] {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    String months[] = { " ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec", };
    Global global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.repeated_date_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global=(Global)getApplicationContext();
        cancel = (ImageView) findViewById(R.id.cancel);
        date_listView = (ListView) findViewById(R.id.date_listView);
        date_listView.setAdapter(new BookDateAdapter(this,global.getBookdateArray()));



    }


    class BookDateAdapter extends BaseAdapter {
        Context c;
        LayoutInflater inflatore;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        BookDateAdapter(Context c, ArrayList<HashMap<String, String>> list) {
            this.c = c;
            this.list = list;
            inflatore = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return list.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView=inflatore.inflate(R.layout.book_date_list_item,null);
            TextView date_txtView=(TextView)convertView.findViewById(R.id.date_txtView);
            TextView book_txtView=(TextView)convertView.findViewById(R.id.book_txtview);

            String data = list.get(position).get(GlobalConstants.EVENT_START_DATE);
            String split[] = data.split("-");
            String minth = split[1];
            String date = split[2];
            int mm = Integer.parseInt(minth);




            String data1 = list.get(position).get(GlobalConstants.EVENT_END_DATE);
            String split1[] = data1.split("-");
            String minth1 = split1[1];
            String date1 = split1[2];
            int mm1 = Integer.parseInt(minth1);
            Calendar calendar = Calendar.getInstance();
            Date startdateObj = new Date();
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startdateObj = date_format.parse(list.get(position).get(GlobalConstants.EVENT_START_DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(startdateObj);
            Calendar calendar2 = Calendar.getInstance();

            Date enddateObj = new Date();
            SimpleDateFormat date_format1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                enddateObj = date_format1.parse(list.get(position).get(GlobalConstants.EVENT_END_DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar2.setTime(enddateObj);

            date_txtView.setText( days[calendar.get(Calendar.DAY_OF_WEEK)-1]+","+months[mm]+ " " + date + " " + split[0]+"-"+ days[calendar2.get(Calendar.DAY_OF_WEEK)-1]+","+months[mm1]+ " " + date1+ " " + split1[0]);
            book_txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(c,ConfirmDetailsActivity.class);
                    i.putExtra(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                    i.putExtra("pos", String.valueOf(position));
                    startActivity(i);
                }
            });

            return convertView;
        }
    }

}
