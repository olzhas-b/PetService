/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.ItemPetSitterBinding
import com.example.budka.utils.setUpPriceMask
import com.example.budka.view.ServiceProvidersListFragmentDirections
import com.squareup.picasso.Picasso
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.roundToLong

class ServiceProvidersViewHolder constructor(
    val itemPetSitterBinding: ItemPetSitterBinding,
    var favListener: FavListener ?= null,
    var navigationListener: NavigationListener ?= null,
    var isMyServicesPage: Boolean = false

): RecyclerView.ViewHolder(itemPetSitterBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(serviceProviderData: ServiceProvider){
        var image: String? = null
        val df = DecimalFormat("#,#")
        itemPetSitterBinding.petSitterRb.rating =(df.format(serviceProviderData.user?.averageRating)?:"0.0").toFloat()
        if(!serviceProviderData.images.isNullOrEmpty()){
            image = serviceProviderData.images[0]
        }

        Picasso.get().load(image).fit().centerCrop().into(itemPetSitterBinding.perSitterIv)
        if(isMyServicesPage && serviceProviderData.serviceType!=null){

            itemPetSitterBinding.petSitterNameTv.text = if(serviceProviderData.serviceType!=0)
                ServiceType.values()[serviceProviderData.serviceType-1].value
            else
                ""
            itemPetSitterBinding.deleteBtn.visibility = View.VISIBLE
            itemPetSitterBinding.bookmarkIv.visibility = View.GONE
            itemPetSitterBinding.deleteBtn.setOnClickListener {
                navigationListener?.deleteService(serviceProviderData.id)
            }
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
    fun deleteService(serviceId: Int)

}