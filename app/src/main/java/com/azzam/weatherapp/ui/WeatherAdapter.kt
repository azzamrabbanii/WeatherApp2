package com.azzam.weatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azzam.weatherapp.BuildConfig
import com.azzam.weatherapp.data.ListItem
import com.azzam.weatherapp.databinding.RowItemWeatherBinding
import com.azzam.weatherapp.utils.HelperFunctions.formatterDegree
import com.azzam.weatherapp.utils.sizeIconWeather2x
import com.azzam.weatherapp.utils.sizeIconWeather4x
import com.bumptech.glide.Glide
import okio.blackholeSink
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.MyViewHolder>() {

    private val listWeather = ArrayList<ListItem>()

    class MyViewHolder(val binding: RowItemWeatherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder (
        RowItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listWeather[position]
        holder.binding.apply {
            val maxTemp = "Max :" + formatterDegree(data.main?.tempMax)
            tvMaxDegree.text = maxTemp
            val minTemp = "Min :" + formatterDegree(data.main?.tempMin)
            tvMinDegree.text = minTemp

            val date = data.dtTxt?.take(10)
            val time = data.dtTxt?.takeLast(8)

            val dateArray = date?.split("-")?.toTypedArray()
            val timeArray = time?.split(":")?.toTypedArray()

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray?.get(0) as String))
            calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray?.get(0) as String))
            calendar.set(Calendar.MINUTE, 0)

            val dateFormat = SimpleDateFormat("EEE MMM d", Locale.getDefault())
                .format(calendar.time).toString()
            tvItemDate.text = dateFormat

            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                .format(calendar.time).toString()
            tvItemTime.text = timeFormat

            val iconId = data.weather?.get(0)?.icon
            val iconUrl = BuildConfig.ICON_URL + iconId + sizeIconWeather2x
            Glide.with(imgItemWeather.context).load(iconUrl)
                .into(imgItemWeather)
        }
    }

    override fun getItemCount() = listWeather.size

    fun setData(data : List<ListItem>?) {
        if (data == null) return
        listWeather.clear()
        listWeather.addAll(data)
    }

}