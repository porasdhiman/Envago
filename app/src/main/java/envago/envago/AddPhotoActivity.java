package envago.envago;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.AppIndex;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by vikas on 04-01-2017.
 */

public class AddPhotoActivity extends BaseActivity {
    RelativeLayout view_photo_layout;
    ImageView img;
    GridView selected_img_grid;
    private DisplayImageOptions options;
    Global global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        global=(Global)getApplicationContext();
        view_photo_layout=(RelativeLayout)findViewById(R.id.view_photo_layout);
        img=(ImageView)findViewById(R.id.img);
        selected_img_grid=(GridView)findViewById(R.id.selected_img_grid);
        view_photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AddPhotoActivity.this,MultiPhotoSelectActivity.class);
                startActivityForResult(i,1);
            }
        });

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();

    }
    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.

    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            imageLoader.displayImage("file://" + global.getListImg().get(1), img, options, new SimpleImageLoadingListener() {
                public void onLoadingComplete(Bitmap loadedImage) {
                    Animation anim = AnimationUtils.loadAnimation(AddPhotoActivity.this, R.anim.fade_in);
                    img.setAnimation(anim);
                    anim.start();
                }
            });
            selected_img_grid.setAdapter(new SelectedImgAdapter(this,global.getListImg()));
        }

    }
   public class SelectedImgAdapter extends BaseAdapter{
       Context c;
       ArrayList<String> list=new ArrayList<>();
       LayoutInflater inflater;
       Holder holder;
       SelectedImgAdapter(Context c,ArrayList<String> list){
           this.c=c;
           this.list=list;
           inflater=LayoutInflater.from(c);
       }
       @Override
       public int getCount() {
           return list.size();
       }

       @Override
       public Object getItem(int position) {
           return null;
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           holder=new Holder();
           if(convertView==null){
               convertView=inflater.inflate(R.layout.select_row_item_layout,null);
               holder.checked=(CheckBox)convertView.findViewById(R.id.checkBox1);
               holder.selected_img=(ImageView) convertView.findViewById(R.id.imageView1);
               convertView.setTag(holder);
           }else{
               holder = (Holder) convertView.getTag();
           }
           holder.checked.setVisibility(View.GONE);


           imageLoader.displayImage("file://" + list.get(position), holder.selected_img, options, new SimpleImageLoadingListener() {

               public void onLoadingComplete(Bitmap loadedImage) {
                   Animation anim = AnimationUtils.loadAnimation(AddPhotoActivity.this, R.anim.fade_in);
                   holder.selected_img.setAnimation(anim);
                   anim.start();
               }
           });
           return convertView;
       }
       class Holder{
           CheckBox checked;
           ImageView selected_img;
       }
   }
}
