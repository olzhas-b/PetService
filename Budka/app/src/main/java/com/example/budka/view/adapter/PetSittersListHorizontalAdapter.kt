/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.NearestPetSitterItemBinding
import com.example.budka.view.adapter.viewHolder.PetSittersListHorizontalViewHolder
import com.example.budka.view.adapter.viewHolder.PetsListHorizontalViewHolder

class PetSittersListHorizontalAdapter internal constructor(
): RecyclerView.Adapter<PetSittersListHorizontalViewHolder>() {
    var petSitterList: List<ServiceProvider> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetSittersListHorizontalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = NearestPetSitterItemBinding.inflate(inflater, parent, false)
        return PetSittersListHorizontalViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: PetSittersListHorizontalViewHolder, position: Int) {
        holder.setUp(petSitterList[position])
    }

    override fun getItemCount(): Int {
        return  petSitterList.size
    }

    fun updatePetSittersList(petSitterList: List<ServiceProvider>){
        this.petSitterList = petSitterList
        Log.d("myList", petSitterList.toString())
        notifyDataSetChanged()
    }

}