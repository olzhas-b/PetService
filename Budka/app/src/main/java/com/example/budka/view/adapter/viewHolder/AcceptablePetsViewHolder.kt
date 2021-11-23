package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.ServiceProvider
import com.example.budka.databinding.ItemPetTypeBinding
import com.example.budka.databinding.NearestPetSitterItemBinding
import com.example.budka.utils.setUpPriceMask
import com.squareup.picasso.Picasso

class AcceptablePetsViewHolder  constructor(
    val itemPetTypeBinding: ItemPetTypeBinding
): RecyclerView.ViewHolder(itemPetTypeBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petType: String){
        when(petType) {
            "dog" ->     {
                itemPetTypeBinding.petNameTv.text = "Собаки"
                Picasso.get().load(R.drawable.ic_dog).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
            "cat" ->     {
                itemPetTypeBinding.petNameTv.text = "Кошки"
                Picasso.get().load(R.drawable.ic_cat).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
            "fish" ->     {
                itemPetTypeBinding.petNameTv.text = "Рыбы"
                Picasso.get().load(R.drawable.ic_fish).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
            "bird" ->     {
                itemPetTypeBinding.petNameTv.text = "Птицы"
                Picasso.get().load(R.drawable.ic_bird).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
            "horse" ->     {
                itemPetTypeBinding.petNameTv.text = "Лошади"
                Picasso.get().load(R.drawable.ic_horse).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
            "livestock" ->     {
                itemPetTypeBinding.petNameTv.text = "Домашний скот"
                Picasso.get().load(R.drawable.ic_livestock).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
            "reptile" ->     {
                itemPetTypeBinding.petNameTv.text = "Рептилии"
                Picasso.get().load(R.drawable.ic_reptile).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
            "smallanimal" ->     {
                itemPetTypeBinding.petNameTv.text = "Мелкие питомцы"
                Picasso.get().load(R.drawable.ic_smallanimal).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetTypeBinding.petTypeIv)
            }
        }

    }
}