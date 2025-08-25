package com.zip.zipandroid.base

import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.ZipAdBean
import com.zip.zipandroid.bean.ZipAppConfigBean
import com.zip.zipandroid.bean.ZipCodeBean
import com.zip.zipandroid.bean.ZipHomeDataBean
import com.zip.zipandroid.bean.ZipLoginResponse
import com.zip.zipandroid.bean.ZipUserInfoBean
import com.zip.zipandroid.utils.FormReq
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ZipApi {


    @POST("api/v4/ziplead/customer/sendSmsCode")
    fun getCode(@Body body: FormReq): Observable<ZipBaseRes<ZipCodeBean>>


    @POST("api/v4/ziplead/customer/login")
    fun zipLogin(@Body body: FormReq): Observable<ZipBaseRes<ZipLoginResponse>>


    @POST("api/v4/ziplead/product/origin")
    fun getHomeData(@Body body: FormReq): Observable<ZipBaseRes<ZipHomeDataBean>>

    @POST("api/v4/ziplead/selectAdvertList")
    fun selectAdvertList(@Body body: FormReq): Observable<ZipBaseRes<ZipAdBean>>

    @POST("api/v4/ziplead/app-configuration")
    fun getConfig(@Body body: FormReq): Observable<ZipBaseRes<ZipAppConfigBean>>

    @POST("api/v4/ziplead/customer/queryBaseInfo")
    fun getUserInfo(@Body body: FormReq): Observable<ZipBaseRes<ZipUserInfoBean>>

    @GET("api/v4/ziplead/dict/getPersonalInformationDict")
    fun getPersonalInformationDict(
        @Query("fakitinAiki") fakitinAiki: String,
        @Query("sigarBincike") sigarBincike: String,
        @Query("tushen") tushen: String,
        @Query("matsakaici") matsakaici: String,
        @Query("lambarMutum") lambarMutum: String,
        @Query("idAbokinCiniki") idAbokinCiniki: String,
        @Query("sanyaHannu") sanyaHannu: String,
    ): Observable<ZipBaseRes<PersonalInformationDictBean>>


}