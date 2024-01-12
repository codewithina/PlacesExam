package com.example.placesexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentHolder, MyPlacesFragment())
            .commit()

        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, AddNewSpotFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        navView.selectedItemId = R.id.myPlacesFragment

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentHolder, AllPlacesFragment())
                        .commit()
                    true
                }

                R.id.myPlacesFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentHolder, MyPlacesFragment())
                        .commit()
                    true
                }

                R.id.mapFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentHolder, MapFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}