package com.zip.zipandroid.utils.phonedate.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.PermissionChecker
import java.util.*

class LocationProvider(private val context: Context) {
    private var listeners: ArrayList<LocationListener>
    private var isFetching = false
    private var locationManager: LocationManager? = null
    private var gpsListener: android.location.LocationListener? = null
    private var networkListener: android.location.LocationListener? = null
    private var passiveListener: android.location.LocationListener? = null
    private var timer: Timer? = null
    fun fetchLocation(list: Array<LocationListener>) {
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
        if (PermissionChecker.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            doFetchData()
        } else {
            onFinishedWithFailure("permission denied")
        }
    }

    private fun doFetchData() {
        try {
            locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager == null) {
                onFinishedWithFailure("can not get LocationManager")
                return
            }
            var isGPSEnabled = false
            var isNetworkEnabled = false
            var isPassiveEnabled = false
            val allProviders =
                locationManager!!.allProviders
            if (allProviders != null) {
                if (allProviders.contains(LocationManager.GPS_PROVIDER)) {
                    isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }
                if (allProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                    isNetworkEnabled =
                        locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                }
                if (allProviders.contains(LocationManager.PASSIVE_PROVIDER)) {
                    isPassiveEnabled =
                        locationManager!!.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
                }
            }
            if (!isGPSEnabled && !isNetworkEnabled && !isPassiveEnabled) {
                onFinishedWithFailure("no provider enabled")
                return
            }
            if (isGPSEnabled) {
                gpsListener = SimpleLocationListener()
                locationManager!!.requestSingleUpdate(
                    LocationManager.GPS_PROVIDER,
                    gpsListener as SimpleLocationListener,
                    context.mainLooper
                )
            }
            if (isNetworkEnabled) {
                networkListener = SimpleLocationListener()
                locationManager!!.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER,
                    networkListener as SimpleLocationListener,
                    context.mainLooper
                )
            }
            if (isPassiveEnabled) {
                passiveListener = SimpleLocationListener()
                locationManager!!.requestSingleUpdate(
                    LocationManager.PASSIVE_PROVIDER,
                    passiveListener as SimpleLocationListener,
                    context.mainLooper
                )
            }
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    onFinishedWithFailure("timeout")
                }
            }, 15000)
        } catch (e: Exception) {
            //TODO  java.lang.IllegalArgumentException: provider doesn't exist: network ,env: API19  simulator.
            e.printStackTrace()
            onFinishedWithFailure(e.message)
        }
    }

    private fun syncGetListenersAndClear(): ArrayList<LocationListener> {
        var list: ArrayList<LocationListener>
        synchronized(this) {
            list = listeners
            listeners = ArrayList()
            isFetching = false
        }
        return list
    }

    private fun removeLocationListener() {
        if (locationManager != null) {
            if (gpsListener != null) {
                locationManager!!.removeUpdates(gpsListener!!)
                gpsListener = null
            }
            if (networkListener != null) {
                locationManager!!.removeUpdates(networkListener!!)
                networkListener = null
            }
            if (passiveListener != null) {
                locationManager!!.removeUpdates(passiveListener!!)
                passiveListener = null
            }
            locationManager = null
        }
    }

    private fun onFinishedWithSuccess(location: Location) {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        removeLocationListener()
        val listenerList =
            syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onSuccess(location)
            }
        }
    }

    private fun onFinishedWithFailure(message: String?) {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        removeLocationListener()
        val listenerList =
            syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onError(message)
            }
        }
    }

    internal inner class SimpleLocationListener : android.location.LocationListener {
        override fun onLocationChanged(location: Location) {
            onFinishedWithSuccess(location)
        }

        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    init {
        listeners = ArrayList()
    }
}