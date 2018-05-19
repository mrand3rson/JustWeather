package com.example.justweather.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.justweather.App
import com.example.justweather.R
import com.example.justweather.mvp.models.*
import com.example.justweather.mvp.presenters.MainPresenter
import com.example.justweather.mvp.views.MainView
import com.example.justweather.ui.adapters.VerticalSpaceItemDecorator
import com.example.justweather.ui.adapters.WeatherAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : MvpAppCompatActivity(),
        MainView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    companion object {
        var isOnline: Boolean = false
    }

    private val CODE_GET_PERMISSION = 1

    private lateinit var todayLayout: ViewGroup
    private lateinit var weekLayout: ViewGroup
    private lateinit var progressToday: ProgressBar
    private lateinit var progressWeek: ProgressBar
    private lateinit var imageView: ImageView

    private lateinit var countryView: TextView
    private lateinit var cityView: TextView
    private lateinit var weatherView: TextView
    private lateinit var recycler: RecyclerView

    private lateinit var adapter: WeatherAdapter

    private lateinit var mLocationCallback: LocationCallback
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    @InjectPresenter
    lateinit var presenter: MainPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appbar_main)
        bindViews()

        presenter.mainScheduler = AndroidSchedulers.mainThread()
        presenter.processScheduler = Schedulers.io()
        presenter.apiService = WeatherApiService.create()

        isOnline = (application as App).isReadyToLoadWeather()
        if (isOnline) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult == null) {
                        return
                    }

                    val location = locationResult.lastLocation
                    presenter.getWeather(location.latitude, location.longitude)
                    mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
                }
            }
        } else {
            Toast.makeText(this, "Offline mode enabled", Toast.LENGTH_SHORT).show();
            presenter.tryOfflineLoad()
        }
    }


    private fun bindViews() {
        progressToday = findViewById(R.id.progress_today)
        progressWeek = findViewById(R.id.progress_week)
        todayLayout = findViewById(R.id.today_layout)
        weekLayout = findViewById(R.id.week_layout)

        imageView = findViewById(R.id.image_today)
        countryView = findViewById(R.id.country)
        cityView = findViewById(R.id.city)
        weatherView = findViewById(R.id.weather_description)
        recycler = findViewById(R.id.recycler)
    }

    override fun onResume() {
        super.onResume()

        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            startLocationUpdates()
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                            CODE_GET_PERMISSION)
                }
            } else {
                buildGoogleApiClient()
            }
        }
    }

    override fun onPause() {
        if (mFusedLocationClient != null)
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
        super.onPause()
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        startLocationUpdates()
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, "CONNEctioN FAILED", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        createLocationRequest()
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                Looper.myLooper())
    }

    @SuppressLint("RestrictedApi")
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CODE_GET_PERMISSION) {
            if (grantResults.size == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                startLocationUpdates()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    override fun provideData(data: ApiResponse?) {
        if (data?.list != null) {
            val today: MeteoData? = data.list?.get(0)
            fillToday(today, data.city!!)

            adapter = WeatherAdapter(data.list?.subList(1, data.list?.size!! - 1).orEmpty())
            recycler.layoutManager = LinearLayoutManager(this)
            recycler.addItemDecoration(
                    VerticalSpaceItemDecorator(resources.getDimension(R.dimen.recycler_vertical_spacing).toInt())
            )
            recycler.adapter = adapter

            progressWeek.visibility = View.GONE
        }
    }

    private fun fillToday(today: MeteoData?, city: City) {
        if (today != null) {
            val mainWeather = today.weather?.get(0)?.main
            val temperature = today.main?.getTemperature()?.inC()
            val template = "$mainWeather $temperature °C"
            countryView.text = city.country
            cityView.text = city.name
            weatherView.text = template

            if (isOnline) {
                val query = WeatherImageLoader.buildQuery(today.weather?.get(0)?.icon)
                WeatherImageLoader.setBitmapFromURL(query, object : ImageSetterCallback {
                    override fun setBitmap(bitmap: Bitmap) {
                        imageView.setImageBitmap(bitmap)
                    }
                })
            }
            progressToday.visibility = View.GONE
        }
    }

    override fun onLoadError() {
        Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show()
    }
}
