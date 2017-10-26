package com.margo.farmiu.downloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.margo.farmiu.downloader.DownloadProgress.Bus;
import com.margo.farmiu.downloader.DownloadProgress.DownloadProgressInterceptor;
import com.margo.farmiu.downloader.DownloadProgress.ProgressEvent;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by User on 027 27.09.17.
 */

// better?

public class RetrofitBuilder {

    private Retrofit mRetrofit;
    private Observable<?> mObservable;
    private Retrofit.Builder mRetrofitBuilder;

    public RetrofitBuilder(String url, Boolean isBus, Bus bus) {

        mRetrofitBuilder = getBuilder();
        if(isBus) {
            mRetrofitBuilder.client(getClient(bus));
        }
            mRetrofit = mRetrofitBuilder
                    .baseUrl(url)
                    .build();

    }

    public Retrofit.Builder getBuilder() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder RetrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));

        return RetrofitBuilder;
    }

    public OkHttpClient getClient(Bus eventBus) {


        DownloadProgressInterceptor downloadInterceptor = new DownloadProgressInterceptor(eventBus);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(downloadInterceptor)
                .build();
        return okHttpClient;
    }

    public Retrofit getRetrofit()
    {
        return mRetrofit;
    }

}
