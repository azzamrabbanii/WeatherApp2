package com.azzam.weatherapp.network

import com.azzam.weatherapp.BuildConfig
import com.azzam.weatherapp.data.ForecastResponse
import com.azzam.weatherapp.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    fun weatherByCity(
        @Query("q") city: String,
        @Query("appid") api_key: String = BuildConfig.API_KEY
    ) : Call<WeatherResponse>

    @GET("forecast")
    fun forecastByCity(
        @Query("q") city: String,
        @Query("appid") api_key: String = BuildConfig.API_KEY
    ) : Call<ForecastResponse>

    @GET("weather")
    fun weatherByCoordinate(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") api_key: String = BuildConfig.API_KEY
    ) : Call<WeatherResponse>

}