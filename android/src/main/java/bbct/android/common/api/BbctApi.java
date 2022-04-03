package bbct.android.common.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BbctApi {
    @GET("baseball_cards/")
    Call<List<BaseballCard>> getBaseballCards();

    @GET("baseball_cards/{id}/")
    Call<BaseballCard> getBaseballCard(@Path("id") int id);

    @POST("baseball_cards/")
    Call<BaseballCard> createBaseballCard(@Body BaseballCard card);

    @PATCH("baseball_cards/{id}/")
    Call<BaseballCard> updateBaseballCard(@Path("id") int id, @Body BaseballCard card);
}