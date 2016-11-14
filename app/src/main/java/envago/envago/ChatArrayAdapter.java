package envago.envago;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ChatArrayAdapter extends BaseAdapter{

    private TextView chatText;
    private ArrayList<HashMap<String,String>> chatMessageList = new ArrayList<>();
    private Context context;



    ChatArrayAdapter(Context context, ArrayList<HashMap<String, String>> chatMessageList) {


        this.context = context;
        this.chatMessageList=chatMessageList;
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
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String user_id=chatMessageList.get(position).get("id");

        Log.e("user id",user_id);
        if (user_id.equalsIgnoreCase(CommonUtils.UserID(context))) {
            row = inflater.inflate(R.layout.right, parent, false);

        }else{
            row = inflater.inflate(R.layout.left, parent, false);

        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageList.get(position).get("text"));
        return row;
    }
}
