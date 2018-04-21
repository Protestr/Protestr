package org.protestr.app.data.models.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by someone on 5/07/17.
 */

public class User implements Parcelable {
    @SerializedName("user_id")
    private String id;
    @SerializedName("user_email")
    private String email;
    private String username;
    @SerializedName("last_login")
    private long lastLogin;
    private String password;
    @SerializedName("profile_pic_url")
    private String profilePicUrl;

    public User() {
        // empty constructor
    }

    public User(String id, String email, String username, String password, String profilePicUrl) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.profilePicUrl = profilePicUrl;
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        username = in.readString();
        lastLogin = in.readLong();
        password = in.readString();
        profilePicUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof User && ((User) obj).getId().equals(id));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(email);
        parcel.writeString(username);
        parcel.writeLong(lastLogin);
        parcel.writeString(password);
        parcel.writeString(profilePicUrl);
    }
}
