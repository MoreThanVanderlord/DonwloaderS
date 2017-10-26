package com.margo.farmiu.downloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ihhira.android.filechooser.FileChooser;
import java.io.File;

import static com.ihhira.android.filechooser.FileChooser.*;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileChooser fileChooser = new FileChooser(this, getString(R.string.choose_directory), DialogType.SELECT_DIRECTORY, null);
        FileSelectionCallback callback = file -> {
            int o = 0;
            //Do something with the selected file
        };
        fileChooser.show(callback);
    }
}
