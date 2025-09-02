package com.zip.zipandroid.bean

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.utils.StringUtils
import com.zip.zipandroid.utils.phonedate.applist.InstalledApp
import com.zip.zipandroid.utils.phonedate.calendar.CalendarInfos
import com.zip.zipandroid.utils.phonedate.calllog.CallLog
import com.zip.zipandroid.utils.phonedate.sms.SMSMessage

class ZipPushData {
    var callLogs: Array<ZipCallLogBean?>? = null
        private set

    //    var contacts: Array<ContactsBean?>? = null
//        private set
    var message: Array<ZipSmsBean?>? = null
        private set
    var installedApps: Array<InstalledAppsBean?>? = null
        private set
    var deviceInfo: DeviceInfoModel
    var photoData: PhotoData = PhotoData()
//    var calendarInfo: Array<CalendarInfos?>? = null
//    var mediaDatas: MutableList<PhotoDataModel>? = null

    fun setCallLogs(callLogs: Array<CallLog?>?) {
        try {
            if (callLogs != null && callLogs.size > 0) {
                val callLogBeans = arrayOfNulls<ZipCallLogBean>(callLogs.size)
                for (i in callLogs.indices) {
                    val callLogBean = ZipCallLogBean()
                    callLogBean.name = if (StringUtils.isEmpty(callLogs[i]?.name)) callLogs[i]?.name else ""
                    callLogBean.number = if (StringUtils.isEmpty(callLogs[i]?.number)) callLogs[i]?.number else ""
                    //                        callLogBean.setDate(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(callLogs[i].date));
                    callLogBean.date = callLogs[i]?.date.toString() + ""
                    callLogBean.duration = callLogs[i]?.duration.toString() + ""
                    callLogBean.type = callLogs[i]?.type.toString() + ""
                    callLogBeans[i] = callLogBean
                }
                this.callLogs = callLogBeans
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

    fun setMessage(messages: Array<SMSMessage?>?) {
        try {
            if (messages != null && messages.size > 0) {
                val smsBeans = arrayOfNulls<ZipSmsBean>(messages.size)
                for (i in messages.indices) {
                    val smsBean = ZipSmsBean()
                    smsBean.address = if (StringUtils.isNotEmpty(messages[i]?.address)) messages[i]?.address else ""
                    smsBean.body = if (StringUtils.isNotEmpty(messages[i]?.body)) messages[i]?.body else ""
                    //                        smsBean.setDate(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(messages[i].date));
                    smsBean.date = messages[i]?.date.toString() + ""
                    //                        smsBean.setDateSent(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(messages[i].dateSent));
                    smsBean.dateSent = messages[i]?.dateSent.toString() + ""
//                    smsBean.id = messages[i]?.id
                    messages[i]?.status?.let { smsBean.setStatus(it) }
                    messages[i]?.type?.let { smsBean.setType(it) }
                    smsBeans[i] = smsBean
                }
                message = smsBeans
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

    fun setInstalledApps(installedApps: Array<InstalledApp?>?) {
        try {
            if (installedApps != null && installedApps.size > 0) {
                val appsBeans = arrayOfNulls<InstalledAppsBean>(installedApps.size)
                for (i in installedApps.indices) {
                    val installedAppsBean = InstalledAppsBean()
                    installedAppsBean.appLabel = if (StringUtils.isNotEmpty(installedApps[i]?.appLabel)) installedApps[i]?.appLabel else ""
                    //                        installedAppsBean.setFirstInstallTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(installedApps[i].firstInstallTime));
                    installedAppsBean.firstInstallTime = installedApps[i]?.firstInstallTime.toString() + ""
                    //                        installedAppsBean.setLastUpdateTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(installedApps[i].lastUpdateTime));
                    installedAppsBean.lastUpdateTime = installedApps[i]?.lastUpdateTime.toString() + ""
                    installedAppsBean.packageName = if (StringUtils.isNotEmpty(installedApps[i]?.packageName)) installedApps[i]?.packageName else ""
                    appsBeans[i] = installedAppsBean
                }
                this.installedApps = appsBeans
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
    }

    fun setCalendarInfos(calendar: Array<CalendarInfos?>?) {
        try {
            if (calendar != null && calendar.size > 0) {
                val appsBeans = arrayOfNulls<CalendarInfos>(calendar.size)
                for (i in calendar.indices) {
                    val calendarInfo = CalendarInfos()
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
        deviceInfo = DeviceInfoModel()
    }
}