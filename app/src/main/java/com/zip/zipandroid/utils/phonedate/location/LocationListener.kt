package com.zip.zipandroid.utils.phonedate.location

import android.location.Location

interface LocationListener {
    fun onSuccess(location: Location?)
    fun onError(message: String?)
}