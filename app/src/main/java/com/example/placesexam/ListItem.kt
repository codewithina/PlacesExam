package com.example.placesexam

import java.io.Serializable

class ListItem (
    val userId: String? = null,
    var placeId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val lat: Double,
    val lng: Double
) : Serializable