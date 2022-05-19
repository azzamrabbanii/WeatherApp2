package com.azzam.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azzam.weatherapp.BuildConfig
import com.azzam.weatherapp.R
import com.azzam.weatherapp.data.ForecastResponse
import com.azzam.weatherapp.data.WeatherResponse
import com.azzam.weatherapp.databinding.ActivityMainBinding
import com.azzam.weatherapp.utils.HelperFunctions.formatterDegree
import com.azzam.weatherapp.utils.sizeIconWeather4x
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.http.Query
import retrofit2.http.Tag

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var _viewModel: MainViewModel? = null
    private val viewModel get() = _viewModel as MainViewModel

    private val mAdapter by lazy { WeatherAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.isAppearanceLightNavigationBars = true

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        searchCity()
        getWeatherByCity()
        getWeatherByCurrentLocation()
    }

    private fun getWeatherByCity() {
        viewModel.getWeatherByCity().observe(this){
            setupView(it, null)
        }
        viewModel.getForecastByCity().observe(this){
            setupView(null, it)
        }
    }

    fun setupView(weather: WeatherResponse?, forecast: ForecastResponse?){
        binding.apply {
            weather?.let {
                tvCity.text = it.name
                tvDegree.text = formatterDegree(it.main?.temp)

                val iconId = it.weather?.get(0)?.icon
                val iconUrl = BuildConfig.ICON_URL + iconId + sizeIconWeather4x
                Glide.with(this@MainActivity).load(iconUrl)
                    .into(imgIcWeather)
            }

            mAdapter.setData(forecast?.list)
            binding.rvWeather.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun getWeatherByCurrentLocation() {
        val fusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    1000
                )
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                try {
                    val lat = it.latitude
                    val lon = it.longitude

                    viewModel.weatherByCurrentLocation(lat, lon)
                    viewModel.forecastByCurrentLocation(lat, lon)
                } catch (e: Throwable) {
                    Log.e("MainActivity", "LastLocation Coordination $it" )
                    Log.e("MainActivity", "Couldn't ge latitude & longitude")
                }
            }
            .addOnFailureListener {
                Log.e("MainActivity", "Failed getting current location")
            }

        viewModel.weatherByCurrentLocation(0.0, 0.0)
        viewModel.forecastByCurrentLocation(2.0, 0.6)
        viewModel.getWeatherByCurrentLocation().observe(this){
            binding.apply {
                tvCity.text = it.name
                tvDegree.text = formatterDegree(it.main?.temp)

                val iconId = it.weather?.get(0)?.icon
                val iconUrl = BuildConfig.ICON_URL + iconId + sizeIconWeather4x
                Glide.with(this@MainActivity).load(iconUrl)
                    .into(imgIcWeather)
            }
        }

        viewModel.getForecastByCurrentLocation().observe(this){
            mAdapter.setData(it.list)
            binding.rvWeather.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun searchCity() {
        binding.edtSearch.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.weatherByCity(it)
                        viewModel.getForecastByCity(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        )

    }
}