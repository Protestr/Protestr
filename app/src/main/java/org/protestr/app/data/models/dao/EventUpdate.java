package org.protestr.app.data.models.dao;

public class EventUpdate {
    private String userId;
    private String username;
    private String profilePicUrl;
    private String message;
    private String eventId;
    private boolean isFromAdmin;
    private long timestamp;

    public EventUpdate(String userId, String username, String profilePicUrl, String message, String eventId, boolean isFromAdmin, long timestamp) {
        this.userId = userId;
        this.username = username;
        this.profilePicUrl = profilePicUrl;
        this.message = message;
        this.eventId = eventId;
        this.isFromAdmin = isFromAdmin;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isFromAdmin() {
        return isFromAdmin;
    }

    public void setFromAdmin(boolean fromAdmin) {
        isFromAdmin = fromAdmin;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
