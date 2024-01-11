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

class HomeFragment : Fragment() {

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
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val rvPlaces = rootView.findViewById<RecyclerView>(R.id.recyclerViewHome)

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
        
        db.collection("places")
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
