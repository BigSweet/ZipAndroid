package com.zip.zipandroid.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class ZipBaseViewModel : ViewModel(), IRxDisManger {

    private var disposables: CompositeDisposable? = null
    override fun onCleared() {
        if (disposables != null) {
            disposables?.clear()
        }
    }

    override fun addReqDisposable(disposable: Disposable) {
        if (null == disposables) {
            disposables = CompositeDisposable()
        }
        disposables?.add(disposable)
    }

    override fun clear() {

    }
}