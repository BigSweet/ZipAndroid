package com.zip.zipandroid.base;


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
                .header("parisrocketloaf", "es")
                .header("labunfoldfiercehuge", "MX")
                .header("birdcagefat", "1289")
                .header("scarsaleswoman", UserInfo.getInstance().getUserId() + "")
                .header("shoppingdiplomadullasleep", "1")
                .header("disappointmentspaceshipglue", UserInfo.getInstance().getToken())
                .build();
        return chain.proceed(request);
    }

}
