package com.zip.zipandroid.base;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RxSchedulers extends BaseRxSchedulers {

    public static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {

        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }
}
