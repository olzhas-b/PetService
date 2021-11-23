package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Properties
import com.example.budka.databinding.ItemOtherPropertiesBinding
import com.example.budka.databinding.ItemPetTypeBinding
import com.example.budka.view.adapter.viewHolder.AcceptablePetsViewHolder
import com.example.budka.view.adapter.viewHolder.OtherPropertiesVIewHolder
import java.util.*

class OtherPropertiesAdapter internal constructor(): RecyclerView.Adapter<OtherPropertiesVIewHolder>(){
    var otherPropertiesList: List<Properties> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherPropertiesVIewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemOtherPropertiesBinding.inflate(inflater, parent, false)
        return  OtherPropertiesVIewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: OtherPropertiesVIewHolder, position: Int) {
        holder.setUp(otherPropertiesList[position])
    }

    override fun getItemCount(): Int {
        return otherPropertiesList.size
    }

    fun updatePropertiesList(otherPropertiesList: List<Properties>){
        this.otherPropertiesList = otherPropertiesList
        notifyDataSetChanged()
    }

}