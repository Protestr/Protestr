package es.dmoral.protestr.api;

import java.util.ArrayList;

import es.dmoral.protestr.api.models.Event;
import es.dmoral.protestr.utils.Constants;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import es.dmoral.protestr.api.models.ResponseStatus;

/**
 * Created by grender on 13/02/17.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST(Constants.LOGIN_ENDPOINT)
    Call<ResponseStatus> attemptLogin(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST(Constants.SIGN_UP_ENDPOINT)
    Call<ResponseStatus> attemptSignup(@Field("username") String username, @Field("password") String password);

    @GET(Constants.EVENTS_ENDPOINT)
    Call<ArrayList<Event>> getNewEvents();
}
