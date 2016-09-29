package envago.envago;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by poras on 17-06-2016.
 */
public class SlidePageActivity extends FragmentActivity {

    ViewPager pager;
    TextView bottom_txt;
    ImageView img1, img2, img3;
    int imgArray[] = {R.drawable.slider_one, R.drawable.slider_two, R.drawable.slider_three};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        setContentView(R.layout.slider_pager_layout);
        bottom_txt = (TextView) findViewById(R.id.bottom_txt);
        pager = (ViewPager) findViewById(R.id.viewpager);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        pager.setAdapter(new CutomePagerAdapter(SlidePageActivity.this, imgArray));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                Log.e("vale", "" + arg0);
                if (arg0 == 2) {
                    bottom_txt.setVisibility(View.VISIBLE);
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
        bottom_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent UserRegister = new Intent(SlidePageActivity.this, LoginActivity.class);
                startActivity(UserRegister);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();


            }
        });

    }


}
