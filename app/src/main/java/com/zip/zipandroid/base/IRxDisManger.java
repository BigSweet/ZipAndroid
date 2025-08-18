package com.zip.zipandroid.base;

import io.reactivex.disposables.Disposable;

public interface IRxDisManger {
    void addReqDisposable(Disposable disposable);

    void clear();
}
