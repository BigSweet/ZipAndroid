package com.zip.zipandroid.utils.phonedate.location

import android.location.Location

interface ZipLocationListener {
    fun onSuccess(location: Location?)
    fun onError(message: String?)
}