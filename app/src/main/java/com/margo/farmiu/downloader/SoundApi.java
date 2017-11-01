package com.margo.farmiu.downloader;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.margo.farmiu.downloader.DownloadProgress.Bus;
import com.margo.farmiu.downloader.DownloadProgress.ProgressEvent;
import com.margo.farmiu.downloader.Model.Pojo.DownloadLink;
import com.margo.farmiu.downloader.Model.Pojo.Playlist;
import com.margo.farmiu.downloader.Model.Pojo.Track;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by User on 002 02.10.17.
 */

public class SoundApi {

    private final Retrofit mRetrofit;
    private final SoundCloudService mSoundCloudService;
    private SoundCloudActivity mActivity;
    private final String dir = Environment.getExternalStorageDirectory() +
            File.separator + "Music downloader" + File.separator;
    private final String slashTrack = "/tracks";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public SoundApi(SoundCloudActivity activity) {
        mActivity = activity;
        mRetrofit = new RetrofitBuilder(SoundCloudService.SOUND_ClOUD_API, false, null).getRetrofit();
        mSoundCloudService = mRetrofit.create(SoundCloudService.class);
    }



    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public void getIntentInfo(Intent intent) {

        String s = intent.getClipData().getItemAt(0).getText().toString();


        if (!s.isEmpty()) {
            String url = s.substring(s.lastIndexOf("https://soundcloud"));
            if(url.contains("/sets/")) {
                getPlaylist(url);
            }
            else {
                download(url);
            }
        }
        else {
            Toast.makeText(mActivity, "Try again", Toast.LENGTH_SHORT);
        }
    }


    private void getPlaylist(String playlist) {

        mSoundCloudService.getPlaylistInfo(playlist, SoundCloudService.CLIENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Playlist::getTracks)
                .flatMap(tracks -> Observable.from(tracks))
                .flatMap(track ->  downloadFromLink(track, track.getId(),
                        getRetrofit(track.getId()).create(SoundCloudDownloadService.class)))
               .subscribe(new Subscriber<File>() {
                   @Override
                   public void onCompleted() {
                       mActivity.setTextStringViewText("A");

                   }

                   @Override
                   public void onError(Throwable e) {
                       mActivity.setTextStringViewText(e.toString());
                   }

                   @Override
                   public void onNext(File file) {
                       mActivity.setTextStringViewText("A");

                   }
               });


    }

    private void download(String trackUrl) {

        Subscription subscribe = getTrack(trackUrl)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<File>() {
                        @Override
                        public void onCompleted() {
                            int i = 0;
                        }

                        @Override
                        public void onError(Throwable e) {
                            mActivity.setTextViewText(R.string.download_error);
                            mActivity.getNumberProgressBar().setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onNext(File file) {
                            // everything is
                            System.out.println("File downloaded : " + file.getAbsolutePath());
                        }
                    });
    }

    private Retrofit getRetrofit(String identifier) {

        Bus bus = new Bus();
        final Subscription progressSubscription = bus.filteredObservable(ProgressEvent.class)
                .onBackpressureDrop()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ProgressEvent>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        this.unsubscribe();
                    }

                    @Override
                    public void onNext(ProgressEvent progressEvent) {
                        if(progressEvent.getDownloadIdentifier().equals(identifier)) {
                            int num =(int)(100*progressEvent.getBytesRead()/progressEvent.getContentLength());
                            mActivity.getNumberProgressBar().setProgress(num);
                        }
                    }
                });
        return new RetrofitBuilder(SoundCloudDownloadService.SOUND_LOAD, true, bus).getRetrofit();
    }

    private Observable<File> getTrack(String trackUrl) {


        final String identifier = trackUrl;
        Retrofit retrofit = getRetrofit(identifier);
        SoundCloudDownloadService soundCloudDownloadService = retrofit.create(SoundCloudDownloadService.class);

        return mSoundCloudService.getTrackInfo(trackUrl + slashTrack, SoundCloudService.CLIENT_ID)
                .flatMap(track -> downloadFromLink(track, identifier,
                            soundCloudDownloadService));

    }

    private Observable<File> downloadFromLink(Track track, String identifier, SoundCloudDownloadService soundCloudDownloadService) {


            return mSoundCloudService.getLink(track.getId(), SoundCloudService.CLIENT_ID)
                .subscribeOn(Schedulers.io())
                .map(url -> (url.getHttp_mp3_128_url().getPath().substring(1) + "&" + url.getHttp_mp3_128_url().getQuery())
                .split("=|&"))
                .flatMap(urls -> {

                    Observable<retrofit2.Response<ResponseBody>> observable = soundCloudDownloadService.download(urls[0], urls[2], urls[4], urls[6], identifier);

                    return observable
                            .flatMap(new Func1<retrofit2.Response<ResponseBody>, Observable<File>>() {
                                @Override
                                public Observable<File> call(retrofit2.Response<ResponseBody> response) {
                                    try {
                                        //           progressSubscription.unsubscribe(); // UNSUBSCRIBE EVENT ON FINISH !
                                        // you should check here for errors (or use doOnNext and doOnError methods)

                                        File directory = new File(dir);
                                        if(!directory.exists()) {
                                            directory.mkdir();
                                        }
                                        String fileName = track.getTitle(); // you can get it from header if set

                                        File file = new File(dir, fileName+".mp3");
                                        BufferedSink sink = Okio.buffer(Okio.sink(file));
                                        sink.writeAll(response.body().source());
                                        sink.close();

                                        return Observable.just(file);
                                    } catch (IOException e) {
                                        return Observable.error(e);
                                    }
                                }
                            });
                });
    }






}


