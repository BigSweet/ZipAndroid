package com.zip.zipandroid.utils.phonedate.applist

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import java.util.*

class ZipInstalledAppsProvider(private val context: Context) {
    private var listeners: ArrayList<ZipInstalledAppListener>
    private var isFetching = false
    fun fetchInstalledApps(listeners: Array<ZipInstalledAppListener>) {
        synchronized(this) {
            for (listener: ZipInstalledAppListener in listeners) {
                if (!this.listeners.contains(listener)) {
                    this.listeners.add(listener)
                }
            }
            if (isFetching) {
                return
            }
            isFetching = true
        }
        Thread(Runnable {
            var zipInstalledApps = arrayOfNulls<ZipInstalledApp>(0)
            try {
                zipInstalledApps = fetchAppInfos()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            onFinishedWithAppInfos(zipInstalledApps)
        }).start()
    }

    private fun fetchAppInfos(): Array<ZipInstalledApp?> {
        val pm = context.packageManager
        val infos =
            pm.getInstalledPackages(0)
        val appInfos: MutableList<ZipInstalledApp> =
            ArrayList()
        for (info: PackageInfo in infos) {
            //third party app
            if ((info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                val appInfo = ZipInstalledApp()
                appInfo.appLabel = info.applicationInfo.loadLabel(pm).toString()
                appInfo.firstInstallTime = info.firstInstallTime.toString()
                appInfo.lastUpdateTime = info.lastUpdateTime.toString()
                appInfo.packageName = info.packageName
                appInfo.versionCode = info.versionCode
                appInfo.versionName = info.versionName
                //                appInfo.logo = info.applicationInfo.loadIcon(pm);
                appInfos.add(appInfo)
            }
        }
        return appInfos.toTypedArray()
    }

    private fun onFinishedWithAppInfos(infos: Array<ZipInstalledApp?>) {
        var listenerList: ArrayList<ZipInstalledAppListener>
        synchronized(this) {
            listenerList = listeners
            listeners = ArrayList()
            isFetching = false
        }
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onInstalledAppsFetched(infos)
            }
        }
    }

    init {
        listeners = ArrayList()
    }
}