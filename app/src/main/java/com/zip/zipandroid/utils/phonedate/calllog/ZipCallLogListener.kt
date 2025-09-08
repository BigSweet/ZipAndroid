package com.zip.zipandroid.utils.phonedate.calllog

interface ZipCallLogListener {
    fun onCallLogsFetched(zipCallLogs: Array<ZipCallLog?>?)
    fun onError(message: String?)
}