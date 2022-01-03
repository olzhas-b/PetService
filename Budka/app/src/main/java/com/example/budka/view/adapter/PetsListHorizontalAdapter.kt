package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Pet
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.ItemPetBinding
import com.example.budka.view.adapter.viewHolder.PetsListHorizontalViewHolder
import com.example.budka.viewModel.PetsListViewModel

class PetsListHorizontalAdapter internal constructor(
): RecyclerView.Adapter<PetsListHorizontalViewHolder>() {
    var petList: List<Pet> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetsListHorizontalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemNearestPetsBinding.inflate(inflater, parent, false)
        return PetsListHorizontalViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: PetsListHorizontalViewHolder, position: Int) {
        holder.setUp(petList[position])
    }

    override fun getItemCount(): Int {
        return  petList.size
    }

    fun updatePetList(petList: List<Pet>){
        this.petList = petList
        notifyDataSetChanged()
    }

}