/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.databinding.ItemPetSizeBinding
import com.example.budka.databinding.ItemPetTypeBinding
import com.squareup.picasso.Picasso

class AcceptablePetSizeViewHolder constructor(
    val itemPetSizeBinding: ItemPetSizeBinding
): RecyclerView.ViewHolder(itemPetSizeBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petSize: String){
        Log.d("mySizes", petSize.toString())
        Picasso.get().isLoggingEnabled = true
        when(petSize) {
            "superSmall" ->     {
                itemPetSizeBinding.petSizeTv.text = "1-5 кг"
                itemPetSizeBinding.petSizeIv.setImageResource(R.drawable.ic___5kg)
            }
            "small" ->     {
                itemPetSizeBinding.petSizeTv.text = "5-10 кг"
                itemPetSizeBinding.petSizeIv.setImageResource(R.drawable.ic_5_10kg)
            }
            "medium" ->     {
                itemPetSizeBinding.petSizeTv.text = "10-20 кг"
                itemPetSizeBinding.petSizeIv.setImageResource(R.drawable.ic__0_20)
            }
            "big" ->     {
                itemPetSizeBinding.petSizeTv.text = "20-40 кг"
                itemPetSizeBinding.petSizeIv.setImageResource(R.drawable.ic__0_40)
            }
            "superBig" ->     {
                itemPetSizeBinding.petSizeTv.text = "40+ кг"
                itemPetSizeBinding.petSizeIv.setImageResource(R.drawable.ic__0_)
            }

        }

    }
}