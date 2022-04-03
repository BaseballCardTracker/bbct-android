package bbct.android.common.api;

import retrofit2.Retrofit;

public class RetrofitClient {
    private static final String BASE_URL = "http://localhost:8000/api/";

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        }

        return retrofit;
    }
}
