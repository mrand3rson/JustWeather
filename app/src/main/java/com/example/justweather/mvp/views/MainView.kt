package com.example.justweather.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.justweather.mvp.models.ApiResponse
import com.example.justweather.mvp.models.MeteoData

/**
 * Created by Andrei on 17.05.2018.
 */
@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun provideData(data: ApiResponse?)
    fun onLoadError()
}