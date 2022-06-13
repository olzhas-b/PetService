/*
 * *
 *  * Created by Ali Ashkeyev on 17.05.2022, 15:45
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 17.05.2022, 15:45
 *
 */

package com.example.budka.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.UploadImage
import com.example.budka.databinding.ItemUploadImageBinding
import com.example.budka.databinding.ItemUploadPdfBinding
import com.example.budka.view.adapter.viewHolder.PdfActionListener
import com.example.budka.view.adapter.viewHolder.UploadImageViewHolder
import com.example.budka.view.adapter.viewHolder.UploadNewImageListener
import com.example.budka.view.adapter.viewHolder.UploadPdfViewHolder

class UploadPdfAdapter (
    var actionListener: PdfActionListener,
    var isCreatePage: Boolean? = true
) : RecyclerView.Adapter<UploadPdfViewHolder>(){
    var uploadImageList: MutableList<UploadImage> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadPdfViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemUploadPdfBinding.inflate(inflater, parent, false)
        return  UploadPdfViewHolder(itemBinding, actionListener,parent.context, isCreatePage )
    }

    override fun onBindViewHolder(holder: UploadPdfViewHolder, position: Int) {
        holder.setUp(uploadImageList[position])
    }

    override fun getItemCount(): Int {
        return uploadImageList.size
    }

    fun updatePdfList(uploadImageList: List<UploadImage>){
        this.uploadImageList = uploadImageList as MutableList<UploadImage>
        notifyDataSetChanged()
    }



}