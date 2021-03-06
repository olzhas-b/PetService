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
import android.os.Handler
import android.os.Looper
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
    private lateinit var runnable: Runnable
    private lateinit var petSitterListObserver: Observer<NetworkResult<ServiceProviderResponse>>
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
                        petSittersListViewModel.fetchPetSittersList(1, country, city, null)
                        petSittersListViewModel.getPetSittersList().observe(viewLifecycleOwner, petSitterListObserver)


                    }

                } else {

                    Toast.makeText(

                        context,
                        "???????????????? ????????????????????",
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
        activity?.window?.decorView?.systemUiVisibility = 0
        viewBinding.petSittersShimmerLayout.startShimmerAnimation()
        viewBinding.petsShimmerLayout.startShimmerAnimation()
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            runnable = Runnable {
                viewBinding.petSittersShimmerLayout.startShimmerAnimation()
                viewBinding.petSittersShimmerLayout.visibility = View.VISIBLE
                viewBinding.petsShimmerLayout.startShimmerAnimation()
                viewBinding.petsShimmerLayout.visibility = View.VISIBLE
                petSittersListViewModel.fetchPetSittersList(1, country, city,null)
                petsListViewModel.fetchPetsList()
                setObservers()
                viewBinding.swipeRefreshLayout.isRefreshing = false
            }
            Handler(
                Looper.getMainLooper()).postDelayed(runnable, 3000.toLong())
        }
        viewBinding.swipeRefreshLayout.setColorSchemeResources(R.color.mainColor)
        Picasso.get().load(R.drawable.banner).fit().centerCrop().placeholder(R.drawable.banner).into(viewBinding.bannerIv)
        if(savedInstanceState==null){
            petSittersListViewModel.fetchPetSittersList(1, country, city,null)
            petsListViewModel.fetchPetsList()

        }
        petSitterListObserver = Observer {result->
            result.doIfSuccess {
                viewBinding.petSittersShimmerLayout.stopShimmerAnimation()
                viewBinding.petSittersShimmerLayout.visibility = View.GONE
                petSittersListHorizontalAdapter.updatePetSittersList(it?.rows)

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
                viewBinding.petsShimmerLayout.stopShimmerAnimation()
                viewBinding.petsShimmerLayout.visibility = View.GONE
                pets?.let{petsListHorizontalAdapter.updatePetList(it)}
            }
            result.doIfFailure{ error, data ->
                error?.let{(activity as MainActivity).showAlert(it)}

            }




        })

        petSittersListViewModel.getPetSittersList().observe(viewLifecycleOwner, petSitterListObserver)


    }

    private fun setupAdapter(){
        petsListHorizontalAdapter = PetsListHorizontalAdapter {pet-> findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToPetDetailFragment(petId = pet.id))}
        petSittersListHorizontalAdapter = PetSittersListHorizontalAdapter(navigationListener = this)
        val petsLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false )
        with(viewBinding){
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



    }

    private fun setOnClickListener(){
        viewBinding.apply {
            petSitterBtn.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections
                    .actionMainPageFragmentToServiceProvidersFragment(1))
            }
            nearestPetSitterMoreTv.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(1))
            }

            nearestPetsMoreTv.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToPetsFragment())
            }
            petWalking.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(2))

            }
            vet.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(3))

            }
            training.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(4))

            }
            grooming.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(5))

            }
            hostel.setOnClickListener {
                it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProvidersFragment(6))

            }

            moreBtn.setOnClickListener { it.findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToCreateServiceRequiredFragment()) }
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

                        Toast.makeText(context, "???????????????? ????????????????????", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {

                    Toast.makeText(context, "???????????????? ????????????????????", Toast.LENGTH_LONG).show()
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
        activity?.window?.statusBarColor = resources.getColor(R.color.white)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun navigate(serviceProviderData: ServiceProvider) {
        findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToServiceProviderDetailFragment(serviceProviderData))

    }

    override fun deleteService(serviceId: Int) {
    }


}