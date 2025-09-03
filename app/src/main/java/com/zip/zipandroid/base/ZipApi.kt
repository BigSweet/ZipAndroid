package com.zip.zipandroid.base

import com.zip.zipandroid.bean.AddressInfoBean
import com.zip.zipandroid.bean.BvnInfoBean
import com.zip.zipandroid.bean.CreditListBean
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.ProductDidInfo
import com.zip.zipandroid.bean.ProductPidBean
import com.zip.zipandroid.bean.RealUploadUserBean
import com.zip.zipandroid.bean.UploadImgBean
import com.zip.zipandroid.bean.ZipAdBean
import com.zip.zipandroid.bean.ZipAppConfigBean
import com.zip.zipandroid.bean.ZipBandCardBean
import com.zip.zipandroid.bean.ZipBankNameListBean
import com.zip.zipandroid.bean.ZipBizBean
import com.zip.zipandroid.bean.ZipCodeBean
import com.zip.zipandroid.bean.ZipCouponItemBean
import com.zip.zipandroid.bean.ZipCouponListBean
import com.zip.zipandroid.bean.ZipHomeDataBean
import com.zip.zipandroid.bean.ZipLoginResponse
import com.zip.zipandroid.bean.ZipOrderAdmissionBean
import com.zip.zipandroid.bean.ZipOrderListBean
import com.zip.zipandroid.bean.ZipQueryCardBean
import com.zip.zipandroid.bean.ZipRealNameBean
import com.zip.zipandroid.bean.ZipRiskLevelBean
import com.zip.zipandroid.bean.ZipTriaBean
import com.zip.zipandroid.bean.ZipUploadUserInfoBean
import com.zip.zipandroid.bean.ZipUserInfoBean
import com.zip.zipandroid.utils.FormReq
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ZipApi {


    @POST("api/v4/ziplead/customer/sendSmsCode")
    fun getCode(@Body body: FormReq): Observable<ZipBaseRes<ZipCodeBean>>


    @POST("api/v4/ziplead/product/trial")
    fun orderTrial(@Body body: FormReq): Observable<ZipBaseRes<ZipTriaBean>>

    @POST("api/v4/ziplead/creationOrderByMx")
    fun creationOrderByMx(@Body body: FormReq): Observable<ZipBaseRes<ZipBizBean>>





    @POST("api/v4/ziplead/product/findProductByPid")
    fun findProductDueByPid(@Body body: FormReq): Observable<ZipBaseRes<List<ProductDidInfo>>>

    @POST("api/v4/ziplead/customer/login")
    fun zipLogin(@Body body: FormReq): Observable<ZipBaseRes<ZipLoginResponse>>

    @POST("api/v4/ziplead/risk_level")
    fun getRiskLevel(@Body body: FormReq): Observable<ZipBaseRes<ZipRiskLevelBean>>


    @POST("api/v4/ziplead/product/origin")
    fun getHomeData(@Body body: FormReq): Observable<ZipBaseRes<ZipHomeDataBean>>

    @POST("api/v4/ziplead/selectAdvertList")
    fun selectAdvertList(@Body body: FormReq): Observable<ZipBaseRes<ZipAdBean>>

    @POST("api/v4/ziplead/app-configuration")
    fun getConfig(@Body body: FormReq): Observable<ZipBaseRes<ZipAppConfigBean>>

    @POST("api/v4/ziplead/coupon/myCouponList")
    fun getCouponList(@Body body: FormReq): Observable<ZipBaseRes<ZipCouponListBean>>

    @POST("api/v4/ziplead/coupon/topPriorityCoupon")
    fun topPriorityCoupon(@Body body: FormReq): Observable<ZipBaseRes<ZipCouponItemBean?>>


    @POST("api/v4/ziplead/bankCard/queryCard")
    fun zipQueryCard(@Body body: FormReq): Observable<ZipBaseRes<ZipQueryCardBean>>

    @POST("api/v4/ziplead/customer/queryBaseInfo")
    fun getUserInfo(@Body body: FormReq): Observable<ZipBaseRes<ZipUserInfoBean>>

    @POST("api/v4/ziplead/customer/queryBaseInfo")
    fun getUploadUserInfo(@Body body: FormReq): Observable<ZipBaseRes<RealUploadUserBean>>

    @POST("api/v4/ziplead/bankCard/bindCard")
    fun zipBandCard(@Body body: FormReq): Observable<ZipBaseRes<ZipBandCardBean>>

    @POST("api/v4/ziplead/bankCard/changeCard")
    fun changeCard(@Body body: FormReq): Observable<ZipBaseRes<Any>>

    @POST("api/v4/ziplead/checkBvn")
    fun checkBvnInfo(@Body body: FormReq): Observable<ZipBaseRes<BvnInfoBean>>


    @POST("api/v4/ziplead/customer/renewMemInfo")
    fun saveUserInfo(@Body body: FormReq): Observable<ZipBaseRes<Any>>

    @POST("api/v4/ziplead/customer/realName")
    fun realName(@Body body: FormReq): Observable<ZipBaseRes<ZipRealNameBean>>

    @POST("api/v4/ziplead/customer/saveMemberBehavior")
    fun saveMemberBehavior(@Body body: FormReq): Observable<ZipBaseRes<Any>>

    @POST("api/v4/ziplead/mxPostalCodeInfoList")
    fun getAllAddressInfo(@Body body: FormReq): Observable<ZipBaseRes<List<AddressInfoBean>>>


    @POST("api/v4/ziplead/orders")
    fun getAllOrderList(@Body body: FormReq): Observable<ZipBaseRes<ZipOrderListBean>>

    @GET("api/v4/ziplead/dict/getCreditHistoryDict")
    fun getCreditHistoryDict(
        @Query("fakitinAiki") fakitinAiki: String,
        @Query("sigarBincike") sigarBincike: String,
        @Query("tushen") tushen: String,
        @Query("matsakaici") matsakaici: String,
        @Query("lambarMutum") lambarMutum: String,
        @Query("idAbokinCiniki") idAbokinCiniki: String,
        @Query("sanyaHannu") sanyaHannu: String,
    ): Observable<ZipBaseRes<CreditListBean>>

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

    @POST("api/v4/ziplead/file/upload")
    fun uploadImg(@Body body: RequestBody): Observable<ZipBaseRes<UploadImgBean>>

    @POST("api/v4/ziplead/file/getPath")
    fun getImgPath(@Body body: FormReq): Observable<ZipBaseRes<HashMap<Any, Any>>>

    @POST("api/v4/ziplead/bankCard/bankList")
    fun getBankList(@Body body: FormReq): Observable<ZipBaseRes<ZipBankNameListBean>>


    @POST("api/v4/ziplead/admittanceSubmmit")
    fun admission(@Body body: FormReq): Observable<ZipBaseRes<ZipOrderAdmissionBean>>


    @POST("api/v4/ziplead/creationOrderBefore")
    fun creationOrderBefore(@Body body: FormReq): Observable<ZipBaseRes<ZipBizBean>>

}