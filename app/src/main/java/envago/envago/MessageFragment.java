package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MessageFragment extends Activity implements OnClickListener {

    //private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private TextView buttonSend;
    private boolean side = false;
    ImageView messageBack;
    Dialog dialog2;
    SharedPreferences sharedPreferences;
    Editor edit;
    Global global;
    ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> listing = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> Demolisting = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map = new HashMap<String, String>();
    NotificationManager mNotificationManager;
    String chatMessage;
    TextView typing_txt;
    int i = 0;
    RelativeLayout main_layout;
    ImageView search_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.message_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        sharedPreferences = getSharedPreferences("chat", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        buttonSend = (TextView) findViewById(R.id.send);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        Fonts.overrideFonts(this, main_layout);
        listView = (ListView) findViewById(R.id.msgview);
        typing_txt = (TextView) findViewById(R.id.typing_txt);
        search_button=(ImageView)findViewById(R.id.search_button);
        search_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setNotifyType("0");
                finish();
            }
        });

        chatText = (EditText) findViewById(R.id.msg);
        /*chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					return sendChatMessage();
				}
				return false;
			}
		});*/
        dialogWindow();
        get_list();
        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //sendChatMessage();
                if (chatText.getText().toString().equalsIgnoreCase(null) || chatText.getText().toString().equalsIgnoreCase("")) {

                } else {
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    chatMessage = chatText.getText().toString();
                    Demolisting = global.getListing();
                    map.put("id", CommonUtils.UserID(MessageFragment.this));
                    map.put("text", chatMessage);
                    map.put("image", global.getUser_url());
                    map.put("created", formatdate2(date));
                    Demolisting.add(map);
                    global.setListing(Demolisting);
                    chatArrayAdapter = new ChatArrayAdapter(MessageFragment.this, global.getListing());
                    listView.setAdapter(chatArrayAdapter);

                    listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


                    // to scroll the list view to bottom on data change
                    chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            listView.setSelection(chatArrayAdapter.getCount() - 1);
                        }
                    });
                    chatArrayAdapter.notifyDataSetChanged();
                    chatText.setText("");
                    get_message();
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(buttonSend.getWindowToken(), 0);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {

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

	/*private boolean sendChatMessage() {
		chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
		chatText.setText("");
		side = !side;
		return true;
	}*/

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //typing_txt.setVisibility(View.VISIBLE);
            i = 1;
            Demolisting.clear();
            get_list();
            // do other stuff here
        }
    };

    @Override
    protected void onResume() {
        this.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
        edit = sharedPreferences.edit();
        edit.putBoolean("message", true);

        edit.commit();
        super.onResume();
    }

    ;

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        this.unregisterReceiver(mMessageReceiver);
        edit = sharedPreferences.edit();
        edit.putBoolean("message", false);

        edit.commit();
        super.onPause();
    }

    public void CallAPI(Context context) {

        Intent intent = new Intent("unique_name");

        // put whatever data you want to send, if any

        // send broadcast
        context.sendBroadcast(intent);
    }

    //--------------------------------List-API----------------------------

    public void get_list() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (i == 0) {
                    dialog2.dismiss();
                }

                Log.e("Categoryyyy", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {

                        // JSONObject data = obj.getJSONObject("data");

                        JSONArray events = obj.getJSONArray("data");
                        for (int i = 0; i < events.length(); i++) {
                            JSONObject arrobj = events.getJSONObject(i);

                            HashMap<String, String> details = new HashMap<>();

                            details.put("text", arrobj.getString("text"));
                            details.put("created", arrobj.getString("created"));

                            JSONObject user_detail = arrobj.getJSONObject("user_detail");
                            details.put("id", user_detail.getString("id"));

                            details.put("image", user_detail.getString("image"));
                            details.put("username", user_detail.getString("username"));
                            listing.add(details);

                        }
                        global.setListing(listing);

                        if (listing.size() > 0) {
                            chatArrayAdapter = new ChatArrayAdapter(MessageFragment.this, global.getListing());

                            listView.setAdapter(chatArrayAdapter);

                            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


                            // to scroll the list view to bottom on data change
                            chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                                @Override
                                public void onChanged() {
                                    super.onChanged();
                                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                                }
                            });
                            chatArrayAdapter.notifyDataSetChanged();
                            i = 0;
                            //typing_txt.setVisibility(View.GONE);



                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog2.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(GlobalConstants.EVENT_ID, global.getEvent_id());

               /* if (getActivity().getIntent().getExtras().getString("status").equalsIgnoreCase("single"))
                {
                    params.put(GlobalConstants.MAIN_CAT_ID, main_id);
                    params.put(GlobalConstants.EVENT_CAT_ID, sub_id);
                }*/

                /*params.put(GlobalConstants.LATITUDE,global.getLat() );
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.RESPONSE_TYPE, "list");*/

                params.put("action", "get_group_chat");

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MessageFragment.this);
        requestQueue.add(cat_request);
    }

    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow() {
        dialog2 = new Dialog(MessageFragment.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.progrees_dialog);
        AVLoadingIndicatorView loaderView = (AVLoadingIndicatorView) dialog2.findViewById(R.id.loader_view);
        loaderView.show();

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        dialog2.show();
    }

    public void get_message() {
        StringRequest cat_request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();
                Log.e("Categoryyyy", s);
                try {
                    JSONObject obj = new JSONObject(s);
                    String res = obj.getString("success");

                    if (res.equalsIgnoreCase("1")) {




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog2.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(MessageFragment.this));
                params.put(GlobalConstants.EVENT_ID, global.getEvent_id());
                params.put("text", chatMessage);

               /* if (getActivity().getIntent().getExtras().getString("status").equalsIgnoreCase("single"))
                {
                    params.put(GlobalConstants.MAIN_CAT_ID, main_id);
                    params.put(GlobalConstants.EVENT_CAT_ID, sub_id);
                }*/

                /*params.put(GlobalConstants.LATITUDE,global.getLat() );
                params.put(GlobalConstants.LONGITUDE, global.getLong());
                params.put(GlobalConstants.RESPONSE_TYPE, "list");*/

                params.put("action", "send_message");

                Log.e("paramsssssssss", params.toString());
                return params;
            }
        };

        cat_request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MessageFragment.this);
        requestQueue.add(cat_request);
    }

}