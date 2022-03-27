/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 20:20
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 20:20
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.Pet
import com.example.budka.data.model.UploadImage
import com.example.budka.databinding.ItemPetBinding
import com.example.budka.databinding.ItemUploadImageBinding
import com.squareup.picasso.Picasso

class UploadImageViewHolder (
    val itemUploadImageBinding: ItemUploadImageBinding,
    var uploadNewImageListener: UploadNewImageListener ?= null
): RecyclerView.ViewHolder(itemUploadImageBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(image: UploadImage){
        if(image.isFirstElement){
            itemUploadImageBinding.uploadIv.setImageResource(R.drawable.ic_upload_photo)
        }
        else{
            Picasso.get().load(image.imageUri).fit().centerCrop().placeholder(R.drawable.img_aktos)
                .into(itemUploadImageBinding.uploadIv)
            itemUploadImageBinding.deleteBtn.visibility = View.VISIBLE
            itemUploadImageBinding.deleteBtn.setOnClickListener {
                uploadNewImageListener?.deleteImg(image)
            }
        }
        itemView.setOnClickListener {
            uploadNewImageListener?.upload(image.isFirstElement)
        }
    }
}

interface UploadNewImageListener {
    fun upload(isFirstElement: Boolean)
    fun deleteImg(image: UploadImage)
}