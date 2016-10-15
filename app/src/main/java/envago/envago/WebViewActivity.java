package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vikas on 04-10-2016.
 */
public class WebViewActivity extends Activity {
    WebView web_view;
    ImageView map_image, back_button;
    TextView web_view_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.web_view_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        web_view = (WebView) findViewById(R.id.web_view);
        web_view_title = (TextView) findViewById(R.id.web_view_title);
        map_image = (ImageView) findViewById(R.id.map_button);
        back_button = (ImageView) findViewById(R.id.back_button);
        map_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WebViewActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        web_view_title.setText(getIntent().getExtras().getString("title"));
        if (web_view_title.getText().equals("PRICING POLICY"))
        {
        web_view.loadUrl("file:///android_asset/pricing.html");

            }
        else if (web_view_title.getText().equals("PRIVACY POLICY"))
        {
            web_view.loadUrl("file:///android_asset/privacypolicy.html");

        }
        else
        {
            web_view.loadUrl("file:///android_asset/termsandconditions.html");
        }
       /* String doc="<iframe src='http://docs.google.com/gview?embedded=true&url=https://www.scribd.com/document/326469804/Pricing?secret_password=kReqjJCpvnq5ZnyxnA2k'"+
                " width='100%' height='100%' style='border: none;'></iframe>";


        web_view.setWebViewClient(new AppWebViewClients());
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.loadData(doc , "text/html",  "UTF-8");
    }

    public class AppWebViewClients extends WebViewClient {



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }*/
    }
}