package envago.envago;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by worksdelight on 08/02/17.
 */

public class DocumentImgActivity extends Activity {
    ImageView doc_img;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_iimg_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        preferences =  getSharedPreferences(GlobalConstants.PREFNAME, MODE_PRIVATE);
        editor = preferences.edit();
        doc_img=(ImageView)findViewById(R.id.doc_img);
        if (preferences.getString(GlobalConstants.DOCUMENT, "").contains("http")) {
            Picasso.with(this).load(preferences.getString(GlobalConstants.DOCUMENT, "")).into(doc_img);
        } else {
            if(!preferences.getString(GlobalConstants.DOCUMENT, "").equalsIgnoreCase("")) {
                Picasso.with(this).load(new File(preferences.getString(GlobalConstants.DOCUMENT, ""))).into(doc_img);

                //profilepic.setImageURI(Uri.fromFile(new File(preferences.getString(GlobalConstants.IMAGE, ""))));
            }
        }

    }
}
