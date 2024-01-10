package com.example.placesexam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private lateinit var adapter: ListItemAdapter
    private val places = mutableListOf(ListItem())
    val mockData = listOf(
        ListItem(
            id = 1,
            name = "Place A",
            description = "Description of Place A",
            latitude = 37.7749,
            longitude = -122.4194
        ),
        ListItem(
            id = 2,
            name = "Place B",
            description = "Description of Place B",
            latitude = 40.7128,
            longitude = -74.0060
        ),
        ListItem(
            id = 3,
            name = "Place C",
            description = "Description of Place C",
            latitude = 51.5074,
            longitude = -0.1278
        ),
        // ... Lägg till fler platser om det behövs
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Hitta din RecyclerView i layouten
        val rvPlaces = rootView.findViewById<RecyclerView>(R.id.recyclerViewHome)

        // Instansiera adaptern med din listdata och klicklyssnare
        adapter = ListItemAdapter(mockData) { clickedItem ->
            // Implementera önskad åtgärd vid klick på listelementet
            // Exempelvis: startActivity(Intent(requireContext(), TravelActivity::class.java).putExtra("id", clickedItem.id))
        }

        // Koppla adaptern till RecyclerView
        rvPlaces.adapter = adapter

        // Instansiera en LinearLayoutManager för din RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        rvPlaces.layoutManager = layoutManager

        // Lägg till en DividerItemDecoration för att lägga till avstånd mellan listelement
        rvPlaces.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))

        return rootView
    }
}
