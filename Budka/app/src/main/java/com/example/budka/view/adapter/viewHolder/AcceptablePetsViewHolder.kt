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
        Log.d("myType", petType.toString())

        when(petType) {
            "dog" ->     {
                itemPetTypeBinding.petNameTv.text = "Собаки"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_dog)
            }
            "cat" ->     {
                itemPetTypeBinding.petNameTv.text = "Кошки"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_cat)
            }
            "fish" ->     {
                itemPetTypeBinding.petNameTv.text = "Рыбы"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_fish)
            }
            "bird" ->     {
                itemPetTypeBinding.petNameTv.text = "Птицы"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_bird)
            }
            "horse" ->     {
                itemPetTypeBinding.petNameTv.text = "Лошади"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_horse)
            }
            "livestock" ->     {
                itemPetTypeBinding.petNameTv.text = "Домашний скот"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_livestock)
            }
            "reptile" ->     {
                itemPetTypeBinding.petNameTv.text = "Рептилии"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_reptile)
            }
            "smallanimal" ->     {
                itemPetTypeBinding.petNameTv.text = "Мелкие питомцы"
                itemPetTypeBinding.petTypeIv.setImageResource(R.drawable.ic_smallanimal)
            }
        }

    }
}