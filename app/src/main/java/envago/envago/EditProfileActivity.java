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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jhang on 9/27/2016.
 */
public class EditProfileActivity extends Activity {


    LinearLayout change_pass;
    EditText username_edit, email_edit, name_edit, phone_edit, paypal_edit, document_edit, about_edit;
    ImageView user_tick, email_tick, name_tick, phone_tick, paypal_tick, document_tick, about_tick, back_button;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CircleImageView profileimg;
    Button sumbit;
    Dialog dialog2;
    String selectedImagePath = "";
    Dialog camgllry;


    String username_var, email_var, name_var, phone_var, paypal_var, document_var, about_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        preferences = getSharedPreferences(GlobalConstants.PREFNAME, MODE_PRIVATE);
        editor = preferences.edit();


        profileimg = (CircleImageView) findViewById(R.id.profile_img_editprofile);
        change_pass = (LinearLayout) findViewById(R.id.body_phone_changepass);
        username_edit = (EditText) findViewById(R.id.username_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);
        name_edit = (EditText) findViewById(R.id.name_edit);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        paypal_edit = (EditText) findViewById(R.id.paypal_edit);
        document_edit = (EditText) findViewById(R.id.document_edit);
        about_edit = (EditText) findViewById(R.id.about_edit);
        sumbit = (Button) findViewById(R.id.edit_sumbit_buttn);

        user_tick = (ImageView) findViewById(R.id.right_img);
        email_tick = (ImageView) findViewById(R.id.right_img_email);
        name_tick = (ImageView) findViewById(R.id.right_img_name);
        phone_tick = (ImageView) findViewById(R.id.right_img_phone);
        paypal_tick = (ImageView) findViewById(R.id.right_img_paypal);
        document_tick = (ImageView) findViewById(R.id.right_img_id);
        about_tick = (ImageView) findViewById(R.id.right_img_about);
        back_button = (ImageView) findViewById(R.id.back_editprofile);

        getinfo();
        getvalues();

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditProfileActivity.this, ChangePassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
            }
        });

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalues();
                dialogWindow();
                editprofile();

            }
        });

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailog();
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

            if (preferences.getString(GlobalConstants.IMAGE, "").contains("http")) {
                Picasso.with(this).load(preferences.getString(GlobalConstants.IMAGE, ""));
            } else {
                profileimg.setImageURI(Uri.fromFile(new File(preferences.getString(GlobalConstants.IMAGE, ""))));
            }
        }

        if (username.length() == 0) {

        } else {
            username_edit.setText(username);
            user_tick.setImageResource(R.drawable.right_red);
        }

        if (email.length() == 0) {

        } else {
            email_edit.setText(email);
            email_tick.setImageResource(R.drawable.right_red);
        }

        if (name.length() == 0) {

        } else {
            name_edit.setText(name);
            name_tick.setImageResource(R.drawable.right_red);
        }

        if (phone.length() == 0) {

        } else {
            phone_edit.setText(phone);
            phone_tick.setImageResource(R.drawable.right_red);
        }

        if (paypal.length() == 0) {

        } else {
            paypal_edit.setText(paypal);
            paypal_tick.setImageResource(R.drawable.right_red);
        }

        if (document.length() == 0) {

        } else {
            document_edit.setText(document);
            document_tick.setImageResource(R.drawable.right_red);
        }
        if (about.length() == 0) {

        } else {
            about_edit.setText(about);
            about_tick.setImageResource(R.drawable.right_red);
        }

        editor.commit();

    }


    //----------------------------Edit-Profile-API--------------------------------------------


    public void editprofile() {
        StringRequest edit_profile = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();
                try {
                    JSONObject response_obj = new JSONObject(s);

                    String msg = response_obj.getString("success");
                    if (msg.equalsIgnoreCase("1")) {
                        String msg_response = response_obj.getString("msg");


                        Toast.makeText(getApplicationContext(), msg_response, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response_obj.getString("msg"), Toast.LENGTH_SHORT).show();

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
                params.put(GlobalConstants.USERID, CommonUtils.UserID(EditProfileActivity.this));
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

        edit_profile.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(edit_profile);
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

        document_var = document_edit.getText().toString();
        if (!document_var.equalsIgnoreCase("") || !document_var.equalsIgnoreCase(null)) {

            editor.putString(GlobalConstants.DOCUMENT, document_var);

        }
        about_var = about_edit.getText().toString();
        if (!about_var.equalsIgnoreCase("") || !about_var.equalsIgnoreCase(null)) {

            editor.putString(GlobalConstants.ABOUT, about_var);

        }

        editor.commit();
    }

    public void dialogWindow() {
        dialog2 = new Dialog(this);
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
        camgllry = new Dialog(this);
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
