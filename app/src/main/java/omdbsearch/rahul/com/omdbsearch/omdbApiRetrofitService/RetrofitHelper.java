package omdbsearch.rahul.com.omdbsearch.omdbApiRetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import omdbsearch.rahul.com.omdbsearch.model.Detail;
import omdbsearch.rahul.com.omdbsearch.model.Result;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by shahr on 05/01/17.
 */

public class RetrofitHelper {
    private static final String API_URL = "http://www.omdbapi.com";
    private static Omdbapi sOmdbApi;

    public static class ResultWithDetail {
        private List<Detail> mMovieDetailList;
        private String mTotalResults;
        private String mResponse;

        public ResultWithDetail(Result result) {
            mTotalResults = result.totalResults;
            mResponse = result.Response;
            mMovieDetailList = new ArrayList<>();
        }

        public void addToList(Detail detail) {
            mMovieDetailList.add(detail);
        }

        public List<Detail> getMovieDetailList() {
            return mMovieDetailList;
        }

        public String getTotalResults() {
            return mTotalResults;
        }

        public String getResponse() {
            return mResponse;
        }
    }

    public interface Omdbapi {
        @GET("/?type=movie")
        Call<Result> Result(
                @Query("s") String Title);

        @GET("/?plot=full")
        Call<Detail> Detail(
                @Query("i") String ImdbId);
    }

    private static void setsOmdbApi() {
        if (sOmdbApi == null) {
            // Create a REST adapter which points the omdb API.
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OMDB API interface.
            sOmdbApi = retrofit.create(Omdbapi.class);
        }
    }

    public static Result performSearch(String title) throws IOException {
        setsOmdbApi();

        // Create a call instance for looking up the movie names by title.
        Call<Result> call = sOmdbApi.Result(title);

        return call.execute().body();
    }

    public static Detail getDetail(String imdbId) throws IOException {
        setsOmdbApi();

        Call<Detail> call = sOmdbApi.Detail(imdbId);

        return call.execute().body();
    }
}
