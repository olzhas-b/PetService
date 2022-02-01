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
import com.example.budka.R
import com.example.budka.databinding.FragmentMyServicesBinding
import com.example.budka.databinding.FragmentProfileBinding

class MyServicesFragment : Fragment() {

    private var _viewBinding: FragmentMyServicesBinding? = null
    private val viewBinding get() = _viewBinding!!

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
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    private fun setListeners(){
        viewBinding.fab.setOnClickListener {
            it.findNavController().navigate(MyServicesFragmentDirections.actionMyServicesToCreateServiceRequiredFragment())
        }
    }
}