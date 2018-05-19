package com.example.justweather.mvp.presenters

import com.example.justweather.mvp.models.ApiResponse
import com.example.justweather.mvp.models.Weather
import com.example.justweather.mvp.models.WeatherApiService
import com.example.justweather.mvp.views.MainView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory
import java.util.*

/**
 * Created by Andrei on 18.05.2018.
 */
class MainPresenterTest {

    @Mock
    lateinit var apiService: WeatherApiService

    private lateinit var view : MainView

    private lateinit var presenter : MainPresenter
    private lateinit var testScheduler: TestScheduler


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        presenter = MainPresenter()

        presenter.mainScheduler = testScheduler
        presenter.processScheduler = testScheduler

        presenter.apiService = apiService
    }

    @Test
    fun getWeather_whenDataIsAvailable_shouldShowResult() {
        var providedResult = false
        view = object: MainView {
            override fun provideData(data: ApiResponse?) {
                providedResult = true
            }

            override fun onLoadError() {
                providedResult = false
            }
        }

        presenter.attachView(view)

        presenter.getWeather(-122.084, 37.422)
        testScheduler.triggerActions()
        verify(view, VerificationModeFactory.only()).provideData(any())
    }

    @Test
    fun getWeather_whenDataIsAvailable_resultNonNull() {
        var verifyNotNull = true
        view = object: MainView {
            override fun provideData(data: ApiResponse?) {
                verifyNotNull = (data != null) && (data.list != null)
            }

            override fun onLoadError() {

            }
        }

        presenter.attachView(view)
        
        presenter.getWeather(-122.084, 37.422)
        testScheduler.triggerActions()
        assertTrue(verifyNotNull)
    }
}