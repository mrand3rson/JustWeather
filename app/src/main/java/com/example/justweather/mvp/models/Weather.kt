package com.example.justweather.mvp.models

import io.realm.RealmObject

/**
 * Created by Andrei on 17.05.2018.
 */
open class Weather: RealmObject() {

    private var id: Int = 0
    lateinit var main: String
    lateinit var description: String
    lateinit var icon: String
}