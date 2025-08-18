package com.zip.zipandroid.base

import com.zip.zipandroid.bean.ZipCodeBean
import com.zip.zipandroid.utils.FormReq
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ZipApi {


    @POST("api/customer/sendSmsCode")
    fun getCode(@Body body: FormReq): Observable<BaseRes<ZipCodeBean>>


}