package com.example.justweather

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import io.realm.Realm

/**
 * Created by Andrei on 17.05.2018.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

    fun isReadyToLoadWeather(): Boolean {
        return isOnline() && isGpsEnabled()
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun isGpsEnabled(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            return true
        }
        return false
    }
}