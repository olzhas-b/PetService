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

class PetSittersListHorizontalViewHolder  constructor(
    val nearestPetSitterItemBinding: NearestPetSitterItemBinding
): RecyclerView.ViewHolder(nearestPetSitterItemBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petSitterData: ServiceProvider){
        nearestPetSitterItemBinding.nearestPetSitterNameTv.text = petSitterData.first_name
        nearestPetSitterItemBinding.nearestPetSitterLocation.text = petSitterData.city + ',' + petSitterData.country
        nearestPetSitterItemBinding.nearestPerSitterRatingTv.text = petSitterData.average_rating.toString()
        nearestPetSitterItemBinding.nearestPetSitterPriceTv.setUpPriceMask(petSitterData.price.toString(),petSitterData.currencyCode ,petSitterData.pricePerTime)
        Picasso.get().load(petSitterData.avatar).fit().centerCrop().placeholder(R.drawable.akimbek).into(nearestPetSitterItemBinding.nearestPerSitterIv
        )

    }
}