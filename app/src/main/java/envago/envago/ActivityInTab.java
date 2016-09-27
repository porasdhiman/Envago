package envago.envago;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ActivityInTab extends FragmentActivity {

    AlertDialog builder;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_tab);

    }

    void navigateTo(Fragment fragment, int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction anim_frag = fragmentManager.beginTransaction();

        if (i == 1) {
           // anim_frag.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
           // anim_frag.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        }

        anim_frag.replace(R.id.contentintab, fragment).addToBackStack(null).commit();


    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {

            super.onBackPressed();
        } else {
            builder = new AlertDialog.Builder(ActivityInTab.this).setMessage("Do You Want To Exit?")
                    .setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            finish();

                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            builder.dismiss();
                        }
                    }).setIcon(R.drawable.ic_launcher).show();

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment uploadType = getSupportFragmentManager().findFragmentById(R.id.contentintab);

        if (uploadType != null) {
            uploadType.onActivityResult(requestCode, resultCode, data);
        }/*
         * else{
		 * 
		 * }
		 */
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void progress() {
        Log.e("dddddddddddd", "open");
        progressDialog = ProgressDialog.show(ActivityInTab.this, "", "Sending Fax...");
    }


    /*connect client to Google Play Services*/
    @Override
    public void onStart() {
        super.onStart();


    }

    /*close connection to Google Play Services*/

}
