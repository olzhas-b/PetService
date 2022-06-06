/*
 * *
 *  * Created by Ali Ashkeyev on 12.01.22 14:17
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12.01.22 14:17
 *
 */

package com.example.budka.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.budka.R
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
    private var unauthorized: Boolean = false


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
        with(viewBinding) {
            profileViewModel.getProfile().observe(viewLifecycleOwner, { result ->
                result.doIfSuccess { profile ->

                    profile?.apply {
                        user = profile
                        Picasso.get().load(avatar).into(userAvatar)
                        tvName.text = fullName
                        tvAddress.text = city + ", " + country
                        userRating.rating = averageRating
                        rateCount.text = (countRating ?: 0).toString()
                        favCount.text = (cntFavorite ?: 0).toString()
                        if(user.isVerified)
                            verificationBlock.visibility = View.VISIBLE
                        else
                            verificationBlock.visibility = View.GONE
                    }
                    logOutBtn.text = "Выйти"
                    unauthorized = false
                }
                result.doIfFailure { error, data ->
                    logOutBtn.text = "Войти"
                    unauthorized = true
                    if (error != null) {
                        if (error.contains("401")) {
                            showLogin()
                        } else
                            error.let { (activity as MainActivity).showAlert(it) }
                    }

                }
            })
        }
    }

    private fun setListeners(){

            viewBinding.myServicesLayout.setOnClickListener {
                if(!unauthorized) {
                    it.findNavController().navigate(
                        ProfileFragmentDirections.actionProfileFragmentToMyServices(
                            "service",
                            user.id
                        )
                    )
                } else {
                    showLogin()
                }
            }
            viewBinding.myPetsLayout.setOnClickListener {
                if(!unauthorized) {
                it.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToMyServices(
                        "pets",
                        user.id
                    )
                )} else
                    showLogin()
            }
            viewBinding.logOutBtn.setOnClickListener {
                if(!unauthorized) {
                    profileViewModel.logOut().observe(viewLifecycleOwner, { result ->
                        (activity as MainActivity).showProgressBar(result.show)
                        result.doIfSuccess {
                            (activity as MainActivity).showProgressBar()
                            sessionManager.deleteSession()
                            val intent = Intent(activity, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            activity?.finish()

                        }
                        result.doIfFailure { error, data ->
                            error?.let { (activity as MainActivity).showAlert(it) }
                            sessionManager.deleteSession()
                            val intent = Intent(activity, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            activity?.finish()

                        }

                    })
                }
                else {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    this.startActivity(intent)
                }


            }
            viewBinding.accountLayout.setOnClickListener {
                if(!unauthorized)
                    it.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToChangeProfileFragment(user)
                )
                else
                    showLogin()
            }
        viewBinding.verificationLayout.setOnClickListener {
                it.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToOtpPhoneFragment()
                )
        }
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