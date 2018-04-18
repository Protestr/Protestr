package es.dmoral.protestr.data.api;

import java.util.ArrayList;

import es.dmoral.protestr.data.models.dao.Event;
import es.dmoral.protestr.data.models.dao.ImgurStatus;
import es.dmoral.protestr.data.models.dao.ResponseStatus;
import es.dmoral.protestr.data.models.dao.User;
import es.dmoral.protestr.utils.Constants;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by grender on 13/02/17.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(Constants.LOGIN_ENDPOINT)
    Call<User> attemptLogin(@Field("user_email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST(Constants.SIGN_UP_ENDPOINT)
    Call<User> attemptSignup(@Field("user_email") String email, @Field("username") String username,
                             @Field("password") String password, @Field("profile_pic_url") String profilePicUrl);

    @POST(Constants.UPLOAD_IMAGE_URL)
    Call<ImgurStatus> uploadImage(@Header("Authorization") String clientId, @Body RequestBody imageBody);

    @FormUrlEncoded
    @POST(Constants.NEW_EVENT_ENDPOINT)
    Call<ResponseStatus> createNewEvent(@Field("user_id") String userId, @Field("password") String password,
                                        @Field("title") String title, @Field("image_url") String imageUrl,
                                        @Field("description") String description, @Field("from") String from,
                                        @Field("location_name") String locationName, @Field("latitude") String latitude,
                                        @Field("longitude") String longitude, @Field("iso3_country") String iso3Code,
                                        @Field("participants") int participants,
                                        @Field("event_admins[]") ArrayList<String> eventAdmins);


    @FormUrlEncoded
    @POST(Constants.JOIN_EVENT_ENDPOINT)
    Call<ResponseStatus> joinEvent(@Field("user_email") String email, @Field("password") String password,
                                   @Field("event_id") String eventId);
    @FormUrlEncoded
    @POST(Constants.LEAVE_EVENT_ENDPOINT)
    Call<ResponseStatus> leaveEvent(@Field("user_email") String email, @Field("password") String password,
                                    @Field("event_id") String eventId, @Field("is_admin") boolean isAdmin);
    @FormUrlEncoded
    @POST(Constants.DELETE_EVENT_ENDPOINT)
    Call<ResponseStatus> deleteEvent(@Field("user_email") String email, @Field("password") String password,
                                     @Field("event_id") String eventId);

    @GET(Constants.EVENTS_ENDPOINT + "?")
    Call<ArrayList<Event>> getAllNewEvents(@Query("offset") int offset, @Query("limit") int limit,
                                           @Query("order") String order, @Query("lat") double lat,
                                           @Query("lng") double lng, @Query("user_id") String userId);

    @GET(Constants.EVENTS_ENDPOINT + "?")
    Call<ArrayList<Event>> getNewEventsByIso3(@Query("iso3") String iso3Code, @Query("offset") int offset,
                                              @Query("limit") int limit, @Query("order") String order,
                                              @Query("user_id") String userId);


    @GET(Constants.EVENTS_ENDPOINT + "/{event_id}")
    Call<Event> getEventById(@Path("event_id") String eventId, @Query("user_id") String userId);

    @GET(Constants.FILTER_USERS_ENDPOINT + "?")
    Call<ArrayList<User>> filterUsers(@Query("query") String query);
}
