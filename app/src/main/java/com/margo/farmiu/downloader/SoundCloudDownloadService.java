package com.margo.farmiu.downloader;

import com.google.android.gms.common.api.Response;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

import static com.margo.farmiu.downloader.DownloadProgress.DownloadProgressInterceptor.DOWNLOAD_IDENTIFIER_HEADER;

/**
 * Created by User on 004 04.10.117.
 */

public interface SoundCloudDownloadService {

    String SOUND_LOAD = "https://cf-media.sndcdn.com/";

    @GET("{path}")
    @Streaming
    Observable<retrofit2.Response<ResponseBody>> download(@Path("path") String source, @Query("Policy") String policy, @Query("Signature") String signature,
                                                         @Query("Key-Pair-Id") String keyPairId, @Header(DOWNLOAD_IDENTIFIER_HEADER) String downloadIdentifier);
}
