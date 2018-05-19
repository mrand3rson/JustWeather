package com.example.justweather.mvp.presenters

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.justweather.mvp.models.ApiResponse
import com.example.justweather.mvp.models.WeatherApiService
import com.example.justweather.mvp.models.MeteoData
import com.example.justweather.mvp.views.MainView
import io.reactivex.Scheduler
import io.realm.Realm

/**
 * Created by Andrei on 17.05.2018.
 */
@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    lateinit var processScheduler: Scheduler
    lateinit var mainScheduler: Scheduler
    lateinit var apiService: WeatherApiService


    fun getWeather(lat: Double, lon: Double) {
        apiService.getForecast5(lat, lon, WeatherApiService.API_KEY)
                .observeOn(mainScheduler)
                .subscribeOn(processScheduler)
                .subscribe(
                        {result -> onSuccess(result)},
                        {error  -> onError(error)}
                )
    }

    private fun onSuccess(response: ApiResponse) {
        if (response.list?.isEmpty() == false) {
            persist(response)
            viewState.provideData(response)
        } else {
            viewState.onLoadError()
        }
    }

    private fun onError(error: Throwable) {
        Log.e(TAG_ERROR, error.message)
//        error.printStackTrace()
        viewState.onLoadError()
    }

    private fun persist(results: ApiResponse) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        realm.delete(MeteoData::class.java)
        realm.delete(ApiResponse::class.java)
        realm.copyToRealm(results)

        realm.commitTransaction()
    }
    fun tryOfflineLoad() {
        val lastResponse: ApiResponse? = retrieveFromRealm()
        if (lastResponse != null) {
            viewState.provideData(lastResponse)
        } else {
            viewState.onLoadError()
        }
    }

    private fun retrieveFromRealm(): ApiResponse? {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val savedResponse: ApiResponse? =
                realm.where(ApiResponse::class.java)
                        .findFirst()

        realm.commitTransaction()
        return savedResponse
    }


    companion object {
        val TAG_ERROR: String = "MainPresenter:"

    }
}
