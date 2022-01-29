/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.data.model.ServiceDetail
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.FragmentPetSitterDetailBinding
import com.example.budka.view.adapter.*
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ServiceDetailViewModel
import com.example.budka.viewModel.ServicesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_sitter_detail.*
import org.koin.android.viewmodel.ext.android.viewModel


class ServiceProviderDetailFragment: Fragment() {
    private lateinit var serviceDetail: ServiceDetail
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
    private lateinit var userServicesAdapter: UserServicesAdapter
    private lateinit var webView:WebView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPetSitterDetailBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceProvider = arg.user
        viewBinding.mapSnapshot.settings.javaScriptEnabled = true
        val htmlData = "<html><head><style Type=\"text/css\"> img { position: absolute; width: 300px; height: 300px; left: 50%; top: 50%; margin-left: -150px; margin-top: -150px; }</style> </head> <body> <img src=\"https://static-maps.yandex.ru/1.x/?lang=ru&ll=76.944609,43.227016&pt=76.944609,43.227016,pm2rdm&size=450,450&z=16&l=map\"> </body> </html>"
        viewBinding.mapSnapshot.loadData(htmlData, "text/html", "UTF-8")
        serviceDetailViewModel.fetchServiceDetail(serviceProvider.service_id)
        petListViewModel.fetchUserPetsList(serviceProvider.id)
        servicesViewModel.fetchUserServicesList(serviceProvider.id)

        viewBinding.mapSnapshot.setOnTouchListener(View.OnTouchListener(){
            view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN->  view.findNavController().navigate(ServiceProviderDetailFragmentDirections.actionServiceProviderDetailFragmentToMapsFragment(false))

            }
            return@OnTouchListener true
        })
        setUpAdapter()
        setObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers(){
        Picasso.get().load(serviceProvider.avatar).fit().centerCrop().placeholder(R.drawable.img_aktos).into(viewBinding.albumIv)
        Picasso.get().load(serviceProvider.avatar).fit().centerCrop().placeholder(R.drawable.img_aktos).into(viewBinding.userAvatar)
        viewBinding.petSitterNameTv.text = serviceProvider.first_name + " " + serviceProvider.last_name
        viewBinding.addressTv.text = serviceProvider.city +", " + serviceProvider.country
        serviceDetailViewModel.getServiceDetail().observe(viewLifecycleOwner, {
            Log.d("details", it.toString())
            setServiceDetail(it)

        })
        petListViewModel.getUserPetsList().observe(viewLifecycleOwner, {
            userPetsAdapter.updatePetList(it)
        })
        servicesViewModel.getUserServicesList().observe(viewLifecycleOwner, {
            userServicesAdapter.updateServiceList(it)
        })



    }

    private fun setServiceDetail(serviceDetail: ServiceDetail){
        viewBinding.serviceDescriptionTv.text = serviceDetail.description
        serviceDetail.acceptablePets?.let { acceptablePetTypesAdapter.updatePetTypeList(it) }
        serviceDetail.acceptableSize?.let { acceptablePetSizesAdapter.updatePetSizeList(it) }
        serviceDetail.additionalProperties?.let { otherPropertiesAdapter.updatePropertiesList(it) }
    }

    private fun setUpAdapter(){
        acceptablePetSizesAdapter = AcceptablePetSizesAdapter()
        acceptablePetTypesAdapter = AcceptablePetTypesAdapter()
        otherPropertiesAdapter = OtherPropertiesAdapter()
        userPetsAdapter = UserPetsAdapter()
        userServicesAdapter = UserServicesAdapter()

        val petTypeLayoutManager = GridLayoutManager(activity, 2 )
        acceptablePetTypesRv.layoutManager = petTypeLayoutManager
        acceptablePetTypesRv.adapter = acceptablePetTypesAdapter
        acceptablePetTypesRv.setHasFixedSize(true)
        acceptablePetTypesRv.setItemViewCacheSize(20)
        acceptablePetTypesRv.isNestedScrollingEnabled = false

        val petSizeLayoutManager = GridLayoutManager(activity, 3 )
        acceptablePetSizesRv.layoutManager = petSizeLayoutManager
        acceptablePetSizesRv.adapter = acceptablePetSizesAdapter
        acceptablePetSizesRv.setHasFixedSize(true)
        acceptablePetSizesRv.setItemViewCacheSize(20)
        acceptablePetSizesRv.isNestedScrollingEnabled =  false

        val otherPropertiesLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false )
        otherPropertiesRv.layoutManager = otherPropertiesLayoutManager
        otherPropertiesRv.adapter = otherPropertiesAdapter
        otherPropertiesRv.setHasFixedSize(true)
        otherPropertiesRv.setItemViewCacheSize(20)
        otherPropertiesRv.isNestedScrollingEnabled =  false

        val userPetsLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false )
        userPetsRv.layoutManager = userPetsLayoutManager
        userPetsRv.adapter = userPetsAdapter
        userPetsRv.setHasFixedSize(true)
        userPetsRv.setItemViewCacheSize(20)
        userPetsRv.isNestedScrollingEnabled =  false

        val userServicesLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false )
        userServicesRv.layoutManager = userServicesLayoutManager
        userServicesRv.adapter = userServicesAdapter
        userServicesRv.setHasFixedSize(true)
        userServicesRv.setItemViewCacheSize(20)
        userServicesRv.isNestedScrollingEnabled =  false
    }
}