package com.zip.zipandroid.bean

class ZipAdBean : ArrayList<ZipAdBeanItem>()

data class ZipAdBeanItem(
    val advertAreaId: Long,
    val advertAreaName: String,
    val advertContent: String,
    val advertSort: Int,
    val advertTitle: String,
    val appName: Any,
    val areaRevealType: Int,
    val bundleId: Any,
    val channelName: Any,
    val clickId: Any,
    val clientId: Any,
    val createTime: Long,
    val delFlag: Int,
    val deviceId: Any,
    val deviceInfo: Any,
    val endDate: Long,
    val extra: Any,
    val id: Long,
    val imgUrl: String,
    val isNeedLogin: Int,
    val isrotationplay: Int,
    val linkedUrl: String,
    val packageName: Any,
    val parent: Any,
    val source: Any,
    val startDate: Long,
    val startEndDate: Any,
    val status: Int,
    val tenantId: Long,
    val updateTime: Long,
    val userStatus: Int,
    val userType: Int,
    val version: Any
)