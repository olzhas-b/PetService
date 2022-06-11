/*
 * *
 *  * Created by Ali Ashkeyev on 26.02.2022, 18:48
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 26.02.2022, 18:48
 *
 */

package com.example.budka.view

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
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
import com.example.budka.databinding.FragmentUpdateProfileBinding
import com.example.budka.utils.FileUtils
import com.example.budka.viewModel.CountriesListViewModel
import com.example.budka.viewModel.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_sitter_detail.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ChangeProfileFragment: Fragment() {
    private var _viewBinding: FragmentUpdateProfileBinding? = null
    private val viewBinding get() = _viewBinding!!
    val arg: ChangeProfileFragmentArgs by navArgs()
    private val countriesListViewModel: CountriesListViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()
    val REQUEST_CODE = 200
    private  val REQUEST_STORAGE_PERMISSION = 1
    private val PERMISSION_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var imageUri: Uri? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(hasStoragePermission()) {
            savePhotoFromUrl()
        }
        else {
            requestStoragePermission()
        }
        autoFillFields(arg.profile)
        setObservers()
        setListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imageUri = data?.data
            Picasso.get().load(imageUri).fit().centerCrop().into(viewBinding.userAvatar)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    @SuppressLint("SetTextI18n")
    private fun autoFillFields(user: User){
        user.apply {
            viewBinding.apply {
                updateNameEt.setText(firstName)
                updateSurnameEt.setText(lastName)
                updatePhoneEt.setText(phone)
                updateDescriptionEt.setText(description)
                countriesEdV.setText(country)
                cityEdV.setText(city)
                Picasso.get().load(avatar).fit().centerCrop().into(userAvatar)
            }
        }
    }

    private fun setListeners(){

        viewBinding.updatePhoto.setOnClickListener {
            if(hasStoragePermission()) {
                openFileExplorer()
            }
            else {
                requestStoragePermission()
            }
        }
        viewBinding.confirmBtn.setOnClickListener {
            viewBinding.apply {
                 val user =UserUpdate(
                    firstName = updateNameEt.text.toString(),
                    lastName = updateSurnameEt.text.toString(),
                    fullName = updateNameEt.text.toString() +" "+ updateSurnameEt.text.toString(),
                    city = cityEdV.text.toString(),
                    country = countriesEdV.text.toString(),
                    description = updateDescriptionEt.text.toString()
                )
                val image = imageUri?.let { it1 -> prepareFilePart("image", it1) }

                if (image != null) {
                    profileViewModel.updateProfile(image, user).observe(viewLifecycleOwner, { result ->
                        result.doIfSuccess {
                            (activity as MainActivity).showProgressBar()
                            val successDialog = AlertDialog.Builder(requireContext())
                            successDialog.setIcon(R.drawable.ic_baseline_check_24)
                            successDialog.setTitle("Профиль успешно обновлён!")
                            successDialog.setPositiveButton(
                                "OK"
                            ) {_,_ ->
                                findNavController().navigate(ChangeProfileFragmentDirections.actionChangeProfileFragmentToProfileFragment())
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

                    })
                }
            }
        }
    }


    private fun setObservers(){
        countriesListViewModel.fetchCountryList().observe(viewLifecycleOwner, Observer {
            setCountries(it)
        })
    }
    private fun setCountries(countries: List<CountryData>){
        val country = mutableListOf<String>()
        for(cc in countries){
            country.add(cc.country)
        }
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_country,
            R.id.text_view_country_item,
            country
        )
        val cityAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_city,
            R.id.text_view_city_item
        )

        viewBinding.countriesEdV.setAdapter(adapter)
        viewBinding.countriesEdV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                cityAdapter.clear()
                countries.firstOrNull {
                    it.country == p0.toString()
                }?.let { cityAdapter.addAll(it.cities) }
            }
        })

        viewBinding.cityEdV.setAdapter(cityAdapter)
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
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.R){
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(String.format("package:%s", requireContext().applicationContext.packageName))
                    startActivityForResult(intent, REQUEST_STORAGE_PERMISSION)
                } catch (e: Exception){
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivityForResult(intent, REQUEST_STORAGE_PERMISSION)
                }
            }else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    REQUEST_STORAGE_PERMISSION
                )
            }
        }
    }


    private fun hasStoragePermission(): Boolean {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager()
        } else{
            val result = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

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

    fun savePhotoFromUrl(){
        val job = Job()
        val scopeForSaving = CoroutineScope(job + Dispatchers.Main)
        arg.profile.avatar?.let { avatar ->
            if(avatar.length>5) {
                val url = URL(avatar)
                val num = avatar.substring(avatar.lastIndexOf('/') + 1)
                scopeForSaving.launch {
                    saveToStorage(url, arg.profile.fullName + num)
                }
            }
        }

    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part{
        val file = FileUtils.getFile(requireContext(), fileUri)
        val requestFile = RequestBody.create(MediaType.parse(requireContext().contentResolver.getType(fileUri)), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
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
}