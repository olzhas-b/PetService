/*
 * *
 *  * Created by Ali Ashkeyev on 17.05.2022, 14:14
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 17.05.2022, 14:14
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.R
import com.example.budka.data.model.UploadImage
import com.example.budka.databinding.ItemUploadImageBinding
import com.example.budka.databinding.ItemUploadPdfBinding
import com.pspdfkit.document.PdfDocumentLoader
import com.squareup.picasso.Picasso
import java.io.File
import java.lang.Exception

class UploadPdfViewHolder (
    val itemUploadPdfBinding: ItemUploadPdfBinding,
    var pdfActionListener: PdfActionListener ?= null,
    var context: Context,
    var isCreatePage: Boolean? = true

): RecyclerView.ViewHolder(itemUploadPdfBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(image: UploadImage) {
        if (image.isFirstElement) {
            itemUploadPdfBinding.uploadIv.setImageResource(R.drawable.ic_upload_photo)
            itemUploadPdfBinding.deleteBtn.visibility = View.GONE
            itemUploadPdfBinding.fileNameTv.visibility = View.GONE

        } else {
            val document = image.imageUri?.let { PdfDocumentLoader.openDocument(context, it) }
            val pageIndex = 0
            val pageImageSize = document?.getPageSize(pageIndex)?.toRect()

            // Set the thumbnail size to be five times smaller than the actual page size.
            val thumbnailImageSize =
                pageImageSize?.let{
                    RectF(0f, 0f, pageImageSize.width() / 5, pageImageSize.height() / 5)
                }
            // Create the image
            val bitmap = thumbnailImageSize?.let {
                document.renderPageToBitmap(
                    context,
                    pageIndex,
                    it. width ().toInt(),
                it.height().toInt()
                )
            }
            itemUploadPdfBinding.uploadIv.setImageBitmap(bitmap)
            itemUploadPdfBinding.fileNameTv.text = image.fileName
            itemUploadPdfBinding.fileNameTv.visibility = View.VISIBLE
            itemUploadPdfBinding.deleteBtn.visibility = if(isCreatePage == true) View.VISIBLE else View.GONE
                itemUploadPdfBinding.deleteBtn.setOnClickListener {
                    pdfActionListener?.deletePdf(image)
                }
            }
        itemView.setOnClickListener {
            pdfActionListener?.upload(image.isFirstElement, image)
        }
    }
}

interface PdfActionListener {
    fun upload(isFirstElement: Boolean, image: UploadImage)
    fun deletePdf(image: UploadImage)
}