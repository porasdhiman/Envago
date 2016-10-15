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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
 * Created by vikas on 21-09-2016.
 */
public class UserFragment extends Fragment {

    ImageView map_button;
    CircleImageView profilepic;
    Dialog camgllry;
    LinearLayout settings, pricing_layout, privacy, terms;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView username;
    Dialog dialog2;
    String selectedImagePath = "";


    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.profile, container, false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.textcolor));
        }

        preferences = getActivity().getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        editor = preferences.edit();


        map_button = (ImageView) v.findViewById(R.id.map_button);

        profilepic = (CircleImageView) v.findViewById(R.id.profile_img);
        settings = (LinearLayout) v.findViewById(R.id.settings_view);
        pricing_layout = (LinearLayout) v.findViewById(R.id.pricing_layout);
        privacy = (LinearLayout) v.findViewById(R.id.privacy_layout);
        username = (TextView) v.findViewById(R.id.username);
        terms = (LinearLayout) v.findViewById(R.id.terms_layout);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dailog();
            }
        });
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivityForResult(intent,2);


            }
        });
        pricing_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "PRICING POLICY");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "PRIVACY POLICY");
                startActivity(intent);

            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "TERMS AND CONDITIONS");
                startActivity(intent);

            }
        });
        if (preferences.getString(GlobalConstants.USERNAME, "").equalsIgnoreCase("")) {
            dialogWindow();
            profile_api();
        } else {

            username.setText(preferences.getString(GlobalConstants.USERNAME, ""));
            //  String img_url = preferences.getString(GlobalConstants.IMAGE, "");
            if (preferences.getString(GlobalConstants.IMAGE, "").length() == 0) {

            } else {

                if (preferences.getString(GlobalConstants.IMAGE, "").contains("http")) {
                    Picasso.with(getActivity()).load(preferences.getString(GlobalConstants.IMAGE, ""));
                } else {
                    profilepic.setImageURI(Uri.fromFile(new File(preferences.getString(GlobalConstants.IMAGE, ""))));
                }
            }


        }
        return v;
    }

    public void dailog() {
        camgllry = new Dialog(getActivity());
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
        }else if (requestCode == 2) {
            if (preferences.getString(GlobalConstants.IMAGE, "").length() == 0) {

            } else {

                if (preferences.getString(GlobalConstants.IMAGE, "").contains("http")) {
                    Picasso.with(getActivity()).load(preferences.getString(GlobalConstants.IMAGE, ""));
                } else {
                    profilepic.setImageURI(Uri.fromFile(new File(preferences.getString(GlobalConstants.IMAGE, ""))));
                }
            }
            username.setText(preferences.getString(GlobalConstants.USERNAME, ""));
        }

    }


//--------------------------------Profile-API--------------------------------


    public void profile_api() {
        StringRequest request = new StringRequest(Request.Method.POST, GlobalConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog2.dismiss();
                Log.e("hahahahahahaha", s);
                try {
                    JSONObject respose_obj = new JSONObject(s);
                    String obj = respose_obj.getString("success");
                    if (obj.equalsIgnoreCase("1")) {
                        JSONObject json_data = respose_obj.getJSONObject("data");

                        editor.putString(GlobalConstants.EMAIL, json_data.getString(GlobalConstants.EMAIL));
                        editor.putString(GlobalConstants.CONTACT, json_data.getString(GlobalConstants.CONTACT));
                        editor.putString(GlobalConstants.FIRST_NAME, json_data.getString(GlobalConstants.FIRST_NAME));
                        editor.putString(GlobalConstants.LAST_NAME, json_data.getString(GlobalConstants.LAST_NAME));
                        editor.putString(GlobalConstants.USERNAME, json_data.getString(GlobalConstants.USERNAME));
                        editor.putString(GlobalConstants.ADDRESS, json_data.getString(GlobalConstants.ADDRESS));
                        editor.putString(GlobalConstants.PAYPAL, json_data.getString(GlobalConstants.PAYPAL));
                        editor.putString(GlobalConstants.ABOUT, json_data.getString(GlobalConstants.ABOUT));
                        editor.putString(GlobalConstants.DOCUMENT, json_data.getString(GlobalConstants.DOCUMENT));
                        editor.putString(GlobalConstants.IMAGE, json_data.getString(GlobalConstants.IMAGE));

                        editor.commit();

                        username.setText(preferences.getString(GlobalConstants.USERNAME, ""));
                        String img_url = preferences.getString(GlobalConstants.IMAGE, "");
                        if (img_url.length() == 0) {

                        } else {
                            Picasso.with(getActivity()).load(img_url).into(profilepic);
                        }


                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), respose_obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("action", GlobalConstants.GETPROFILE_ACTION);
                Log.e("heheheheheheheh", params.toString());


                return params;


            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    public void dialogWindow() {
        dialog2 = new Dialog(getActivity());
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
//-------------------------------image-upload-------------------------------------------
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profilepic.setImageBitmap(bm);
        Uri uri = getImageUri(getActivity(), bm);
        try {
            selectedImagePath = getFilePath(getActivity(), uri);
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
        profilepic.setImageBitmap(thumbnail);
        Uri uri = getImageUri(getActivity(), thumbnail);
        try {
            selectedImagePath = getFilePath(getActivity(), uri);
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
