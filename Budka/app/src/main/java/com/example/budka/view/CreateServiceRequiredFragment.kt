/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 13:15
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 13:15
 *
 */

package com.example.budka.view

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.CreateServiceRequiredFragmentBinding
import com.example.budka.viewModel.CountriesListViewModel
import com.example.budka.viewModel.ServiceDetailViewModel
import com.example.budka.viewModel.createServiceViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class CreateServiceRequiredFragment : Fragment(), SetLocationInterface {
    private var _viewBinding: CreateServiceRequiredFragmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val countriesListViewModel: CountriesListViewModel by viewModel()
    private val serviceDetailViewModel: ServiceDetailViewModel by viewModel()
    private val createSerViewModel: createServiceViewModel by activityViewModels()
    val args: CreateServiceRequiredFragmentArgs by navArgs()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var serviceDetail: ServiceDetail
    private var acceptablePetTypes = MutableLiveData<String>()
    private var acceptablePetSize = MutableLiveData<Int>()
    private var uriList = mutableListOf<UploadImage>()
    private  var properties: List<Properties>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = CreateServiceRequiredFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null){
            args.user?.let {
                it.price?.let { it1 -> viewBinding.priceEt.setText(it1.toString()) }
                viewBinding.countriesEdV.setText(it.user?.country)
                viewBinding.cityEdV.setText(it.user?.city)
                savePhotoFromUrl()
                serviceDetailViewModel.fetchServiceDetail(it.id)
            }
        }
        setObservers()
        setAdapters()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun setObservers(){
        countriesListViewModel.fetchCountryList().observe(viewLifecycleOwner, Observer {
            setCountries(it)
        })

        serviceDetailViewModel.getServiceDetail().observe(viewLifecycleOwner, {
            viewBinding.apply {
                it.doIfSuccess {
                    it?.apply {
                        summaryEt.setText(description)
                        acceptablePetTypes.value = acceptablePets
                        acceptablePetSize.value = acceptableSize
                        this@CreateServiceRequiredFragment.latitude = latitude
                        this@CreateServiceRequiredFragment.longitude = longitude
                        createSerViewModel.propertiesList.value = additionalProperties
                    }

                }
            }
        })
    }

    private fun setListeners(){
        viewBinding.mapIv.setOnClickListener {
            val fragment = MapsFragment(true, setLocationInterface = this)
            parentFragmentManager.beginTransaction().add(R.id.fragment, fragment).addToBackStack("requiredFm").commit()
//            it.findNavController().navigate(CreateServiceRequiredFragmentDirections.actionCreateServiceRequiredFragmentToMapsFragment(true))

        }
        viewBinding.optionalNavigateBtn.setOnClickListener {
            if(validateFields()) {
                var serviceType: Int = 0
                ServiceType.from(viewBinding.serviceTypeSp.selectedItem.toString())?.let {
                    serviceType = ServiceType.valueOf(it.name).ordinal + 1
                }

                val summary = viewBinding.summaryEt.text.toString()
                val petType = viewBinding.petTypeSp.selectedItem.toString()
                val petSize = viewBinding.petSizeSp.selectedItem.toString()
                val currencyCode = if (viewBinding.currencySp.selectedItem.toString()
                        .equals(" ")
                ) null else viewBinding.currencySp.selectedItem.toString()
                val country = viewBinding.countriesEdV.text.toString()
                val city = viewBinding.cityEdV.text.toString()
                val price = viewBinding.priceEt.text.toString().toInt()
                val pricePerTime = viewBinding.pricePerTime.selectedItem.toString()
                val requireFields = ServiceRequiredField(
                    args.user?.id,
                    serviceType,
                    summary,
                    petType,
                    petSize,
                    country,
                    city,
                    this.longitude,
                    this.latitude,
                    price,
                    currencyCode,
                    pricePerTime
                )
                createSerViewModel.imageList.value = uriList
                it.findNavController().navigate(
                    CreateServiceRequiredFragmentDirections.actionCreateServiceRequiredFragmentToCreateServiceOptionalFragment(
                        requiredField = requireFields, operationType = args.operationType
                    )
                )
            }
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

    private fun setAdapters(){
        setPetSize()
        setServiceType()
        setPetTypes()
        setPricePerTime()
        setCurrencyType()

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
        args.user?.user?.let {
            viewBinding.countriesEdV.setText(it.country)
            viewBinding.cityEdV.setText(it.city)
        }
    }

    private fun setPetTypes(){
        val petTypeList = mutableListOf<String>()
        for (pet in PetType.values()) {
            petTypeList.add(pet.value)
        }
        val map = mutableMapOf<String, Boolean>()
        petTypeList.forEach {
            map[it] = false
        }
        viewBinding.petTypeSp.setItems(petTypeList, "Выбрать", null) {}


        acceptablePetTypes.observe(viewLifecycleOwner, {
        if(it.length > 2 && !it.contains("Выбрать")) {
                val splittedPetTypes =it.split(",")
                splittedPetTypes.forEach {
                    if(map.containsKey(it)){
                        map[it] = true
                    }
                }
                viewBinding.petTypeSp.setItems(
                    petTypeList,
                    it,
                    map.values.toBooleanArray()
                ) {}
            }
            else
                viewBinding.petTypeSp.setItems(petTypeList, "Выбрать", null) {}
        })
    }

    private fun setServiceType() {
        val serviceType = mutableListOf<String>()
        for (service in ServiceType.values()) {
            serviceType.add(service.value)
        }
        val serviceAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_pet_type_filter, R.id.text_view_pet_type_item, serviceType
        )
        viewBinding.serviceTypeSp.adapter = serviceAdapter
        args.user?.let {
            it.serviceType?.let { it1 -> viewBinding.serviceTypeSp.setSelection(it1-1) }
        }
    }

    private fun setCurrencyType() {
        val currencyType = mutableListOf<String>()
        for (currency in CurrencyType.values()) {
            currencyType.add(currency.value)
        }
        val currencyAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_currency_type, R.id.text_view_pet_type_item, currencyType
        )
        viewBinding.currencySp.adapter = currencyAdapter
        args.user?.let {
            it.currencyCode?.let { it1 -> CurrencyType.from(it1)?.let { it2 ->
                viewBinding.currencySp.setSelection(
                    it2.ordinal)
            } }
        }
    }

    private fun setPricePerTime() {
        val pricePerTime = mutableListOf<String>()
        for (pricept in PricePerTime.values()) {
            pricePerTime.add(pricept.value)
        }

        val pricePerTimeAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_currency_type, R.id.text_view_pet_type_item, pricePerTime
        )
        viewBinding.pricePerTime.adapter = pricePerTimeAdapter
        args.user?.let {
            it.pricePerTime?.let { it1 -> PricePerTime.from(it1)?.let { it2 ->
                viewBinding.pricePerTime.setSelection(
                    it2.ordinal)
            } }
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
        acceptablePetSize.observe(viewLifecycleOwner,{
            viewBinding.petSizeSp.setSelection(
                when (it) {
                    5 -> 0
                    10 -> 1
                    20 -> 2
                    30 -> 3
                    40 -> 4
                    50 -> 5
                    else -> return@observe
                }
            )
        })
    }

    override fun sendLocation(longitude: Double, latitude: Double) {
        this.longitude = longitude
        this.latitude = latitude
    }

    fun savePhotoFromUrl(){
        val job = Job()
        val scopeForSaving = CoroutineScope(job + Dispatchers.Main)
        args.user.let { service ->
            service?.images?.forEach {
                val url = URL(it)
                val num = it.substring(it.lastIndexOf('/')+1)
                scopeForSaving.launch {
                    saveToStorage(url, service.user?.fullName + num)
                }
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
                            uriList.add(UploadImage(uri, false))
                        }
                    } catch (e: Exception) {
                        Timber.d(e.toString())
                    }

            }
        }

    private fun validateFields(): Boolean {
        val serviceType = viewBinding.serviceTypeSp.selectedItem.toString()

        val summary = viewBinding.summaryEt.text.toString()
        val petType = viewBinding.petTypeSp.selectedItem.toString()
        val petSize = viewBinding.petSizeSp.selectedItem.toString()
        val currencyCode = if(viewBinding.currencySp.selectedItem.toString().equals(" ")) null else viewBinding.currencySp.selectedItem.toString()
        val country = viewBinding.countriesEdV.text.toString()
        val city = viewBinding.cityEdV.text.toString()
        val price = viewBinding.priceEt.text.toString()
        val pricePerTime = viewBinding.pricePerTime.selectedItem.toString()
        return serviceType!=""&&summary!=""&&petType!=""&&petSize!=""&&currencyCode!=""&&country!=""&&
                city!=""&&price!=""&&pricePerTime!=""
    }




}

@Parcelize
data class UriList(
    val uriList:  @RawValue List<UploadImage>?
): Parcelable

@Parcelize
data class PropertiesList(

    val propertiesList:  @RawValue List<Properties>?
): Parcelable