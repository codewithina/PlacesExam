package com.example.placesexam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AllPlacesFragment : Fragment() {

    private lateinit var adapter: ListItemAdapter
    private val places = mutableListOf<ListItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFirestore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_all_places, container, false)
        val rvPlaces = rootView.findViewById<RecyclerView>(R.id.rvAllPlaces)

        adapter = ListItemAdapter(places) { clickedItem ->
            val placeInfo = PlaceInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable("clickedItemKey", clickedItem)
            placeInfo.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, placeInfo)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // Adapter to the rv
        rvPlaces.adapter = adapter

        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvPlaces.layoutManager = layoutManager

        return rootView
    }

    private fun getDataFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("places")
            .get()
            .addOnSuccessListener { result ->
                val placeList = ArrayList<ListItem>()

                for (document in result) {
                    val placeId = document.id
                    val userId = document.getString("userId")
                    val name = document.getString("name")
                    val description = document.getString("description")
                    val lat = document.getDouble("lat")
                    val lng = document.getDouble("lng")
                    val imageUrl = document.getString("imageUrl")

                    if (name != null && description != null && lat != null && lng != null && imageUrl != null) {
                        val listItem = ListItem(userId, placeId, name, description, imageUrl, lat, lng)
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
