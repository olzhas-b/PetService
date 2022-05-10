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
import com.example.budka.data.model.Pet
import com.example.budka.data.model.ServiceProvider
import com.example.budka.data.model.Services
import com.example.budka.databinding.ItemOtherServiceBinding
import com.example.budka.databinding.ItemPetBinding
import com.example.budka.view.adapter.viewHolder.UserPetsHorizontalViewHolder
import com.example.budka.view.adapter.viewHolder.UserServicesViewHolder

class UserServicesAdapter internal constructor(
): RecyclerView.Adapter<UserServicesViewHolder>() {
    var serviceList: List<ServiceProvider> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserServicesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemOtherServiceBinding.inflate(inflater, parent, false)
        return UserServicesViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: UserServicesViewHolder, position: Int) {
        holder.setUp(serviceList[position])
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    fun updateServiceList(serviceList: List<ServiceProvider>) {
        this.serviceList = serviceList
        notifyDataSetChanged()
    }
}

