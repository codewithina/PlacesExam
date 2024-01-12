package com.example.placesexam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var email : EditText
    private lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        email = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            signUp()
        }
        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signUp(){

        val user = email.text.toString()
        val password = password.text.toString()

        if (user.isEmpty() || password.isEmpty()) {
            return
        }
        auth.createUserWithEmailAndPassword(user, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.d("!!!", "Registrerad")
                    startMainActivity()
                } else {
                    Log.d("!!!", "Inte registrerad")
                }
            }
    }

    private fun signIn() {
        val user = email.text.toString()
        val password = password.text.toString()
        if (user.isEmpty() || password.isEmpty()) {
            return
        }
        auth.signInWithEmailAndPassword(user, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.d("!!!", "Inloggad")
                    startMainActivity()
                } else {
                    Log.d("!!!", "Inte inloggad")
                }
            }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //TODO: If user already signed in

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startMainActivity()
        }
    }
}