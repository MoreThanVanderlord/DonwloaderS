package com.margo.farmiu.downloader.DownloadProgress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by User on 006 06.10.17.
 */

public class DownloadProgressInterceptor implements Interceptor {

    public static final String DOWNLOAD_IDENTIFIER_HEADER = "download-identifier";

    private final Bus eventBus;

    public DownloadProgressInterceptor(Bus eventBus) {
        this.eventBus = eventBus;
    }

    public Bus getEventBus() {
        return eventBus;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        Response.Builder builder = originalResponse.newBuilder();

        String downloadIdentifier = originalResponse.request().header(DOWNLOAD_IDENTIFIER_HEADER);

       boolean fileIdentifierIsSet = downloadIdentifier != null && !downloadIdentifier.isEmpty();

        if(fileIdentifierIsSet) {
            builder.body(new DownloadProgressResponseBody(downloadIdentifier, originalResponse.body(), new DownloadProgressListener() {
                @Override
                public void update(String identifier, long bytesRead, long contentLength, boolean done) {
                    eventBus.post(new ProgressEvent(identifier, contentLength, bytesRead));
                }
            }));
        }
        else {
            builder.body(originalResponse.body());
        }

        return builder.build();
    }
}
