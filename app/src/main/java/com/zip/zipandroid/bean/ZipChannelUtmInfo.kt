package com.zip.zipandroid.bean

class ZipChannelUtmInfo(val advertiserId:String,val ambushThirdKeyinfo:List<AmbushThirdKeyinfo>,val facebookUid:String,
    val utmCampaign:String,val utmContent:String,val utmMedium:String,val utmSource:String,val utmTerm:String) {
}


data class AmbushThirdKeyinfo(val nauInKwaito:String,val makullinSirri:String,val idNaUraTaUku:String,val appId:String)