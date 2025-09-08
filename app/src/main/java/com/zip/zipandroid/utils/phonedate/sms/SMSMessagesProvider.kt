package com.zip.zipandroid.utils.phonedate.sms

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import androidx.core.content.PermissionChecker
import com.blankj.utilcode.util.PermissionUtils
import java.util.*

class SMSMessagesProvider(private val context: Context) {
    private var listeners: ArrayList<SMSMessageListener>
    private var isFetching = false
    fun fetchSMSMessages(list: Array<SMSMessageListener>) {
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
        if (PermissionUtils.isGranted(Manifest.permission.READ_SMS)
        ) {
            doFetchData()
        } else {
            onFinishedWithFailure("permission denied")
        }
    }

    private fun doFetchData() {
        Thread(Runnable {
            try {
                val messages =
                    this@SMSMessagesProvider.fetchSMSMessages()
                onFinishedWithSMSMessages(messages)
            } catch (e: Exception) {
                e.printStackTrace()
                onFinishedWithFailure(e.message)
            }
        }).start()
    }

    private fun syncGetListenersAndClear(): ArrayList<SMSMessageListener> {
        var list: ArrayList<SMSMessageListener>
        synchronized(this) {
            list = listeners
            listeners = ArrayList()
            isFetching = false
        }
        return list
    }

    private fun onFinishedWithSMSMessages(messages: Array<SMSMessage?>) {
        val listenerList = syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onSMSMessagesFetched(messages)
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

    private fun fetchSMSMessages(): Array<SMSMessage?> {
        val messages = ArrayList<SMSMessage>()
        val cr = context.contentResolver
        var cursor: Cursor? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            String[] projections = new String[]{Sms.BODY, Sms._ID, Sms.ADDRESS, Sms.DATE, Sms.DATE_SENT,
//                    Sms.PERSON, Sms.SEEN, Sms.THREAD_ID, Sms.READ, Sms.LOCKED, Sms.ERROR_CODE, Sms.PROTOCOL,
//                    Sms.REPLY_PATH_PRESENT, Sms.SERVICE_CENTER, Sms.STATUS, Sms.SUBJECT, Sms.TYPE};
            val projections = arrayOf(
                Telephony.Sms.BODY,
                Telephony.Sms._ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.DATE,
                Telephony.Sms.DATE_SENT,
                Telephony.Sms.STATUS,
                Telephony.Sms.TYPE
            )
            //            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                projections = Arrays.copyOf(projections, projections.length + 1);
//                projections[projections.length - 1] = Sms.CREATOR;//红米3 MIUI 8.0.7.0   android 版本5.1.1 没有Sms.CREATOR
//            }
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                projections = Arrays.copyOf(projections, projections.length + 1);
//                projections[projections.length - 1] = Sms.SUBSCRIPTION_ID;
//            }
            cursor = cr.query(
                Telephony.Sms.CONTENT_URI,
                projections,
                null,
                null,
                Telephony.Sms.DEFAULT_SORT_ORDER
            )
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val message = SMSMessage()
                        message.body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY))
                        message.id = cursor.getLong(cursor.getColumnIndex(Telephony.Sms._ID))
                        message.date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE))
                        message.dateSent =
                            cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE_SENT))
                        message.address =
                            cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS))
                        message.type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE))
                        message.status = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.STATUS))
                        messages.add(message)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } else {
            cursor = cr.query(Uri.parse("content://sms"), null, null, null, "date DESC")
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val message = SMSMessage()
                        message.body = cursor.getString(cursor.getColumnIndex("body"))
                        message.id = cursor.getLong(cursor.getColumnIndex(Telephony.Sms._ID))
                        message.date = cursor.getLong(cursor.getColumnIndex("date"))
                        message.dateSent = cursor.getLong(cursor.getColumnIndex("date_sent"))
                        message.address = cursor.getString(cursor.getColumnIndex("address"))
                        message.type = cursor.getInt(cursor.getColumnIndex("type"))
                        message.status = cursor.getInt(cursor.getColumnIndex("status"))
                        messages.add(message)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        }
        return messages.toArray(arrayOf<SMSMessage>())
    }

    init {
        listeners = ArrayList()
    }
}