package com.zip.zipandroid.utils.phonedate.applist

interface ZipInstalledAppListener {
    fun onInstalledAppsFetched(zipInstalledApps: Array<ZipInstalledApp?>?)
}