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
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by vikas on 06-01-2017.
 */

public class AdvantureDescriptionActivity extends Activity {
    EditText desc_editText;
    Global global;
    Button submit_button;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    ImageView back_button_create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventaure_desc_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        back_button_create=(ImageView)findViewById(R.id.back_button_create);
        sp=getSharedPreferences(GlobalConstants.CREATE_DATA, Context.MODE_PRIVATE);
        ed=sp.edit();
        desc_editText=(EditText)findViewById(R.id.desc_editText);
        global=(Global)getApplicationContext();
        if(!sp.getString(GlobalConstants.EVENT_DESCRIPTION,"").equalsIgnoreCase("")){
            desc_editText.setText(sp.getString(GlobalConstants.EVENT_DESCRIPTION,""));
            desc_editText.setSelection(sp.getString(GlobalConstants.EVENT_DESCRIPTION,"").length());
        }
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit_button=(Button)findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desc_editText.getText().toString().length()==0){
                    Toast.makeText(AdvantureDescriptionActivity.this,"Please enter description",Toast.LENGTH_SHORT).show();
                }else{

                    global.setDescriptionString(desc_editText.getText().toString());
                    ed.putString(GlobalConstants.EVENT_DESCRIPTION,global.getDescriptionString());
                    ed.commit();
                    finish();
                }

            }
        });
    }
}
