package envago.envago;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by vikas on 06-01-2017.
 */

public class AdvantureDescriptionActivity extends Activity {
    EditText desc_editText;
    Global global;
    Button submit_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventaure_desc_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global=(Global)getApplicationContext();
        desc_editText=(EditText)findViewById(R.id.desc_editText);

        submit_button=(Button)findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setDescriptionString(desc_editText.getText().toString());
                finish();
            }
        });
    }
}
