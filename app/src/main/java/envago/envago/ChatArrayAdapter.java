package envago.envago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ChatArrayAdapter extends BaseAdapter {

    private TextView chatText, created_date;
    private ArrayList<HashMap<String, String>> chatMessageList = new ArrayList<>();
    private Context context;
    TextView user_txt;

    Global global;
String url;
    ChatArrayAdapter(Context context, ArrayList<HashMap<String, String>> chatMessageList) {

        global = (Global) context.getApplicationContext();
        this.context = context;
        this.chatMessageList = chatMessageList;
    }


    public int getCount() {
        return chatMessageList.size();
    }

    public Object getItem(int index) {
        return index;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String user_id = chatMessageList.get(position).get("id");


        if (user_id.equalsIgnoreCase(CommonUtils.UserID(context))) {
            row = inflater.inflate(R.layout.right, parent, false);
            global.setUser_url(chatMessageList.get(position).get("image"));

        } else {
            row = inflater.inflate(R.layout.left, parent, false);
            user_txt = (TextView) row.findViewById(R.id.user_txt);
            user_txt.setText(chatMessageList.get(position).get("username"));
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageList.get(position).get("text"));
        created_date = (TextView) row.findViewById(R.id.date_txt);

       /* ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(chatMessageList.get(position).get("username").substring(0, 1), color1);
        url = GlobalConstants.IMAGE_URL + chatMessageList.get(position).get("image");
        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {*/

            //Picasso.with(context).load(GlobalConstants.IMAGE_URL + chatMessageList.get(position).get("image")).placeholder(R.drawable.user_back).transform(new CircleTransform()).resize(25, 25).into(user_img);
       /* } else {
            user_img.setImageDrawable(drawable2);
        }*/
        created_date.setText(formatdate2(chatMessageList.get(position).get("created").split(" ")[0]));
        return row;
    }

    public String formatdate2(String fdate) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat d = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }

}
