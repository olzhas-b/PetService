/*
 * *
 *  * Created by Ali Ashkeyev on 06.06.2022, 17:04
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 06.06.2022, 17:04
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.UploadImage
import com.example.budka.databinding.ItemUploadPdfBinding
import com.example.budka.databinding.SliderLayoutBinding
import com.pspdfkit.document.PdfDocumentLoader
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso

class SliderViewHolder (
    val sliderLayoutBinding: SliderLayoutBinding
): SliderViewAdapter.ViewHolder(sliderLayoutBinding.root) {

    fun setUp(image: Int) {
        Picasso.get().load(image).fit().centerInside().placeholder(R.drawable.banner)
            .into(sliderLayoutBinding.infoPageIv)
        }

}
