package envago.envago;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




/**
 * Created by worksdelight on 20/01/17.
 */

public class NewCreateAdvantureVideoForm extends FragmentActivity implements View.OnClickListener {

    ViewPager pager;
    TextView bottom_txt;
    ImageView img1, img2, img3,back_button_create;
    String slider_upper_txt[];
    String slider_bottom_txt[];
    MyVideoView videoView1;
    TextView slider_sign_up_btn, slider_fb_btn,document_status_txt;
    LinearLayout slider_sign_in_layout;



    String username_mString, email_mString, id_mString;
    Dialog dialog2;
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }


        setContentView(R.layout.new_create_advanture_vidwo_layout);
        // bottom_txt = (TextView) findViewById(R.id.bottom_txt);
        global = (Global) getApplicationContext();

        document_status_txt=(TextView)findViewById(R.id.document_status_txt);

        //---------------------------------
        pager = (ViewPager) findViewById(R.id.viewpager);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        back_button_create=(ImageView) findViewById(R.id.back_button_create);
        videoView1 = (MyVideoView) findViewById(R.id.videoView1);
slider_sign_up_btn=(TextView)findViewById(R.id.slider_sign_up_btn);
        slider_sign_in_layout = (LinearLayout) findViewById(R.id.slider_sign_in_layout);
        String uriPath = "android.resource://envago.envago/" + R.raw.k2_1408;


        Uri uri = Uri.parse(uriPath);

        videoView1.setVideoURI(uri);
        videoView1.requestFocus();
        videoView1.start();

        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        slider_upper_txt = getResources().getStringArray(R.array.slide1_txt);
        slider_bottom_txt = getResources().getStringArray(R.array.slide1_bottom_txt);

        pager.setAdapter(new CutomePagerAdapter(NewCreateAdvantureVideoForm.this, slider_upper_txt, slider_bottom_txt));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                Log.e("vale", "" + arg0);
                if (arg0 == 2) {

                    img3.setImageResource(R.drawable.not_filled);
                    img2.setImageResource(R.drawable.filled);
                    img1.setImageResource(R.drawable.filled);
                } else if (arg0 == 1) {
                    img3.setImageResource(R.drawable.filled);
                    img2.setImageResource(R.drawable.not_filled);
                    img1.setImageResource(R.drawable.filled);

                } else {
                    img3.setImageResource(R.drawable.filled);
                    img2.setImageResource(R.drawable.filled);
                    img1.setImageResource(R.drawable.not_filled);


                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        if (global.getIsVerified().equalsIgnoreCase("Approved")) {
            slider_sign_up_btn.setOnClickListener(this);
                }
        else {
            slider_sign_in_layout.setOnClickListener(this);
            document_status_txt.setText("Upload document");
                }
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.slider_sign_in_layout:
                Intent j = new Intent(NewCreateAdvantureVideoForm.this, CreateAdventuresActivity.class);
                startActivity(j);

                finish();
                break;
            case R.id.slider_sign_up_btn:
                Intent k = new Intent(NewCreateAdvantureVideoForm.this, NewAdvantureForm.class);
                startActivity(k);

                finish();

                break;

        }
    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }

}