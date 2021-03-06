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
import com.example.budka.data.model.User
import com.example.budka.databinding.ItemPetSitterBinding
import com.example.budka.view.adapter.viewHolder.EditTextChangeListener
import com.example.budka.view.adapter.viewHolder.FavListener
import com.example.budka.view.adapter.viewHolder.NavigationListener
import com.example.budka.view.adapter.viewHolder.ServiceProvidersViewHolder

class ServiceProvidersAdapter internal constructor(
    var favListener: FavListener?= null,
    var navigationListener: NavigationListener?= null,
    var isMyServicesPage: Boolean = false

): RecyclerView.Adapter<ServiceProvidersViewHolder>(){
    var employeesList: List<ServiceProvider> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceProvidersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPetSitterBinding.inflate(inflater, parent, false)
        return  ServiceProvidersViewHolder(itemBinding, favListener, navigationListener, isMyServicesPage)
    }

    override fun onBindViewHolder(holder: ServiceProvidersViewHolder, position: Int) {
        holder.setUp(employeesList[position])
    }

    override fun getItemCount(): Int {
        return employeesList.size
    }

    fun updateEmployeeList(employeeList: List<ServiceProvider>?){
        if (employeeList != null) {
            this.employeesList = employeeList
        }
        notifyDataSetChanged()
    }

    fun removeElement(id: Int){
        this.employeesList = this.employeesList.filter { it.id != id }
        notifyDataSetChanged()
    }

}