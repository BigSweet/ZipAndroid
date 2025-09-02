package com.zip.zipandroid.utils.phonedate.sms

import com.zip.zipandroid.utils.phonedate.sms.SMSMessage

interface SMSMessageListener {
    fun onSMSMessagesFetched(messages: Array<SMSMessage?>?)
    fun onError(message: String?)
}