/*
 * *
 *  * Created by Ali Ashkeyev on 18.02.2022, 22:58
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 18.02.2022, 22:58
 *
 */

package com.example.budka.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budka.R
import com.example.budka.data.model.NetworkResult
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.doIfFailure
import com.example.budka.data.model.doIfSuccess
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
    private lateinit var runnable: Runnable


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
        viewBinding.petSittersShimmerLayout.startShimmerAnimation()
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            runnable = Runnable {
                viewBinding.petSittersShimmerLayout.startShimmerAnimation()
                viewBinding.petSittersShimmerLayout.visibility = View.VISIBLE
                petSittersListViewModel.fetchFavoriteServices()
                setObservers()
                viewBinding.swipeRefreshLayout.isRefreshing = false
            }
            Handler(
                Looper.getMainLooper()).postDelayed(runnable, 3000.toLong())
        }
        viewBinding.swipeRefreshLayout.setColorSchemeResources(R.color.mainColor)
        setupAdapter()
        setObservers()
    }

    private fun setObservers() {
        petSittersListViewModel.getFavoriteServices().observe(viewLifecycleOwner) { result ->
            result.doIfSuccess {
                (activity as MainActivity).showProgressBar()
                viewBinding.petSittersShimmerLayout.stopShimmerAnimation()
                viewBinding.petSittersShimmerLayout.visibility = View.GONE
                viewBinding.errorTv.visibility =
                    if(it.isNullOrEmpty())
                        View.VISIBLE
                    else
                         View.GONE
                serviceProvidersAdapter.updateEmployeeList(it)

            }
            result.doIfFailure { error, data ->
                if (error != null) {
                    if (error.contains("401")) {
                        showLogin()
                    } else {
                        (activity as MainActivity).showProgressBar()
                        error.let { (activity as MainActivity).showAlert(it) }
                    }
                }
            }

            if (result is NetworkResult.Loading) {
                (activity as MainActivity).showProgressBar(true)

            }
        }
    }

    private fun setupAdapter() {
        serviceProvidersAdapter = ServiceProvidersAdapter(favListener = this, navigationListener = this)
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
        serviceProvidersAdapter.removeElement(serviceId)

    }

    override fun navigate(serviceProviderData: ServiceProvider) {
        findNavController().navigate(FavoriteServicesFragmentDirections.actionFavoriteServicesFragmentToServiceProviderDetailFragment(serviceProviderData))
    }

    override fun deleteService(serviceId: Int) {
        Log.d("not", "not")
    }

    private fun showLogin(){
        val errorDialog = AlertDialog.Builder(requireContext())
        errorDialog.setIcon(R.drawable.ic_baseline_error_24)
        errorDialog.setTitle("Войдите пожалуйста в аккаунт")
        errorDialog.setNegativeButton(
            "Вернуться"
        ) { dialog, _ ->

            dialog.cancel()
        }
        errorDialog.setPositiveButton(
            "Войти",
        ){dialog, _ ->
            val intent = Intent(requireContext(), LoginActivity::class.java)
            this.startActivity(intent)
            dialog.dismiss()
        }
        errorDialog.create()
        errorDialog.show()
    }

}