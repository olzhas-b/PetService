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
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.MainPageFragmentBinding
import com.example.budka.utils.Constants
import com.example.budka.view.adapter.PetSittersListHorizontalAdapter
import com.example.budka.view.adapter.PetsListHorizontalAdapter
import com.example.budka.view.adapter.viewHolder.FavListener
import com.example.budka.view.adapter.viewHolder.NavigationListener
import com.example.budka.viewModel.PetSittersListViewModel
import com.example.budka.viewModel.PetsListViewModel
import com.google.android.gms.location.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_page_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainPageFragment: Fragment(), NavigationListener {
    private lateinit var viewBinding: MainPageFragmentBinding
    private lateinit var petsListHorizontalAdapter: PetsListHorizontalAdapter
    private lateinit var petSittersListHorizontalAdapter: PetSittersListHorizontalAdapter
    private val petsListViewModel: PetsListViewModel by viewModel()
    private val petSittersListViewModel: PetSittersListViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var country: String? = null
    private var city: String? = null
    private lateinit var petSitterListObserver: Observer<NetworkResult<List<ServiceProvider>>>
    private lateinit var locationUpdates: LocationCallback


    //    private val petsListHorizontalAdapter by lazy {
//        PetsListHorizontalAdapter(petsListViewModel)
//    }

    init {
        locationUpdates = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                fusedLocationClient.removeLocationUpdates(this)
                if (locationResult != null && locationResult.locations.isNotEmpty()) {
                    val newLocation = locationResult.locations[0]
                    if(context!=null){
                        country = getUserAddress(newLocation.latitude, newLocation.longitude).split(',')[1].substring(1)
                        city = getUserAddress(newLocation.latitude, newLocation.longitude).split(',')[0]
                        petSittersListViewModel.fetchPetSittersList(0, country, city, null)
                        petSittersListViewModel.getPetSittersList().observe(viewLifecycleOwner, petSitterListObserver)


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
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        viewBinding = MainPageFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = resources.getColor(R.color.mainColor)
        Picasso.get().load(R.drawable.banner).fit().centerCrop().placeholder(R.drawable.banner).into(viewBinding.bannerIv)
        if(savedInstanceState==null){
            petSittersListViewModel.fetchPetSittersList(0, country, city,null)

        }
        petSitterListObserver = Observer {result->
            result.doIfSuccess {
                petSittersListHorizontalAdapter.updatePetSittersList(it)

            }
            result.doIfFailure{ error, data ->
                error?.let{(activity as MainActivity).showAlert(it)}

            }

            result.doIfLoading {  }
            }
        setupAdapter()
        setObservers()
        setOnClickListener()
        getLastKnownLocation()

    }

    private fun setObservers(){
        petsListViewModel.getPetsList().observe(viewLifecycleOwner, Observer {result ->
            result.doIfSuccess { pets ->
                pets?.let{petsListHorizontalAdapter.updatePetList(it)}
            }
            result.doIfFailure{ error, data ->
                error?.let{(activity as MainActivity).showAlert(it)}

            }




        })

        petSittersListViewModel.getPetSittersList().observe(viewLifecycleOwner, petSitterListObserver)


    }

    private fun setupAdapter(){
        petsListHorizontalAdapter = PetsListHorizontalAdapter()
        petSittersListHorizontalAdapter = PetSittersListHorizontalAdapter(navigationListener = this)
        val petsLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false )
        mainPagePetsRv.layoutManager = petsLayoutManager
        mainPagePetsRv.addItemDecoration(DividerItemDecoration(activity, petsLayoutManager.orientation))
        mainPagePetsRv.adapter = petsListHorizontalAdapter
        mainPagePetsRv.setHasFixedSize(true)
        mainPagePetsRv.setItemViewCacheSize(20)
        mainPagePetsRv.isNestedScrollingEnabled = false

        val petSittersLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false )
        nearPetSitterRv.layoutManager = petSittersLayoutManager
        nearPetSitterRv.adapter = petSittersListHorizontalAdapter
        nearPetSitterRv.setHasFixedSize(true)
        nearPetSitterRv.setItemViewCacheSize(20)
        nearPetSitterRv.isNestedScrollingEnabled =  false


    }

    private fun setOnClickListener(){
        petSitterBtn.setOnClickListener {
            it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(0))
        }
        viewBinding.apply {
            petWalking.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(1))

            }
            vet.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(2))

            }
            training.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(3))

            }
            grooming.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(4))

            }
            hostel.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(5))

            }
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
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationUpdates, null
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

    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationClient.removeLocationUpdates(locationUpdates)

        petSittersListViewModel.getPetSittersList().removeObserver (petSitterListObserver)
    }

    override fun navigate(serviceProviderData: ServiceProvider) {
        findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProviderDetailFragment(serviceProviderData))

    }

    override fun deleteService(serviceId: Int) {
    }


}