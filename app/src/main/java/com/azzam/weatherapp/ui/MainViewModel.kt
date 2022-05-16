package com.azzam.weatherapp.ui

import androidx.lifecycle.ViewModel
import com.azzam.weatherapp.data.WeatherResponse
import com.azzam.weatherapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    fun weatherByCity(city: String) {
        ApiConfig.getApiService().weatherByCity(city).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        } )
    }
}