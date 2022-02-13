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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.databinding.FragmentMyServicesBinding
import com.example.budka.databinding.FragmentProfileBinding
import com.example.budka.view.adapter.ServiceProvidersAdapter
import kotlinx.android.synthetic.main.fragment_my_services.*
import kotlinx.android.synthetic.main.fragment_pet_sitters_page.*

class MyServicesFragment : Fragment() {

    private var _viewBinding: FragmentMyServicesBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val args: MyServicesFragmentArgs by navArgs()
    private lateinit var serviceProvidersAdapter: ServiceProvidersAdapter


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
                setServiceListeners()
                setupServiceAdapter()
            }
            "pets" -> {
                setPetsListeners()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
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
        serviceProvidersAdapter = ServiceProvidersAdapter()
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        myServicesRv.layoutManager = layoutManager
        myServicesRv.adapter = serviceProvidersAdapter
        myServicesRv.setHasFixedSize(true)
        myServicesRv.setItemViewCacheSize(20)
    }
}