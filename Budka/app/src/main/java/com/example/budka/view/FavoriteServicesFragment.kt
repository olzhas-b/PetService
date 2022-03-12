/*
 * *
 *  * Created by Ali Ashkeyev on 18.02.2022, 22:58
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 18.02.2022, 22:58
 *
 */

package com.example.budka.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.FragmentFavoriteServicesBinding
import com.example.budka.view.adapter.ServiceProvidersAdapter
import com.example.budka.view.adapter.viewHolder.FavListener
import com.example.budka.view.adapter.viewHolder.NavigationListener
import com.example.budka.viewModel.PetSittersListViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class FavoriteServicesFragment: Fragment(), FavListener, NavigationListener {
    private lateinit var viewBinding: FragmentFavoriteServicesBinding
    private lateinit var serviceProvidersAdapter: ServiceProvidersAdapter
    private val petSittersListViewModel: PetSittersListViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanseState: Bundle?
    ): View {
        viewBinding = FragmentFavoriteServicesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            petSittersListViewModel.fetchFavoriteServices()
        }
        setupAdapter()
        setObservers()
    }

    private fun setObservers() {
        petSittersListViewModel.getFavoriteServices().observe(viewLifecycleOwner, {
            serviceProvidersAdapter.updateEmployeeList(it)
        })
    }

    private fun setupAdapter() {
        serviceProvidersAdapter = ServiceProvidersAdapter(favListener = this)
        viewBinding.serviceProvidersRv.adapter = serviceProvidersAdapter
        viewBinding.serviceProvidersRv.setHasFixedSize(true)
        viewBinding.serviceProvidersRv.setItemViewCacheSize(20)
    }


    override fun changeFavourite(isFavourite: Boolean, serviceId: Int) {
        if (isFavourite) {
            petSittersListViewModel.putLike(serviceId)
        } else {
            petSittersListViewModel.deleteLike(serviceId)
        }
    }

    override fun navigate(serviceProviderData: ServiceProvider) {
        findNavController().navigate(FavoriteServicesFragmentDirections.actionFavoriteServicesFragmentToServiceProviderDetailFragment(serviceProviderData))
    }

    override fun deleteService(serviceId: Int) {
        Log.d("not", "not")
    }

}