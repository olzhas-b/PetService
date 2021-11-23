package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.databinding.ItemPetSizeBinding
import com.example.budka.databinding.ItemPetTypeBinding
import com.squareup.picasso.Picasso

class AcceptablePetSizeViewHolder constructor(
    val itemPetSizeBinding: ItemPetSizeBinding
): RecyclerView.ViewHolder(itemPetSizeBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(petSize: String){
        when(petSize) {
            "superSmall" ->     {
                itemPetSizeBinding.petSizeTv.text = "1-5 кг"
                Picasso.get().load(R.drawable.ic___5kg).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetSizeBinding.petSizeIv)
            }
            "small" ->     {
                itemPetSizeBinding.petSizeTv.text = "5-10 кг"
                Picasso.get().load(R.drawable.ic_5_10kg).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetSizeBinding.petSizeIv)
            }
            "medium" ->     {
                itemPetSizeBinding.petSizeTv.text = "10-20 кг"
                Picasso.get().load(R.drawable.ic__0_20).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetSizeBinding.petSizeIv)
            }
            "big" ->     {
                itemPetSizeBinding.petSizeTv.text = "20-40 кг"
                Picasso.get().load(R.drawable.ic__0_40).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetSizeBinding.petSizeIv)
            }
            "superBig" ->     {
                itemPetSizeBinding.petSizeTv.text = "40+ кг"
                Picasso.get().load(R.drawable.ic__0_).fit().centerCrop().placeholder(R.drawable.akimbek).into(itemPetSizeBinding.petSizeIv)
            }

        }

    }
}