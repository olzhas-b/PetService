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
import com.example.budka.data.model.Services
import com.example.budka.databinding.ItemOtherServiceBinding
import com.example.budka.databinding.ItemPetBinding
import com.example.budka.utils.setUpPriceMask
import com.squareup.picasso.Picasso

class UserServicesViewHolder (
    val itemOtherServiceBinding: ItemOtherServiceBinding
): RecyclerView.ViewHolder(itemOtherServiceBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(services: ServiceProvider){
        when(services.serviceType) {
            1 ->     {
                itemOtherServiceBinding.serviceNameTv.text = "Зооняня"
                itemOtherServiceBinding.serviceIconIv.setImageResource(R.drawable.ic_pet_sitter)
            }
            2 ->     {
                itemOtherServiceBinding.serviceNameTv.text = "Выгул"
                itemOtherServiceBinding.serviceIconIv.setImageResource(R.drawable.ic_pet_walking)
            }
            3 ->     {
                itemOtherServiceBinding.serviceNameTv.text = "Ветеринария"
                itemOtherServiceBinding.serviceIconIv.setImageResource(R.drawable.ic_vet)
            }
            4 ->     {
                itemOtherServiceBinding.serviceNameTv.text = "Дрессировка"
                itemOtherServiceBinding.serviceIconIv.setImageResource(R.drawable.ic_training)
            }
            5 ->     {
                itemOtherServiceBinding.serviceNameTv.text = "Груминг"
                itemOtherServiceBinding.serviceIconIv.setImageResource(R.drawable.ic_grooming)
            }
            6 ->     {
                itemOtherServiceBinding.serviceNameTv.text = "Зоогостиницы"
                itemOtherServiceBinding.serviceIconIv.setImageResource(R.drawable.ic_zoohostel)
            }


    }
        itemOtherServiceBinding.servicePriceTv.setUpPriceMask(services.price.toString(),services.currencyCode ,services.pricePerTime)
}
}