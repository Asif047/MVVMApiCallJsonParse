package com.asif047.jsonparsingmvvm.dataSource;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.asif047.jsonparsingmvvm.apiUtils.JSONParser;
import com.asif047.jsonparsingmvvm.apiUtils.NetworkState;
import com.asif047.jsonparsingmvvm.apiUtils.ServiceGenerator;
import com.asif047.jsonparsingmvvm.dataModels.TMDBWebservices;
import com.asif047.jsonparsingmvvm.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesInTheaterDataSource extends PageKeyedDataSource<Long, Movie> {

    private static String TAG = "MoviesInTheaterData";
    private TMDBWebservices tmdbWebservices;
    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private Executor retryExecutor;

    public MoviesInTheaterDataSource(Executor retryExecutor, TMDBWebservices webService ) {
        tmdbWebservices = webService;
        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
        this.retryExecutor = retryExecutor;
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Movie> callback) {

        Log.e(TAG, "Load Initials: ");
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        tmdbWebservices.getMoviesInTheater(ServiceGenerator.API_DEFAULT_PAGE_KEY).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseString;
                List<Movie> movieList;

                if(response.isSuccessful() && response.code() == 200) {
                    try{
                        initialLoading.postValue(NetworkState.LOADING);
                        networkState.postValue(NetworkState.LOADED);
                        responseString = response.body().string();
                        movieList = JSONParser.getMovieList(responseString);
                        callback.onResult(movieList, null, (long) 2);
                    } catch (IOException | JSONException e) {

                    }
                } else {
                    Log.e(TAG, "On Response error"+response.message());
                    initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                String errorMessage = t.getMessage();
                Log.e(TAG, "On failure:"+ errorMessage);
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Movie> callback) {

        networkState.postValue(NetworkState.LOADING);
        tmdbWebservices.getMoviesInTheater(params.key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject responseJson;
                String responseString;
                List<Movie> moviesList;
                Long nextKey;

                if(response.isSuccessful() && response.code() == 200) {
                    try{
                        initialLoading.postValue(NetworkState.LOADING);
                        networkState.postValue(NetworkState.LOADED);

                        responseString = response.body().string();
                        moviesList = JSONParser.getMovieList(responseString);

                        responseJson = new JSONObject(responseString);
                        nextKey = (params.key == responseJson.getInt("total_pages"))?null: params.key+1;

                        callback.onResult(moviesList, nextKey);

                    } catch (IOException | JSONException e) {

                    }
                } else {
                    Log.e(TAG, "On response error:"+response.message());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                String errorMessage = t.getMessage();
                Log.e(TAG, "On Failure:"+errorMessage);
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

            }
        });

    }
}
