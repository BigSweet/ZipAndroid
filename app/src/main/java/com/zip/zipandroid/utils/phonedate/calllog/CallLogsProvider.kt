package com.zip.zipandroid.utils.phonedate.calllog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog.Calls
import android.provider.ContactsContract
import android.text.TextUtils
import androidx.core.content.PermissionChecker
import java.util.*

class CallLogsProvider(private val context: Context) {
    private var listeners: ArrayList<CallLogListener>
    private var isFetching = false
    @SuppressLint("WrongConstant")
    fun fetchCallLogs(list: Array<CallLogListener>) {
        synchronized(this) {
            for (listener in list) {
                if (!listeners.contains(listener)) {
                    listeners.add(listener)
                }
            }
            if (isFetching) {
                return
            }
            isFetching = true
        }
        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            doFetchData()
        } else {
            onFinishedWithFailure("permission denied")
        }
    }

    private fun doFetchData() {
        Thread(Runnable {
            try {
                val callLogs =
                    this@CallLogsProvider.fetchCallLogs()
                onFinishedWithCallLogs(callLogs)
            } catch (e: Exception) {
                e.printStackTrace()
                onFinishedWithFailure(e.message)
            }
        }).start()
    }

    @SuppressLint("MissingPermission")
    private fun fetchCallLogs(): Array<CallLog?> {
        val callLogs =
            ArrayList<CallLog>()
        val cr = context.contentResolver
        val cursor =
            cr.query(Calls.CONTENT_URI, null, null, null, Calls.DEFAULT_SORT_ORDER)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val callLog =
                        CallLog()
                    callLog.date = cursor.getLong(cursor.getColumnIndex(Calls.DATE))
                    callLog.duration = cursor.getInt(cursor.getColumnIndex(Calls.DURATION))
                    callLog.number = cursor.getString(cursor.getColumnIndex(Calls.NUMBER))
                    val name =
                        cursor.getString(cursor.getColumnIndex(Calls.CACHED_NAME))
                    callLog.name = if (TextUtils.isEmpty(name)) numberToName(
                        context,
                        callLog.number
                    ) else name
                    callLog.type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE))
                    callLogs.add(callLog)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return callLogs.toArray(arrayOf<CallLog>())
    }

    private fun syncGetListenersAndClear(): ArrayList<CallLogListener> {
        var list: ArrayList<CallLogListener>
        synchronized(this) {
            list = listeners
            listeners = ArrayList()
            isFetching = false
        }
        return list
    }

    private fun onFinishedWithCallLogs(callLogs: Array<CallLog?>) {
        val listenerList = syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onCallLogsFetched(callLogs)
            }
        }
    }

    private fun onFinishedWithFailure(message: String?) {
        val listenerList = syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onError(message)
            }
        }
    }

    private fun numberToName(
        context: Context,
        number: String?
    ): String {
        var cur: Cursor? = null
        return try {
            /// number is the phone number
            val lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number)
            )
            val mPhoneNumberProjection = arrayOf(
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME
            )
            cur = context.contentResolver
                .query(lookupUri, mPhoneNumberProjection, null, null, null)
            if (cur != null && cur.moveToFirst()) cur.getString(
                cur.getColumnIndex(
                    ContactsContract.PhoneLookup.DISPLAY_NAME
                )
            ) else ""
        } catch (e: Exception) {
            ""
        } finally {
            cur?.close()
        }
    }

    init {
        listeners = ArrayList()
    }
}