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
import android.widget.HorizontalScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Pet
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.ItemPetBinding
import com.example.budka.databinding.ItemPetProfileBinding
import com.example.budka.view.adapter.viewHolder.*

class UserPetsAdapter internal constructor(myViewType: Int,
                                           var navigationListener: PetEditListener?= null,

                                           ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var petList: List<Pet> = emptyList()
    val HORIZONTAL = 1
    val VERTICAL = 0
    val myViewType = myViewType

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when(viewType){
            HORIZONTAL -> {
                val itemBinding = ItemPetBinding.inflate(inflater, parent, false)
                return UserPetsHorizontalViewHolder(itemBinding)
            }
            VERTICAL -> {
                val itemBinding = ItemPetProfileBinding.inflate(inflater, parent, false)
                return PetsListVerticalViewHolder(itemBinding, navigationListener)
            }
        }
        return UserPetsHorizontalViewHolder(ItemPetBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            HORIZONTAL -> {
                val viewHolder = holder as UserPetsHorizontalViewHolder
                viewHolder.setUp(petList[position])
            }
            VERTICAL -> {
                val viewHolder = holder as PetsListVerticalViewHolder
                viewHolder.setUp(petList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return myViewType
    }


    override fun getItemCount(): Int {
        return petList.size
    }

    fun updatePetList(petList: List<Pet>) {
        this.petList = petList
        notifyDataSetChanged()
    }

}

