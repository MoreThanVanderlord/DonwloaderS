package com.margo.farmiu.downloader;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.margo.farmiu.downloader.DownloadProgress.ProgressEvent;
import com.margo.farmiu.downloader.Model.Pojo.Track;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SoundCloudActivity extends AppCompatActivity {

    private CustomDrawableView mCustomDrawableView;
    private TextView mTextView;
    private NumberProgressBar mNumberProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_cloud);
        mTextView = (TextView) findViewById(R.id.text_view);
        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        mNumberProgressBar.setProgress(0);
/*
        mElasticDownloadView = (ElasticDownloadView) findViewById(R.id.elastic_download_view);
        mElasticDownloadView.startIntro();
        mElasticDownloadView.setBackground(ContextCompat.getDrawable(this, R.drawable.avd_start));*/


        SoundApi soundApi = new SoundApi(this);
        SoundApi.verifyStoragePermission(this);
        soundApi.getIntentInfo(getIntent());

    }

    public void setTextViewText(int text) {
        mTextView.setText(text);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public NumberProgressBar getNumberProgressBar() {
        return mNumberProgressBar;
    }

    public void makeToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT);
    }

    public void setTextStringViewText(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT);
    }
}
