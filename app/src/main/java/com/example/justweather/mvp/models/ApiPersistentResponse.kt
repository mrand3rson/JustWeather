package com.example.justweather.mvp.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Andrei on 17.05.2018.
 */

open class ApiPersistentResponse: RealmObject() {
    var cod: Int = 0

    @SerializedName("list")
    lateinit var list: RealmList<MeteoData>
}
