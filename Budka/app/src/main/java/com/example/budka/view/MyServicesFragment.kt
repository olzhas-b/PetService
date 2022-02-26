/*
 * *
 *  * Created by Ali Ashkeyev on 12.01.22 21:00
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12.01.22 19:41
 *
 */

package com.example.budka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.FragmentMyServicesBinding
import com.example.budka.view.adapter.ServiceProvidersAdapter
import com.example.budka.view.adapter.UserPetsAdapter
import com.example.budka.view.adapter.viewHolder.NavigationListener
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ServicesViewModel
import kotlinx.android.synthetic.main.fragment_my_services.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyServicesFragment : Fragment(), NavigationListener {

    private var _viewBinding: FragmentMyServicesBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val args: MyServicesFragmentArgs by navArgs()
    private lateinit var serviceProvidersAdapter: ServiceProvidersAdapter
    private lateinit var myPetsAdapter: UserPetsAdapter
    private val petsListViewModel: PetsListViewModel by viewModel()
    private val myservicesViewModel: ServicesViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentMyServicesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(args.toPage){
            "service" -> {
                if(savedInstanceState==null) {
                    myservicesViewModel.fetchUserServicesList(args.userId)
                }
                setMyServicesObserver()
                setServiceListeners()
                setupServiceAdapter()
            }
            "pets" -> {
                if(savedInstanceState==null) {
                    petsListViewModel.fetchUserPetsList(args.userId)
                }
                setMyPetsObserver()
                setupPetsAdapter()
                setPetsListeners()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    private fun setMyPetsObserver(){
        petsListViewModel.getUserPetsList().observe(viewLifecycleOwner, {
            myPetsAdapter.updatePetList(it)
        })
    }

    private fun setMyServicesObserver(){
        myservicesViewModel.getUserServicesList().observe(viewLifecycleOwner, {
            serviceProvidersAdapter.updateEmployeeList(it)
        })
    }

    private fun setServiceListeners(){
        viewBinding.fab.setOnClickListener {

            it.findNavController()
                .navigate(MyServicesFragmentDirections.actionMyServicesToCreateServiceRequiredFragment())

        }

    }

    private fun setPetsListeners(){
        viewBinding.fab.setOnClickListener {
            it.findNavController().navigate(
                MyServicesFragmentDirections.actionMyServicesToCreatePetFragment()
            )
        }
        }


    private fun setupServiceAdapter(){
        serviceProvidersAdapter = ServiceProvidersAdapter(isMyServicesPage = true, navigationListener = this)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        myServicesRv.layoutManager = layoutManager
        myServicesRv.adapter = serviceProvidersAdapter
        myServicesRv.setHasFixedSize(true)
        myServicesRv.setItemViewCacheSize(20)
    }

    private fun setupPetsAdapter(){
        myPetsAdapter = UserPetsAdapter(0)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        myServicesRv.layoutManager = layoutManager
        myServicesRv.adapter = myPetsAdapter
        myServicesRv.setHasFixedSize(true)
        myServicesRv.setItemViewCacheSize(20)
    }

    override fun navigate(serviceProviderData: ServiceProvider) {
        findNavController().navigate(MyServicesFragmentDirections.actionMyServicesToServiceProviderDetailFragment(serviceProviderData))
    }
}