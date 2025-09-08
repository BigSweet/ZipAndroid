package com.zip.zipandroid.utils.phonedate.sms

interface ZipSMSMessageListener {
    fun onSMSMessagesFetched(messages: Array<ZipSMSMessage?>?)
    fun onError(message: String?)
}