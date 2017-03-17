package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikas on 10-11-2016.
 */
public class ConfirmDetailsActivity extends Activity implements View.OnClickListener {
    TextView person_name, date_details, Time_details, person_count, total_money, location_of_event, duration_txtView;
    ImageView minus, add;
    Button procced_btn;
    //-----------------------------------Paypal variable

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "ATu7TrT4HhoSprVHhzQYVhVoI_QrBo_-vUDqSMPWnrGJqvOtSyo4rJ-3mAVn-iaW5EyN7oeI3OjG09Jt";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;
    Global global;
    int i = 1, total, Change_total;
    Date startDate, endDate;
    Dialog dialog2;
    TextView count_txtView;
    String months[] = {" ", "Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct", "Nov",
            "Dec",};
    ImageView search_button;
    TextView remanning_place_txt;
    int r_place, discount, flat;
    RelativeLayout coupon_applied_layout;
    TextView discount_txt, add_coupont_txtView;
    EditText coupon_edit;
    ImageView cancel_view_img;
    String coupon_id = "";
    TextView dis_money;
    String discount_type = "";
    int leftTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.confirm_detail_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global = (Global) getApplicationContext();
        duration_txtView = (TextView) findViewById(R.id.duration_txtView);
        search_button = (ImageView) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discount_type.equalsIgnoreCase("")) {
                    finish();
                } else {
                    dialogWindow();
                    cancelApi();
                }

            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        startDate = new Date();
        endDate = new Date();
        try {
            Log.e("aaraaaa", global.getBookdateArray().toString());
            startDate = dateFormat.parse(global.getBookdateArray().get(Integer.parseInt(getIntent().getExtras().getString("pos"))).get(GlobalConstants.EVENT_START_DATE));
            endDate = dateFormat.parse(global.getBookdateArray().get(Integer.parseInt(getIntent().getExtras().getString("pos"))).get(GlobalConstants.EVENT_END_DATE));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        duration_txtView.setText("("+String.valueOf(getDaysDifference(startDate, endDate)) + " Days)");
        person_name = (TextView) findViewById(R.id.person_name);
        person_name.setText(cap(global.getEvent_name()));
        date_details = (TextView) findViewById(R.id.date_details);
        remanning_place_txt = (TextView) findViewById(R.id.remanning_place_txt);
        String data = global.getBookdateArray().get(Integer.parseInt(getIntent().getExtras().getString("pos"))).get(GlobalConstants.EVENT_END_DATE);
        String split[] = data.split("-");
        String minth = split[1];
        String date = split[2];
        int mm = Integer.parseInt(minth);

        date_details.setText(formatdate2(global.getBookdateArray().get(Integer.parseInt(getIntent().getExtras().getString("pos"))).get(GlobalConstants.EVENT_START_DATE)) + " to " + formatdate2(global.getBookdateArray().get(Integer.parseInt(getIntent().getExtras().getString("pos"))).get(GlobalConstants.EVENT_END_DATE)));
        Time_details = (TextView) findViewById(R.id.Time_details);
        Time_details.setText(global.getEvent_meetin_point() + "\n" + global.getEvent_time());
        person_count = (TextView) findViewById(R.id.person_count);
        person_count.setText(String.valueOf(i) + " person");
        total_money = (TextView) findViewById(R.id.total_money);
        total_money.setText("$" + global.getEvent_price());
        total = Integer.parseInt(global.getEvent_price());
        Change_total = total;
        location_of_event = (TextView) findViewById(R.id.location_of_event);
        location_of_event.setText(global.getEvent_loc());
        minus = (ImageView) findViewById(R.id.minus);
        add = (ImageView) findViewById(R.id.plus);
        procced_btn = (Button) findViewById(R.id.procced_btn);
        procced_btn.setOnClickListener(this);
        add.setOnClickListener(this);
        minus.setOnClickListener(this);
        coupon_applied_layout = (RelativeLayout) findViewById(R.id.coupon_applied_layout);
        discount_txt = (TextView) findViewById(R.id.discount_txt);
        add_coupont_txtView = (TextView) findViewById(R.id.add_coupon_txtView);
        coupon_edit = (EditText) findViewById(R.id.coupon_edit);
        cancel_view_img = (ImageView) findViewById(R.id.cancel_view_img);
        add_coupont_txtView.setOnClickListener(this);
        cancel_view_img.setOnClickListener(this);
        dis_money = (TextView) findViewById(R.id.dis_money);
        coupon_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coupon_applied_layout.setVisibility(View.GONE);
                cancel_view_img.setVisibility(View.VISIBLE);
                total_money.setTextColor(Color.parseColor("#000000"));
                add_coupont_txtView.setVisibility(View.VISIBLE);
                dis_money.setText("");
                total_money.setPaintFlags(0);
                return false;
            }
        });
        //------------------------------Intent for call paypal service----------------------
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        remanning_place_txt.setText("Remaining places " + getIntent().getExtras().getString(GlobalConstants.remaining_places));
        r_place = Integer.parseInt(getIntent().getExtras().getString(GlobalConstants.remaining_places));
    }

    public String cap(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    //---------------------------------------------Payment method----------------------------------
    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(ConfirmDetailsActivity.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
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

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject().toString(4));
                        dialogWindow();
                        joinEventApi();
//                        Toast.makeText(getActivity(), "Registered", Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.e("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.e("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(ConfirmDetailsActivity.this,
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(ConfirmDetailsActivity.this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(ConfirmDetailsActivity.this,
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(ConfirmDetailsActivity.this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus:
                if (i == r_place) {

                } else {
                    i++;
                    person_count.setText(String.valueOf(i) + " person");
                    Change_total = Change_total + total;
                    total_money.setText("$" + String.valueOf(Change_total));


                    if (!discount_type.equalsIgnoreCase("")) {
                        if (discount_type.equalsIgnoreCase("percentage")) {
                            if (discount == 100) {

                                dis_money.setText("$0");
                            } else {
                                int t = Change_total - (Change_total * discount / 100);
                                dis_money.setText("$" + String.valueOf(t));
                            }


                        } else {
                            if (flat > Change_total) {

                                dis_money.setText("$0");

                            } else {
                                int t = Change_total - flat;
                                dis_money.setText("$" + String.valueOf(t));
                            }

                        }
                    }
                }

                break;
            case R.id.minus:
                if (i != 1) {
                    i--;
                    person_count.setText(String.valueOf(i) + " person");
                    if (total != Change_total) {
                        Change_total = Change_total - total;
                        total_money.setText("$" + String.valueOf(Change_total));

                    }
                    if (!discount_type.equalsIgnoreCase("")) {
                        if (discount_type.equalsIgnoreCase("percentage")) {

                            if (discount == 100) {

                                dis_money.setText("$0");
                            } else {
                                int t = Change_total - (Change_total * discount / 100);
                                dis_money.setText("$" + String.valueOf(t));
                            }


                        } else {
                            if (flat > Change_total) {

                                dis_money.setText("$0");

                            } else {
                                int t = Change_total - flat;
                                dis_money.setText("$" + String.valueOf(t));
                            }

                        }
                    }
                }
                break;
            case R.id.procced_btn:
                if (discount_type.equalsIgnoreCase("")) {
                    thingToBuy = new PayPalPayment(new BigDecimal(String.valueOf(Change_total)), "USD",
                            "HeadSet", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(ConfirmDetailsActivity.this, PaymentActivity.class);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                } else {
                    dialogWindow();
                    addCouponWithPaymentApi();

                }

                break;
            case R.id.add_coupon_txtView:
                if (coupon_edit.getText().length() == 0 || coupon_edit.getText().toString().equalsIgnoreCase("")) {
                    coupon_edit.setError("Please enter coupon/voucher code");
                } else {
                    dialogWindow();
                    coponApplyApi();
                }
                /*add_coupont_txtView.setVisibility(View.GONE);
                coupon_applied_layout.setVisibility(View.VISIBLE);*/
                break;
            case R.id.cancel_view_img:
                dis_money.setText("");
                total_money.setTextColor(Color.parseColor("#000000"));
                add_coupont_txtView.setVisibility(View.VISIBLE);
                coupon_applied_layout.setVisibility(View.GONE);
                total_money.setPaintFlags(0);
                break;
        }

    }

    public static int getDaysDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;

        return (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    //-------------------------------join event api-------------------------------
    private void joinEventApi() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();
                        Log.e("responseddd", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                               /* SharedPreferences sp=getSharedPreferences("chat", Context.MODE_PRIVATE);
                                SharedPreferences.Editor ed=sp.edit();
                                ed.putString("chat","chat");
                                ed.commit();*/

                                Intent i = new Intent(ConfirmDetailsActivity.this, Tab_Activity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(i);


                                Toast.makeText(ConfirmDetailsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ConfirmDetailsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(ConfirmDetailsActivity.this));
                params.put(GlobalConstants.EVENT_ID, getIntent().getExtras().getString(GlobalConstants.EVENT_ID));
                params.put("no_of_tickets", String.valueOf(i));
                params.put("total_price", String.valueOf(leftTotal));
                params.put("event_date_id", global.getBookdateArray().get(Integer.parseInt(getIntent().getExtras().getString("pos"))).get(GlobalConstants.ID));


                params.put("action", "join_event");
                Log.e("join_event", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //-------------------------------cancel  api-------------------------------
    private void coponApplyApi() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();
                        Log.e("responseddd", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                                JSONObject data = obj.getJSONObject("data");

                                JSONObject coupon = data.getJSONObject("coupon");
                                coupon_id = coupon.getString("id");
                                if (Change_total > Integer.parseInt(coupon.getString("min_order_value"))) {

                                    discount_type = coupon.getString("discount_type");
                                    if (discount_type.equalsIgnoreCase("percentage")) {

                                        discount = Integer.parseInt(coupon.getString("discount_value"));
                                        discount_txt.setText(coupon_edit.getText().toString() + " applied sucessfully, " + coupon.getString("discount_value") + "% discount");

                                        if (discount == 100) {

                                            dis_money.setText("$0");
                                            total_money.setTextColor(getResources().getColor(R.color.textcolor));
                                            total_money.setPaintFlags(total_money.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        } else {
                                            int t = Change_total - (Change_total * discount / 100);
                                            dis_money.setText("$" + String.valueOf(t));
                                            total_money.setTextColor(getResources().getColor(R.color.textcolor));
                                            total_money.setPaintFlags(total_money.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        }


                                    } else {
                                        flat = Integer.parseInt(coupon.getString("discount_value"));
                                        discount_txt.setText(coupon_edit.getText().toString() + " applied sucessfully, flat $" + coupon.getString("discount_value") + " discount");

                                        if (flat > Change_total) {

                                            dis_money.setText("$0");
                                            total_money.setTextColor(getResources().getColor(R.color.textcolor));
                                            total_money.setPaintFlags(total_money.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                        } else {
                                            int t = Change_total - flat;
                                            dis_money.setText("$" + String.valueOf(t));
                                            total_money.setTextColor(getResources().getColor(R.color.textcolor));
                                            total_money.setPaintFlags(total_money.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        }

                                    }

                                    add_coupont_txtView.setVisibility(View.GONE);
                                    coupon_applied_layout.setVisibility(View.VISIBLE);
                                } else {

                                    coupon_applied_layout.setVisibility(View.VISIBLE);
                                    discount_txt.setText("Coupon not valid");
                                    cancel_view_img.setVisibility(View.GONE);
                                }
                            } else {
                                coupon_applied_layout.setVisibility(View.VISIBLE);
                                discount_txt.setText("Coupon not valid");
                                cancel_view_img.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("coupon_code", coupon_edit.getText().toString());
                params.put("user_id", CommonUtils.UserID(ConfirmDetailsActivity.this));


                params.put("action", "get_coupon_detail");
                Log.e("get_coupon_detail", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //-------------------------------cancel  api-------------------------------
    private void cancelApi() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();
                        Log.e("responseddd", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                                finish();

                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_coupon_id", String.valueOf(coupon_id));


                params.put("action", "cancel_applied_coupon");
                Log.e("cancel_applied_coupon", params.toString());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //-------------------------------cancel  api-------------------------------
    private void addCouponWithPaymentApi() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();
                        Log.e("responseddd", response);
                        try {
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("success");
                            if (status.equalsIgnoreCase("1")) {
                                if (discount_type.equalsIgnoreCase("")) {
                                    thingToBuy = new PayPalPayment(new BigDecimal(String.valueOf(Change_total)), "USD",
                                            "HeadSet", PayPalPayment.PAYMENT_INTENT_SALE);
                                    Intent intent = new Intent(ConfirmDetailsActivity.this, PaymentActivity.class);
                                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                                    leftTotal = Change_total;
                                } else {
                                    leftTotal = Integer.parseInt(dis_money.getText().toString().replace("$", "").trim());
                                    Log.e("total", String.valueOf(leftTotal));
                                    if (leftTotal == 0) {
                                        dialogWindow();
                                        joinEventApi();
                                    } else {
                                        thingToBuy = new PayPalPayment(new BigDecimal(String.valueOf(leftTotal)), "USD",
                                                "HeadSet", PayPalPayment.PAYMENT_INTENT_SALE);
                                        Intent intent = new Intent(ConfirmDetailsActivity.this, PaymentActivity.class);
                                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                                        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                                    }

                                }


                            } else {
                                Toast.makeText(ConfirmDetailsActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", CommonUtils.UserID(ConfirmDetailsActivity.this));
                params.put("coupon_id", String.valueOf(coupon_id));
                params.put("actual_price", total_money.getText().toString().replace("$", "").trim());
                params.put("discounted_price", dis_money.getText().toString().replace("$", "").trim());

                if (discount_type.equalsIgnoreCase("percentage")) {
                    params.put("discount", String.valueOf(discount));


                } else {
                    params.put("discount", String.valueOf(flat));


                }


                params.put("action", "apply_coupon");
                Log.e("apply_coupon", params.toString() + " " + String.valueOf(discount));
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //---------------------------Progrees Dialog-----------------------
    public void dialogWindow() {
        dialog2 = new Dialog(this);
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
}
