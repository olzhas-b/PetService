package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Pet
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.ItemPetBinding
import com.example.budka.view.adapter.viewHolder.PetsListHorizontalViewHolder
import com.example.budka.view.adapter.viewHolder.UserPetsHorizontalViewHolder

class UserPetsAdapter internal constructor(
): RecyclerView.Adapter<UserPetsHorizontalViewHolder>() {
    var petList: List<Pet> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserPetsHorizontalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPetBinding.inflate(inflater, parent, false)
        return UserPetsHorizontalViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: UserPetsHorizontalViewHolder, position: Int) {
        holder.setUp(petList[position])
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    fun updatePetList(petList: List<Pet>) {
        this.petList = petList
        notifyDataSetChanged()
    }
}

