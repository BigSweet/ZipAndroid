package com.zip.zipandroid.utils.phonedate.calllog

import com.zip.zipandroid.utils.phonedate.calllog.CallLog

interface CallLogListener {
    fun onCallLogsFetched(callLogs: Array<CallLog?>?)
    fun onError(message: String?)
}