/*
 * *
 *  * Created by Ali Ashkeyev on 06.06.2022, 17:12
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 06.06.2022, 17:12
 *
 */

package com.example.budka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budka.R
import com.example.budka.databinding.FragmentFavoriteServicesBinding
import com.example.budka.databinding.FragmentInformationPageBinding
import com.example.budka.view.adapter.ServiceProvidersAdapter
import com.example.budka.view.adapter.SliderAdapter
import com.example.budka.view.adapter.viewHolder.FavListener
import com.example.budka.view.adapter.viewHolder.NavigationListener
import com.example.budka.viewModel.PetSittersListViewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderView
import org.koin.android.viewmodel.ext.android.viewModel

class InformationPageFragment : Fragment() {
    private lateinit var viewBinding: FragmentInformationPageBinding
    private lateinit var sliderAdapter: SliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanseState: Bundle?
    ): View {
        viewBinding = FragmentInformationPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sliderImageList = mutableListOf<Int>()
        sliderImageList.add(R.drawable.banner1)
        sliderImageList.add(R.drawable.banner2)
        sliderImageList.add(R.drawable.banner3)
        sliderImageList.add(R.drawable.banner)

        val adapter = SliderAdapter()
        adapter.updateImageList(sliderImageList)
        with(viewBinding.slider){
            autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            setSliderAdapter(adapter)
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            scrollTimeInSec = 3
            isAutoCycle = true
            startAutoCycle()
        }


    }
}