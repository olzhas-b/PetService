package com.example.budka.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.databinding.FragmentPetSittersPageBinding
import com.example.budka.view.adapter.ServiceProvidersAdapter
import com.example.budka.viewModel.PetSittersListViewModel
import kotlinx.android.synthetic.main.fragment_pet_sitters_page.*
import org.koin.android.viewmodel.ext.android.viewModel


class ServiceProvidersListFragment: Fragment() {
    val arg: ServiceProvidersListFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentPetSittersPageBinding
    private lateinit var serviceProvidersAdapter: ServiceProvidersAdapter
    private val petSittersListViewModel: PetSittersListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanseState: Bundle?
    ): View {
        viewBinding = FragmentPetSittersPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    }

}