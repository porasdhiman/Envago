package envago.envago;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    public int NOTIFICATION_ID;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    static final String TAG = "Envago";
    String datamessage, senderid, userreply, reciverid, identity_type = "0";
    Global global;
    Context context;
    SharedPreferences mSharedPreferences;
    Editor mEditor;
    boolean isapp_inbackground_boolean;
    Bundle extras;
    PendingIntent contentIntent;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("in gcm", "we are in pushnotification");
        global = (Global) getApplicationContext();

        extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        NOTIFICATION_ID = (int) System.currentTimeMillis();
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Delico App", "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Delico App", "Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                for (int i = 0; i < 3; i++) {
                    Log.i(TAG, "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }




                    sendNotification("Ouryatar App", extras.getString("message"));



            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String notificationTitle, String notificationMessage) {


        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

            contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Tab_Activity.class), 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher).setContentTitle(notificationTitle).setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage))
                .setContentText(notificationMessage);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());


    }


}