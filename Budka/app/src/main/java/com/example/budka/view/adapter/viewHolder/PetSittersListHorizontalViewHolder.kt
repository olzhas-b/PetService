/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.ItemPetSitterBinding
import com.example.budka.databinding.NearestPetSitterItemBinding
import com.example.budka.utils.setUpPriceMask
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class PetSittersListHorizontalViewHolder  constructor(
    val nearestPetSitterItemBinding: NearestPetSitterItemBinding,
    var navigationListener: NavigationListener ?= null,

    ): RecyclerView.ViewHolder(nearestPetSitterItemBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petSitterData: ServiceProvider){
        var image: String? = null
        val df = DecimalFormat("#.#")
        nearestPetSitterItemBinding.nearestPetSitterNameTv.text = petSitterData.user?.fullName
        nearestPetSitterItemBinding.nearestPetSitterLocation.text = petSitterData.user?.country + ", " + petSitterData.user?.city
        nearestPetSitterItemBinding.nearestPerSitterRatingTv.text = (df.format(petSitterData.user?.averageRating)?:"0.0")
        nearestPetSitterItemBinding.nearestPetSitterPriceTv.setUpPriceMask(petSitterData.price.toString(),petSitterData.currencyCode ,petSitterData.pricePerTime)
        if(!petSitterData.images.isNullOrEmpty()){
            image = petSitterData.images[0]
        }
        Picasso.get().load(image).fit().centerCrop().placeholder(R.drawable.akimbek).into(nearestPetSitterItemBinding.nearestPerSitterIv)
        itemView.setOnClickListener {
            navigationListener?.navigate(petSitterData)
        }



    }
}