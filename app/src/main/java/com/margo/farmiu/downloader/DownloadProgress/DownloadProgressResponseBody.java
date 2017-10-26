package com.margo.farmiu.downloader.DownloadProgress;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by User on 008 08.10.17.
 */

public class DownloadProgressResponseBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private String mDownloadIdentifier;
    private DownloadProgressListener mDownloadProgressListener;
    private BufferedSource mBufferedSource;


    public DownloadProgressResponseBody(String downloadIdentifier, ResponseBody responseBody, DownloadProgressListener downloadProgressListener) {
        mResponseBody = responseBody;
        mDownloadProgressListener = downloadProgressListener;
        mDownloadIdentifier = downloadIdentifier;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if(mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }

        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                if(bytesRead != -1) {
                    totalBytesRead += bytesRead;
                }

                if(mDownloadProgressListener != null) {
                    mDownloadProgressListener.update(mDownloadIdentifier, totalBytesRead, mResponseBody.contentLength(), bytesRead != -1);
                }
                return bytesRead;
            }
        };
    }
}
