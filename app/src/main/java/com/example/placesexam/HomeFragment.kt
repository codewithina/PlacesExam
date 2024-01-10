package com.example.placesexam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private lateinit var adapter: ListItemAdapter
    private val places = mutableListOf<ListItem>()
    val mockData = listOf(
        ListItem(
            id = "1",
            name = "Place A",
            description = "Description of Place A",
            latitude = 37.7749,
            longitude = -122.4194
        ),
        ListItem(
            id = "2",
            name = "Place B",
            description = "Description of Place B",
            latitude = 40.7128,
            longitude = -74.0060
        ),
        ListItem(
            id = "3",
            name = "Place C",
            description = "Description of Place C",
            latitude = 51.5074,
            longitude = -0.1278
        ),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFirestore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val rvPlaces = rootView.findViewById<RecyclerView>(R.id.recyclerViewHome)

        adapter = ListItemAdapter(places) { clickedItem ->
            // When clicking the items, open fragment for places
        }

        // Adapter to the rv
        rvPlaces.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        rvPlaces.layoutManager = layoutManager

        // Space between list items
        rvPlaces.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )

        return rootView
    }

    fun getDataFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.collection("users").document(userId!!)
            .collection("places")
            .get()
            .addOnSuccessListener { result ->
                val placeList = ArrayList<ListItem>()

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

                places.clear()
                places.addAll(placeList)

                updateRecyclerView()
            }
    }

    private fun updateRecyclerView() {
        adapter.updateData(places)
    }


}
