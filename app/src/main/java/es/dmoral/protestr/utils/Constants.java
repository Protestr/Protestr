package es.dmoral.protestr.utils;

/**
 * Created by grender on 13/02/17.
 */

public class Constants {
    public static final String BASE_URL = "http://37.15.248.20:5900/";
    public static final String API_URL = BASE_URL + "api/v1/";
    public static final String USERS_ENDPOINT = "users";
    public static final String LOGIN_ENDPOINT = USERS_ENDPOINT + "/login";
    public static final String SIGN_UP_ENDPOINT = USERS_ENDPOINT + "/signup";
    public static final String EVENTS_ENDPOINT = "events";

    public static final String PREFERENCES_LOGGED_IN = "logged_in";
    public static final String PREFERENCES_USERNAME = "username";
    public static final String PREFERENCES_PASSWORD = "password";
}
