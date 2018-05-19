package com.example.justweather.mvp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Andrei on 17.05.2018.
 */
object WeatherImageLoader {
    private val IMAGE_DIMENSION = 250
    private val BASE_URL = "http://openweathermap.org/img/w/"


    fun buildQuery(param: String?): String {
        return "$BASE_URL$param.png"
    }

    fun setBitmapFromURL(src: String, setter: ImageSetterCallback) {
        Single.fromCallable<Any> {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe { bitmap ->
                    val scaledBitmap =
                            Bitmap.createScaledBitmap(bitmap as Bitmap?,
                                    IMAGE_DIMENSION,
                                    IMAGE_DIMENSION,
                                    false)
                    setter.setBitmap(scaledBitmap)
                }
    }
}