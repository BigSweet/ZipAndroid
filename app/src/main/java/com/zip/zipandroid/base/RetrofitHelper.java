package com.zip.zipandroid.base;

import com.google.gson.GsonBuilder;
import com.zip.zipandroid.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final int TIMEOUT_READ = 10;
    private static final int TIMEOUT_CONNECTION = 10;
    private static Retrofit retrofit;

    static {
        init();
    }

    public static void init() {
        ZipHttpLoggingInterceptor loggingInterceptor = new ZipHttpLoggingInterceptor();
        loggingInterceptor.setLevel(ZipHttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new SecurityInterceptor());
        builder.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
        }
        builder.connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                //失败重连
                .retryOnConnectionFailure(true)
                .build();

        OkHttpClient client = builder.build();
        String baseUrl;
        if (BuildConfig.DEBUG) {
//            baseUrl = "https://loansapp.solyacredito.com/";
            baseUrl = "http://94.74.106.175/cobicredit/";
        } else {
//            baseUrl = "http://overseas-loansapp-web.finbeeapp.co/";
            baseUrl = "https://overseas-loansapp-web.noahsapp.com.cn/";
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
    }

    private RetrofitHelper() {
    }

    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
