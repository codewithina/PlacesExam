package com.example.placesexam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlacesViewModel : ViewModel() {
    val places: MutableLiveData<List<ListItem>> = MutableLiveData<List<ListItem>>()
}