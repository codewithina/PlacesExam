package com.example.placesexam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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
        val viewModel = ViewModelProvider(requireActivity()).get(PlacesViewModel::class.java)

        mapFragment.getMapAsync { googleMap ->

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(59.3293, 18.0686), 10f))
            viewModel.places.observe(viewLifecycleOwner, Observer { places ->

                places.forEach { place ->
                    val location = LatLng(place.lat, place.lng)
                    googleMap.addMarker(MarkerOptions().position(location).title(place.name)) }

            })
        }
    }
}