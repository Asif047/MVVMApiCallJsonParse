package com.asif047.jsonparsingmvvm.dataModels;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBWebservices {

    @GET("movie/now_playing")
    Call<ResponseBody> getMoviesInTheater(@Query("page") long pageNumber);

}
