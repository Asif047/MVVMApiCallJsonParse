package com.asif047.jsonparsingmvvm.dataSource;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.asif047.jsonparsingmvvm.dataModels.TMDBWebservices;

import java.util.concurrent.Executor;

public class MoviesInTheaterDataSourceFactory extends DataSource.Factory {

    private static final String TAG = "MoviesInTheaterData";
    MoviesInTheaterDataSource moviesDataSource;
    MutableLiveData<MoviesInTheaterDataSource> mutableLiveData;

    Executor executor;
    TMDBWebservices webService;

    public MoviesInTheaterDataSourceFactory(Executor executor, TMDBWebservices webService) {
        this.executor = executor;
        this.webService = webService;
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<MoviesInTheaterDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    @Override
    public DataSource create() {


        Log.e(TAG, "create:");
        moviesDataSource = new MoviesInTheaterDataSource(executor, webService);
        mutableLiveData.postValue(moviesDataSource);
        return moviesDataSource;

    }
}
