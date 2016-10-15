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
import android.telecom.Call;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
 * Created by jhang on 9/30/2016.
 */
public class CallVolunteers extends Activity implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {

    EditText name, address, about, paypal;
    CheckBox individual, group, licensed, nlicensed;
    ImageView back_button, document_pic;
    Button submit;
    RelativeLayout upload_id;
    String selectedImagePath = "";
    Dialog camgllry;
    String usertype, license;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    HttpEntity resEntity;
    String message;
    Dialog dialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.call_volunteers);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        preferences = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        about = (EditText) findViewById(R.id.about);
        paypal = (EditText) findViewById(R.id.paypal);

        individual = (CheckBox) findViewById(R.id.chk_individual);
        group = (CheckBox) findViewById(R.id.chk_group);
        licensed = (CheckBox) findViewById(R.id.chk_licensed);
        nlicensed = (CheckBox) findViewById(R.id.chk_notlicensed);


        back_button = (ImageView) findViewById(R.id.back_button_volunteers);
        document_pic = (ImageView) findViewById(R.id.document_pic);

        upload_id = (RelativeLayout) findViewById(R.id.upload_id_button);
        submit = (Button) findViewById(R.id.submit_button);

        individual.setOnClickListener(this);
        group.setOnClickListener(this);
        licensed.setOnClickListener(this);
        nlicensed.setOnClickListener(this);
        back_button.setOnClickListener(this);
        upload_id.setOnClickListener(this);
        submit.setOnClickListener(this);


        name.setOnTouchListener(this);
        address.setOnFocusChangeListener(this);
        about.setOnFocusChangeListener(this);
        paypal.setOnFocusChangeListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();


        if (id == R.id.name) {
            name.setBackground(getResources().getDrawable(R.drawable.stroke));
            address.setBackground(getResources().getDrawable(R.drawable.rectangle));
            about.setBackground(getResources().getDrawable(R.drawable.rectangle));
            paypal.setBackground(getResources().getDrawable(R.drawable.rectangle));

        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasfocus) {
        int id = view.getId();


        if (id == R.id.address && hasfocus) {
            name.setBackground(getResources().getDrawable(R.drawable.rectangle));
            address.setBackground(getResources().getDrawable(R.drawable.stroke));
            about.setBackground(getResources().getDrawable(R.drawable.rectangle));
            paypal.setBackground(getResources().getDrawable(R.drawable.rectangle));
        } else if (id == R.id.about && hasfocus) {
            name.setBackground(getResources().getDrawable(R.drawable.rectangle));
            address.setBackground(getResources().getDrawable(R.drawable.rectangle));
            about.setBackground(getResources().getDrawable(R.drawable.stroke));
            paypal.setBackground(getResources().getDrawable(R.drawable.rectangle));
        } else if (id == R.id.paypal && hasfocus) {
            name.setBackground(getResources().getDrawable(R.drawable.rectangle));
            address.setBackground(getResources().getDrawable(R.drawable.rectangle));
            about.setBackground(getResources().getDrawable(R.drawable.rectangle));
            paypal.setBackground(getResources().getDrawable(R.drawable.stroke));
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.chk_individual) {
            individual.setChecked(true);
            usertype = individual.getText().toString();
            group.setChecked(false);
        }

        if (id == R.id.chk_group) {
            individual.setChecked(false);
            group.setChecked(true);
            usertype = group.getText().toString();
        }

        if (id == R.id.chk_licensed) {
            licensed.setChecked(true);
            nlicensed.setChecked(false);
            license = licensed.getText().toString();
        }
        if (id == R.id.chk_notlicensed) {
            licensed.setChecked(false);
            nlicensed.setChecked(true);
            license = nlicensed.getText().toString();
        }

        if (id == R.id.back_button_volunteers) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }


        if (id == R.id.upload_id_button) {
            dailog();
        }

        if (id == R.id.submit_button) {
            if(address.getText().toString().length()==0){
address.setError("Please enter Address");
            }else if(paypal.getText().toString().length()==0){
                paypal.setError("Please enter paypal");
            }else if(about.getText().toString().length()==0){
                about.setError("Please enter about");
            }else if(license.length()==0){
                Toast.makeText(CallVolunteers.this,"Please select license or not licensed",Toast.LENGTH_SHORT).show();
            }else if(usertype.length()==0){
                Toast.makeText(CallVolunteers.this,"Please select group or individual",Toast.LENGTH_SHORT).show();
            }else if(name.getText().toString().length()==0){
                name.setError("Please enter name");

            }else{
                dialogWindow();
                new Thread(null, address_request, "")
                        .start();
            }


        }

    }


    public void dailog() {
        camgllry = new Dialog(this);
        camgllry.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camgllry.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        camgllry.setContentView(R.layout.camera_dialog);

        camgllry.show();

        onclick();

    }

    public void onclick() {
        final LinearLayout camera, gallery;

        camera = (LinearLayout) camgllry.findViewById(R.id.camera_layout);
        gallery = (LinearLayout) camgllry.findViewById(R.id.gallery_layout);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 1);
                camgllry.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
                camgllry.dismiss();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 0) {
                if (data != null) {


                    onSelectFromGalleryResult(data);

                   // document_pic.setVisibility(View.VISIBLE);

                }
            }else{

                }
        } else if (requestCode == 1) {

            onCaptureImageResult(data);
            //camgllry.dismiss();
          //  document_pic.setVisibility(View.VISIBLE);

        }

    }


    //--------------------------------form-API-------------------------------------

   /* public void send_document() {
        StringRequest request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    String response_name= response.getString("success");

                    if (response_name.equalsIgnoreCase("1"))
                    {
                        String message = response.getString("msg");
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put(GlobalConstants.USERID, CommonUtils.UserID(CallVolunteers.this));
                params.put(GlobalConstants.FULL_NAME, name.getText().toString());
                params.put(GlobalConstants.ADDRESS, address.getText().toString());
                params.put(GlobalConstants.PAYPAL, paypal.getText().toString());
                params.put(GlobalConstants.ABOUT, about.getText().toString());
                params.put(GlobalConstants.USER_TYPE, usertype);
                params.put(GlobalConstants.LICENSE, license);
                params.put(GlobalConstants.DOCUMENT,preferences.getString("document",""));
                params.put("action", GlobalConstants.UPLOAD_DOCUMENT_ACTION);

                Log.e("Document", params.toString());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(CallVolunteers.this);
        requestQueue.add(request);
    }*/
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
                Toast.makeText(CallVolunteers.this, message,
                        Toast.LENGTH_SHORT).show();
              /*  Intent sign = new Intent(CallVolunteers.this, LoginActivity.class);

                startActivity(sign);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
*/
            } else {
                Toast.makeText(CallVolunteers.this, message,
                        Toast.LENGTH_SHORT).show();
            }

        }

    };
    // ------------------------------------------------------upload
    // method---------------
    private String doFileUpload() {
        String success = "false";

        String urlString = GlobalConstants.URL ;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();
            if (selectedImagePath.length() > 0) {
                File file1 = new File(selectedImagePath);
                FileBody bin1 = new FileBody(file1, "image/png");
                reqEntity.addPart(GlobalConstants.DOCUMENT, bin1);
            }

            reqEntity.addPart(GlobalConstants.USERID, new StringBody(CommonUtils.UserID(this)));


            reqEntity.addPart(GlobalConstants.FULL_NAME, new StringBody(name.getText().toString()));
            reqEntity.addPart(GlobalConstants.ADDRESS, new StringBody(address.getText().toString()));
            reqEntity.addPart(GlobalConstants.PAYPAL, new StringBody(paypal.getText().toString()));
            reqEntity.addPart(GlobalConstants.ABOUT, new StringBody(about.getText().toString()));
            reqEntity.addPart(GlobalConstants.USER_TYPE, new StringBody(usertype));
            reqEntity.addPart(GlobalConstants.LICENSE, new StringBody(license));
            reqEntity.addPart("action", new StringBody(GlobalConstants.UPLOAD_DOCUMENT_ACTION));

            post.setEntity(reqEntity);

            Log.e("params",selectedImagePath+" "+usertype+" "+license+" "+GlobalConstants.UPLOAD_DOCUMENT_ACTION+" "+name.getText()+" "+address.getText());
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();

            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                JSONObject obj = new JSONObject(response_str);
                String status = obj.getString("success");
                if (status.equalsIgnoreCase("1")) {
                    success = "true";
                    message=obj.getString("msg");
                } else {
                    success = "false";
                    message=obj.getString("msg");
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


    //-------------------------------------------------Image Code---------------------


    //-------------------------------image-upload-------------------------------------------
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(CallVolunteers.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        document_pic.setImageBitmap(bm);
        Uri uri = getImageUri(CallVolunteers.this, bm);
        try {
            selectedImagePath = getFilePath(CallVolunteers.this, uri);
            editor.putString(GlobalConstants.DOCUMENT, selectedImagePath);
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
        document_pic.setImageBitmap(thumbnail);
        Uri uri = getImageUri(CallVolunteers.this, thumbnail);
        try {
            selectedImagePath = getFilePath(CallVolunteers.this, uri);
            editor.putString(GlobalConstants.DOCUMENT, selectedImagePath);
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
    //---------------------------Progrees Dialog-----------------------
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
}

