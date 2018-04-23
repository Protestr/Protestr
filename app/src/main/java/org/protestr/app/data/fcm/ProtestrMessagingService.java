package org.protestr.app.data.fcm;

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
import android.util.SparseIntArray;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.protestr.app.data.models.dao.Notification;

import java.util.HashMap;

import org.protestr.app.R;
import org.protestr.app.data.models.dao.Notification;
import org.protestr.app.ui.activities.splash.SplashActivity;

/**
 * Created by someone on 26/06/17.
 */

public class ProtestrMessagingService extends FirebaseMessagingService {

    public static SparseIntArray notificationCount = new SparseIntArray();

    public static final String DEFAULT_PROTESTR_EVENT_CHANNEL = "EVENTS_CHANNEL";

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

        if (notificationCount.indexOfKey(notification.getRecipientId().hashCode()) < 0) {
            notificationCount.put(notification.getRecipientId().hashCode(), 0);
        } else {
            notificationCount.put(notification.getRecipientId().hashCode(),
                    notificationCount.get(notification.getRecipientId().hashCode()) + 1);
        }

        final int currentCount = notificationCount.get(notification.getRecipientId().hashCode());

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, DEFAULT_PROTESTR_EVENT_CHANNEL)
                .setLights(Color.WHITE, 1000, 1000)
                .setContentTitle(notification.getTitle().isEmpty() ?
                        (notification.getRecipientName().isEmpty() ? getString(R.string.app_name) : notification.getRecipientName())
                        : notification.getTitle())
                .setContentText(currentCount > 1 ? getString(R.string.people_talking_about_event) : notification.getBody())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_goriot_flat)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(android.app.Notification.PRIORITY_DEFAULT);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(notification.getTitle().isEmpty() ?
                    (notification.getRecipientName().isEmpty() ? getString(org.protestr.app.R.string.app_name) : notification.getRecipientName())
                    : notification.getTitle());
            bigTextStyle.bigText(currentCount > 1 ? getString(R.string.people_talking_about_event) : notification.getBody());

            notificationBuilder.setStyle(bigTextStyle);
        }

        if (notification.getType() != Notification.NOTIFICATION_TYPE_ENTIRE_APP) {
            notificationBuilder.setNumber(notificationCount.get(notification.getRecipientId().hashCode()));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        android.app.Notification notificationToShow = notificationBuilder.build();
        notificationToShow.defaults |= android.app.Notification.DEFAULT_VIBRATE;

        if (notificationManager != null) {
            notificationManager.notify(notification.getType() == Notification.NOTIFICATION_TYPE_ENTIRE_APP ?
                    notificationCount.get(notification.getRecipientId().hashCode()) :
                    notification.getRecipientId().hashCode(), notificationToShow);
        }
    }

}
