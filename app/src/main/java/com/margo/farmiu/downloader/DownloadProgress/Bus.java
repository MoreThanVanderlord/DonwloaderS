package com.margo.farmiu.downloader.DownloadProgress;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created by User on 006 06.10.17.
 */

public class Bus
{
    private final PublishSubject<Object> mBusSubject;

    public Bus() {
        mBusSubject = PublishSubject.create();
    }

    public void post(Object event) {
        mBusSubject.onNext(event);
    }

    public Observable<Object> observable() {
        return mBusSubject;
    }

    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        return mBusSubject.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object event) {
                return eventClass.isInstance(event);
            }
        })
                .map(new Func1<Object, T>() {
                    @SuppressWarnings("uncheked")
                    @Override
                    public T call(Object event) {
                        return (T) event;
                    }
                });
    }


}
