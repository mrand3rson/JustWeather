package com.example.justweather.mvp.models

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Andrei on 03.05.2018.
 */

interface WeatherApiService {

    @GET("forecast")
    fun getForecast5(@Query("lat") lat: Double,
                     @Query("lon") lon: Double,
                     @Query("APPID") apiKey: String) : Observable<ApiResponse>

    companion object {
        val API_KEY: String = "460645603e0799fc46cbe6aebd8f62e4"

        fun create(): WeatherApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://api.openweathermap.org/data/2.5/")
                    .build()

            return retrofit.create(WeatherApiService::class.java)
        }
    }
}
