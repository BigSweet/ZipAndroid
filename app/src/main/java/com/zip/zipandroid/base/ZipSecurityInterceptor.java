package com.zip.zipandroid.base;


import com.zip.zipandroid.BuildConfig;
import com.zip.zipandroid.utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class ZipSecurityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .method(original.method(), original.body());
        String clientId = Constants.INSTANCE.getClient_id();
        if (BuildConfig.DEBUG) {
            clientId = Constants.INSTANCE.getClient_id();
//            clientId = Constants.INSTANCE.getRelease_client_id();
        } else {
            clientId = Constants.INSTANCE.getRelease_client_id();
        }

        Request request = requestBuilder
                .header("language", "es_MX")
                .header("clientId", clientId)
                .build();
        return chain.proceed(request);
    }

}
