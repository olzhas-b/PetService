/*
 * *
 *  * Created by Ali Ashkeyev on 14.01.22 18:31
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 14.01.22 18:31
 *
 */

package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Properties
import com.example.budka.databinding.ItemCreateOtherPropertiesBinding
import com.example.budka.databinding.ItemOtherPropertiesBinding
import com.example.budka.view.adapter.viewHolder.AddPropertiesViewHolder
import com.example.budka.view.adapter.viewHolder.EditTextChangeListener
import com.example.budka.view.adapter.viewHolder.OtherPropertiesVIewHolder
import com.example.budka.view.adapter.viewHolder.UploadNewImageListener

class AddPropertiesAdapter(
    var editTextChangeListener: EditTextChangeListener ?= null
) : RecyclerView.Adapter<AddPropertiesViewHolder>() {
    var otherPropertiesList: List<Properties> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPropertiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemCreateOtherPropertiesBinding.inflate(inflater, parent, false)
        return AddPropertiesViewHolder(itemBinding, editTextChangeListener)
    }

    override fun onBindViewHolder(holder: AddPropertiesViewHolder, position: Int) {
        holder.setUp(otherPropertiesList[position])
    }

    override fun getItemCount(): Int {
        return otherPropertiesList.size
    }

    fun updatePropertiesList(otherPropertiesList: List<Properties>) {
        this.otherPropertiesList = otherPropertiesList
        notifyDataSetChanged()
    }
}