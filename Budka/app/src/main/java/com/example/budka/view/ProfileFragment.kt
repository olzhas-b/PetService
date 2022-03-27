/*
 * *
 *  * Created by Ali Ashkeyev on 12.01.22 14:17
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12.01.22 14:17
 *
 */

package com.example.budka.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.budka.data.model.*
import com.example.budka.databinding.FragmentProfileBinding
import com.example.budka.viewModel.ProfileViewModel
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment: Fragment() {
    private var _viewBinding: FragmentProfileBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var sessionManager: SessionManager
    private val profileViewModel: ProfileViewModel by viewModel()
    private lateinit var user: User


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
        if(savedInstanceState==null)
            profileViewModel.fetchProfile()
        setObservers()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers(){
        profileViewModel.getProfile().observe(viewLifecycleOwner, { result ->
            result.doIfSuccess { profile ->
                profile?.apply {
                    user = profile
                    Picasso.get().isLoggingEnabled = true
                    Picasso.get().load(avatar).into(viewBinding.userAvatar)
                    viewBinding.tvName.text = fullName
                    viewBinding.tvAddress.text = city + ", " + country
                    viewBinding.userRating.rating = averageRating
                    viewBinding.rateCount.text = (countRating?:0).toString()
                    viewBinding.favCount.text = (cntFavorite?:0).toString()
                }
            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }

            }
        })
    }

    private fun setListeners(){
        viewBinding.myServicesLayout.setOnClickListener {
            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMyServices("service", user.id ))
        }
        viewBinding.myPetsLayout.setOnClickListener{
            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMyServices("pets", user.id))
        }
        viewBinding.logOutBtn.setOnClickListener {
            profileViewModel.logOut().observe(viewLifecycleOwner, {result->
                (activity as MainActivity).showProgressBar(result.show)
                result.doIfSuccess {
                    (activity as MainActivity).showProgressBar()
                    sessionManager.deleteSession()
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()

                }
                result.doIfFailure{ error, data ->
                    error?.let{(activity as MainActivity).showAlert(it)}
                    sessionManager.deleteSession()
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()

                }

            })


        }
        viewBinding.accountLayout.setOnClickListener {
            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangeProfileFragment(user))
        }
    }
}