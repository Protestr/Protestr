package es.dmoral.protestr.api;

import java.util.ArrayList;

import es.dmoral.protestr.models.models.Event;
import es.dmoral.protestr.models.models.ImgurStatus;
import es.dmoral.protestr.models.models.ResponseStatus;
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
    Call<ResponseStatus> attemptLogin(@Field("user_email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST(Constants.SIGN_UP_ENDPOINT)
    Call<ResponseStatus> attemptSignup(@Field("user_email") String email, @Field("username") String username,
                                       @Field("password") String password);

    @POST(Constants.UPLOAD_IMAGE_URL)
    Call<ImgurStatus> uploadImage(@Header("Authorization") String clientId, @Body RequestBody imageBody);

    @FormUrlEncoded
    @POST(Constants.NEW_EVENT_ENDPOINT)
    Call<ResponseStatus> createNewEvent(@Field("user_email") String userName, @Field("password") String password,
                                        @Field("title") String title, @Field("image_url") String imageUrl,
                                        @Field("description") String description, @Field("from") String from,
                                        @Field("location_name") String locationName, @Field("latitude") String latitude,
                                        @Field("longitude") String longitude, @Field("iso3_country") String iso3Code);

    @GET(Constants.EVENTS_ENDPOINT + "?")
    Call<ArrayList<Event>> getAllNewEvents(@Query("offset") int offset, @Query("limit") int limit,
                                           @Query("order") String order);

    @GET(Constants.EVENTS_ENDPOINT + "/{iso3}" + "?")
    Call<ArrayList<Event>> getNewEvents(@Path("iso3") String iso3Code, @Query("offset") int offset, @Query("limit") int limit, @Query("order") String order);
}
