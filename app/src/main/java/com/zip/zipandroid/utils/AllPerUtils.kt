package com.zip.zipandroid.utils

import android.Manifest

object AllPerUtils {


//    val phoneStatusPer = Manifest.permission.BLUETOOTH
    val netWorkStatusPer = Manifest.permission.ACCESS_NETWORK_STATE
//    val redCalendar = Manifest.permission.READ_CALENDAR
    val wifiStatus = Manifest.permission.ACCESS_WIFI_STATE

    fun getAllPer(): ArrayList<String> {
        val list = arrayListOf<String>()
//        list.add(phoneStatusPer)
        list.add(netWorkStatusPer)
//        list.add(redCalendar)
        list.add(wifiStatus)
        return list
    }
}