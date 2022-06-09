/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.*
import android.view.*
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.FragmentPetSitterDetailBinding
import com.example.budka.view.adapter.*
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ServiceDetailViewModel
import com.example.budka.viewModel.ServicesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_sitter_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class ServiceProviderDetailFragment: Fragment() {
    private lateinit var serviceProvider: ServiceProvider
    private val serviceDetailViewModel: ServiceDetailViewModel by viewModel()
    private val petListViewModel: PetsListViewModel by viewModel()
    private val servicesViewModel: ServicesViewModel by viewModel()
    val arg: ServiceProviderDetailFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentPetSitterDetailBinding
    private lateinit var acceptablePetTypesAdapter: AcceptablePetTypesAdapter
    private lateinit var acceptablePetSizesAdapter: AcceptablePetSizesAdapter
    private lateinit var otherPropertiesAdapter: OtherPropertiesAdapter
    private lateinit var userPetsAdapter: UserPetsAdapter
    private lateinit var albumViewPagerAdapter: AlbumViewPagerAdapter
    private lateinit var userServicesAdapter: UserServicesAdapter
    private lateinit var webView:WebView
    private  var petSize: List<String> = listOf("superSmall", "small", "medium", "big", "superBig")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPetSitterDetailBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceProvider = arg.user
        serviceDetailViewModel.fetchServiceDetail(serviceProvider.id)
        serviceProvider.user?.let {
            petListViewModel.fetchUserPetsList(it.id)
        servicesViewModel.fetchUserServicesList(it.id)
            viewBinding.profileLayout.setOnClickListener { btn->
                btn.findNavController().navigate(
                    ServiceProviderDetailFragmentDirections.actionServiceProviderDetailFragmentToUserProfileFragment(
                        it.id
                    )
                )
            }
        }
        setUpAdapter()
        setObservers()
        viewBinding.callBtn.setOnClickListener {
checkPermission()
        }
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                )) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42
                )
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun callPhone(){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + arg.user.user?.phone))
        startActivity(intent)
    }


    @SuppressLint("SetTextI18n")
    private fun setObservers(){
        if(!serviceProvider.images.isNullOrEmpty()){

            Picasso.get().load(serviceProvider.user?.avatar).fit().centerCrop().placeholder(R.drawable.img_aktos).into(
                viewBinding.userAvatar
            )
        }
        serviceProvider.images?.let { albumViewPagerAdapter.updateImageList(it) }

        viewBinding.petSitterNameTv.text = serviceProvider.user?.fullName
        viewBinding.addressTv.text = serviceProvider.user?.country +", " + serviceProvider.user?.city
        serviceDetailViewModel.getServiceDetail().observe(viewLifecycleOwner, { result ->
            result.doIfSuccess { serviceDetail ->
                serviceDetail?.let {
                    setServiceDetail(serviceDetail)
                }
            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }

            }
        })
        petListViewModel.getUserPetsList().observe(viewLifecycleOwner,
            { result ->
                result.doIfSuccess { pets ->
                    pets?.let { userPetsAdapter.updatePetList(it) }
                }
                result.doIfFailure { error, data ->
                    error?.let { (activity as MainActivity).showAlert(it) }

                }
            }
        )
        servicesViewModel.getUserServicesList().observe(viewLifecycleOwner, { result ->
            result.doIfSuccess {
                it?.let { userServicesAdapter.updateServiceList(it) }

            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }

            }

            result.doIfLoading { }
        })



    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun setServiceDetail(serviceDetail: ServiceDetail){
        viewBinding.serviceDescriptionTv.text = serviceDetail.description
        when(serviceDetail.acceptableSize){
            5 -> acceptablePetSizesAdapter.updatePetSizeList(petSize.subList(0, 0))
            10 -> acceptablePetSizesAdapter.updatePetSizeList(petSize.subList(0, 1))
            20 -> acceptablePetSizesAdapter.updatePetSizeList(petSize.subList(0, 2))
            30 -> acceptablePetSizesAdapter.updatePetSizeList(petSize.subList(0, 3))
            40 -> acceptablePetSizesAdapter.updatePetSizeList(petSize.subList(0, 4))
            50 -> acceptablePetSizesAdapter.updatePetSizeList(petSize)
        }
        serviceDetail.acceptablePets.let { acceptablePetTypesAdapter.updatePetTypeList(it.split(',')) }
        serviceDetail.additionalProperties?.let { otherPropertiesAdapter.updatePropertiesList(it) }

        viewBinding.mapSnapshot.settings.javaScriptEnabled = true
        val htmlData = "<html><head><style Type=\"text/css\"> img { position: absolute; width: 300px; height: 300px; left: 50%; top: 50%; margin-left: -150px; margin-top: -150px; }</style> </head> <body> <img src=\"https://static-maps.yandex.ru/1.x/?lang=ru&ll=${serviceDetail.longitude},${serviceDetail.latitude}&pt=${serviceDetail.longitude},${serviceDetail.latitude},pm2rdm&size=450,450&z=16&l=map\"> </body> </html>"
        viewBinding.mapSnapshot.loadData(htmlData, "text/html", "UTF-8")

        viewBinding.mapSnapshot.setOnTouchListener(View.OnTouchListener() { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> view.findNavController().navigate(
                    ServiceProviderDetailFragmentDirections.actionServiceProviderDetailFragmentToMapsFragment(
                        setLocation = false,
                        longitude = serviceDetail.longitude.toFloat(),
                        latitude = serviceDetail.latitude.toFloat()
                    )
                )

            }
            return@OnTouchListener true
        })

        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())

        addresses = geocoder.getFromLocation(
            serviceDetail.latitude,
            serviceDetail.longitude,
            1
        )


        val city: String = addresses[0].getLocality()?.let{ "$it," }?:run {  ""}
        val country: String = addresses[0].getCountryName()?.let{ "$it," }?:run {  ""}
        val postalCode: String = addresses[0].getPostalCode() ?:run {  ""}
        val street: String = addresses[0].thoroughfare?.let{ "$it," }?:run {  ""}
        val region: String = addresses[0].subLocality?.let{ "$it," }?:run {  ""}
        viewBinding.locationTv.text = "$street $region $city $country $postalCode"
    }

    private fun setUpAdapter(){
        acceptablePetSizesAdapter = AcceptablePetSizesAdapter()
        acceptablePetTypesAdapter = AcceptablePetTypesAdapter()
        otherPropertiesAdapter = OtherPropertiesAdapter()
        userPetsAdapter = UserPetsAdapter(1, listener ={pet-> findNavController().navigate(ServiceProviderDetailFragmentDirections.actionServiceProviderDetailFragmentToPetDetailFragment(pet))})
        userServicesAdapter = UserServicesAdapter()
        albumViewPagerAdapter = AlbumViewPagerAdapter()
        viewBinding.albumVp.adapter = albumViewPagerAdapter

        val petTypeLayoutManager = GridLayoutManager(activity, 2)
        acceptablePetTypesRv.layoutManager = petTypeLayoutManager
        acceptablePetTypesRv.adapter = acceptablePetTypesAdapter
        acceptablePetTypesRv.setHasFixedSize(true)
        acceptablePetTypesRv.setItemViewCacheSize(20)
        acceptablePetTypesRv.isNestedScrollingEnabled = false

        val petSizeLayoutManager = GridLayoutManager(activity, 3)
        acceptablePetSizesRv.layoutManager = petSizeLayoutManager
        acceptablePetSizesRv.adapter = acceptablePetSizesAdapter
        acceptablePetSizesRv.setHasFixedSize(true)
        acceptablePetSizesRv.setItemViewCacheSize(20)
        acceptablePetSizesRv.isNestedScrollingEnabled =  false

        val otherPropertiesLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        otherPropertiesRv.layoutManager = otherPropertiesLayoutManager
        otherPropertiesRv.adapter = otherPropertiesAdapter
        otherPropertiesRv.setHasFixedSize(true)
        otherPropertiesRv.setItemViewCacheSize(20)
        otherPropertiesRv.isNestedScrollingEnabled =  false

        val userPetsLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        userPetsRv.layoutManager = userPetsLayoutManager
        userPetsRv.adapter = userPetsAdapter
        userPetsRv.setHasFixedSize(true)
        userPetsRv.setItemViewCacheSize(20)
        userPetsRv.isNestedScrollingEnabled =  false

        val userServicesLayoutManager = GridLayoutManager(activity, 2)
        userServicesRv.layoutManager = userServicesLayoutManager
        userServicesRv.adapter = userServicesAdapter
        userServicesRv.setHasFixedSize(true)
        userServicesRv.setItemViewCacheSize(20)
        userServicesRv.isNestedScrollingEnabled =  false
    }
}