package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vikas on 10-11-2016.
 */
public class ConfirmDetailsActivity extends Activity implements View.OnClickListener {
    TextView person_name, date_details, Time_details, person_count, total_money, location_of_event,duration_txtView;
    ImageView minus, add;
    Button procced_btn;
    //-----------------------------------Paypal variable

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "Af64A4qjlISjmdbpxvgMs47i5Yl_Quuh8u4Ah-tfxo_DRH2ya-9MQpIEY2XLElcraeq2ox8Xvx6kUJRx";

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
    int i = 1,total,Change_total;
    Date startDate,endDate;
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
        duration_txtView=(TextView)findViewById(R.id.duration_txtView);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
         startDate=new Date();
         endDate=new Date();
        try {

            startDate=dateFormat.parse(global.getEvent_start_date());
            endDate=dateFormat.parse(global.getEvent_end_date());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        duration_txtView.setText(String.valueOf(getDaysDifference(startDate,endDate))+" Days");
        person_name = (TextView) findViewById(R.id.person_name);
        person_name.setText(global.getEvent_name());
        date_details = (TextView) findViewById(R.id.date_details);
        date_details.setText(global.getEvent_start_date() + " to " + global.getEvent_end_date());
        Time_details = (TextView) findViewById(R.id.Time_details);
        Time_details.setText(global.getEvent_time());
        person_count = (TextView) findViewById(R.id.person_count);
        person_count.setText(String.valueOf(i) + "Person");
        total_money = (TextView) findViewById(R.id.total_money);
        total_money.setText("$" + global.getEvent_price());
        total=Integer.parseInt(global.getEvent_price());
        Change_total=total;
        location_of_event = (TextView) findViewById(R.id.location_of_event);
        location_of_event.setText(global.getEvent_loc());
        minus = (ImageView) findViewById(R.id.minus);
        add = (ImageView) findViewById(R.id.plus);
        procced_btn = (Button) findViewById(R.id.procced_btn);
        procced_btn.setOnClickListener(this);
        add.setOnClickListener(this);
        minus.setOnClickListener(this);

        //------------------------------Intent for call paypal service----------------------
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    //---------------------------------------------Payment method----------------------------------
    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(ConfirmDetailsActivity.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
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
                i++;
                person_count.setText(String.valueOf(i) + "Person");
                Change_total=Change_total+total;
                total_money.setText("$" + String.valueOf(Change_total));
                break;
            case R.id.minus:
                if (i != 1) {
                    i--;
                    person_count.setText(String.valueOf(i) + "Person");
                    if(total!=Change_total){
                        Change_total=Change_total-total;
                        total_money.setText("$" + String.valueOf(Change_total));
                    }
                }
                break;
            case R.id.procced_btn:
                thingToBuy = new PayPalPayment(new BigDecimal("10"), "USD",
                        "HeadSet", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(ConfirmDetailsActivity.this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                break;
        }

    }
    public static int getDaysDifference(Date fromDate, Date toDate)
    {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }
}
