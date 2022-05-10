/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.ItemPetTypeBinding
import com.example.budka.databinding.NearestPetSitterItemBinding
import com.example.budka.utils.setUpPriceMask
import com.squareup.picasso.Picasso

class AcceptablePetsViewHolder  constructor(
    val itemPetTypeBinding: ItemPetTypeBinding
): RecyclerView.ViewHolder(itemPetTypeBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petType: String){

        when(petType) {
            "Собаки" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_dog)
            }
            "Кошки" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_cat)
            }
            "Рыбы" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_fish)
            }
            "Птицы" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_bird)
            }
            "Лошади" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_horse)
            }
            "Домашний скот" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_livestock)
            }
            "Рептилии" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_reptile)
            }
            "Мелкие питомцы" ->     {
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_smallanimal)
            }
            else -> {
                itemPetTypeBinding.petTypeIv.visibility = View.GONE
                itemPetTypeBinding.petNameTv.visibility = View.GONE
            }
        }
        itemPetTypeBinding.petNameTv.text = petType.toString()


    }
}