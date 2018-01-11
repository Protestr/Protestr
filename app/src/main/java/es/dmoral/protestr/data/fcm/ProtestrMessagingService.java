package es.dmoral.protestr.data.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.dao.Notification;
import es.dmoral.protestr.ui.activities.splash.SplashActivity;

/**
 * Created by grender on 26/06/17.
 */

public class ProtestrMessagingService extends FirebaseMessagingService {

    public static HashMap<String, Integer> notificationCount = new HashMap<>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(new Notification(remoteMessage.getData()));
    }

    private void sendNotification(Notification notification) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (!notificationCount.containsKey(notification.getSenderId())) {
            notificationCount.put(notification.getSenderId(), 0);
        } else {
            notificationCount.put(notification.getSenderId(),
                    notificationCount.get(notification.getSenderId()) + 1);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLights(Color.WHITE, 1000, 1000)
                .setContentTitle(notification.getTitle().isEmpty() ? getString(R.string.app_name) :
                        notification.getTitle())
                .setContentText(notification.getBody())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_goriot_flat)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(android.app.Notification.PRIORITY_DEFAULT);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(notification.getTitle().isEmpty() ? getString(R.string.app_name) :
                    notification.getTitle());
            bigTextStyle.bigText(notification.getBody());

            notificationBuilder.setStyle(bigTextStyle);
        }

        if (notification.getType() != 0) {
            notificationBuilder.setNumber(notificationCount.get(notification.getSenderId()));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        android.app.Notification notificationToShow = notificationBuilder.build();
        notificationToShow.defaults |= android.app.Notification.DEFAULT_VIBRATE;

        notificationManager.notify(notification.getType() == 0 ?
                notificationCount.get(notification.getSenderId()) :
                Integer.parseInt(notification.getSenderId()), notificationToShow);
    }

}
