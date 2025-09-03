package com.zip.zipandroid.bean

import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.utils.phonedate.location.device.DeviceInfoUtil


class DeviceInfoModel {
    //    private val simSerialNumber: String
    private val idAndroid: String
    private val naKwaikwayi: Boolean
    private val sigarTsarinAiki: String
    private val kasa: String?
    private val nauInCibiyarSadarwa: String?
    private val adireshinMAC: String?
    private val idNaUraGaba: String
    private val idMaiSaya: String
    private val sunanDaukar: String?
    private val samfurin: String
    private val adadinKwaKwalwa: String
    private val warwareNunin: String
    private val tsarinNaUra: String?
    private val adadinAjiya: String
    private val UUID: String?
    private val adadinCPU: Int
    private val saurinCPU: String
    private val adireshinIP: String
    private val idTallaGoogle: String?

    init {
        val deviceInfoUtil = DeviceInfoUtil(ZipApplication.instance)
//        simSerialNumber = deviceInfoUtil.getSimId()
        idAndroid = deviceInfoUtil.getAndroidId()
        naKwaikwayi = deviceInfoUtil.isSimulator()
        sigarTsarinAiki = deviceInfoUtil.getOSVersion()
        kasa = deviceInfoUtil.getCountry()
        nauInCibiyarSadarwa = deviceInfoUtil.getNetworkType()
        adireshinMAC = deviceInfoUtil.getMacAddress()
        idNaUraGaba = deviceInfoUtil.getGenaralDeviceId()
        idMaiSaya = deviceInfoUtil.idForVendor
        sunanDaukar = deviceInfoUtil.getCarrierName()
        samfurin = deviceInfoUtil.getModel()
        adadinKwaKwalwa = deviceInfoUtil.getTotalMemory()
        warwareNunin = deviceInfoUtil.getDisplayResolution()
        tsarinNaUra = deviceInfoUtil.getDeviceArch()
        adadinAjiya = deviceInfoUtil.getTotalStorage()
        UUID = deviceInfoUtil.getUUID()
        adadinCPU = deviceInfoUtil.getCPUCount()
        saurinCPU = deviceInfoUtil.getCPUSpeed()
        adireshinIP = deviceInfoUtil.getIPAddress()



        idTallaGoogle = ""
    }
}