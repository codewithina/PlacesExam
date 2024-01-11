package com.example.placesexam

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var adapter: ListItemAdapter
private val places = mutableListOf<ListItem>()

fun getAllPlacesFirestore(): ArrayList<ListItem> {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val placeList = ArrayList<ListItem>()

    // db.collection(places) for all places
    db.collection("places")
        .get()
        .addOnSuccessListener { result ->

            for (document in result) {
                val name = document.getString("name")
                val description = document.getString("description")
                val lat = document.getDouble("lat")
                val lng = document.getDouble("lng")
                val imageUrl = document.getString("imageUrl")

                if (name != null && description != null && lat != null && lng != null && imageUrl != null) {
                    val listItem = ListItem(userId, name, description, imageUrl, lat, lng)
                    placeList.add(listItem)
                }
            }
        }
    return placeList
}

private fun updateRecyclerView() {
    adapter.updateData(places)
}