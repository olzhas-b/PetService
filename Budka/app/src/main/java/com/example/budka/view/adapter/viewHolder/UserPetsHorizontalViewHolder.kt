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
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.ItemPetBinding
import com.squareup.picasso.Picasso

class UserPetsHorizontalViewHolder (
    val itemPetBinding: ItemPetBinding
): RecyclerView.ViewHolder(itemPetBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petsData: Pet){
        itemPetBinding.petNameTv.text = petsData.name
        Picasso.get().load(petsData.avatar).fit().centerCrop().placeholder(R.drawable.img_aktos).into(itemPetBinding.petsAvatarIv)

    }
}