package es.dmoral.protestr.api.models;

import java.util.Date;

/**
 * Created by grender on 15/02/17.
 */

public class Event {
    private long event_id;
    private long user_id;
    private String title;
    private String description;
    private long from;
    private long to;
    private int rating;
    private String location_name;
    private String image_url;
    private long latitude;
    private long longitude;
    private String iso3_country;

    public Event(long event_id, long user_id, String title, String description, long from, long to, int rating, String location_name, String image_url, long latitude, long longitude, String iso3_country) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.from = from;
        this.to = to;
        this.rating = rating;
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

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
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
