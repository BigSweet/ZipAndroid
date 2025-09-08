package com.zip.zipandroid.bean

import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.utils.phonedate.location.device.ZipDeviceInfoUtil


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
        val zipDeviceInfoUtil = ZipDeviceInfoUtil(ZipApplication.instance)
//        simSerialNumber = deviceInfoUtil.getSimId()
        idAndroid = zipDeviceInfoUtil.getAndroidId()
        naKwaikwayi = zipDeviceInfoUtil.isSimulator()
        sigarTsarinAiki = zipDeviceInfoUtil.getOSVersion()
        kasa = zipDeviceInfoUtil.getCountry()
        nauInCibiyarSadarwa = zipDeviceInfoUtil.getNetworkType()
        adireshinMAC = zipDeviceInfoUtil.getMacAddress()
        idNaUraGaba = zipDeviceInfoUtil.getGenaralDeviceId()
        idMaiSaya = zipDeviceInfoUtil.idForVendor
        sunanDaukar = zipDeviceInfoUtil.getCarrierName()
        samfurin = zipDeviceInfoUtil.getModel()
        adadinKwaKwalwa = zipDeviceInfoUtil.getTotalMemory()
        warwareNunin = zipDeviceInfoUtil.getDisplayResolution()
        tsarinNaUra = zipDeviceInfoUtil.getDeviceArch()
        adadinAjiya = zipDeviceInfoUtil.getTotalStorage()
        UUID = zipDeviceInfoUtil.getUUID()
        adadinCPU = zipDeviceInfoUtil.getCPUCount()
        saurinCPU = zipDeviceInfoUtil.getCPUSpeed()
        adireshinIP = zipDeviceInfoUtil.getIPAddress()



        idTallaGoogle = ""
    }
}