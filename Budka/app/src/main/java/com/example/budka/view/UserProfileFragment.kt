/*
 * *
 *  * Created by Ali Ashkeyev on 12.03.22 14:40
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12.03.22 14:40
 *
 */

package com.example.budka.view

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.data.model.SessionManager
import com.example.budka.data.model.User
import com.example.budka.databinding.FragmentUserProfileBinding
import com.example.budka.databinding.VoteDialogBinding
import com.example.budka.view.adapter.UserPetsAdapter
import com.example.budka.view.adapter.UserServicesAdapter
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ProfileViewModel
import com.example.budka.viewModel.ServicesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_sitter_detail.*
import org.koin.android.viewmodel.ext.android.viewModel


class UserProfileFragment: Fragment() {
    private var _viewBinding: FragmentUserProfileBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var sessionManager: SessionManager
    private val profileViewModel: ProfileViewModel by viewModel()
    private val petListViewModel: PetsListViewModel by viewModel()
    private val servicesViewModel: ServicesViewModel by viewModel()
    private val arg: UserProfileFragmentArgs by navArgs()
    private lateinit var user: User
    private lateinit var userPetsAdapter: UserPetsAdapter
    private lateinit var userServicesAdapter: UserServicesAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sessionManager = SessionManager(requireContext())
        _viewBinding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null) {
            profileViewModel.fetchProfile(arg.userId)
            petListViewModel.fetchUserPetsList(arg.userId)
            servicesViewModel.fetchUserServicesList(arg.userId)
        }

        setObservers()
        setListeners()
        setupAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers(){
        profileViewModel.getProfile().observe(viewLifecycleOwner, { profile ->
            profile.apply {
                user = profile
                Picasso.get().load(avatar).into(viewBinding.userAvatar)
                viewBinding.tvName.text = fullName
                viewBinding.tvAddress.text = city + ", " + country
                viewBinding.userRating.rating = averageRating
                viewBinding.profileAboutTv.text = description
            }
        })
        petListViewModel.getUserPetsList().observe(viewLifecycleOwner, {
            userPetsAdapter.updatePetList(it)
        })
        servicesViewModel.getUserServicesList().observe(viewLifecycleOwner, {
            userServicesAdapter.updateServiceList(it)
        })
    }

    private fun setListeners(){
        viewBinding.ratingLy.setOnClickListener {
            showRatingDialog()
        }

    }

    private fun setupAdapter(){
        userPetsAdapter = UserPetsAdapter(1)
        userServicesAdapter = UserServicesAdapter()

        val userPetsLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        userPetsRv.layoutManager = userPetsLayoutManager
        userPetsRv.adapter = userPetsAdapter
        userPetsRv.setHasFixedSize(true)
        userPetsRv.setItemViewCacheSize(20)
        userPetsRv.isNestedScrollingEnabled =  false

        val userServicesLayoutManager = GridLayoutManager(activity, 2)
        userServicesRv.layoutManager = userServicesLayoutManager
        userServicesRv.adapter = userServicesAdapter
        userServicesRv.setHasFixedSize(true)
        userServicesRv.setItemViewCacheSize(20)
        userServicesRv.isNestedScrollingEnabled =  false

    }

    fun showRatingDialog() {
        val voteDialog = AlertDialog.Builder(requireContext())
        val bind :VoteDialogBinding = VoteDialogBinding.inflate(layoutInflater)
        voteDialog.setIcon(R.drawable.btn_star_big_on)
        voteDialog.setTitle("Оцените работу "+viewBinding.tvName.text.toString())
        voteDialog.setView(bind.root)
        voteDialog.setPositiveButton(
            "Оценить"
        ) { dialog, which ->
            profileViewModel.setRating(arg.userId, bind.ratingbar.rating.toInt())
            profileViewModel.fetchProfile(arg.userId)
            profileViewModel.getProfile()
            dialog.dismiss()
        }
            .setNegativeButton(
                "Отмена"
            ) { dialog, id -> dialog.cancel() }
        voteDialog.create()
        voteDialog.show()
    }

}