package com.margo.farmiu.downloader;

import com.google.android.gms.common.api.Response;
import com.margo.farmiu.downloader.Model.Pojo.DownloadLink;
import com.margo.farmiu.downloader.Model.Pojo.MusicFile;
import com.margo.farmiu.downloader.Model.Pojo.Playlist;
import com.margo.farmiu.downloader.Model.Pojo.Track;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by User on 026 26.09.17.
 */

public interface SoundCloudService {

    String CLIENT_ID = "5f016c08c2201881c4217afd5f52e065";
    String SOUND_ClOUD_API = "http://api.soundcloud.com/";

    @GET("resolve.json")
    Observable<Track> getTrackInfo(@Query("url") String url, @Query("client_id") String clientId);


   @GET("resolve.json")
   Observable<Playlist> getPlaylistInfo(@Query("url") String url, @Query("client_id") String clientId);


   @GET("i1/tracks/{id}/streams")
    Observable<DownloadLink> getLink(@Path("id") String trackId, @Query("client_id") String clientId);

}
