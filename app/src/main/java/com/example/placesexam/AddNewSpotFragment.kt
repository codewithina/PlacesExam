package com.example.placesexam
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddNewSpotFragment : Fragment() {

    private lateinit var image: ImageView
    private lateinit var selectedImageUri: Uri
    private lateinit var storageReference: StorageReference
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var name: String
    private lateinit var description: String
    private var lat: Double? = null
    private var lng: Double? = null

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

        image.setOnClickListener {
            openGallery()
        }

        saveButton.setOnClickListener {
            name = view.findViewById<EditText>(R.id.editTextName).text.toString()
            description = view.findViewById<EditText>(R.id.editTextDescription).text.toString()
            val latString = view.findViewById<EditText>(R.id.editTextLat).text.toString()
            lat = latString.toDoubleOrNull()
            val lngString = view.findViewById<EditText>(R.id.editTextLng).text.toString()
            lng = lngString.toDoubleOrNull()

            if (lat == null || lng == null) {
                Toast.makeText(
                    context,
                    "Latitude and longitude must be valid numbers!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            uploadImageToFirebaseStorage()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            image.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToFirebaseStorage() {
        storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("images/${System.currentTimeMillis()}.jpg")

        imageRef.putFile(selectedImageUri)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    saveDataToFirestore(imageUrl)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDataToFirestore(imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val newPlaceRef =
            db.collection("users").document(userId!!).collection("places").document()

        val placeData = hashMapOf(
            "name" to name,
            "description" to description,
            "lat" to lat,
            "lng" to lng,
            "imageUrl" to imageUrl
        )

        newPlaceRef.set(placeData)
            .addOnSuccessListener {
                Toast.makeText(context, "Place data added successfully!", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
