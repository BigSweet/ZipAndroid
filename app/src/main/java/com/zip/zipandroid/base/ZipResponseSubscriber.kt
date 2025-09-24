package com.zip.zipandroid.base

import android.app.Activity
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.event.ZipLoginOutEvent
import com.zip.zipandroid.utils.ZipEventBusUtils
import com.zip.zipandroid.utils.ZipLoadingUtils.dismiss
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class ZipResponseSubscriber<T> @JvmOverloads constructor(activity: Activity? = null, showDialog: Boolean = false, showFailToast: Boolean = true) : Observer<ZipBaseRes<T>> {
    override fun onError(e: Throwable) {
        try {
            e.printStackTrace()
            if (!NetworkUtils.isConnected() || e is UnknownHostException) {
                onFailure(NET_OR_SERVER_ERROR, "The network connection failed, please check the network connection")
            } else if (e is HttpException) {
                val exception = e
                if (exception.code() == 404) {
                    onFailure(404, "The request does not exist(404)")
                } else if (exception.code() == 504) {
                    onFailure(NET_OR_SERVER_ERROR, "The network connection failed, please check the network connection")
                } else {
                    onFailure(NET_OR_SERVER_ERROR, "Request failed, please try again(" + exception.code() + ")")
                }
            } else if (e is SocketTimeoutException) {
                onFailure(NET_TIME_OUT, "Request timeout (SocketTimeOut)")
            } else {
                onFailure(NET_OR_SERVER_ERROR, "Request failed\n$e")
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(t: ZipBaseRes<T>) {
        if (t != null) {
            //同步本地与服务器时间差
            if (t.resultCode == REQUEST_SUCCESS) {
                try {
                    if (t.result == null) {
                        onSuccess(Any() as T)
                    } else {
                        onSuccess(t.result)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    if (t.resultCode == 10006) {
                        ZipEventBusUtils.post(ZipLoginOutEvent())
                    } else {
                        onFailure(t.resultCode, t.msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Throws(Exception::class)
    open fun onSuccess(t: T) {
    }

    @Throws(Exception::class)
    open fun onFailure(code: Int, message: String?) {
        dismiss()
        ToastUtils.showShort(message)
    }

    companion object {
        const val REQUEST_SUCCESS = 200
        var NET_OR_SERVER_ERROR = 0X4562
        var NET_TIME_OUT = 0X4563
    }
}
