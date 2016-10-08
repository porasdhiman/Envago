package envago.envago;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;


@SuppressWarnings("deprecation")
public class Tab_Activity extends TabActivity {

    TabHost tabHost;
    SharedPreferences sharedPreferences;
    Editor mEditor;
    ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        tabHost = getTabHost();

        setTabs();
        tabHost.setCurrentTab(1);
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            // tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.action_back);


            ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);
            //vg.setBackgroundColor(Color.TRANSPARENT);

            //TextView tv1 = (TextView) vg.getChildAt(1);
            @SuppressWarnings("unused")
            ImageView img = (ImageView) vg.getChildAt(0);

			/*if (i == 0) {
                tv1.setTextColor(Color.parseColor("#0076be"));

			} else {
				tv1.setTextColor(Color.parseColor("#999999"));
			}*/
        }

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                int index = tabHost.getCurrentTab();

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);

                    vg.setBackgroundColor(Color.TRANSPARENT);

                    //TextView tv1 = (TextView) vg.getChildAt(1);
                    @SuppressWarnings("unused")
                    ImageView img = (ImageView) vg.getChildAt(0);
					/*if (i == index) {

						tv1.setTextColor(Color.parseColor("#0076be"));

					}

					else {

						tv1.setTextColor(Color.parseColor("#999999"));

					}*/

                }

            }

        });

    }

    private void setTabs() {

        addTab(R.drawable.tab_add, AddActivity.class);

        addTab(R.drawable.tab_home, HomeActivity.class);

        addTab(R.drawable.tab_favorite, FavoriteActivity.class);

        addTab(R.drawable.tab_user, UserActivity.class);

    }

    private void addTab(int drawableId, Class<?> c) {

        Intent intent = new Intent(this, c);

        TabHost.TabSpec spec = tabHost.newTabSpec("tab");

        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, getTabWidget(), false);

		/*TextView title = (TextView) v.findViewById(R.id.title);

		title.setText(labelId);
*/
        ImageView icon = (ImageView) v.findViewById(R.id.icon);

        icon.setImageResource(drawableId);

        spec.setIndicator(v);

        spec.setContent(intent);

        tabHost.addTab(spec);

    }






}