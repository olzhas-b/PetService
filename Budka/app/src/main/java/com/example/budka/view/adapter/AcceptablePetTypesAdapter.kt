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
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.ItemPetSitterBinding
import com.example.budka.databinding.ItemPetTypeBinding
import com.example.budka.view.adapter.viewHolder.AcceptablePetsViewHolder
import com.example.budka.view.adapter.viewHolder.ServiceProvidersViewHolder

class AcceptablePetTypesAdapter internal constructor(): RecyclerView.Adapter<AcceptablePetsViewHolder>(){
    var petTypesList: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptablePetsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPetTypeBinding.inflate(inflater, parent, false)
        return  AcceptablePetsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AcceptablePetsViewHolder, position: Int) {
        holder.setUp(petTypesList[position])
    }

    override fun getItemCount(): Int {
        return petTypesList.size
    }

    fun updatePetTypeList(petTypeList: List<String>){
        this.petTypesList = petTypeList
        notifyDataSetChanged()
    }

}