package com.zip.zipandroid.base;

import android.app.Activity;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zip.zipandroid.event.ZipLoginOutEvent;
import com.zip.zipandroid.utils.ZipEventBusUtils;
import com.zip.zipandroid.utils.ZipLoadingUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


public class ZipResponseSubscriber<T> implements Observer<ZipBaseRes<T>> {
    public static final int REQUEST_SUCCESS = 200;
    public static int NET_OR_SERVER_ERROR = 0X4562;
    public static int NET_TIME_OUT = 0X4563;


    public ZipResponseSubscriber() {
        this(null, false, true);
    }

    public ZipResponseSubscriber(Activity activity, boolean showDialog) {
        this(activity, showDialog, true);
    }

    public ZipResponseSubscriber(Activity activity, boolean showDialog, boolean showFailToast) {
    }


    @Override
    public final void onError(Throwable e) {
        try {
            e.printStackTrace();
            if (!NetworkUtils.isConnected() || e instanceof UnknownHostException) {
                onFailure(NET_OR_SERVER_ERROR, "The network connection failed, please check the network connection");
            } else if (e instanceof HttpException) {
                HttpException exception = (HttpException) e;
                if (exception.code() == 404) {
                    onFailure(404, "The request does not exist(404)");
                } else if (exception.code() == 504) {
                    onFailure(NET_OR_SERVER_ERROR, "The network connection failed, please check the network connection");
                } else {
                    onFailure(NET_OR_SERVER_ERROR, "Request failed, please try again(" + exception.code() + ")");
                }

            } else if (e instanceof SocketTimeoutException) {
                onFailure(NET_TIME_OUT, "Request timeout (SocketTimeOut)");
            } else {
                onFailure(NET_OR_SERVER_ERROR, "Request failed\n" + e.toString());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public final void onNext(ZipBaseRes<T> t) {
        if (t != null) {
            //同步本地与服务器时间差
            if (t.getResultCode() == REQUEST_SUCCESS) {
                try {
                    if (t.getResult() == null) {
                        onSuccess((T) new Object());
                    } else {
                        onSuccess(t.getResult());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (t.getResultCode() == 10006) {
                        ZipEventBusUtils.post(new ZipLoginOutEvent());
                    } else {
                        onFailure(t.getResultCode(), t.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void onSuccess(T t) throws Exception {
    }

    public void onFailure(int code, String message) throws Exception {
        ZipLoadingUtils.INSTANCE.dismiss();
        ToastUtils.showShort(message);
    }
}
