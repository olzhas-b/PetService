/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 20:34
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 20:34
 *
 */

package com.example.budka.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.data.model.Properties
import com.example.budka.data.model.Services
import com.example.budka.data.model.UploadImage
import com.example.budka.databinding.CreateServiceOptionalFragmentBinding
import com.example.budka.view.adapter.AddPropertiesAdapter
import com.example.budka.view.adapter.PetSittersListHorizontalAdapter
import com.example.budka.view.adapter.PetsListHorizontalAdapter
import com.example.budka.view.adapter.UploadImageAdapter
import com.example.budka.view.adapter.viewHolder.EditTextChangeListener
import com.example.budka.view.adapter.viewHolder.UploadNewImageListener
import com.example.budka.viewModel.CreateServiceViewModel
import com.example.budka.viewModel.PetsListViewModel
import kotlinx.android.synthetic.main.create_service_optional_fragment.*
import kotlinx.android.synthetic.main.main_page_fragment.*
import kotlinx.android.synthetic.main.main_page_fragment.mainPagePetsRv
import org.koin.android.viewmodel.ext.android.viewModel

class CreateServiceOptionalFragment : Fragment(), UploadNewImageListener, EditTextChangeListener {
    val REQUEST_CODE = 200
    private var _viewBinding: CreateServiceOptionalFragmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var uploadImageAdapter: UploadImageAdapter
    private lateinit var addPropertiesAdapter: AddPropertiesAdapter
    private val createServiceViewModel: CreateServiceViewModel by viewModel()
    private var imageList = mutableListOf<UploadImage>()
    private var propertiesList = mutableListOf<Properties>()
    val sendPropertiesMap = mutableMapOf<String, String?>()
    var ss = ""
    val arg: CreateServiceOptionalFragmentArgs by navArgs()




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageUri: Uri?

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){


            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData?.itemCount

                if (count != null) {
                    for (i in 0 until count) {
                        imageUri = data.clipData?.getItemAt(i)?.uri
                        imageList.add(UploadImage(imageUri, false))
                        //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                    }
                }

            } else if (data?.getData() != null) {
                // if single image is selected
                     imageUri = data.data
                imageList.add(UploadImage(imageUri, false))
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview
            }
            uploadImageAdapter.updateImageList(imageList)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = CreateServiceOptionalFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageList.add(UploadImage(null, true))
        populateProperty(arg.requiredField.serviceType)


        setupAdapter()
        uploadImageAdapter.updateImageList(imageList)
        addPropertiesAdapter.updatePropertiesList(propertiesList)
        viewBinding.check.setOnClickListener {
            Log.d("properties", sendPropertiesMap.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setupAdapter() {
        uploadImageAdapter = UploadImageAdapter(uploadNewImageListener = this)
        addPropertiesAdapter = AddPropertiesAdapter(editTextChangeListener = this)
        val imageLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val propertiesLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewBinding.uploadImageRv.layoutManager = imageLayoutManager
        viewBinding.uploadImageRv.adapter = uploadImageAdapter
        viewBinding.uploadImageRv.setHasFixedSize(true)
        viewBinding.uploadImageRv.setItemViewCacheSize(20)
        viewBinding.uploadImageRv.isNestedScrollingEnabled = false

        viewBinding.otherPropertiesRv.layoutManager = propertiesLayoutManager
        viewBinding.otherPropertiesRv.adapter = addPropertiesAdapter
        viewBinding.otherPropertiesRv.setHasFixedSize(true)
        viewBinding.otherPropertiesRv.setItemViewCacheSize(20)
        viewBinding.otherPropertiesRv.isNestedScrollingEnabled = false
    }

    private fun populateProperty(serviceType: String) {
        when(serviceType){
            "Зооняни"->{
                propertiesList.clear()
                propertiesList.add(Properties("Количество питомцев", null ))
                propertiesList.add(Properties("Прогулки в день", null))
                propertiesList.add(Properties("Тип недвижимости", null))
                propertiesList.add(Properties("Аварийный транспорт", null))
            }

            "Выгул"->{
                propertiesList.clear()
                propertiesList.add(Properties("Длительность прогулки", null ))
            }
            "Ветеринары"->{
                propertiesList.clear()
            }
            "Дрессировка"->{
                propertiesList.clear()
                propertiesList.add(Properties("Квалификация", null ))
                propertiesList.add(Properties("Количество питомцев", null ))
                propertiesList.add(Properties("Методы дрессировки", null ))
                propertiesList.add(Properties("Как скоро ожидать результат", null ))
            }
            "Груминг"->{
                propertiesList.clear()
            }
            "Зоогостиницы"->{
                propertiesList.clear()
                propertiesList.add(Properties("Количество питомцев", null ))
                propertiesList.add(Properties("Прогулки в день", null))
                propertiesList.add(Properties("Тип недвижимости", null))
                propertiesList.add(Properties("Аварийный транспорт", null))
            }
            else ->{
                propertiesList.clear()
            }

        }

        propertiesList.add(Properties("Выезд", null))
//        propertiesList.add(Properties("Последняя активность", null))
    }

    override fun upload(isFirstElement: Boolean) {
        if (isFirstElement){
            if (Build.VERSION.SDK_INT < 19) {
                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Choose Pictures")
                    , REQUEST_CODE
                )
            }
            else { // For latest versions API LEVEL 19+
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    override fun changeText(property: Properties) {
        sendPropertiesMap[property.label!!] = property.text
    }

}