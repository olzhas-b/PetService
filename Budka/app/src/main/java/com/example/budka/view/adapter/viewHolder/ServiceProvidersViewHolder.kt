/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.ItemPetSitterBinding
import com.example.budka.utils.setUpPriceMask
import com.example.budka.view.ServiceProvidersListFragmentDirections
import com.squareup.picasso.Picasso

class ServiceProvidersViewHolder constructor(
    val itemPetSitterBinding: ItemPetSitterBinding,
    var favListener: FavListener ?= null,
    var navigationListener: NavigationListener ?= null,
    var isMyServicesPage: Boolean = false

): RecyclerView.ViewHolder(itemPetSitterBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(serviceProviderData: ServiceProvider){
        var image: String? = null
        itemPetSitterBinding.petSitterRb.rating = serviceProviderData.user?.averageRating?.toFloat()?:0.0.toFloat()
        if(!serviceProviderData.images.isNullOrEmpty()){
            image = serviceProviderData.images[0]
        }

        Picasso.get().load(image).fit().centerCrop().into(itemPetSitterBinding.perSitterIv)
        if(isMyServicesPage && serviceProviderData.serviceType!=null){
            itemPetSitterBinding.petSitterNameTv.text = ServiceType.values()[serviceProviderData.serviceType].value
        } else {
            itemPetSitterBinding.petSitterNameTv.text = serviceProviderData.user?.fullName

        }
        itemPetSitterBinding.petSitterLocationTv.text = serviceProviderData.user?.country + ' ' + serviceProviderData.user?.city
        itemPetSitterBinding.petSitterPriceTv.setUpPriceMask(serviceProviderData.price.toString(),serviceProviderData.currencyCode ,serviceProviderData.pricePerTime)
        itemView.setOnClickListener {
            navigationListener?.navigate(serviceProviderData)
        }
        if(serviceProviderData.isFavorite == true){
            itemPetSitterBinding.bookmarkIv.setImageResource(R.drawable.ic_filled_bookmark)
        } else{
            itemPetSitterBinding.bookmarkIv.setImageResource(R.drawable.ic_bookmark)
        }

        itemPetSitterBinding.bookmarkIv.setOnClickListener {
            if(serviceProviderData.isFavorite == true){
                favListener?.changeFavourite(false, serviceProviderData.id)
                itemPetSitterBinding.bookmarkIv.setImageResource(R.drawable.ic_bookmark)

            } else{
                favListener?.changeFavourite(true, serviceProviderData.id)
                itemPetSitterBinding.bookmarkIv.setImageResource(R.drawable.ic_filled_bookmark)

            }
        }

    }
}

interface FavListener {
    fun changeFavourite(isFavourite: Boolean, serviceId: Int)
}

interface NavigationListener {
    fun navigate(serviceProviderData: ServiceProvider)
}