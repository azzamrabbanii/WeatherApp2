package com.azzam.weatherapp.utils

import java.math.RoundingMode

object HelperFunctions {

    fun formatterDegree(temp: Double?) : String {
        val temperature = temp as Double
        val tempToCelsius = temperature - 273.0
        val formatDegree = tempToCelsius.toBigDecimal().setScale(2,RoundingMode.CEILING) // pembulatan
        return "$formatDegree °C"
    }
}