package envago.envago;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vikas on 04-01-2017.
 */

public class AddPhotoActivity extends BaseActivity {
    RelativeLayout view_photo_layout;
    ImageView img, cancel_icon_img;
    GridView selected_img_grid;
    private DisplayImageOptions options;
    Global global;
    TextView button1;
    boolean iSEdit = false;
    int i=1,size;
ImageView back_button_create;
    Button submit_button;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    ArrayList<String> sharedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }
        sp=getSharedPreferences(GlobalConstants.CREATE_DATA,Context.MODE_PRIVATE);
        ed=sp.edit();
        global = (Global) getApplicationContext();
        submit_button=(Button)findViewById(R.id.submit_button);
        back_button_create=(ImageView)findViewById(R.id.back_button_create);
        button1 = (TextView) findViewById(R.id.button1);
        view_photo_layout = (RelativeLayout) findViewById(R.id.view_photo_layout);
        img = (ImageView) findViewById(R.id.img);
        cancel_icon_img = (ImageView) findViewById(R.id.cancel_icon_img);
        selected_img_grid = (GridView) findViewById(R.id.selected_img_grid);
        back_button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddPhotoActivity.this, MultiPhotoSelectActivity.class);
                startActivityForResult(i, 1);
            }
        });

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button1.getText().toString().equalsIgnoreCase("edit")) {
                    button1.setText("Done");
                    cancel_icon_img.setVisibility(View.VISIBLE);
                    iSEdit=true;
                    size=global.getListImg().size();
                    selected_img_grid.setAdapter(new SelectedImgAdapter(AddPhotoActivity.this, global.getListImg()));
CommonUtils.setGridViewHeightBasedOnChildren(selected_img_grid,3);
                } else {

                }
            }
        });
        cancel_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if(global.getListImg().size() != 0) {
                   global.getListImg().remove(0);
                   if (global.getListImg().size() == 0) {

                       img.setImageResource(R.drawable.add_photo);
                       iSEdit = false;
                       cancel_icon_img.setVisibility(View.GONE);
                   } else {
                       imageLoader.displayImage("file://" + global.getListImg().get(0), img, options, new SimpleImageLoadingListener() {
                           public void onLoadingComplete(Bitmap loadedImage) {
                               Animation anim = AnimationUtils.loadAnimation(AddPhotoActivity.this, R.anim.fade_in);
                               img.setAnimation(anim);
                               anim.start();
                           }
                       });
                       selected_img_grid.setAdapter(new SelectedImgAdapter(AddPhotoActivity.this, global.getListImg()));
                       CommonUtils.setGridViewHeightBasedOnChildren(selected_img_grid,3);
                   }
               }else{
                   img.setImageResource(R.drawable.add_photo);
                   iSEdit = false;
                   cancel_icon_img.setVisibility(View.GONE);
               }
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(!sp.getString(GlobalConstants.IMAGEDATA,"").equalsIgnoreCase("")){
            sharedData= new ArrayList<String>(Arrays.asList(sp.getString(GlobalConstants.IMAGEDATA,"").substring(1,sp.getString(GlobalConstants.IMAGEDATA,"").length()-1).split(","))) ;
            Log.e("shared data",sharedData.toString());
            global.setListImg(sharedData);
            button1.setVisibility(View.VISIBLE);
            imageLoader.displayImage("file://" + global.getListImg().get(0), img, options, new SimpleImageLoadingListener() {
                public void onLoadingComplete(Bitmap loadedImage) {

                }
            });

            selected_img_grid.setAdapter(new SelectedImgAdapter(AddPhotoActivity.this, global.getListImg()));
            CommonUtils.setGridViewHeightBasedOnChildren(selected_img_grid,3);
        }

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
        if (requestCode == 1) {
            if(global.getListImg().size()>0) {
                button1.setVisibility(View.VISIBLE);
                imageLoader.displayImage("file://" + global.getListImg().get(0), img, options, new SimpleImageLoadingListener() {
                    public void onLoadingComplete(Bitmap loadedImage) {
                        Animation anim = AnimationUtils.loadAnimation(AddPhotoActivity.this, R.anim.fade_in);
                        img.setAnimation(anim);
                        anim.start();
                    }
                });
                selected_img_grid.setAdapter(new SelectedImgAdapter(this, global.getListImg()));
                CommonUtils.setGridViewHeightBasedOnChildren(selected_img_grid, 3);
            }
        }

    }

    public class SelectedImgAdapter extends BaseAdapter {
        Context c;
        ArrayList<String> list = new ArrayList<>();
        LayoutInflater inflater;
        Holder holder;
        boolean value;

        SelectedImgAdapter(Context c, ArrayList<String> list) {
            this.c = c;
            this.list = list;

            inflater = LayoutInflater.from(c);
        }

        @Override
        public int getViewTypeCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            holder = new Holder();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.select_row_item_layout, null);
                holder.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.selected_img = (ImageView) convertView.findViewById(R.id.imageView1);
                holder.cancel_img = (ImageView) convertView.findViewById(R.id.cancel_img);
                holder.cancel_img.setTag(position);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            if (iSEdit == true) {
                holder.cancel_img.setVisibility(View.VISIBLE);

            }
            holder.checked.setVisibility(View.GONE);

            holder.cancel_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);


                    notifyDataSetChanged();
                    if (list.size() != 0) {
                        imageLoader.displayImage("file://" + global.getListImg().get(0), img, options, new SimpleImageLoadingListener() {
                            public void onLoadingComplete(Bitmap loadedImage) {
                            }
                        });
                    }else{
                        img.setImageResource(R.drawable.add_photo);
                        iSEdit = false;
                        cancel_icon_img.setVisibility(View.GONE);
                    }

                }
            });
            imageLoader.displayImage("file://" + list.get(position), holder.selected_img, options, new SimpleImageLoadingListener() {

                public void onLoadingComplete(Bitmap loadedImage) {
                    Animation anim = AnimationUtils.loadAnimation(AddPhotoActivity.this, R.anim.fade_in);
                    holder.selected_img.setAnimation(anim);
                    anim.start();
                }
            });
            return convertView;
        }

        class Holder {
            CheckBox checked;
            ImageView selected_img, cancel_img;
        }
    }
}
