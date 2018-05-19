package com.example.justweather.mvp.models

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Andrei on 17.05.2018.
 */
open class MeteoData : RealmObject() {

    var weather: RealmList<Weather>? = null
    var wind: Wind? = null
    var main: WeatherMetrics? = null
    var dt: Long = 0
    var dt_txt: String = ""
}