/*
 * *
 *  * Created by Ali Ashkeyev on 13.02.2022, 15:45
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.02.2022, 15:45
 *
 */

package com.example.budka.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.CreatePetFragmentBinding
import com.example.budka.utils.FileUtils
import com.example.budka.view.adapter.UploadPdfAdapter
import com.example.budka.view.adapter.viewHolder.PdfActionListener
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
import com.pusher.pushnotifications.PushNotifications;
import android.provider.OpenableColumns
import android.util.Log
import android.widget.*
import androidx.core.content.FileProvider.getUriForFile
import com.example.budka.utils.GenericFileProvider
import com.example.budka.utils.WebDownloadSource
import com.pspdfkit.document.download.DownloadJob
import com.pspdfkit.document.download.DownloadRequest
import com.pspdfkit.internal.utilities.a.getUriForFile
import java.net.MalformedURLException
import java.text.SimpleDateFormat
import java.util.*

class CreatePetFragment : Fragment(), PdfActionListener {
    val REQUEST_CODE = 200
    val REQUEST_PDFCODE = 12
    private val REQUEST_STORAGE_PERMISSION = 1
    private var _viewBinding: CreatePetFragmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val petsListViewModel: PetsListViewModel by viewModel()
    private var imageUri: Uri? = null
    private var pdfUri: Uri? = null
    val args: CreatePetFragmentArgs by navArgs()
    private lateinit var petObserver: Observer<NetworkResult<Pet>>
    private var pdfList = mutableListOf<UploadImage>()
    private lateinit var uploadPdfAdapter: UploadPdfAdapter
    val myCalendarEnd = Calendar.getInstance()
    private var pdfToPet = mutableListOf<MultipartBody.Part>()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = CreatePetFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            imageUri = data?.data
            Picasso.get().load(imageUri).into(viewBinding.uploadIv)

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PDFCODE) {
            pdfUri = data?.data
            if (data?.getClipData() != null) {
                var count = data.clipData?.itemCount
                if (count != null) {
                    for (i in 0 until count) {
                        pdfUri = data.clipData?.getItemAt(i)?.uri
                        if (pdfUri.toString().startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor =
                                    pdfUri?.let { requireActivity().contentResolver.query(it, null, null, null, null) }
                                if (cursor != null && cursor.moveToFirst()) {
                                    val displayName =
                                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    pdfList.add(UploadImage(pdfUri, false,displayName, true))
                                }
                            } finally {
                                cursor?.close()
                            }
                        }
                        else {
                            pdfList.add(UploadImage(pdfUri, false, isModified = true))
                        }
                    }
                }
            } else if (data?.getData() != null) {
                // if single image is selected
                pdfUri = data.data
                if (pdfUri.toString().startsWith("content://")) {
                    var cursor: Cursor? = null
                    try {
                        cursor =
                            pdfUri?.let { requireActivity().contentResolver.query(it, null, null, null, null) }
                        if (cursor != null && cursor.moveToFirst()) {
                            val displayName =
                                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            pdfList.add(UploadImage(pdfUri, false,displayName, true))

                        }
                    } finally {
                        cursor?.close()
                    }
                }
                else {
                    pdfList.add(UploadImage(pdfUri, false, isModified = true))
                }
            }
            uploadPdfAdapter.updatePdfList(pdfList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdfList.add(UploadImage(null, true))
        if(hasStoragePermission()) {
            args.pet?.image?.let {
                savePhotoFromUrl()
                Picasso.get().load(args.pet?.image).into(viewBinding.uploadIv)

            } ?: run {
                viewBinding.uploadIv.setImageResource(R.drawable.ic_upload_photo)
            }
            saveDocumentsFromUrl()
        }
        else {
            requestStoragePermission()
        }
        setObservers()
        setAdapters()
        setListeners()
        viewBinding.petNameEt.setText(args.pet?.name)
        viewBinding.petBreedEt.setText(args.pet?.breed)
        viewBinding.groupCb.isChecked = args.pet?.isGroup == true
        args.pet?.count?.let { viewBinding.petCountEt.setText(it.toString()) }
        uploadPdfAdapter.updatePdfList(pdfList)
        val date = args.pet?.expireDate?.split('.')
        date?.let{initDate(it)}

        viewBinding.petCountBk.visibility =
            if (viewBinding.groupCb.isChecked)
                View.VISIBLE
            else
                View.GONE
        viewBinding.groupCb.setOnCheckedChangeListener { _, checked ->
            viewBinding.petCountBk.visibility =
                if (checked)
                    View.VISIBLE
                else
                    View.GONE
        }


    }

    private fun initDate(date: List<String>){
        myCalendarEnd.set(Calendar.YEAR, date[2].toInt())
        myCalendarEnd.set(Calendar.MONTH, date[1].toInt())
        myCalendarEnd.set(Calendar.DAY_OF_MONTH, date[0].toInt())
        updateLabel()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setObservers() {
        petObserver = Observer { result ->
            result.doIfSuccess { pet ->
                if(pdfList.all { !it.isModified }){
                    if (pet != null && args.operationType != "update")
                        PushNotifications.addDeviceInterest("petId=${pet.id}")
                        (activity as MainActivity).showProgressBar()
                        val successDialog = AlertDialog.Builder(requireContext())
                        successDialog.setIcon(R.drawable.ic_baseline_check_24)
                        successDialog.setTitle("Данные сохранены!")
                        successDialog.setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            findNavController().navigate(CreatePetFragmentDirections.actionCreatePetFragmentToProfileFragment())
                        }
                        successDialog.create()
                        successDialog.show()
                } else {
                    for(pdf in pdfList){
                        pdf.imageUri?.let {
                            pdfToPet.add(prepareFilePart("attachments", it))
                        }
                    }
                    if (pet != null) {
                        petsListViewModel.uploadAttachment(pet.id, pdfToPet).observe(viewLifecycleOwner) { result->
                            result.doIfSuccess {
                                (activity as MainActivity).showProgressBar()
                                val successDialog = AlertDialog.Builder(requireContext())
                                successDialog.setIcon(R.drawable.ic_baseline_check_24)
                                successDialog.setTitle("Данные сохранены!")
                                successDialog.setPositiveButton(
                                    "OK"
                                ) { _, _ ->
                                    findNavController().navigate(CreatePetFragmentDirections.actionCreatePetFragmentToProfileFragment())
                                }
                                successDialog.create()
                                successDialog.show()
                                if (args.operationType != "update") {
                                    PushNotifications.addDeviceInterest("petId=${pet.id}")
                                }
                            }
                            result.doIfFailure { error, data ->
                                (activity as MainActivity).showProgressBar()
                                error?.let { (activity as MainActivity).showAlert(it) }

                            }

                            if (result is NetworkResult.Loading) {
                                (activity as MainActivity).showProgressBar(true)

                            }


                        }
                    }


                }
            }
            result.doIfFailure { error, data ->
                (activity as MainActivity).showProgressBar()
                error?.let { (activity as MainActivity).showAlert(it) }

            }

            if (result is NetworkResult.Loading) {
                (activity as MainActivity).showProgressBar(true)

            }

        }

    }


    private fun setListeners() {

        val date = DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, month: Int, day: Int ->
            myCalendarEnd.set(Calendar.YEAR, year)
            myCalendarEnd.set(Calendar.MONTH, month)
            myCalendarEnd.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }
        viewBinding.vacDateBlock.setOnClickListener {
            DatePickerDialog(requireActivity(), date, myCalendarEnd.get(Calendar.YEAR),
                myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.vacDateEt.setOnClickListener {
            DatePickerDialog(requireActivity(), date, myCalendarEnd.get(Calendar.YEAR),
                myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.cardView.setOnClickListener {
            if (hasStoragePermission()) {
                openFileExplorer()
            } else {
                requestStoragePermission()
            }
        }
        viewBinding.confirmButton.setOnClickListener {
            if (viewBinding.groupCb.isChecked && viewBinding.petCountEt.text.toString()
                    .toInt() < 2
            ) {
                viewBinding.errorText.visibility = View.VISIBLE
            } else {
                viewBinding.errorText.visibility = View.GONE

                val petSize =
                    when (viewBinding.petSizeSp.selectedItem) {
                        "меньше 5кг" -> 5
                        "меньше 10кг" -> 10
                        "меньше 20кг" -> 20
                        "меньше 30кг" -> 30
                        "меньше 40кг" -> 40
                        "больше 40кг" -> 50
                        else -> null
                    }
                val pet = PetCreate(
                    name = viewBinding.petNameEt.text.toString(),
                    type = viewBinding.petTypeSp.selectedItem.toString(),
                    breed = viewBinding.petBreedEt.text.toString(),
                    weight = petSize,
                    expireDate = viewBinding.vacDateEt.text.toString(),
                    isGroup = viewBinding.groupCb.isChecked,
                    count = viewBinding.petCountEt.text.toString().toInt()
                )

                val image = imageUri?.let { it1 -> prepareFilePart("image", it1) }

                if (image != null) {
                    if (args.operationType == "update")
                        args.pet?.let { it1 ->
                            petsListViewModel.updatePet(image, pet, it1.id).observe(
                                viewLifecycleOwner, petObserver
                            )
                        }
                    else
                        if (validateFields())
                            petsListViewModel.createPet(image, pet)
                                .observe(viewLifecycleOwner, petObserver)
                        else {
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


    }

    private fun setAdapters() {
        setPetTypes()
        setPetSize()
        uploadPdfAdapter = UploadPdfAdapter(this)
        viewBinding.uploadPdfRv.adapter = uploadPdfAdapter
        viewBinding.uploadPdfRv.setHasFixedSize(true)
        viewBinding.uploadPdfRv.setItemViewCacheSize(20)
        viewBinding.uploadPdfRv.isNestedScrollingEnabled = false
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

        args.pet?.let { pet ->
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

        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFileExplorer()
                } else {
                    val showRational =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )

                    if (showRational) {
                        Timber.d("Storage permission denied")
                    } else {
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:${requireActivity().packageName}")
                        ).apply {
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
            if(SDK_INT >=Build.VERSION_CODES.R){
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
        if(SDK_INT >=Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager()
        } else{
            val result = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun openFileExplorer() {
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures"), REQUEST_CODE
            )
        } else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }
    }


    private fun openPdfFileExplorer() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = FileUtils.getFile(requireContext(), fileUri)
        val requestFile = RequestBody.create(
            MediaType.parse(requireContext().contentResolver.getType(fileUri)),
            file
        )
        val fileName = if (file.name.length >= 40) file.name.substring(0..39) else file.name
        return MultipartBody.Part.createFormData(partName, fileName, requestFile)
    }

    fun savePhotoFromUrl() {
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

        withContext(Dispatchers.IO) {
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

    private fun saveDocumentsFromUrl(){

        args.pet?.docs.let { docs ->
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
                            pdfList.add(UploadImage(GenericFileProvider.getUriForFile(requireContext(),
                            requireContext().applicationContext.packageName, output), false))
                            uploadPdfAdapter.updatePdfList(pdfList)
                        }

                        override fun onError(exception: Throwable) {
                            Timber.d(exception.toString())
                        }
                    })
                }

            }
        }
    }

    private fun validateFields(): Boolean {
        val name = viewBinding.petNameEt.text.toString()

        val petType = viewBinding.petTypeSp.selectedItem.toString()
        val breed = viewBinding.petBreedEt.text.toString()
        val petSize = viewBinding.petSizeSp.selectedItem.toString()

        return name != "" && petSize != "" && petType != "" && petSize != "" && breed != ""
    }

    override fun upload(isFirstElement: Boolean, image: UploadImage) {
        if (isFirstElement) {
            openPdfFileExplorer()
        } else {
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
    }


    override fun deletePdf(image: UploadImage) {
        args.pet?.let { petsListViewModel.deletePetDoc(it.id) }
        pdfList.remove(image)
        uploadPdfAdapter.updatePdfList(pdfList)
    }

    private fun updateLabel() {
        val myDateFormat = "dd.MM.yyyy"
        val dateFormat = SimpleDateFormat(myDateFormat, Locale.getDefault())
        viewBinding.vacDateEt.setText(dateFormat.format(myCalendarEnd.time))

    }

}



