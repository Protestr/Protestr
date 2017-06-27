package es.dmoral.protestr.models.models;

/**
 * Created by grender on 15/02/17.
 */

public class Event {

    private long event_id;
    private long user_id;
    private String title;
    private String description;
    private long from_date;
    private long to_date;
    private int participants;
    private String location_name;
    private String image_url;
    private double latitude;
    private double longitude;
    private String iso3_country;

    public Event(long event_id, long user_id, String title, String description, long from_date, long to_date, int participants, String location_name, String image_url, double latitude, double longitude, String iso3_country) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.from_date = from_date;
        this.to_date = to_date;
        this.participants = participants;
        this.location_name = location_name;
        this.image_url = image_url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iso3_country = iso3_country;
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public long getFrom_date() {
        return from_date;
    }

    public void setFrom_date(long from_date) {
        this.from_date = from_date;
    }

    public long getTo_date() {
        return to_date;
    }

    public void setTo_date(long to_date) {
        this.to_date = to_date;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public String getIso3_country() {
        return iso3_country;
    }

    public void setIso3_country(String iso3_country) {
        this.iso3_country = iso3_country;
    }
}
