package com.zip.zipandroid.base;


import com.zip.zipandroid.utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class SecurityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .method(original.method(), original.body());
        Request request = requestBuilder
                .header("language", "es_MX")
                .header("clientId", Constants.INSTANCE.getClient_id())
                .build();
        return chain.proceed(request);
    }

}
