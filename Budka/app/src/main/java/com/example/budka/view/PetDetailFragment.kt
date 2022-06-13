/*
 * *
 *  * Created by Ali Ashkeyev on 22.03.22 20:40
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 22.03.22 20:40
 *
 */

package com.example.budka.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.FragmentPetDetailBinding
import com.example.budka.utils.GenericFileProvider
import com.example.budka.utils.WebDownloadSource
import com.example.budka.view.adapter.*
import com.example.budka.view.adapter.viewHolder.PdfActionListener
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ProfileViewModel
import com.example.budka.viewModel.ServiceDetailViewModel
import com.example.budka.viewModel.ServicesViewModel
import com.pspdfkit.document.download.DownloadJob
import com.pspdfkit.document.download.DownloadRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pet_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.net.MalformedURLException
import java.net.URL

class PetDetailFragment: Fragment(), PdfActionListener {
    private lateinit var pet: Pet
    private lateinit var viewBinding: FragmentPetDetailBinding
    private lateinit var otherPropertiesAdapter: OtherPropertiesAdapter
    private lateinit var userPetsAdapter: UserPetsAdapter
    private lateinit var albumViewPagerAdapter: AlbumViewPagerAdapter
    private lateinit var docsAdapter: UploadPdfAdapter
    private val arg: PetDetailFragmentArgs by navArgs()
    private val profileViewModel: ProfileViewModel by viewModel()
    private val petsListViewModel: PetsListViewModel by viewModel()
    private var pdfList = mutableListOf<UploadImage>()
    private val REQUEST_STORAGE_PERMISSION = 1




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPetDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()

        arg.pet?.let {
            pet = it
            if(savedInstanceState==null){
                profileViewModel.fetchProfile(pet.userID)
            }
            viewBinding.profileLayout.setOnClickListener {
                findNavController().navigate(PetDetailFragmentDirections.actionPetDetailFragmentToUserProfileFragment(pet.userID))
            }
            setObservers()
            saveDocumentsFromUrl()


        }?: run{
            petsListViewModel.getPetDetail(arg.petId).observe(viewLifecycleOwner) { result ->
                result.doIfSuccess {
                    if (it != null) {
                        pet = it
                        if (savedInstanceState == null) {
                            profileViewModel.fetchProfile(pet.userID)
                        }
                        viewBinding.profileLayout.setOnClickListener {
                            findNavController().navigate(
                                PetDetailFragmentDirections.actionPetDetailFragmentToUserProfileFragment(
                                    pet.userID
                                )
                            )
                        }
                        setObservers()
                        if(hasStoragePermission()) {
                            saveDocumentsFromUrl()
                        }
                        else {
                            requestStoragePermission()
                        }
                    }
                }
                result.doIfFailure { error, data ->
                    error?.let { (activity as MainActivity).showAlert(it) }
                }

            }
        }

    }



    @SuppressLint("SetTextI18n")
    private fun setObservers(){

        albumViewPagerAdapter.updateImageList(listOf(pet.image))
        profileViewModel.getProfile().observe(viewLifecycleOwner, {result->
            result.doIfSuccess {
                Picasso.get().load(it?.avatar).fit().centerCrop().placeholder(R.drawable.img_aktos).into(viewBinding.userAvatar)
                viewBinding.petSitterNameTv.text = it?.fullName
            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }
            }
        })

        val weight = when(pet.weight){
            5 -> "меньше 5кг"
            10 -> "меньше 10кг"
            20 -> "меньше 20кг"
            30 -> "меньше 30кг"
            40 -> "меньше 40кг"
            50 -> "больше 40кг"
            else -> null
        }


        val propertiesList = mutableListOf<Properties>()
        propertiesList.add(Properties(label = "Имя", text = pet.name))
        propertiesList.add(Properties(label = "Вид", text = pet.type))
        propertiesList.add(Properties(label = "Порода", text = pet.breed))
        propertiesList.add(Properties(label = "Истечение вакцинации", text = pet.expireDate))
        propertiesList.add(Properties(label = "Вес", text = weight))
        pet.count?.let{if (it>1) propertiesList.add(Properties(label = "Количество", text = it.toString()))}
        otherPropertiesAdapter.updatePropertiesList(propertiesList)

    }


    private fun setUpAdapter(){

        otherPropertiesAdapter = OtherPropertiesAdapter()
        albumViewPagerAdapter = AlbumViewPagerAdapter()
        viewBinding.albumVp.adapter = albumViewPagerAdapter
        docsAdapter = UploadPdfAdapter(this, isCreatePage = false)


        val otherPropertiesLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false )
        otherPropertiesRv.layoutManager = otherPropertiesLayoutManager
        otherPropertiesRv.adapter = otherPropertiesAdapter
        otherPropertiesRv.setHasFixedSize(true)
        otherPropertiesRv.setItemViewCacheSize(20)
        otherPropertiesRv.isNestedScrollingEnabled =  false

        documentsRv.adapter = docsAdapter
        documentsRv.setHasFixedSize(true)
        documentsRv.setItemViewCacheSize(20)
        documentsRv.isNestedScrollingEnabled =  false

    }

    private fun saveDocumentsFromUrl(){

        pet.docs.let { docs ->
            docs?.forEach {doc ->
                val source: WebDownloadSource = try {
                    // Try to parse the URL pointing to the PDF document. If an error occurs, log it and leave the example.
                    WebDownloadSource(URL(doc.url))
                } catch (e: MalformedURLException) {
                    Log.e("PDFDownloadError", "Error while trying to parse the PDF Download URL.", e)
                    return
                }
                val docName = doc.name
                // Build a download request based on various input parameters. Provide the web source pointing to the document.
                val request = DownloadRequest.Builder(requireContext())
                    .source(source)
                    .outputFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$docName"))
                    .overwriteExisting(true)
                    .build()
                val asyncJob = Job()
                val scopeForSaving = CoroutineScope(asyncJob + Dispatchers.Main)
                scopeForSaving.launch {
                    val job = DownloadJob.startDownload(request)
                    job.setProgressListener(object : DownloadJob.ProgressListenerAdapter() {
                        override fun onComplete(output: File) {
                            pdfList.add(UploadImage(
                                GenericFileProvider.getUriForFile(requireContext(),
                                requireContext().applicationContext.packageName, output), false))
                            docsAdapter.updatePdfList(pdfList)
                        }

                        override fun onError(exception: Throwable) {
                            Timber.d(exception.toString())
                    }
            })
        }

    }
        }
    }

    private fun hasStoragePermission(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager()
        } else{
            val result = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
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

    override fun upload(isFirstElement: Boolean, image: UploadImage) {
        requestStoragePermission()
        val target =  Intent(Intent.ACTION_VIEW)
        target.setDataAndType(image.imageUri, "application/pdf")
        target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intent = Intent.createChooser(target, "Открыть файл")
        try {
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException){
            val toast = Toast.makeText(requireContext(), "Установите приложение для PDF", Toast.LENGTH_LONG )
            toast.show()
        }
    }

    override fun deletePdf(image: UploadImage) {
    }
}