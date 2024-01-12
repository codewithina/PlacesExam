package com.example.placesexam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val viewModel = ViewModelProvider(requireActivity())[PlacesViewModel::class.java]

        mapFragment.getMapAsync { googleMap ->

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(62.1983, 17.5514), 5f))
            viewModel.places.observe(viewLifecycleOwner) { places ->

                places.forEach { place ->
                    val location = LatLng(place.lat, place.lng)
                    val marker =
                        googleMap.addMarker(MarkerOptions().position(location).title(place.name))

                    if (marker != null) {
                        marker.tag = place
                    }
                }

            }

            googleMap.setOnInfoWindowClickListener { clickedMarker ->
                val placeInfo = PlaceInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable(
                    "clickedItemKey",
                    clickedMarker.tag as ListItem
                ) // antar att titeln är din nyckel
                placeInfo.arguments = bundle
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentHolder, placeInfo)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
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
}