package envago.envago;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jhang on 9/18/2016.
 */
public class SplashActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , ResultCallback<LocationSettingsResult> {

    // --------------code for gcm
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //String SENDER_ID = "407984051965";
    String SENDER_ID = "513380904305";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regId;
    Context context;
    String TAG = "Device Token Log";
    //------------------------
    Global global;
    SharedPreferences sp;
    //--------------Location lat long variable---------

    private Location mLastLocation, myLocation;
    LocationManager mLocationManager;
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */

    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;


    protected String mLatitudeLabel;
    protected String mLongitudeLabel;

    RelativeLayout splash_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        global = (Global) getApplicationContext();
        sp = getSharedPreferences(GlobalConstants.PREFNAME, Context.MODE_PRIVATE);
        context = getApplicationContext();
        buildGoogleApiClient();
        splash_img = (RelativeLayout) findViewById(R.id.splash_img);

        //----------------------------------------Marshmallow Permission-----------------

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId(context);
            Log.e(TAG, " Google Play Services APK found.");
            if (regId.isEmpty()) {

                registerInBackground();
            }
        } else {
            Log.e(TAG, "No valid Google Play Services APK found.");
        }
        /*global.setLat("30.7046");
        global.setLong("76.7179");*/
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        String coarselocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
        String cameraPermission = Manifest.permission.CAMERA;
        String wstorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String rstorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String networkPermission = Manifest.permission.ACCESS_NETWORK_STATE;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasFinePermission = SplashActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarsePermission = SplashActivity.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            int hascameraPermission = SplashActivity.this.checkSelfPermission(Manifest.permission.CAMERA);
            int haswstorage = SplashActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasrstorage = SplashActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasaccessnetworkState = SplashActivity.this.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
            List<String> permissions = new ArrayList<String>();
            if (hasFinePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(locationPermission);
            }
            if (hasCoarsePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(coarselocationPermission);
            }
            if (hascameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(cameraPermission);
            }
            if (haswstorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(wstorage);
            }
            if (hasrstorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(rstorage);
            }
            if (hasaccessnetworkState != PackageManager.PERMISSION_GRANTED) {
                permissions.add(networkPermission);
            }
            if (!permissions.isEmpty()) {
                String[] params = permissions.toArray(new String[permissions.size()]);
                requestPermissions(params, 0);

            } else {



                    Handler splashhandler = new Handler();
                    splashhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            splash_img.setBackgroundResource(R.drawable.splash_2);
                            Handler splashhandler = new Handler();
                            splashhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    splash_img.setBackgroundResource(R.drawable.splash_2);

                                    if (CommonUtils.UserID(SplashActivity.this).equalsIgnoreCase("")) {

                                        Intent intent = new Intent(SplashActivity.this, SlidePageActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                        finish();
                                    } else {
                                        if (global.getNotifyType().equalsIgnoreCase("0")) {
                                            Intent intent = new Intent(SplashActivity.this, Tab_Activity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                            finish();
                                        } else {
                                            Intent intent = new Intent(SplashActivity.this, MessageFragment.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                            finish();
                                        }
                                    }
                                }
                            }, 1000);


                        }
                    }, 2000);


                // We already have permission, so handle as norma
                //Toast.makeText(MainActivity.this,gps.getLatitude()+""+   gps.getLongitude(),Toast.LENGTH_SHORT).show();
            }
        } else {


                Handler splashhandler = new Handler();
                splashhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        splash_img.setBackgroundResource(R.drawable.splash_2);
                        Handler splashhandler = new Handler();
                        splashhandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                splash_img.setBackgroundResource(R.drawable.splash_2);

                                if (CommonUtils.UserID(SplashActivity.this).equalsIgnoreCase("")) {

                                    Intent intent = new Intent(SplashActivity.this, SlidePageActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                    finish();
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, Tab_Activity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                    finish();
                                }
                            }
                        }, 1000);


                    }
                }, 2000);



            // Toast.makeText(MainActivity.this,gps.getLatitude()+""+   gps.getLongitude(),Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted




                        locatioMethod();
                        Intent intent = new Intent(SplashActivity.this, SlidePageActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        finish();




                    // Toast.makeText(MainActivity.this,gps.getLatitude()+""+   gps.getLongitude(),Toast.LENGTH_SHORT).show();

                } else {
                    // Permission Denied
                    Toast.makeText(SplashActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void hashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("envago.envago", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                //textInstructionsOrLink = (TextView)findViewById(R.id.textstring);
                //textInstructionsOrLink.setText(sign);
                Log.e("Hash Key", sign);
                Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("nope", "nope");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    //------------------------------ Device Token-------------------
    private String getRegistrationId(Context context) {

        // TODO Auto-generated method stub

        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        Log.e("Tag", registrationId);

        return registrationId;

    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(SplashActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                // TODO Auto-generated method stub

                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(SENDER_ID);
                    global.setDeviceToken(regId);
                    msg = "Device registered, registration ID=" + regId;

                    Log.e("getdeviceid", global.getDeviceToken());

                    // You should send the registration ID to your server over
                    // HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your
                    // app.
                    // The request to your server should be authenticated if
                    // your app
                    // is using accounts.

                    // For this demo: we don't need to send it because the
                    // device
                    // will send upstream messages to a server that echo back
                    // the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }
        }.execute(null, null, null);

    }


    private boolean checkPlayServices() {
        // TODO Auto-generated method stub
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;

    }

    //---------------------------Location lat long method-----------------
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
       /* locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(2 * 1000);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // Toast.makeText(SplashActivity.this, "" + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            global.setLat(String.valueOf(mLastLocation.getLatitude()));
            global.setLong(String.valueOf(mLastLocation.getLongitude()));


        } else {
           /* mLocationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                buildAlertMessageNoGPS();
            }*/
            global.setLat("0.0");
            global.setLong("0.0");
          /*  LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(
                            mGoogleApiClient,
                            builder.build()
                    );

            result.setResultCallback(this);
*/
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                finish();
                Toast.makeText(getApplicationContext(), "GPS is enabled please restart app", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    public void locatioMethod() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //Toast.makeText(SplashActivity.this, "" + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            global.setLat(String.valueOf(mLastLocation.getLatitude()));
            global.setLong(String.valueOf(mLastLocation.getLongitude()));


        }
    }


}
