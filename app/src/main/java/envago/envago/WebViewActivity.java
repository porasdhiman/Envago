package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vikas on 04-10-2016.
 */
public class WebViewActivity extends Activity {
WebView web_view;
    ImageView  map_image,back_button;
    TextView web_view_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_layout);
        web_view=(WebView)findViewById(R.id.web_view);
        web_view_title=(TextView)findViewById(R.id.web_view_title);
        map_image=(ImageView)findViewById(R.id.map_button);
        back_button=(ImageView)findViewById(R.id.back_button);
        map_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(WebViewActivity.this,MapsActivity.class);
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
        web_view.loadUrl("file:///android_asset/pricing.html");
    }
}
