package com.margo.farmiu.downloader;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 026 26.09.17.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
