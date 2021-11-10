package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.Pet
import com.example.budka.databinding.ItemNearestPetsBinding
import com.example.budka.databinding.ItemPetBinding
import com.squareup.picasso.Picasso

class PetsListHorizontalViewHolder constructor(
    val itemPetBinding: ItemNearestPetsBinding
): RecyclerView.ViewHolder(itemPetBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petsData: Pet){
        itemPetBinding.mainPagePetNameTv.text = petsData.name
        itemPetBinding.mainPagePetOwnerNameTv.text = petsData.user.first_name+" "+ petsData.user.last_name
        Picasso.get().load(petsData.avatar).fit().centerCrop().placeholder(R.drawable.img_aktos).into(itemPetBinding.mainPagePetsIv)

    }
}