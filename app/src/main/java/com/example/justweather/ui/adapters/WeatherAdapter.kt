package com.example.justweather.ui.adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justweather.R
import com.example.justweather.mvp.models.WeatherImageLoader
import com.example.justweather.mvp.models.ImageSetterCallback
import com.example.justweather.mvp.models.MeteoData
import com.example.justweather.ui.activities.MainActivity
import kotlinx.android.synthetic.main.recycler_item_meteo.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(var data: List<MeteoData>):
        RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_item_meteo, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setIsRecyclable(false)
        holder?.bind(data[position])
    }

    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView), ImageSetterCallback {
        private val IMAGE_DIMENSION = 250
        private val expectedPattern = "dd:MM HH:mm"


        fun bind(item: MeteoData) {
            val dataPattern = "yyyy-MM-dd HH:mm:ss"
            val baseSdf = SimpleDateFormat(dataPattern, Locale.getDefault())
            val date = baseSdf.parse(item.dt_txt)
            val sdf = SimpleDateFormat(expectedPattern, Locale.getDefault())
            itemView.date.text = sdf.format(date)

            if (MainActivity.isOnline) {
                val query = WeatherImageLoader.buildQuery(item.weather?.get(0)?.icon)
                WeatherImageLoader.setBitmapFromURL(query, this)
            }

            //TODO: replace hardcoded templates with string resources
            itemView.weather.text = item.weather?.get(0)?.main
            val degrees = item.main?.getTemperature()?.inC().toString()
            itemView.degrees.text = "$degrees °C"

            itemView.image_wind.setImageResource(R.drawable.wind128)
            val windSpeed = item.wind?.speed.toString()
            itemView.wind.text = "$windSpeed mph"

            itemView.image_humidity.setImageResource(R.drawable.humidity128)
            val humidity = item.main?.humidity.toString()
            itemView.humidity.text = "$humidity %"

            itemView.image_pressure.setImageResource(R.drawable.pressure128)
            val pressure = item.main?.getPressure()?.inMmHg().toString()
            itemView.pressure.text = "$pressure mmHg"
        }

        override fun setBitmap(bitmap: Bitmap) {
            itemView.image_for_date.setImageBitmap(bitmap)
        }
    }
}
