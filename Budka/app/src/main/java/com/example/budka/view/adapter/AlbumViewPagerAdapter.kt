/*
 * *
 *  * Created by Ali Ashkeyev on 12.02.2022, 15:52
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12.02.2022, 15:52
 *
 */

package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.budka.data.model.Properties
import com.example.budka.databinding.ItemAlbumViewPagerBinding
import com.example.budka.databinding.ItemCreateOtherPropertiesBinding
import com.example.budka.view.adapter.viewHolder.AddPropertiesViewHolder
import com.squareup.picasso.Picasso

class AlbumViewPagerAdapter: RecyclerView.Adapter<AlbumViewPagerAdapter.AlbumImageViewHolder>() {
    var images: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewPagerAdapter.AlbumImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemAlbumViewPagerBinding.inflate(inflater, parent, false)
        return AlbumImageViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewPagerAdapter.AlbumImageViewHolder, position: Int) {
        holder.setUp(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun updateImageList(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    inner class AlbumImageViewHolder constructor(
        val itemAlbumViewPagerBinding: ItemAlbumViewPagerBinding
    ): RecyclerView.ViewHolder(itemAlbumViewPagerBinding.root) {

        fun setUp(image: String) {
            Picasso.get().load(image).fit().centerCrop().into(itemAlbumViewPagerBinding.albumIv)
        }
    }
}