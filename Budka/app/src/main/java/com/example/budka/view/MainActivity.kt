/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.budka.R
import com.example.budka.data.model.SessionManager
import com.example.budka.databinding.VoteDialogBinding
import com.example.budka.utils.Constants
import com.example.budka.utils.Constants.Companion.LOCATION_PERMISSION_CODE
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory

class MainActivity: AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sessionManager: SessionManager
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(Constants.YANDEX_MAPS_API_KEY)


        sessionManager = SessionManager(this)
        if (sessionManager.fetchSessionId() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
        }

            if(!checkPermission()) {
                requestPermission()
            }

            setContentView(R.layout.activity_main)
            bottomNavigationView = findViewById(R.id.bottom_nav)
            progressBar = findViewById(R.id.progressBarMain)
            val navController = findNavController(R.id.fragment)
            bottomNavigationView.setupWithNavController(navController)


    }

    private fun checkPermission(): Boolean {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_CODE)
    }

    fun showProgressBar(show: Boolean = false){
        if(show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE


    }

    fun showAlert(error: String){
        val errorDialog = AlertDialog.Builder(this)
        errorDialog.setIcon(R.drawable.ic_baseline_error_24)
        errorDialog.setTitle(error)
        errorDialog.setPositiveButton(
            "Вернуться"
        ) { dialog, _ ->

            dialog.cancel()
        }
        errorDialog.create()
        errorDialog.show()
    }

}