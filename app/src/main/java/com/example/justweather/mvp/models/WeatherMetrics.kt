package com.example.justweather.mvp.models

import com.example.justweather.mvp.models.metrics.Pressure
import com.example.justweather.mvp.models.metrics.Temperature
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 * Created by Andrei on 17.05.2018.
 */
open class WeatherMetrics: RealmObject() {

    fun getTemperature(): Temperature {
        return Temperature(temperature)
    }

    fun getPressure(): Pressure {
        return Pressure(pressure)
    }

    @SerializedName("temp")
    var temperature: Double = 0.toDouble()

    @SerializedName("temp_min")
    var minTemperature: Double = 0.toDouble()

    @SerializedName("temp_max")
    var maxTemperature: Double = 0.toDouble()

    var humidity: Double = 0.toDouble()
    var pressure: Double = 0.toDouble()
}