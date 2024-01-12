package com.example.placesexam

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlaceInfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val clickedItem = it.getSerializable("clickedItemKey") as ListItem?

            if (clickedItem != null) {
                Glide.with(view.context)
                    .load(clickedItem.imageUrl)
                    .into(view.findViewById(R.id.ivThumbnail))
            }

            val placeName = view.findViewById<TextView>(R.id.placeName)
            placeName.text = clickedItem?.name

            val placeDescription = view.findViewById<TextView>(R.id.placeDescription)
            placeDescription.text = clickedItem?.description

            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

            if (clickedItem?.userId == currentUserId) {
                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
                fab.setImageResource(R.drawable.baseline_delete_outline_24)
                fab.setOnClickListener {
                    if (currentUserId != null && clickedItem?.placeId != null) {
                        deletePlace(currentUserId, clickedItem.placeId!!)
                        parentFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(context, "Error: Place does not have an id", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun deletePlace(userId: String, placeId: String) {
        val db = FirebaseFirestore.getInstance()

        // Delete from "users" -> "userId" -> "places"-collection
        db.collection("users").document(userId)
            .collection("places").document(placeId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Document successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error deleting document", e)
            }

        // Delete from "places"-collection
        db.collection("places").document(placeId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Document successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error deleting document", e)
            }
    }

}