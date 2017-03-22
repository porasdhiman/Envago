package envago.envago;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;


/**
 * Created by worksdelight on 20/01/17.
 */

public class NewCreateAdvantureVideoForm extends FragmentActivity implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    ViewPager pager;
    TextView bottom_txt;
    ImageView img1, img2, img3, back_button_create;
    String slider_upper_txt[];
    String slider_bottom_txt[];
    TextureVideoView videoView1;
    TextView slider_sign_up_btn, slider_fb_btn, document_status_txt;
    LinearLayout slider_sign_in_layout;


    String username_mString, email_mString, id_mString;
    Dialog dialog2;
    Global global;

    //------Video controle------------

    private static final String TAG = "VideoPlayer";
    private SurfaceHolder holder;
    private ProgressBar progressBarWait;

    private MediaPlayer player;
    private Timer updateTimer;
    String uriPath;
RelativeLayout main_layout;
    DisplayMetrics dm;
    SurfaceView sur_View;
    MediaController media_Controller;
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
        main_layout=(RelativeLayout)findViewById(R.id.main_layout);
        Fonts.overrideFonts(this,main_layout);
        document_status_txt = (TextView) findViewById(R.id.document_status_txt);

        //---------------------------------
     /*   pager = (ViewPager) findViewById(R.id.viewpager);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);*/
        back_button_create = (ImageView) findViewById(R.id.back_view);
        videoView1 = (TextureVideoView) findViewById(R.id.videoView1);

        slider_sign_up_btn = (TextView) findViewById(R.id.slider_sign_up_btn);
        slider_sign_in_layout = (LinearLayout) findViewById(R.id.slider_sign_in_layout);


        uriPath = "android.resource://envago.envago/" + R.raw.dd;
        TextView slider_txt = (TextView) findViewById(R.id.slider_txt);
        TextView slider_txt_bottom = (TextView) findViewById(R.id.slider_bottom_txt);


      /*  holder = videoView1.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setScreenOnWhilePlaying(true);*/
        //  player.setDisplay(holder);

        Uri uri = Uri.parse(uriPath);


        videoView1.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
        videoView1.setDataSource(this,uri);
        videoView1.setLooping(true);
        videoView1.play();

        slider_upper_txt = getResources().getStringArray(R.array.slide1_txt);
        slider_bottom_txt = getResources().getStringArray(R.array.slide1_bottom_txt);
        slider_txt.setText("Become an \nAdventure Planner");
        Fonts.overrideFontHeavy(NewCreateAdvantureVideoForm.this,slider_txt);
        slider_txt_bottom.setText(slider_bottom_txt[0]);
       /* pager.setAdapter(new CutomePagerAdapter(NewCreateAdvantureVideoForm.this, slider_upper_txt, slider_bottom_txt));
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
        });*/
        slider_sign_up_btn.setOnClickListener(this);
        slider_sign_in_layout.setOnClickListener(this);
        if (global.getIsVerified().equalsIgnoreCase("0")) {
            slider_sign_up_btn.setText("Upload Document again");
            document_status_txt.setText("Document rejected");



        }else  if (global.getIsVerified().equalsIgnoreCase("1")) {
            slider_sign_up_btn.setText("Create an Adventure Now");
            document_status_txt.setText("You are already registered as a planner");
        }
        else if (global.getIsVerified().equalsIgnoreCase("2")) {
            slider_sign_up_btn.setText("Upload Document");
            document_status_txt.setText("A new Planner? Register");
        }
        else {

            slider_sign_up_btn.setText("Pending Approval");
            document_status_txt.setText("Document verification is in process");
        }
        back_button_create.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.slider_sign_in_layout:
                if(global.isVerified.equalsIgnoreCase("0")){
                    Intent j = new Intent(NewCreateAdvantureVideoForm.this, CreateAdventuresActivity.class);
                    startActivity(j);

                    finish();
                }else  if(global.isVerified.equalsIgnoreCase("2")){
                    Intent j = new Intent(NewCreateAdvantureVideoForm.this, CreateAdventuresActivity.class);
                    startActivity(j);

                    finish();
                }
                else  if(global.isVerified.equalsIgnoreCase("1")){
                    Intent k = new Intent(NewCreateAdvantureVideoForm.this, NewAdvantureForm.class);
                    startActivity(k);

                    finish();
                }else{

                }


                break;
            case R.id.back_view:
                finish();

                break;
            case R.id.slider_sign_up_btn:

                if(global.isVerified.equalsIgnoreCase("0")){
                    Intent j = new Intent(NewCreateAdvantureVideoForm.this, CreateAdventuresActivity.class);
                    startActivity(j);

                    finish();
                }else  if(global.isVerified.equalsIgnoreCase("2")){
                    Intent j = new Intent(NewCreateAdvantureVideoForm.this, CreateAdventuresActivity.class);
                    startActivity(j);

                    finish();
                }
                else  if(global.isVerified.equalsIgnoreCase("1")){
                    Intent k = new Intent(NewCreateAdvantureVideoForm.this, NewAdvantureForm.class);
                    startActivity(k);

                    finish();
                }else{

                }


                break;

        }
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    //--------Video method---------
    private void playVideo() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    player.setDataSource(NewCreateAdvantureVideoForm.this, Uri.parse(uriPath));
                    player.prepareAsync();
                              /*player.setDataSource(uriPath);
                              player.prepare();*/
                } catch (Exception e) { // I can split the exceptions to get which error i need.

                    Log.i(TAG, "Error");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    public void surfaceCreated(SurfaceHolder holder) {
        playVideo();
        player.setDisplay(holder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    //prepare the video
    public void onPrepared(MediaPlayer mp) {


        // Adjust the size of the video
        // so it fits on the screen
        int surfaceView_Width = videoView1.getWidth();
        int surfaceView_Height = videoView1.getHeight();

        float video_Width = player.getVideoWidth();
        float video_Height = player.getVideoHeight();

        float ratio_width = surfaceView_Width / video_Width;
        float ratio_height = surfaceView_Height / video_Height;
        float aspectratio = video_Width / video_Height;

        ViewGroup.LayoutParams layoutParams = videoView1.getLayoutParams();

        if (ratio_width > ratio_height) {
            layoutParams.width = (int) (surfaceView_Height * aspectratio);
            layoutParams.height = surfaceView_Height;
        } else {
            layoutParams.width = surfaceView_Width;
            layoutParams.height = (int) (surfaceView_Width / aspectratio);
        }

        videoView1.setLayoutParams(layoutParams);


        if (!player.isPlaying()) {
            player.start();
        }
        //videoView1.setClickable(true);
    }

    // callback when the video is over
    public void onCompletion(MediaPlayer mp) {
        player.setLooping(true);
    }

}