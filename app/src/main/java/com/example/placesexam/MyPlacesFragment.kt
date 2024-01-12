package com.example.placesexam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPlacesFragment : Fragment() {

    private lateinit var adapter: ListItemAdapter
    private val places = mutableListOf<ListItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyDataFirestore()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_places, container, false)
        val rvPlaces = rootView.findViewById<RecyclerView>(R.id.rvMyPlaces)

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

        return rootView

    }

    override fun onResume() {
        super.onResume()

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.setImageResource(R.drawable.baseline_add_24)
        fab.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, AddNewSpotFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    fun getMyDataFirestore() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val viewModel = ViewModelProvider(requireActivity()).get(PlacesViewModel::class.java)

        db.collection("users").document(userId!!)
            .collection("places")
            .get()
            .addOnSuccessListener { result ->
                val placeList = ArrayList<ListItem>()

                for (document in result) {
                    val placeId = document.id
                    val name = document.getString("name")
                    val description = document.getString("description")
                    val lat = document.getDouble("lat")
                    val lng = document.getDouble("lng")
                    val imageUrl = document.getString("imageUrl")

                    if (name != null && description != null && lat != null && lng != null && imageUrl != null) {
                        val listItem =
                            ListItem(userId, placeId, name, description, imageUrl, lat, lng)
                        placeList.add(listItem)
                    }
                }


                viewModel.places.value = placeList
                places.clear()
                places.addAll(placeList)

                updateRecyclerView()
            }
    }

    private fun updateRecyclerView() {
        adapter.updateData(places)
    }
}