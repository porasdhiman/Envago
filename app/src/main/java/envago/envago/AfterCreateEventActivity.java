package envago.envago;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by worksdelight on 19/03/17.
 */

public class AfterCreateEventActivity extends Activity {
    TextView let_txtView, go_to_txt, you_can_txt, you_chat_txt, note_txt;
    LinearLayout main_layout;
Global global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_adventure_create_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global=(Global)getApplicationContext();
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        Fonts.overrideFonts1(this, main_layout);
        let_txtView = (TextView) findViewById(R.id.let_txtView);
        Fonts.overrideFontHeavy(this, let_txtView);
        you_can_txt = (TextView) findViewById(R.id.you_can_txt);
        you_can_txt.setText("You can view all your planned adventures \nunder \"My Adventure.\"");
        go_to_txt = (TextView) findViewById(R.id.go_to_txt);
        you_chat_txt = (TextView) findViewById(R.id.you_chat_txt);
        note_txt = (TextView) findViewById(R.id.note_txt);
        Fonts.overrideFonts1(this, you_can_txt);
        Fonts.overrideFonts1(this, go_to_txt);
        Fonts.overrideFonts1(this, you_chat_txt);
        Fonts.overrideFonts1(this, note_txt);
        go_to_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setCurrent_tab(1);
                Intent i = new Intent(AfterCreateEventActivity.this, Tab_Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });
    }
}
