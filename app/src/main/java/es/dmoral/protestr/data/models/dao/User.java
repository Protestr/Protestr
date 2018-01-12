package es.dmoral.protestr.data.models.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by grender on 5/07/17.
 */

public class User {
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
}
