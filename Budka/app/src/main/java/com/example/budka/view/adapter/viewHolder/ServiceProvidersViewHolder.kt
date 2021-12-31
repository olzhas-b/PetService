package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.User
import com.example.budka.databinding.ItemPetSitterBinding
import com.example.budka.utils.setUpPriceMask
import com.example.budka.view.ServiceProvidersListFragmentDirections
import com.squareup.picasso.Picasso

class ServiceProvidersViewHolder constructor(
    val itemPetSitterBinding: ItemPetSitterBinding
): RecyclerView.ViewHolder(itemPetSitterBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(serviceProviderData: ServiceProvider){
        itemPetSitterBinding.petSitterRb.rating = serviceProviderData.average_rating.toFloat()
        Picasso.get().load(serviceProviderData.avatar).fit().centerCrop().into(itemPetSitterBinding.perSitterIv)
        itemPetSitterBinding.petSitterNameTv.text = serviceProviderData.first_name +' '+ serviceProviderData.last_name
        itemPetSitterBinding.petSitterLocationTv.text = serviceProviderData.location
        itemPetSitterBinding.petSitterPriceTv.setUpPriceMask(serviceProviderData.price.toString(),serviceProviderData.currencyCode ,serviceProviderData.pricePerTime)
        itemView.setOnClickListener {
            itemView.findNavController().navigate(ServiceProvidersListFragmentDirections.actionServiceProvidersFragmentToServiceProviderDetailFragment(serviceProviderData))
        }


    }
}