package com.zip.zipandroid.base;

import io.reactivex.disposables.Disposable;

public interface ZipIRxDisManger {
    void addReqDisposable(Disposable disposable);

    void clear();
}
