/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:55
 *
 */

package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.databinding.ItemPetSizeBinding
import com.example.budka.databinding.ItemPetTypeBinding
import com.example.budka.view.adapter.viewHolder.AcceptablePetSizeViewHolder
import com.example.budka.view.adapter.viewHolder.AcceptablePetsViewHolder

class AcceptablePetSizesAdapter internal constructor(): RecyclerView.Adapter<AcceptablePetSizeViewHolder>() {
    var petSizeList: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptablePetSizeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPetSizeBinding.inflate(inflater, parent, false)
        return AcceptablePetSizeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AcceptablePetSizeViewHolder, position: Int) {
        holder.setUp(petSizeList[position])
    }


    override fun getItemCount(): Int {
        return petSizeList.size
    }

    fun updatePetSizeList(petSizeList: List<String>) {
        this.petSizeList = petSizeList
        notifyDataSetChanged()
    }
}

