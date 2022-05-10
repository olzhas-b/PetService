/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 20:34
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 20:34
 *
 */

package com.example.budka.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.data.model.Properties
import com.example.budka.databinding.CreateServiceOptionalFragmentBinding
import com.example.budka.utils.FileUtils
import com.example.budka.view.adapter.AddPropertiesAdapter
import com.example.budka.view.adapter.UploadImageAdapter
import com.example.budka.view.adapter.viewHolder.EditTextChangeListener
import com.example.budka.view.adapter.viewHolder.UploadNewImageListener
import com.example.budka.viewModel.ServicesViewModel
import com.example.budka.viewModel.createServiceViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class CreateServiceOptionalFragment : Fragment(), UploadNewImageListener, EditTextChangeListener {
    val REQUEST_CODE = 200
    private  val REQUEST_STORAGE_PERMISSION = 1
    private var _viewBinding: CreateServiceOptionalFragmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var uploadImageAdapter: UploadImageAdapter
    private lateinit var addPropertiesAdapter: AddPropertiesAdapter
    private var imageList = mutableListOf<UploadImage>()
    private var imagesToService = mutableListOf<MultipartBody.Part>()
    private var propertiesList = mutableListOf<Properties>()
    val sendPropertiesMap = mutableMapOf<String, String?>()
    val sendPropertiesList = mutableListOf<Properties>()
    var ss = ""
    val arg: CreateServiceOptionalFragmentArgs by navArgs()
    private val servicesViewModel: ServicesViewModel by viewModel()
    private var additionalPropertiesList = MutableLiveData<PropertiesList>()
    private var imageListLiveData = MutableLiveData<UriList>()
    private val createSerViewModel: createServiceViewModel by activityViewModels()
    private lateinit var serviceObserver: Observer<NetworkResult<String>>







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
//        arg.imageList.let {
//            imageListLiveData.value = it
//        }

//        arg.propertiesList?.let {
//           additionalPropertiesList.value = it
//        } ?:run{
//            populateProperty(arg.requiredField.serviceType)
//            addPropertiesAdapter.updatePropertiesList(propertiesList)
//
//        }




        setupAdapter()
        setObservers()
        populateProperty(arg.requiredField.serviceType)
        addPropertiesAdapter.updatePropertiesList(propertiesList)
        uploadImageAdapter.updateImageList(imageList)
//        uploadImageAdapter.updateImageList(imageList)
        viewBinding.check.setOnClickListener {
            serverData()
        }
    }

    fun setObservers() {
        serviceObserver = Observer { result ->
            result.doIfSuccess {
                (activity as MainActivity).showProgressBar()
                val successDialog = AlertDialog.Builder(requireContext())
                successDialog.setIcon(R.drawable.ic_baseline_check_24)
                successDialog.setTitle("Данные сохранены!")
                successDialog.setPositiveButton(
                    "OK"
                ) {_,_ ->
                    findNavController().navigate(CreateServiceOptionalFragmentDirections.actionCreateServiceOptionalFragmentToProfileFragment())
                }
                successDialog.create()
                successDialog.show()
            }
            result.doIfFailure{ error, data ->
                if (error != null) {
                    if(error.contains("401")){
                        showLogin()
                    }else {
                        (activity as MainActivity).showProgressBar()
                        error.let { (activity as MainActivity).showAlert(it) }
                    }
                }
            }

            if(result is NetworkResult.Loading){
                (activity as MainActivity).showProgressBar(true)

            }

        }
        additionalPropertiesList.observe(viewLifecycleOwner, {
            propertiesList.clear()
            it.propertiesList?.let { it1 -> propertiesList.addAll(it1) }
            addPropertiesAdapter.updatePropertiesList(propertiesList)

        })

        imageListLiveData.observe(viewLifecycleOwner, {
            it.uriList?.let { it1 -> imageList.addAll(it1) }
            uploadImageAdapter.updateImageList(imageList)
        })

        createSerViewModel.getImages().observe(viewLifecycleOwner,
            {
                it?.let {
                    imageList.addAll(it)
                }
                uploadImageAdapter.updateImageList(imageList)
            })

        createSerViewModel.getProperties().observe(viewLifecycleOwner,
            {
                it?.let {
                    propertiesList.clear()
                    propertiesList.addAll(it)
                }?:run{
                    populateProperty(arg.requiredField.serviceType)
                }
                addPropertiesAdapter.updatePropertiesList(propertiesList)
            })
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

    private fun populateProperty(serviceType: Int) {
        when(serviceType){
            1->{
                propertiesList.clear()
                propertiesList.add(Properties("Количество питомцев", null ))
                propertiesList.add(Properties("Прогулки в день", null))
                propertiesList.add(Properties("Тип недвижимости", null))
                propertiesList.add(Properties("Аварийный транспорт", null))
            }

            2->{
                propertiesList.clear()
                propertiesList.add(Properties("Длительность прогулки", null ))
            }
            3->{
                propertiesList.clear()
            }
            4->{
                propertiesList.clear()
                propertiesList.add(Properties("Квалификация", null ))
                propertiesList.add(Properties("Количество питомцев", null ))
                propertiesList.add(Properties("Методы дрессировки", null ))
                propertiesList.add(Properties("Как скоро ожидать результат", null ))
            }
            5->{
                propertiesList.clear()
            }
            6->{
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
        if(hasStoragePermission()) {
            if (isFirstElement) {
                openFileExplorer()
            }
        }
        else {
            requestStoragePermission()
        }
    }

    override fun deleteImg(image: UploadImage) {
        imageList.remove(image)
        uploadImageAdapter.updateImageList(imageList)
    }

    override fun changeText(property: Properties) {
//        sendPropertiesList.add(property)
        sendPropertiesMap[property.label!!] = property.text
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    openFileExplorer()
                }
                else {
                    val showRational =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )

                    if (showRational) {
                        Timber.d("Storage permission denied")
                    }
                    else {
                        Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireActivity().packageName}")).apply {
                            addCategory(Intent.CATEGORY_DEFAULT)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }.also { intent ->
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun serverData() {
        val requiredField = arg.requiredField
        val petSize =
            when(requiredField.petSize){
                "меньше 5кг" -> 5
                "меньше 10кг"-> 10
                "меньше 20кг"->20
                "меньше 30кг"->30
                "меньше 40кг"->40
                "больше 40кг"->50
                else -> null
            }

        val localCurrency = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Currency.getInstance(resources.configuration.locales[0]).toString()
        } else {
            "KZT"
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d("myCurrency", Currency.getInstance(resources.configuration.locales[0]).toString())
        }


        val createServiceModel = CreateServiceModel(
            serviceType = requiredField.serviceType,
            price = requiredField.servicePrice,
            currencyCode = requiredField.currencyCode,
            pricePerTime = requiredField.pricePerTime,
            longitude = requiredField.longitude.toDouble(),
            latitude = requiredField.latitude.toDouble(),
            description = requiredField.summary,
            acceptablePets = requiredField.petTypes,
            acceptableSize = petSize,
            additionalProperties = sendPropertiesMap.toList().map { Properties(it.first, it.second) }
        )

        for(image in imageList){
            image.imageUri?.let {
                imagesToService.add(prepareFilePart("images", it))
            }
        }
        if(arg.operationType == "update")
            arg.requiredField.serviceId?.let { servicesViewModel.updateService(imagesToService, createServiceModel, it).observe(viewLifecycleOwner, serviceObserver) }
        else
            servicesViewModel.createService(imagesToService, createServiceModel).observe(viewLifecycleOwner, serviceObserver)
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part{
        val file = FileUtils.getFile(requireContext(), fileUri)
        val requestFile = RequestBody.create(MediaType.parse(requireContext()
            .contentResolver.getType(fileUri)), file)
        val fileName = if(file.name.length>=40) file.name.substring(0..39) else file.name
        return MultipartBody.Part.createFormData(partName, fileName, requestFile)
    }


    private fun requestStoragePermission() {
        if (!hasStoragePermission()) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_STORAGE_PERMISSION)
        }
    }

    private fun hasStoragePermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PERMISSION_GRANTED


    private fun openFileExplorer(){
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

    private fun showLogin(){
        val errorDialog = AlertDialog.Builder(requireContext())
        errorDialog.setIcon(R.drawable.ic_baseline_error_24)
        errorDialog.setTitle("Войдите пожалуйста в аккаунт")
        errorDialog.setNegativeButton(
            "Вернуться"
        ) { dialog, _ ->

            dialog.cancel()
        }
        errorDialog.setPositiveButton(
            "Войти",
        ){dialog, _ ->
            val intent = Intent(requireContext(), LoginActivity::class.java)
            this.startActivity(intent)
            dialog.dismiss()
        }
        errorDialog.create()
        errorDialog.show()
    }
}