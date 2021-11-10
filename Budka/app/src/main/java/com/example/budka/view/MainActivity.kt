package com.example.budka.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.budka.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_nav)
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)
    }
}