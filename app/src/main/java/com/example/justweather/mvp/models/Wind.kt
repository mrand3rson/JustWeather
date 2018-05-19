package com.example.justweather.mvp.models

import io.realm.RealmObject

open class Wind : RealmObject() {
    var speed: Float = 0.toFloat()
    var deg: Float = 0.toFloat()
}
