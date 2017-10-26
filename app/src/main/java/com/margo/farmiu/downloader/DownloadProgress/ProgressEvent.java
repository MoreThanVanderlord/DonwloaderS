package com.margo.farmiu.downloader.DownloadProgress;

/**
 * Created by User on 006 06.10.17.
 */

public class ProgressEvent {
    final int progress;
    final long contentLength;
    final long bytesRead;
    final String downloadIdentifier;

    public ProgressEvent(String downloadIdentifier, long contentLength, long bytesRead) {
        this.downloadIdentifier = downloadIdentifier;
        this.progress = (int) (bytesRead/(contentLength/100f));
        this.contentLength = contentLength;
        this.bytesRead = bytesRead;
    }

    public int getProgress() {
        return progress;
    }

    public long getContentLength() {
        return contentLength;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public boolean percentIsAvailable() {
        return contentLength > 0;
    }

    public String getDownloadIdentifier() {
        return downloadIdentifier;
    }
}

