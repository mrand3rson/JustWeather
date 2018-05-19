package com.example.justweather.mvp.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Andrei on 17.05.2018.
 */

open class ApiResponse : RealmObject() {
    var cod: Int = 0
    var message = 0.0
    var cnt = 0

    var list: RealmList<MeteoData>? = null
    var city: City? = null
}
