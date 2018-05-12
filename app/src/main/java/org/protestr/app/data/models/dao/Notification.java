package org.protestr.app.data.models.dao;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by someone on 26/06/17.
 */

public class Notification {
    public static final int NOTIFICATION_TYPE_ENTIRE_APP = 0;
    public static final int NOTIFICATION_TYPE_ADMIN_MESSAGE = 1;
    public static final int NOTIFICATION_TYPE_USER_MESSAGE = 2;

    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String SENDER_ID = "sender_id";
    public static final String SENDER_NAME = "sender_name";
    public static final String SENDER_PROFILE_PIC = "sender_profile_pic";
    public static final String RECIPIENT_ID = "recipient_id";
    public static final String RECIPIENT_NAME = "recipient_name";
    public static final String TIMESTAMP = "timestamp";

    private int type;
    private String senderId;
    private String senderName;
    private String senderProfilePic;
    private String recipientId;
    private String recipientName;
    private String title;
    private String body;
    private long timestamp;

    public Notification() {
        this(-1, "-1", "-1", "", "", "", "", "", -1);
    }

    public Notification(@NonNull Map<String, String> data) {
        type = data.containsKey(TYPE) ? Integer.valueOf(data.get(TYPE)) : -1;
        senderId = data.containsKey(SENDER_ID) ? data.get(SENDER_ID) : "-1";
        senderName = data.containsKey(SENDER_NAME) ? data.get(SENDER_NAME) : "";
        senderProfilePic = data.containsKey(SENDER_PROFILE_PIC) ? data.get(SENDER_PROFILE_PIC) : "";
        recipientId = data.containsKey(RECIPIENT_ID) ? data.get(RECIPIENT_ID) : "-1";
        recipientName = data.containsKey(RECIPIENT_NAME) ? data.get(RECIPIENT_NAME) : "";
        title = data.containsKey(TITLE) ? data.get(TITLE) : "";
        body = data.containsKey(BODY) ? data.get(BODY) : "";
        timestamp = data.containsKey(TIMESTAMP) ? Long.valueOf(data.get(TIMESTAMP)) : -1;
    }

    public Notification(int type, String senderId, String senderName, String senderProfilePic, String recipientId,
                        String recipientName, String title, String body, long timestamp) {
        this.type = type;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfilePic = senderProfilePic;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfilePic() {
        return senderProfilePic;
    }

    public void setSenderProfilePic(String senderProfilePic) {
        this.senderProfilePic = senderProfilePic;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public EventUpdate toEventUpdate() {
        return new EventUpdate(
                senderId,
                senderName,
                senderProfilePic,
                body,
                recipientId,
                type == NOTIFICATION_TYPE_ADMIN_MESSAGE,
                timestamp
        );
    }
}
