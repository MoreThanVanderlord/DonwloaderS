package com.margo.farmiu.downloader.DownloadProgress;

/**
 * Created by User on 008 08.10.17.
 */

public interface DownloadProgressListener {
    void update(String identifier, long bytesRead, long contentLength, boolean done);
}
