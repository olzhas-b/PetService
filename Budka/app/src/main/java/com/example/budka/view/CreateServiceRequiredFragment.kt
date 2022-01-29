/*
 * *
 *  * Created by Ali Ashkeyev on 13.01.22 13:15
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 13.01.22 13:15
 *
 */

package com.example.budka.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.CreateServiceRequiredFragmentBinding
import com.example.budka.utils.MultiSpinner
import com.example.budka.viewModel.CountriesListViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.reflect.typeOf

class CreateServiceRequiredFragment : Fragment(), MapsFragment.SendLocationInterface {
    private var _viewBinding: CreateServiceRequiredFragmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val countriesListViewModel: CountriesListViewModel by viewModel()


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
    }

    private fun setListeners(){
        viewBinding.mapIv.setOnClickListener {
            val fragment = MapsFragment(true, this)
            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.createServiceRequiredFragment, fragment)?.addToBackStack("requiredFm")?.commit()
//            it.findNavController().navigate(CreateServiceRequiredFragmentDirections.actionCreateServiceRequiredFragmentToMapsFragment(true))

        }
        viewBinding.optionalNavigateBtn.setOnClickListener {
            var serviceType: Int = 0
            ServiceType.from(viewBinding.serviceTypeSp.selectedItem.toString())?.let {
                serviceType = ServiceType.valueOf(it.name).ordinal + 1
            }

            val summary = viewBinding.summaryEt.text.toString()
            val petType = viewBinding.petTypeSp.selectedItem.toString()
            val petSize = viewBinding.petSizeSp.selectedItem.toString()
            val country = viewBinding.countriesEdV.text.toString()
            val city = viewBinding.cityEdV.text.toString()
            val requireFields = ServiceRequiredField(serviceType, summary, petType, petSize, country, city)
            it.findNavController().navigate(CreateServiceRequiredFragmentDirections.actionCreateServiceRequiredFragmentToCreateServiceOptionalFragment(requireFields))
        }
    }

    private fun setAdapters(){
        setPetTypes()
        setPetSize()
        setServiceType()
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

    private fun setPetTypes(){
        val petTypeList = mutableListOf<String>()
        for (pet in PetType.values()){
            petTypeList.add(pet.value)
        }
        viewBinding.petTypeSp.setItems(petTypeList, "Выбрать") {
        }

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
    }

    private fun setPetSize() {
        val petSizeList = mutableListOf<String>()
        petSizeList.add("меньше 5кг")
        petSizeList.add("меньше 10кг")
        petSizeList.add("меньше 20кг")
        petSizeList.add("меньше 40кг")
        petSizeList.add("больше 40кг")
        val petSizeAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_pet_type_filter, R.id.text_view_pet_type_item, petSizeList
        )
        viewBinding.petSizeSp.adapter = petSizeAdapter
    }

    override fun sendLocation(longitude: Double, latitude: Double) {
        Log.d("hejj", longitude.toString())
    }


}