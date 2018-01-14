package es.dmoral.protestr.data.models.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by grender on 15/02/17.
 */

public class Event implements Parcelable {

    @SerializedName("event_id")
    private String eventId;
    @SerializedName("user_id")
    private String userId;
    private String title;
    private String description;
    @SerializedName("from_date")
    private long fromDate;
    @SerializedName("to_date")
    private long toDate;
    private int participants;
    @SerializedName("location_name")
    private String locationName;
    @SerializedName("image_url")
    private String imageUrl;
    private double latitude;
    private double longitude;
    @SerializedName("iso3_country")
    private String iso3Country;
    @SerializedName("is_admin")
    private boolean isAdmin;
    @SerializedName("is_subscribed")
    private boolean isSubscribed;

    public Event(String eventId, String userId, String title, String description, long fromDate,
                 long toDate, int participants, String locationName, String imageUrl, double latitude,
                 double longitude, String iso3Country, boolean isAdmin, boolean isSubscribed) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.participants = participants;
        this.locationName = locationName;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iso3Country = iso3Country;
        this.isAdmin = isAdmin;
        this.isSubscribed = isSubscribed;
    }

    protected Event(Parcel in) {
        eventId = in.readString();
        userId = in.readString();
        title = in.readString();
        description = in.readString();
        fromDate = in.readLong();
        toDate = in.readLong();
        participants = in.readInt();
        locationName = in.readString();
        imageUrl = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        iso3Country = in.readString();
        isAdmin = in.readByte() != 0;
        isSubscribed = in.readByte() != 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getIso3Country() {
        return iso3Country;
    }

    public void setIso3Country(String iso3Country) {
        this.iso3Country = iso3Country;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventId);
        parcel.writeString(userId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(fromDate);
        parcel.writeLong(toDate);
        parcel.writeInt(participants);
        parcel.writeString(locationName);
        parcel.writeString(imageUrl);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(iso3Country);
        parcel.writeByte((byte) (isAdmin ? 1 : 0));
        parcel.writeByte((byte) (isSubscribed ? 1 : 0));
    }
}
