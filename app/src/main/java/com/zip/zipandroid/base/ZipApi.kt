package com.zip.zipandroid.base

import com.zip.zipandroid.bean.AddressInfoBean
import com.zip.zipandroid.bean.BvnInfoBean
import com.zip.zipandroid.bean.CreditListBean
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.ProductDidInfo
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
import com.zip.zipandroid.bean.ZipOrderStatusBean
import com.zip.zipandroid.bean.ZipPayChannelListBean
import com.zip.zipandroid.bean.ZipPayCodeBean
import com.zip.zipandroid.bean.ZipQueryCardBean
import com.zip.zipandroid.bean.ZipRealNameBean
import com.zip.zipandroid.bean.ZipRiskLevelBean
import com.zip.zipandroid.bean.ZipTriaBean
import com.zip.zipandroid.bean.ZipUserInfoBean
import com.zip.zipandroid.utils.ZipFormReq
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ZipApi {


    @POST("api/v4/ziplead/customer/sendSmsCode")
    fun getCode(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipCodeBean>>


    @GET("api/v4/ziplead/getProtocolBeforeLoan")
    fun getProtocolBeforeLoan(
        @Query("adadinNema") applyAmount: String,
        @Query("matakinHadariNext") riskLevel: String,
        @Query("nauInSamfur") productType: String,
        @Query("lambarWaya") phoneNum: String,
        @Query("dNaUraid") did: String,
        @Query("lambarKatin") cardNo: String,
        @Query("idKasuwancin") bizId: String,
        @Query("sunanBanki") bankName: String,
        @Query("lokacinNema") applyTime: String,

        @Query("fakitinAiki") fakitinAiki: String,
        @Query("sigarBincike") sigarBincike: String,
        @Query("tushen") tushen: String,
        @Query("matsakaici") matsakaici: String,
        @Query("lambarMutum") lambarMutum: String,
        @Query("idAbokinCiniki") idAbokinCiniki: String,

        @Query("sunanAiki") sunanAiki: String,
        @Query("sunanYarjejeniya") sunanYarjejeniya: String,
        @Query("sanyaHannu") sanyaHannu: String
        ): Observable<ResponseBody>

    @POST("api/v4/ziplead/getCreditxStatus")
    fun getCreditxStatus(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipOrderStatusBean>>


    @POST("api/v4/ziplead/product/trial")
    fun orderTrial(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipTriaBean>>

    @POST("api/v4/ziplead/creationOrderByMx")
    fun creationOrderByMx(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipBizBean>>


    @POST("api/v4/ziplead/product/findProductByPid")
    fun findProductDueByPid(@Body body: ZipFormReq): Observable<ZipBaseRes<List<ProductDidInfo>>>

    @POST("api/v4/ziplead/customer/login")
    fun zipLogin(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipLoginResponse>>

    @POST("api/v4/ziplead/risk_level")
    fun getRiskLevel(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipRiskLevelBean>>


    @POST("api/v4/ziplead/product/origin")
    fun getHomeData(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipHomeDataBean>>

    @POST("api/v4/ziplead/trackEvent")
    fun trackEvent(@Body body: ZipFormReq): Observable<ZipBaseRes<Any>>

    @POST("api/v4/ziplead/selectAdvertList")
    fun selectAdvertList(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipAdBean>>

    @POST("api/v4/ziplead/app-configuration")
    fun getConfig(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipAppConfigBean>>

    @POST("api/v4/ziplead/coupon/myCouponList")
    fun getCouponList(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipCouponListBean>>

    @POST("api/v4/ziplead/coupon/topPriorityCoupon")
    fun topPriorityCoupon(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipCouponItemBean?>>


    @POST("api/v4/ziplead/bankCard/queryCard")
    fun zipQueryCard(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipQueryCardBean>>

    @POST("api/v4/ziplead/customer/queryBaseInfo")
    fun getUserInfo(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipUserInfoBean>>

    @POST("api/v4/ziplead/customer/queryBaseInfo")
    fun getUploadUserInfo(@Body body: ZipFormReq): Observable<ZipBaseRes<RealUploadUserBean>>

    @POST("api/v4/ziplead/bankCard/bindCard")
    fun zipBandCard(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipBandCardBean>>

    @POST("api/v4/ziplead/bankCard/changeCard")
    fun changeCard(@Body body: ZipFormReq): Observable<ZipBaseRes<Any>>

    @POST("api/v4/ziplead/checkBvn")
    fun checkBvnInfo(@Body body: ZipFormReq): Observable<ZipBaseRes<BvnInfoBean?>>


    @POST("api/v4/ziplead/customer/renewMemInfo")
    fun saveUserInfo(@Body body: ZipFormReq): Observable<ZipBaseRes<Any>>

    @POST("api/v4/ziplead/customer/realName")
    fun realName(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipRealNameBean>>

    @POST("api/v4/ziplead/customer/saveMemberBehavior")
    fun saveMemberBehavior(@Body body: ZipFormReq): Observable<ZipBaseRes<Any>>

    @POST("api/v4/ziplead/mxPostalCodeInfoList")
    fun getAllAddressInfo(@Body body: ZipFormReq): Observable<ZipBaseRes<List<AddressInfoBean>>>


    @POST("api/v4/ziplead/orders")
    fun getAllOrderList(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipOrderListBean>>

    @POST("api/v4/ziplead/generateOfflineRepaymentCode")
    fun generateOfflineRepaymentCode(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipPayCodeBean>>

    @POST("api/v4/ziplead/getRepaymentRoute")
    fun getRepaymentRoute(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipPayChannelListBean>>

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
    fun getImgPath(@Body body: ZipFormReq): Observable<ZipBaseRes<HashMap<Any, Any>>>

    @POST("api/v4/ziplead/bankCard/bankList")
    fun getBankList(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipBankNameListBean>>


    @POST("api/v4/ziplead/admittanceSubmmit")
    fun admission(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipOrderAdmissionBean>>


    @POST("api/v4/ziplead/creationOrderBefore")
    fun creationOrderBefore(@Body body: ZipFormReq): Observable<ZipBaseRes<ZipBizBean>>

}