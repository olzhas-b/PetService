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
import com.example.budka.data.model.Properties
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.ItemOtherPropertiesBinding
import com.example.budka.databinding.NearestPetSitterItemBinding
import com.example.budka.utils.setUpPriceMask
import com.squareup.picasso.Picasso

class OtherPropertiesVIewHolder constructor(
    val otherPropertiesBinding: ItemOtherPropertiesBinding
): RecyclerView.ViewHolder(otherPropertiesBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(properties: Properties){
        otherPropertiesBinding.labelTv.text = properties.label
        otherPropertiesBinding.textTv.text = properties.text

    }
}