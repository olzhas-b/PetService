/*
 * *
 *  * Created by Ali Ashkeyev on 14.01.22 18:30
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 14.01.22 18:30
 *
 */

package com.example.budka.view.adapter.viewHolder

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.example.budka.data.model.Properties
import com.example.budka.databinding.ItemCreateOtherPropertiesBinding
import com.example.budka.databinding.ItemOtherPropertiesBinding

class AddPropertiesViewHolder constructor(
    val otherPropertiesBinding: ItemCreateOtherPropertiesBinding,
    var editTextChangeListener: EditTextChangeListener ?= null

): RecyclerView.ViewHolder(otherPropertiesBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setUp(properties: Properties){
        otherPropertiesBinding.labelTv.text = properties.label
        properties.text?.let{
            otherPropertiesBinding.propertyEt.setText(it)
        }
        otherPropertiesBinding.propertyEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                editTextChangeListener?.changeText(Properties(properties.label, p0.toString()))

            }
        })

    }
}

interface EditTextChangeListener {
    fun changeText(property: Properties)
}