package envago.envago;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by jhang on 9/27/2016.
 */
public class EditProfileActivity extends Activity {


    LinearLayout change_pass;
    TextView username_edit, email_edit, name_edit, phone_edit, paypal_edit, document_edit, about_edit;
    ImageView camera_icon_img,user_tick, email_tick, name_tick, phone_tick, paypal_tick, document_tick, about_tick, back_button;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageView profileimg;
    Button sumbit;
    Dialog dialog2;
    String selectedImagePath = "";
    Dialog camgllry;
    TextView done;
    HttpEntity resEntity;
    String message;

    String username_var, email_var, name_var, phone_var, paypal_var, document_var, about_var;
    /*public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.edit_profile, container, false);
       *//* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }*//*

        preferences =  getSharedPreferences(GlobalConstants.PREFNAME, MODE_PRIVATE);
        editor = preferences.edit();


        profileimg = (ImageView)v. findViewById(R.id.profile_img_editprofile);
        camera_icon_img=(ImageView)v. findViewById(R.id.camera_icon_img);
        //  change_pass = (LinearLayout) findViewById(R.id.body_phone_changepass);
        username_edit = (TextView) v.findViewById(R.id.username_edit);
        email_edit = (TextView) v.findViewById(R.id.email_edit);
        name_edit = (TextView)v. findViewById(R.id.name_edit);
        phone_edit = (TextView) v.findViewById(R.id.phone_edit);
        paypal_edit = (TextView)v. findViewById(R.id.paypal_edit);
        document_edit = (TextView)v. findViewById(R.id.document_edit);
        about_edit = (TextView) v.findViewById(R.id.about_edit);
        done=(TextView)v.findViewById(R.id.done);
        // sumbit = (Button) findViewById(R.id.edit_sumbit_buttn);

      *//*  user_tick = (ImageView) findViewById(R.id.right_img);
        email_tick = (ImageView) findViewById(R.id.right_img_email);
        name_tick = (ImageView) findViewById(R.id.right_img_name);
        phone_tick = (ImageView) findViewById(R.id.right_img_phone);
        paypal_tick = (ImageView) findViewById(R.id.right_img_paypal);
        document_tick = (ImageView) findViewById(R.id.right_img_id);
        about_tick = (ImageView) findViewById(R.id.right_img_about);
        back_button = (ImageView) findViewById(R.id.back_editprofile);
*//*
        getinfo();
        getvalues();

      *//*  change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditProfileActivity.this, ChangePassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
*//**//*
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
*//*
       *//* sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalues();
                dialogWindow();
                editprofile();

            }
        });*//*

        camera_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailog();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalues();
                dialogWindow();
                //editprofile();
                new Thread(null, address_request, "")
                        .start();
            }
        });
return v;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        preferences =  getSharedPreferences(GlobalConstants.PREFNAME, MODE_PRIVATE);
        editor = preferences.edit();


        profileimg = (ImageView) findViewById(R.id.profile_img_editprofile);
        camera_icon_img=(ImageView)findViewById(R.id.camera_icon_img);
        //  change_pass = (LinearLayout) findViewById(R.id.body_phone_changepass);
        username_edit = (TextView)findViewById(R.id.username_edit);
        email_edit = (TextView)findViewById(R.id.email_edit);
        name_edit = (TextView)findViewById(R.id.name_edit);
        phone_edit = (TextView)findViewById(R.id.phone_edit);
        paypal_edit = (TextView)findViewById(R.id.paypal_edit);
        document_edit = (TextView)findViewById(R.id.document_edit);
        about_edit = (TextView)findViewById(R.id.about_edit);
        done=(TextView)findViewById(R.id.done);
        back_button = (ImageView) findViewById(R.id.back_editprofile);
        // sumbit = (Button) findViewById(R.id.edit_sumbit_buttn);

      /*  user_tick = (ImageView) findViewById(R.id.right_img);
        email_tick = (ImageView) findViewById(R.id.right_img_email);
        name_tick = (ImageView) findViewById(R.id.right_img_name);
        phone_tick = (ImageView) findViewById(R.id.right_img_phone);
        paypal_tick = (ImageView) findViewById(R.id.right_img_paypal);
        document_tick = (ImageView) findViewById(R.id.right_img_id);
        about_tick = (ImageView) findViewById(R.id.right_img_about);
        back_button = (ImageView) findViewById(R.id.back_editprofile);
*/
        getinfo();
        getvalues();

      /*  change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditProfileActivity.this, ChangePassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
*/
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /* sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalues();
                dialogWindow();
                editprofile();

            }
        });*/

        camera_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailog();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalues();
                dialogWindow();
                //editprofile();
                new Thread(null, address_request, "")
                        .start();
            }
        });
    }
    //------------------------profile-info-method---------------------------------------------

    public void getinfo() {


        String username = preferences.getString(GlobalConstants.USERNAME, "");
        String email = preferences.getString(GlobalConstants.EMAIL, "");
        String name = preferences.getString(GlobalConstants.FIRST_NAME.concat(GlobalConstants.LAST_NAME), "");
        String phone = preferences.getString(GlobalConstants.CONTACT, "");
        String paypal = preferences.getString(GlobalConstants.PAYPAL, "");
        String document = preferences.getString(GlobalConstants.DOCUMENT, "");
        String about = preferences.getString(GlobalConstants.ABOUT, "");

        if (preferences.getString(GlobalConstants.IMAGE, "").length() == 0) {

        } else {
Log.e("url value",preferences.getString(GlobalConstants.IMAGE, ""));
            if (preferences.getString(GlobalConstants.IMAGE, "").contains("http")) {
                Picasso.with(EditProfileActivity.this).load(preferences.getString(GlobalConstants.IMAGE, "")).into(profileimg);
            } else {
                if(!preferences.getString(GlobalConstants.IMAGE, "").equalsIgnoreCase("")) {
                    profileimg.setImageURI(Uri.fromFile(new File(preferences.getString(GlobalConstants.IMAGE, ""))));
                }
            }
        }

        if (username.length() == 0) {

        } else {
            username_edit.setText(username);
           // user_tick.setImageResource(R.drawable.right_red);
        }

        if (email.length() == 0) {

        } else {
            email_edit.setText(email);

        }

        if (name.length() == 0) {

        } else {
            name_edit.setText(name);

        }

        if (phone.length() == 0) {

        } else {
            phone_edit.setText(phone);

        }

        if (paypal.length() == 0) {

        } else {
            paypal_edit.setText(paypal);

        }


        if (about.length() == 0) {

        } else {
            about_edit.setText(about);

        }
        editor.commit();

    }


    //----------------------------Edit-Profile-API--------------------------------------------


  /*  public void editprofile() {
        StringRequest edit_profile = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();
                try {
                    JSONObject response_obj = new JSONObject(s);

                    String msg = response_obj.getString("success");
                    if (msg.equalsIgnoreCase("1")) {
                        String msg_response = response_obj.getString("msg");


                        Toast.makeText(getActivity(), msg_response, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), response_obj.getString("msg"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog2.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(getActivity()));
                params.put(GlobalConstants.USERNAME, username_var);
                params.put(GlobalConstants.EMAIL, email_var);
                params.put(GlobalConstants.CONTACT, phone_var);
                params.put(GlobalConstants.FIRST_NAME.concat(GlobalConstants.LAST_NAME), name_var);
                params.put(GlobalConstants.PAYPAL, paypal_var);
                params.put(GlobalConstants.DOCUMENT, document_var);
                params.put(GlobalConstants.ABOUT, about_var);
                params.put("action", GlobalConstants.EDIT_PROFILE_ACTION);

                Log.e("Edit profile", params.toString());
                return params;
            }
        };

        edit_profile.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(edit_profile);
    }
*/

    // ------------------------------------------------------upload
    // method---------------
    Runnable address_request = new Runnable() {
        String res = "false";


        @Override
        public void run() {
            try {

                res = doFileUpload();
            } catch (Exception e) {

            }
            Message msg = new Message();
            msg.obj = res;
            address_request_Handler.sendMessage(msg);
        }
    };

    Handler address_request_Handler = new Handler() {
        public void handleMessage(Message msg) {
            String res = (String) msg.obj;
            dialog2.dismiss();
            if (res.equalsIgnoreCase("true")) {
                // terms_dialog.dismiss();
                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(EditProfileActivity.this, message,Toast.LENGTH_SHORT).show();
            }

        }

    };
    private String doFileUpload() {
        String success = "false";

        String urlString = GlobalConstants.URL;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();
            if (selectedImagePath.length() > 0) {
                File file1 = new File(selectedImagePath);
                FileBody bin1 = new FileBody(file1);
                reqEntity.addPart(GlobalConstants.IMAGE, bin1);
            }

            reqEntity.addPart(GlobalConstants.USERID, new StringBody(CommonUtils.UserID(EditProfileActivity.this)));


            reqEntity.addPart(GlobalConstants.USERNAME, new StringBody(username_var));
            reqEntity.addPart(GlobalConstants.EMAIL, new StringBody(email_var));
            reqEntity.addPart(GlobalConstants.CONTACT, new StringBody(phone_var));
            reqEntity.addPart(GlobalConstants.FIRST_NAME.concat(GlobalConstants.LAST_NAME), new StringBody(name_var));
            reqEntity.addPart(GlobalConstants.PAYPAL, new StringBody(paypal_var));
            reqEntity.addPart(GlobalConstants.ABOUT, new StringBody(about_var));
            reqEntity.addPart("action", new StringBody(GlobalConstants.EDIT_PROFILE_ACTION));

            post.setEntity(reqEntity);

            Log.e("params", CommonUtils.UserID(EditProfileActivity.this) + " " + selectedImagePath + " " + username_var + " " + email_var + " " + GlobalConstants.UPLOAD_DOCUMENT_ACTION + " " + phone_var + " " + paypal_var);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();

            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                JSONObject obj = new JSONObject(response_str);
                String status = obj.getString("success");
                if (status.equalsIgnoreCase("1")) {
                    success = "true";
                    message = obj.getString("msg");
                } else {
                    success = "false";
                    message = obj.getString("msg");
                }

                /*JSONObject resp = new JSONObject(response_str);
                String status = resp.getString("status");
                String message = resp.getString("message");
                if (status.equals("1")) {

                    success = "true";

                } else {

                    success = "false";

                }
*/
            }
        } catch (Exception ex) {
        }
        return success;
    }



    public void getvalues() {
        username_var = username_edit.getText().toString();
        if (!username_var.equalsIgnoreCase("") || !username_var.equalsIgnoreCase(null)) {


            editor.putString(GlobalConstants.USERNAME, username_var);

        }
        email_var = email_edit.getText().toString();
        if (!email_var.equalsIgnoreCase("") || !email_var.equalsIgnoreCase(null)) {


            editor.putString(GlobalConstants.EMAIL, email_var);

        }
        name_var = name_edit.getText().toString();
        if (!name_var.equalsIgnoreCase("") || !name_var.equalsIgnoreCase(null)) {


            editor.putString(GlobalConstants.FIRST_NAME.concat(GlobalConstants.LAST_NAME), name_var);


        }
        phone_var = phone_edit.getText().toString();
        if (!phone_var.equalsIgnoreCase("") || !phone_var.equalsIgnoreCase(null)) {

            editor.putString(GlobalConstants.CONTACT, phone_var);

        }
        paypal_var = paypal_edit.getText().toString();
        if (!paypal_var.equalsIgnoreCase("") || !paypal_var.equalsIgnoreCase(null)) {

            editor.putString(GlobalConstants.PAYPAL, paypal_var);

        }

       /* document_var = document_edit.getText().toString();
        if (!document_var.equalsIgnoreCase("") || !document_var.equalsIgnoreCase(null)) {

            editor.putString(GlobalConstants.DOCUMENT, document_var);

        }*/
        about_var = about_edit.getText().toString();
        if (!about_var.equalsIgnoreCase("") || !about_var.equalsIgnoreCase(null)) {

            editor.putString(GlobalConstants.ABOUT, about_var);

        }

        editor.commit();
    }

    public void dialogWindow() {
        dialog2 = new Dialog(EditProfileActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.progrees_dialog);
        AVLoadingIndicatorView loaderView = (AVLoadingIndicatorView) dialog2.findViewById(R.id.loader_view);
        loaderView.show();

        // progress_dialog=ProgressDialog.show(LoginActivity.this,"","Loading...");
        dialog2.show();
    }

    //---------------------------Camera----upload----------------------
    public void dailog() {
        camgllry = new Dialog(EditProfileActivity.this);
        camgllry.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camgllry.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        camgllry.setContentView(R.layout.camera_dialog);

        camgllry.show();

        onclick();

    }

    public void onclick() {
        LinearLayout camera, gallery;

        camera = (LinearLayout) camgllry.findViewById(R.id.camera_layout);
        gallery = (LinearLayout) camgllry.findViewById(R.id.gallery_layout);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 1);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {


                    onSelectFromGalleryResult(data);
                    camgllry.dismiss();

                }
            }
        } else if (requestCode == 1) {
            onCaptureImageResult(data);
            camgllry.dismiss();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(EditProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            camgllry.dismiss();
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileimg.setImageBitmap(bm);
        Uri uri = getImageUri(EditProfileActivity.this, bm);
        try {
            selectedImagePath = getFilePath(EditProfileActivity.this, uri);
            editor.putString(GlobalConstants.IMAGE, selectedImagePath);
            editor.commit();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private void onCaptureImageResult(Intent data) {
        // Uri uri=data.getData();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileimg.setImageBitmap(thumbnail);
        Uri uri = getImageUri(EditProfileActivity.this, thumbnail);
        try {
            selectedImagePath = getFilePath(EditProfileActivity.this, uri);
            editor.putString(GlobalConstants.IMAGE, selectedImagePath);
            editor.commit();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
