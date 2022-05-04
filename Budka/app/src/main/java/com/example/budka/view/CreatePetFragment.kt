/*
 * *
 *  * Created by Ali Ashkeyev on 13.02.2022, 15:45
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.02.2022, 15:45
 *
 */

package com.example.budka.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.CreatePetFragmentBinding
import com.example.budka.databinding.CreateServiceRequiredFragmentBinding
import com.example.budka.utils.FileUtils
import com.example.budka.viewModel.CountriesListViewModel
import com.example.budka.viewModel.PetsListViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class CreatePetFragment : Fragment() {
    val REQUEST_CODE = 200
    private  val REQUEST_STORAGE_PERMISSION = 1
    private var _viewBinding: CreatePetFragmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val petsListViewModel: PetsListViewModel by viewModel()
    private var imageUri: Uri? = null
    val args: CreatePetFragmentArgs by navArgs()
    private lateinit var petObserver: Observer<NetworkResult<Pet>>





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = CreatePetFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imageUri = data?.data
            Picasso.get().load(imageUri).into(viewBinding.uploadIv)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setAdapters()
        setListeners()
        args.pet?.image?.let {
            savePhotoFromUrl()
            Picasso.get().load(args.pet?.image).into(viewBinding.uploadIv)

        }?:run{
            viewBinding.uploadIv.setImageResource(R.drawable.ic_upload_photo)
        }
        viewBinding.petNameEt.setText(args.pet?.name)
        viewBinding.petBreedEt.setText(args.pet?.breed)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setObservers() {
        petObserver = Observer { result ->
            result.doIfSuccess {
                (activity as MainActivity).showProgressBar()
                val successDialog = AlertDialog.Builder(requireContext())
                successDialog.setIcon(R.drawable.ic_baseline_check_24)
                successDialog.setTitle("Данные сохранены!")
                successDialog.setPositiveButton(
                    "OK"
                ) {_,_ ->
                    findNavController().navigate(CreatePetFragmentDirections.actionCreatePetFragmentToProfileFragment())
                }
                successDialog.create()
                successDialog.show()
            }
            result.doIfFailure{ error, data ->
                (activity as MainActivity).showProgressBar()
                error?.let{(activity as MainActivity).showAlert(it)}

            }

            if(result is NetworkResult.Loading){
                (activity as MainActivity).showProgressBar(true)

            }

        }

        }


    private fun setListeners() {
        viewBinding.cardView.setOnClickListener {
            if(hasStoragePermission()) {
                    openFileExplorer()
            }
            else {
                requestStoragePermission()
            }
        }
        viewBinding.confirmButton.setOnClickListener {

            val petSize =
                when(viewBinding.petSizeSp.selectedItem){
                    "меньше 5кг" -> 5
                    "меньше 10кг"-> 10
                    "меньше 20кг"->20
                    "меньше 30кг"->30
                    "меньше 40кг"->40
                    "больше 40кг"->50
                    else -> null
                }
            val pet = PetCreate(name = viewBinding.petNameEt.text.toString(),
                type = viewBinding.petTypeSp.selectedItem.toString(),
                breed = viewBinding.petBreedEt.text.toString(),
                weight = petSize)

            val image = imageUri?.let { it1 -> prepareFilePart("image", it1) }

            if (image != null) {
                if(args.operationType == "update")
                    args.pet?.let { it1 -> petsListViewModel.updatePet(image, pet, it1.id).observe(
                        viewLifecycleOwner, petObserver
                    ) }
                else
                    if(validateFields())
                        petsListViewModel.createPet(image, pet).observe(viewLifecycleOwner, petObserver)
                    else{
                        val errorDialog = AlertDialog.Builder(requireContext())
                        errorDialog.setIcon(R.drawable.ic_baseline_error_24)
                        errorDialog.setTitle("Заполните поля")
                        errorDialog.setPositiveButton(
                            "Вернуться"
                        ) { dialog, _ ->

                            dialog.cancel()
                        }
                        errorDialog.create()
                        errorDialog.show()
                    }
            }


        }


    }

    private fun setAdapters() {
        setPetTypes()
        setPetSize()
    }



    private fun setPetTypes() {
        val petTypeList = mutableListOf<String>()
        for (pet in PetType.values()) {
            petTypeList.add(pet.value)
        }
        val typeAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_pet_type_filter, R.id.text_view_pet_type_item, petTypeList
        )
        viewBinding.petTypeSp.adapter = typeAdapter

        args.pet?.let { pet->
            val position = PetType.values().find { it.value == pet.type }?.ordinal
            if (position != null) {
                viewBinding.petTypeSp.setSelection(position)
            }
        }

    }

    private fun setPetSize() {
        val petSizeList = mutableListOf<String>()
        petSizeList.add("меньше 5кг")
        petSizeList.add("меньше 10кг")
        petSizeList.add("меньше 20кг")
        petSizeList.add("меньше 30кг")
        petSizeList.add("меньше 40кг")
        petSizeList.add("больше 40кг")
        val petSizeAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_pet_type_filter, R.id.text_view_pet_type_item, petSizeList
        )
        viewBinding.petSizeSp.adapter = petSizeAdapter
        when (args.pet?.weight) {
            5 -> 0
            10 -> 1
            20 -> 2
            30 -> 3
            40 -> 4
            50 -> 5
            else -> null
        }?.let {
            viewBinding.petSizeSp.setSelection(
                it
            )
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireActivity().packageName}")).apply {
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

    private fun requestStoragePermission() {
        if (!hasStoragePermission()) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_STORAGE_PERMISSION)
        }
    }

    private fun hasStoragePermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED


    private fun openFileExplorer(){
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures")
                , REQUEST_CODE
            )
        }
        else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part{
        val file = FileUtils.getFile(requireContext(), fileUri)
        val requestFile = RequestBody.create(MediaType.parse(requireContext().contentResolver.getType(fileUri)), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    fun savePhotoFromUrl(){
        val job = Job()
        val scopeForSaving = CoroutineScope(job + Dispatchers.Main)
        args.pet.let { pet ->
                val url = URL(pet?.image)
                val num = pet?.image?.substring(pet.image.lastIndexOf('/') + 1)
            scopeForSaving.launch {
                saveToStorage(url, pet?.name + num)
            }
            }
        }
    private suspend fun saveToStorage(url: URL, imageName: String) {

        withContext(Dispatchers.IO){
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            val path: File =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "Budka") //Creates app specific folder

            path.mkdirs()
            val imageFile = File(path, "$imageName.png") // Imagename.png

            val out = FileOutputStream(imageFile)
            try {
                image.compress(Bitmap.CompressFormat.PNG, 100, out) // Compress Image
                out.flush()
                out.close()

                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(
                    context, arrayOf(imageFile.getAbsolutePath()), null
                ) { path, uri ->
                    imageUri = uri
                }
            } catch (e: Exception) {
                Timber.d(e.toString())
            }

        }
    }

    private fun validateFields(): Boolean {
        val name = viewBinding.petNameEt.text.toString()

        val petType = viewBinding.petTypeSp.selectedItem.toString()
        val breed = viewBinding.petBreedEt.text.toString()
        val petSize = viewBinding.petSizeSp.selectedItem.toString()

        return name!=""&&petSize!=""&&petType!=""&&petSize!=""&&breed!=""
    }

    }



