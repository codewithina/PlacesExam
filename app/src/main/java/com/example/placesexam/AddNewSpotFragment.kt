package com.example.placesexam

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class AddNewSpotFragment : Fragment() {

    private var imageUrl: String? = null
    lateinit var image: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_spot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveButton: Button = view.findViewById(R.id.buttonAdd)
        image = view.findViewById(R.id.addImage)

        saveButton.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.editTextName).text.toString()
            val description = view.findViewById<EditText>(R.id.editTextDescription).text.toString()
            val latString = view.findViewById<EditText>(R.id.editTextLat).text.toString()
            val lat = latString.toDoubleOrNull()
            val lngString = view.findViewById<EditText>(R.id.editTextLng).text.toString()
            val lng = lngString.toDoubleOrNull()

            if (lat == null || lng == null) {
                Toast.makeText(context, "Latitude and longitude must be valid numbers!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = Firebase.firestore
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val newPlaceRef = db.collection("users").document(userId!!).collection("places").document()

            val placeData = hashMapOf(
                "name" to name,
                "description" to description,
                "lat" to lat,
                "lng" to lng,
            )

            newPlaceRef.set(placeData).addOnSuccessListener {
                Toast.makeText(context, "Place data added successfully!", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}