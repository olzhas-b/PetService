/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 20:25
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 20:25
 *
 */

package com.example.budka.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Properties
import com.example.budka.data.model.UploadImage
import com.example.budka.databinding.ItemOtherPropertiesBinding
import com.example.budka.databinding.ItemUploadImageBinding
import com.example.budka.view.adapter.viewHolder.OtherPropertiesVIewHolder
import com.example.budka.view.adapter.viewHolder.UploadImageViewHolder
import com.example.budka.view.adapter.viewHolder.UploadNewImageListener

class UploadImageAdapter(
    var uploadNewImageListener: UploadNewImageListener?= null
) : RecyclerView.Adapter<UploadImageViewHolder>(){
    var uploadImageList: List<UploadImage> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemUploadImageBinding.inflate(inflater, parent, false)
        return  UploadImageViewHolder(itemBinding, uploadNewImageListener)
    }

    override fun onBindViewHolder(holder: UploadImageViewHolder, position: Int) {
        holder.setUp(uploadImageList[position])
    }

    override fun getItemCount(): Int {
        return uploadImageList.size
    }

    fun updateImageList(uploadImageList: List<UploadImage>){
        this.uploadImageList = uploadImageList
        notifyDataSetChanged()
    }

}