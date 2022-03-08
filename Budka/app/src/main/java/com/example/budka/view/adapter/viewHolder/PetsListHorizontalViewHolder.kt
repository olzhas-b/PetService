/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.ItemPetBinding
import com.example.budka.databinding.ItemPetProfileBinding
import com.squareup.picasso.Picasso

class PetsListHorizontalViewHolder constructor(
    val itemPetBinding: ItemNearestPetsBinding,

    ): RecyclerView.ViewHolder(itemPetBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petsData: Pet){
        itemPetBinding.mainPagePetNameTv.text = petsData.name
//        itemPetBinding.mainPagePetOwnerNameTv.text = petsData.user?.first_name+" "+ petsData.user?.last_name
        Picasso.get().load("http://192.168.43.145:8080/api/v1/image/2.png").fit().centerCrop().placeholder(R.drawable.img_aktos).into(itemPetBinding.mainPagePetsIv)

    }


}

class PetsListVerticalViewHolder constructor(
    val itemPetBinding: ItemPetProfileBinding,
    var navigationListener: PetEditListener ?= null,

    ): RecyclerView.ViewHolder(itemPetBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petsData: Pet){
        itemPetBinding.petNameTv.text= petsData.name
//        itemPetBinding.mainPagePetOwnerNameTv.text = petsData.user?.first_name+" "+ petsData.user?.last_name
        Picasso.get().load("http://192.168.43.145:8080/api/v1/image/2.png").fit().centerCrop().placeholder(R.drawable.img_aktos).into(itemPetBinding.petsAvatarIv)
        itemView.setOnClickListener {
            navigationListener?.navigate(petsData)
        }
    }


}

interface PetEditListener {
    fun navigate(petsData: Pet)
}