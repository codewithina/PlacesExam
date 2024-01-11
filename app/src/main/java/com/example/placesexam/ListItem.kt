package com.example.placesexam

import android.widget.ImageView
import java.io.Serializable

class ListItem (
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val lat: Double,
    val lng: Double
) : Serializable