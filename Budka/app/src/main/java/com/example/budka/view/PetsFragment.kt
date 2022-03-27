/*
 * *
 *  * Created by Ali Ashkeyev on 22.03.22 18:49
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 22.03.22 18:49
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
import com.example.budka.data.model.*
import com.example.budka.databinding.FragmentMyServicesBinding
import com.example.budka.databinding.FragmentPetDetailBinding
import com.example.budka.view.adapter.ServiceProvidersAdapter
import com.example.budka.view.adapter.UserPetsAdapter
import com.example.budka.view.adapter.viewHolder.NavigationListener
import com.example.budka.view.adapter.viewHolder.PetEditListener
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ServicesViewModel
import kotlinx.android.synthetic.main.fragment_my_services.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PetsFragment : Fragment() {

    private var _viewBinding: FragmentMyServicesBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var myPetsAdapter: UserPetsAdapter
    private val petsListViewModel: PetsListViewModel by viewModel()


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

                if(savedInstanceState==null) {
                    petsListViewModel.fetchPetsList()
                }
                setPetsObserver()
                setupPetsAdapter()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    private fun setPetsObserver(){
        petsListViewModel.getPetsList().observe(viewLifecycleOwner, { result ->
            result.doIfSuccess { pets ->
                pets?.let { myPetsAdapter.updatePetList(pets) }
            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }

            }
        })
    }




    private fun setupPetsAdapter(){
        myPetsAdapter = UserPetsAdapter(0, listener  = {pet -> findNavController().navigate(PetsFragmentDirections.actionPetsFragmentToPetDetailFragment(pet))}, showDeleteBtn = false)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        myServicesRv.layoutManager = layoutManager
        myServicesRv.adapter = myPetsAdapter
        myServicesRv.setHasFixedSize(true)
        myServicesRv.setItemViewCacheSize(20)
    }


}