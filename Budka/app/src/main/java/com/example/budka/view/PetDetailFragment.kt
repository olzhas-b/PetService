/*
 * *
 *  * Created by Ali Ashkeyev on 22.03.22 20:40
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 22.03.22 20:40
 *
 */

package com.example.budka.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.FragmentPetDetailBinding
import com.example.budka.databinding.FragmentPetSitterDetailBinding
import com.example.budka.view.adapter.*
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ProfileViewModel
import com.example.budka.viewModel.ServiceDetailViewModel
import com.example.budka.viewModel.ServicesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_sitter_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class PetDetailFragment: Fragment() {
    private lateinit var pet: Pet
    private lateinit var viewBinding: FragmentPetDetailBinding
    private lateinit var otherPropertiesAdapter: OtherPropertiesAdapter
    private lateinit var userPetsAdapter: UserPetsAdapter
    private lateinit var albumViewPagerAdapter: AlbumViewPagerAdapter
    private val arg: PetDetailFragmentArgs by navArgs()
    private val profileViewModel: ProfileViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPetDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pet = arg.pet
        if(savedInstanceState==null){
            profileViewModel.fetchProfile(pet.userID)
        }
        setUpAdapter()
        setObservers()
        viewBinding.profileLayout.setOnClickListener {
            findNavController().navigate(PetDetailFragmentDirections.actionPetDetailFragmentToUserProfileFragment(pet.userID))
        }
    }



    @SuppressLint("SetTextI18n")
    private fun setObservers(){

        albumViewPagerAdapter.updateImageList(listOf(pet.image))
        profileViewModel.getProfile().observe(viewLifecycleOwner, {result->
            result.doIfSuccess {
                Picasso.get().load(it?.avatar).fit().centerCrop().placeholder(R.drawable.img_aktos).into(viewBinding.userAvatar)
                viewBinding.petSitterNameTv.text = it?.fullName
            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }
            }
        })

        val weight = when(pet.weight){
            5 -> "меньше 5кг"
            10 -> "меньше 10кг"
            20 -> "меньше 20кг"
            30 -> "меньше 30кг"
            40 -> "меньше 40кг"
            50 -> "больше 40кг"
            else -> null
        }


        val propertiesList = mutableListOf<Properties>()
        propertiesList.add(Properties(label = "Имя", text = pet.name))
        propertiesList.add(Properties(label = "Вид", text = pet.type))
        propertiesList.add(Properties(label = "Порода", text = pet.breed))
        propertiesList.add(Properties(label = "Вес", text = weight))
        otherPropertiesAdapter.updatePropertiesList(propertiesList)

    }


    private fun setUpAdapter(){

        otherPropertiesAdapter = OtherPropertiesAdapter()
        albumViewPagerAdapter = AlbumViewPagerAdapter()
        viewBinding.albumVp.adapter = albumViewPagerAdapter


        val otherPropertiesLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false )
        otherPropertiesRv.layoutManager = otherPropertiesLayoutManager
        otherPropertiesRv.adapter = otherPropertiesAdapter
        otherPropertiesRv.setHasFixedSize(true)
        otherPropertiesRv.setItemViewCacheSize(20)
        otherPropertiesRv.isNestedScrollingEnabled =  false

    }
}