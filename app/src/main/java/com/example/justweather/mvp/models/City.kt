package com.example.justweather.mvp.models

import io.realm.RealmObject

/**
 * Created by Andrei on 18.05.2018.
 */
open class City : RealmObject() {
    var id: Long = 0
    var name: String = ""
    var country: String = ""
}