package envago.envago;

import android.app.Activity;
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
    ImageView back_button;
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

        back_button = (ImageView) findViewById(R.id.back_button);

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

    }
}