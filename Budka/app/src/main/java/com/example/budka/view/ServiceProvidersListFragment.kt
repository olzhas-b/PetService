/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.databinding.FragmentPetSittersPageBinding
import com.example.budka.utils.Constants
import com.example.budka.view.adapter.ServiceProvidersAdapter
import com.example.budka.viewModel.PetSittersListViewModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_pet_sitters_page.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class ServiceProvidersListFragment: Fragment() {
    val arg: ServiceProvidersListFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentPetSittersPageBinding
    private lateinit var serviceProvidersAdapter: ServiceProvidersAdapter
    private val petSittersListViewModel: PetSittersListViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanseState: Bundle?
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        viewBinding = FragmentPetSittersPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLastKnownLocation()
        if(savedInstanceState==null){
            petSittersListViewModel.fetchPetSittersList(arg.serviceType)
        }
        setupAdapter()
        setObservers()
        setOnClickListener()
    }

    private fun setObservers(){
        petSittersListViewModel.getPetSittersList().observe(viewLifecycleOwner, {
            serviceProvidersAdapter.updateEmployeeList(it)
        })
    }

    private fun setupAdapter(){
        serviceProvidersAdapter = ServiceProvidersAdapter()
        val layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL, false)
        service_providers_rv.layoutManager = layoutManager
        service_providers_rv.adapter = serviceProvidersAdapter
        service_providers_rv.setHasFixedSize(true)
        service_providers_rv.setItemViewCacheSize(20)
        service_providers_rv.isNestedScrollingEnabled = false
    }

    private fun setOnClickListener(){
        viewBinding.petSitterFilterIb.setOnClickListener{
            it.findNavController().navigate(ServiceProvidersListFragmentDirections.actionServiceProvidersFragmentToPetSittersFilterFragment())
        }

    }


    private fun getLastKnownLocation() {
        val locationRequest = LocationRequest().apply {
            interval = 120000
            fastestInterval = 120000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        if (!checkPermission()) {
            requestPermission()
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity()) { location ->
                    if (location == null || location.accuracy > 100) {
                        val mLocationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                fusedLocationClient.removeLocationUpdates(this)
                                if (locationResult != null && locationResult.locations.isNotEmpty()) {
                                    val newLocation = locationResult.locations[0]
                                    if(context!=null){
                                        viewBinding.petSitterLocationTv.text =
                                            getUserAddress(newLocation.latitude, newLocation.longitude)
                                    }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Включите геопозицию",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            }
                        }

                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            mLocationCallback, null
                        )
                    } else {
                        Toast.makeText(context, "Включите геопозицию", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Включите геопозицию", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun checkPermission(): Boolean {
        if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), Constants.LOCATION_PERMISSION_CODE
        )
    }

    private fun getUserAddress(latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())

            val address =  geoCoder.getFromLocation(latitude, longitude, 1)
            val cityName = address.get(0).locality
            val countryName = address.get(0).countryName
            return "$cityName, $countryName"

    }

}