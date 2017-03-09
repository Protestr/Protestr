package es.dmoral.protestr.api;

import es.dmoral.protestr.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by grender on 13/02/17.
 */

public class WebService {

    private Retrofit retrofit;

    public WebService() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiInterface getApiInterface() {
        return retrofit.create(ApiInterface.class);
    }
}
