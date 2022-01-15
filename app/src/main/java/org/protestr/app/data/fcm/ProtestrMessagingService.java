package org.protestr.app.data.fcm;

import android.app.NotificationChannel;
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

import org.greenrobot.eventbus.EventBus;
import org.protestr.app.data.models.dao.EventUpdate;
import org.protestr.app.data.models.dao.Notification;

import java.util.HashMap;

import org.protestr.app.R;
import org.protestr.app.data.models.dao.Notification;
import org.protestr.app.data.models.dao.User;
import org.protestr.app.ui.activities.splash.SplashActivity;
import org.protestr.app.utils.PreferencesUtils;

/**
 * Created by someone on 26/06/17.
 */

public class ProtestrMessagingService extends FirebaseMessagingService {

    public static SparseIntArray notificationCount = new SparseIntArray();

    public static final String PROTESTR_EVENT_CHANNEL = "EVENTS_CHANNEL";
    public static final String PROTESTR_ENTIRE_APP_CHANNEL = "ENTIRE_APP_CHANEL";

    public static String currentShownEventId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final User user = PreferencesUtils.getLoggedUser(getApplicationContext());
        final Notification notification = new Notification(remoteMessage.getData());
        if (!PreferencesUtils.isEventMuted(getApplicationContext(), notification.getRecipientId()) &&
                !user.getId().equals(notification.getSenderId()) ||
                (currentShownEventId == null || !currentShownEventId.equals(notification.getRecipientId()))) {
            sendNotification(notification);
        }
        if (notification.getType() != Notification.NOTIFICATION_TYPE_ENTIRE_APP) {
            EventBus.getDefault().post(notification.toEventUpdate());
        }
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

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel eventChannel = new NotificationChannel(PROTESTR_EVENT_CHANNEL,
                    getString(R.string.event_related_notifications),
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel entireAppChannel = new NotificationChannel(PROTESTR_ENTIRE_APP_CHANNEL,
                    getString(R.string.protestr_team_announcements),
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(eventChannel);
                notificationManager.createNotificationChannel(entireAppChannel);
            }
        }

        final String channelId;
        switch (notification.getType()) {
            case Notification.NOTIFICATION_TYPE_ADMIN_MESSAGE:
            case Notification.NOTIFICATION_TYPE_USER_MESSAGE:
                channelId = PROTESTR_EVENT_CHANNEL;
                break;
            default:
                channelId = PROTESTR_ENTIRE_APP_CHANNEL;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
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

        android.app.Notification notificationToShow = notificationBuilder.build();
        notificationToShow.defaults |= android.app.Notification.DEFAULT_VIBRATE;

        if (notificationManager != null) {
            notificationManager.notify(notification.getType() == Notification.NOTIFICATION_TYPE_ENTIRE_APP ?
                    notificationCount.get(notification.getRecipientId().hashCode()) :
                    notification.getRecipientId().hashCode(), notificationToShow);
        }
    }

}
