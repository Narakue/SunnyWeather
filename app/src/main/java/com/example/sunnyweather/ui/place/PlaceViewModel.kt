package com.example.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.place.Place

class PlaceViewModel : ViewModel() {
    private val searchMutableLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>();

    val searchLiveData = Transformations.switchMap(searchMutableLiveData) { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchMutableLiveData.value = query
    }


    fun savePlace(place: Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavedPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()
}