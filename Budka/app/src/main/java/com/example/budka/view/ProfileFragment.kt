/*
 * *
 *  * Created by Ali Ashkeyev on 12.01.22 14:17
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12.01.22 14:17
 *
 */

package com.example.budka.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.budka.data.model.SessionManager
import com.example.budka.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {
    private var _viewBinding: FragmentProfileBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sessionManager = SessionManager(requireContext())
        _viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
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
        viewBinding.myServicesLayout.setOnClickListener {
            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMyServices("service"))
        }
        viewBinding.myPetsLayout.setOnClickListener{
            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMyServices("pets"))
        }
        viewBinding.infoLayout.setOnClickListener {
            sessionManager.deleteSession()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()

        }
    }
}