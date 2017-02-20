package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by worksdelight on 20/01/17.
 */

public class AdaventureDisclamierActivity extends Activity {
    EditText desc_editText;
    Global global;
    Button submit_button;
    TextView title;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventaure_desc_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp=getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed=sp.edit();
        global=(Global)getApplicationContext();
        title=(TextView)findViewById(R.id.title) ;
        title.setText("Adventure disclaimer");
        desc_editText=(EditText)findViewById(R.id.desc_editText);
        if(!sp.getString(GlobalConstants.EVENT_DISCLAIMER,"").equalsIgnoreCase("")){
            desc_editText.setText(sp.getString(GlobalConstants.EVENT_DISCLAIMER,""));
        }else{
            desc_editText.setHint("Please enter disclaimer");

        }
        submit_button=(Button)findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desc_editText.getText().toString().length()==0){
                    Toast.makeText(AdaventureDisclamierActivity.this,"Please enter disclaimer",Toast.LENGTH_SHORT).show();
                }else{
                    global.setEvent_disclaimer(desc_editText.getText().toString());
                    ed.putString(GlobalConstants.EVENT_DISCLAIMER,global.getEvent_disclaimer());
                    ed.commit();
                    finish();
                }

            }
        });
    }
}
