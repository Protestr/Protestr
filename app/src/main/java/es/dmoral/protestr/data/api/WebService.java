package es.dmoral.protestr.data.api;

import es.dmoral.protestr.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by grender on 13/02/17.
 */

public class WebService {

    private static ApiInterface apiInterface;
    private static WebService webService;
    private final Retrofit retrofit;

    private WebService() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WebService getInstance() {
        if (webService == null)
            webService = new WebService();
        return webService;
    }

    public ApiInterface getApiInterface() {
        if (apiInterface == null)
            apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }
}
