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

class ZipCallLogsProvider(private val context: Context) {
    private var listeners: ArrayList<ZipCallLogListener>
    private var isFetching = false


    private fun doFetchData() {
        Thread(Runnable {
            try {
                val callLogs =
                    this@ZipCallLogsProvider.fetchCallLogs()
                onFinishedWithCallLogs(callLogs)
            } catch (e: Exception) {
                e.printStackTrace()
                onFinishedWithFailure(e.message)
            }
        }).start()
    }

    @SuppressLint("MissingPermission")
    private fun fetchCallLogs(): Array<ZipCallLog?> {
        val zipCallLogs =
            ArrayList<ZipCallLog>()
        val cr = context.contentResolver
        val cursor =
            cr.query(Calls.CONTENT_URI, null, null, null, Calls.DEFAULT_SORT_ORDER)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val zipCallLog =
                        ZipCallLog()
                    zipCallLog.date = cursor.getLong(cursor.getColumnIndex(Calls.DATE))
                    zipCallLog.duration = cursor.getInt(cursor.getColumnIndex(Calls.DURATION))
                    zipCallLog.number = cursor.getString(cursor.getColumnIndex(Calls.NUMBER))
                    val name =
                        cursor.getString(cursor.getColumnIndex(Calls.CACHED_NAME))
                    zipCallLog.name = if (TextUtils.isEmpty(name)) numberToName(
                        context,
                        zipCallLog.number
                    ) else name
                    zipCallLog.type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE))
                    zipCallLogs.add(zipCallLog)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return zipCallLogs.toArray(arrayOf<ZipCallLog>())
    }

    private fun syncGetListenersAndClear(): ArrayList<ZipCallLogListener> {
        var list: ArrayList<ZipCallLogListener>
        synchronized(this) {
            list = listeners
            listeners = ArrayList()
            isFetching = false
        }
        return list
    }

    private fun onFinishedWithCallLogs(zipCallLogs: Array<ZipCallLog?>) {
        val listenerList = syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onCallLogsFetched(zipCallLogs)
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