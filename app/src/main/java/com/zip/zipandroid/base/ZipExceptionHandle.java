package com.zip.zipandroid.base;

import android.text.TextUtils;

import com.google.gson.Gson;

import retrofit2.HttpException;


public class ZipExceptionHandle {

    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex;
        if (e instanceof HttpException) {
            ex = new ResponeThrowable(e, -1);
            try {
                ex.msg = new Gson().fromJson(((HttpException) e).response().errorBody().string(), ZipErrorResponse.class).getMsg();
            } catch (Exception e1) {
                ex.msg = "error de red";
            }
            ex.result = ((HttpException) e).code();
        } else if (e instanceof ResponeThrowable) {
            ex = (ResponeThrowable) e;
        } else {
            ex = new ResponeThrowable(e, 1000);
            ex.msg = TextUtils.isEmpty(e.getMessage()) ? "error desconocido" : e.getMessage();
        }
        return ex;
    }
//
//    public static ResponeThrowable handleResponse(BaseResponseEntity response) {
//        return new ResponeThrowable(new Throwable(response.getMsg()), response.getR());
//    }

    public static class ResponeThrowable extends Exception {

        public int result;
        public String msg;

        public ResponeThrowable(Throwable throwable, int result) {
            super(throwable);
            if (!TextUtils.isEmpty(throwable.getMessage())) {
                msg = throwable.getMessage();
            } else {
                msg = "Solicitud fallida";
            }
            this.result = result;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {

    }
}

