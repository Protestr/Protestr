package es.dmoral.protestr.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

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

    private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {

        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return in.nextString().equalsIgnoreCase("1");
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    };

    private WebService() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(boolean.class, booleanAsIntAdapter).create()))
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
