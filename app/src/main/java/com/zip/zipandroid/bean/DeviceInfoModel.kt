package com.zip.zipandroid.bean

import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.utils.phonedate.location.device.DeviceInfoUtil


class DeviceInfoModel {
    //    private val simSerialNumber: String
    private val androidId: String
    private val isSimulator: Boolean
    private val osVersion: String
    private val country: String?
    private val networkType: String?
    private val macAddress: String?
    private val generalDeviceId: String
    private val idForVendor: String
    private val idForAdvertising: String? = null
    private val carrierName: String?
    private val model: String
    private val totalMemory: String
    private val displayResolution: String
    private val deviceArch: String?
    private val totalStorage: String
    private val uuid: String?
    private val cpuCount: Int
    private val cpuSpeed: String
    private val ipAddress: String
    private val googleadid: String?

    init {
        val deviceInfoUtil = DeviceInfoUtil(ZipApplication.instance)
//        simSerialNumber = deviceInfoUtil.getSimId()
        androidId = deviceInfoUtil.getAndroidId()
        isSimulator = deviceInfoUtil.isSimulator()
        osVersion = deviceInfoUtil.getOSVersion()
        country = deviceInfoUtil.getCountry()
        networkType = deviceInfoUtil.getNetworkType()
        macAddress = deviceInfoUtil.getMacAddress()
        generalDeviceId = deviceInfoUtil.getGenaralDeviceId()
        idForVendor = deviceInfoUtil.idForVendor
        carrierName = deviceInfoUtil.getCarrierName()
        model = deviceInfoUtil.getModel()
        totalMemory = deviceInfoUtil.getTotalMemory()
        displayResolution = deviceInfoUtil.getDisplayResolution()
        deviceArch = deviceInfoUtil.getDeviceArch()
        totalStorage = deviceInfoUtil.getTotalStorage()
        uuid = deviceInfoUtil.getUUID()
        cpuCount = deviceInfoUtil.getCPUCount()
        cpuSpeed = deviceInfoUtil.getCPUSpeed()
        ipAddress = deviceInfoUtil.getIPAddress()



        googleadid = ""
    }
}