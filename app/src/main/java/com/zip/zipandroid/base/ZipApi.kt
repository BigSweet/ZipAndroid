package com.zip.zipandroid.base

import com.zip.zipandroid.bean.ZipCodeBean
import com.zip.zipandroid.bean.ZipLoginResponse
import com.zip.zipandroid.utils.FormReq
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ZipApi {


    @POST("api/v4/ziplead/customer/sendSmsCode")
    fun getCode(@Body body: FormReq): Observable<ZipBaseRes<ZipCodeBean>>


    @POST("api/v4/ziplead/customer/login")
    fun zipLogin(@Body body: FormReq): Observable<ZipBaseRes<ZipLoginResponse>>


}