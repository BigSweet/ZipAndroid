package com.zip.zipandroid.base

import io.reactivex.Observable
import retrofit2.http.POST

interface MacawApi {

    //领取奖励
    @POST("/interactive/blood/teller/reward")
    fun getTellerReward(): Observable<Any>



}