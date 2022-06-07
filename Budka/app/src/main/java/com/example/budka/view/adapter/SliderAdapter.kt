/*
 * *
 *  * Created by Ali Ashkeyev on 06.06.2022, 17:02
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 06.06.2022, 17:02
 *
 */

package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.budka.databinding.SliderLayoutBinding
import com.example.budka.view.adapter.viewHolder.SliderViewHolder
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter: SliderViewAdapter<SliderViewHolder>() {
    var bannerImageList: MutableList<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val itemBinding = SliderLayoutBinding.inflate(inflater, parent, false)
        return SliderViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setUp(bannerImageList[position])
    }

    override fun getCount(): Int {
        return bannerImageList.size
    }

    fun updateImageList(bannerImageList: List<Int>) {
        this.bannerImageList = bannerImageList as MutableList<Int>
        notifyDataSetChanged()
    }




}

