package com.example.sunnyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.place.Place
import com.google.gson.Gson

object PlaceDao {
    fun savePlace(place: Place) : Boolean {
        try {
            sharedPreferences().edit {
                putString("place", Gson().toJson(place))
            }
        } catch (e : Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() =
        SunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)
}