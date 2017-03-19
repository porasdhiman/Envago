package envago.envago;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CutomePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    String[] upper_txt;
    String[] bottom_txt;

    public CutomePagerAdapter(Context context, String[] upper_txt, String[] bottom_txt) {
        mContext = context;
        this.upper_txt = upper_txt;
        this.bottom_txt = bottom_txt;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return upper_txt.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.splash_multiple_image, container, false);

      /*  ImageView center_txtView = (ImageView) itemView.findViewById(R.id.pager_img);
        center_txtView.setBackgroundResource(img[position]);
        container.addView(itemView);
*/
        TextView slider_txt = (TextView) itemView.findViewById(R.id.slider_txt);
        TextView slider_txt_bottom = (TextView) itemView.findViewById(R.id.slider_bottom_txt);
        Fonts.overrideFontHeavy(mContext, slider_txt);
        Fonts.overrideFonts(mContext, slider_txt_bottom);
        slider_txt.setText(upper_txt[position]);
        slider_txt_bottom.setText(bottom_txt[position]);
        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
