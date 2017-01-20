package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by worksdelight on 20/01/17.
 */

public class AdaventureDisclamierActivity extends Activity {
    EditText desc_editText;
    Global global;
    Button submit_button;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventaure_desc_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global=(Global)getApplicationContext();
        title=(TextView)findViewById(R.id.title) ;
        title.setText("Adventure Note");
        desc_editText=(EditText)findViewById(R.id.desc_editText);
        desc_editText.setHint("Please enter Note");
        submit_button=(Button)findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setEvent_disclaimer(desc_editText.getText().toString());
                finish();
            }
        });
    }
}
