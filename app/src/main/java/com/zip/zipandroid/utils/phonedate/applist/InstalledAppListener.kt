package com.zip.zipandroid.utils.phonedate.applist

interface InstalledAppListener {
    fun onInstalledAppsFetched(installedApps: Array<InstalledApp?>?)
}