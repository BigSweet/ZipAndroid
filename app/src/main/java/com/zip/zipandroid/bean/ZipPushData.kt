package com.zip.zipandroid.bean

import com.google.gson.JsonSyntaxException
import com.zip.zipandroid.utils.StringUtils
import com.zip.zipandroid.utils.phonedate.applist.ZipInstalledApp
import com.zip.zipandroid.utils.phonedate.calendar.ZipCalendarInfos
import com.zip.zipandroid.utils.phonedate.calllog.ZipCallLog
import com.zip.zipandroid.utils.phonedate.sms.ZipSMSMessage

class ZipPushData {
    var rikodinKira: Array<ZipCallLogBean?>? = null
        private set

    //    var contacts: Array<ContactsBean?>? = null
//        private set
    var sako: Array<ZipSmsBean?>? = null
        private set
//    var aikaceAikaceSanya: Array<InstalledAppsBean?>? = null
//        private set
    var bayaninNaUra: DeviceInfoModel
    var bayaninHoto: PhotoData = PhotoData()
//    var calendarInfo: Array<CalendarInfos?>? = null
//    var mediaDatas: MutableList<PhotoDataModel>? = null

    fun setCallLogs(zipCallLogs: Array<ZipCallLog?>?) {
        try {
            if (zipCallLogs != null && zipCallLogs.size > 0) {
                val callLogBeans = arrayOfNulls<ZipCallLogBean>(zipCallLogs.size)
                for (i in zipCallLogs.indices) {
                    val callLogBean = ZipCallLogBean()
                    callLogBean.sunan = if (StringUtils.isEmpty(zipCallLogs[i]?.name)) zipCallLogs[i]?.name else ""
                    callLogBean.lambar = if (StringUtils.isEmpty(zipCallLogs[i]?.number)) zipCallLogs[i]?.number else ""
                    //                        callLogBean.setDate(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(callLogs[i].date));
                    callLogBean.kwananWata = zipCallLogs[i]?.date.toString() + ""
                    callLogBean.tsawonLokaci = zipCallLogs[i]?.duration.toString() + ""
                    callLogBean.nauIn = zipCallLogs[i]?.type.toString() + ""
                    callLogBeans[i] = callLogBean
                }
                this.rikodinKira = callLogBeans
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

//        fun setContacts(contacts: Array<Contact?>?) {
//            try {
//                if (contacts != null && contacts.size > 0) {
//                    val contactsBeans = arrayOfNulls<ContactsBean>(contacts.size)
//                    for (i in contacts.indices) {
//                        val contactsBean = ContactsBean()
//                        contactsBean.displayName = if (StringUtils.isNotEmpty(contacts[i]?.displayName)) contacts[i]?.displayName else ""
//                        contactsBean.firstName = if (StringUtils.isNotEmpty(contacts[i]?.firstName)) contacts[i]?.firstName else ""
//                        contactsBean.middleName = if (StringUtils.isNotEmpty(contacts[i]?.middleName)) contacts[i]?.middleName else ""
//                        contactsBean.lastName = if (StringUtils.isNotEmpty(contacts[i]?.lastName)) contacts[i]?.lastName else ""
//                        contactsBean.phones = contacts[i]?.phones
//                        contactsBean.setEmails(contacts[i]?.emails)
//                        contactsBean.setPostalAddresses(contacts[i]?.postalAddresses)
//                        contactsBean.companyName = if (StringUtils.isNotEmpty(contacts[i]?.companyName)) contacts[i]?.companyName else ""
//                        contactsBean.jobTitle = if (StringUtils.isNotEmpty(contacts[i]?.jobTitle)) contacts[i]?.jobTitle else ""
//                        contactsBeans[i] = contactsBean
//                    }
//                    this.contacts = contactsBeans
//                }
//            } catch (e: JsonSyntaxException) {
//                e.printStackTrace()
//            }
//        }

    fun setMessage(messages: Array<ZipSMSMessage?>?) {
        try {
            if (messages != null && messages.size > 0) {
                val smsBeans = arrayOfNulls<ZipSmsBean>(messages.size)
                for (i in messages.indices) {
                    val smsBean = ZipSmsBean()
                    smsBean.adireshin = if (StringUtils.isNotEmpty(messages[i]?.address)) messages[i]?.address else ""
                    smsBean.jiki = if (StringUtils.isNotEmpty(messages[i]?.body)) messages[i]?.body else ""
                    //                        smsBean.setDate(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(messages[i].date));
                    smsBean.kwananWata = messages[i]?.date.toString() + ""
                    //                        smsBean.setDateSent(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(messages[i].dateSent));
                    smsBean.kwananWataAika = messages[i]?.dateSent.toString() + ""
//                    smsBean.id = messages[i]?.id
                    messages[i]?.status?.let { smsBean.setStatus(it) }
                    messages[i]?.type?.let { smsBean.setType(it) }
                    smsBeans[i] = smsBean
                }
                sako = smsBeans
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

    fun setInstalledApps(zipInstalledApps: Array<ZipInstalledApp?>?) {
        try {
            if (zipInstalledApps != null && zipInstalledApps.size > 0) {
                val appsBeans = arrayOfNulls<InstalledAppsBean>(zipInstalledApps.size)
                for (i in zipInstalledApps.indices) {
                    val installedAppsBean = InstalledAppsBean()
                    installedAppsBean.alamarAiki = if (StringUtils.isNotEmpty(zipInstalledApps[i]?.appLabel)) zipInstalledApps[i]?.appLabel else ""
                    //                        installedAppsBean.setFirstInstallTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(installedApps[i].firstInstallTime));
                    installedAppsBean.lokacinShigarFarko = zipInstalledApps[i]?.firstInstallTime.toString() + ""
                    //                        installedAppsBean.setLastUpdateTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(installedApps[i].lastUpdateTime));
                    installedAppsBean.lokacinSabuntawaKarshe = zipInstalledApps[i]?.lastUpdateTime.toString() + ""
                    installedAppsBean.sunanFakitin = if (StringUtils.isNotEmpty(zipInstalledApps[i]?.packageName)) zipInstalledApps[i]?.packageName else ""
                    appsBeans[i] = installedAppsBean
                }
//                this.aikaceAikaceSanya = appsBeans
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

    fun setCalendarInfos(calendar: Array<ZipCalendarInfos?>?) {
        try {
            if (calendar != null && calendar.size > 0) {
                val appsBeans = arrayOfNulls<ZipCalendarInfos>(calendar.size)
                for (i in calendar.indices) {
                    val calendarInfo = ZipCalendarInfos()
                    calendarInfo.title = if (StringUtils.isNotEmpty(calendar[i]?.title)) calendar[i]?.title else ""
                    calendarInfo.description = if (StringUtils.isNotEmpty(calendar[i]?.description)) calendar[i]?.description else ""
                    calendarInfo.eventLocation = if (StringUtils.isNotEmpty(calendar[i]?.eventLocation)) calendar[i]?.eventLocation else ""
                    calendarInfo.dtstart = if (StringUtils.isNotEmpty(calendar[i]?.dtstart)) calendar[i]?.dtstart else ""
                    calendarInfo.dtend = if (StringUtils.isNotEmpty(calendar[i]?.dtend)) calendar[i]?.dtend else ""
                    calendarInfo.week = if (StringUtils.isNotEmpty(calendar[i]?.week)) calendar[i]?.week else ""
                    appsBeans[i] = calendarInfo
                }
//                this.calendarInfo = appsBeans
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

    fun setMediaData() {
//        var photoDataList: MutableList<PhotoDataModel>? = null
//        var videoDataList: MutableList<PhotoDataModel>? = null
//        var photoData: String? = MMKV.defaultMMKV()?.getString("photoData", "")
//        var videoData: String? = MMKV.defaultMMKV()?.getString("videoData", "")
//        if (!TextUtils.isEmpty(photoData)) {
//            photoDataList = Gson().fromJson(photoData,
//                object : TypeToken<MutableList<PhotoDataModel?>?>() {}.type)
//        }
//
//        if (!TextUtils.isEmpty(videoData)) {
//            videoDataList = Gson().fromJson(videoData,
//                object : TypeToken<MutableList<PhotoDataModel?>?>() {}.type)
//        }
//
//        if (photoDataList != null && videoDataList != null) {
//            photoDataList.addAll(videoDataList)
//            this.mediaDatas = photoDataList
//        } else {
//            if (photoDataList != null) {
//                this.mediaDatas = photoDataList
//            } else if (videoDataList != null) {
//                this.mediaDatas = videoDataList
//            }
//        }

    }

    init {
        bayaninNaUra = DeviceInfoModel()
    }
}