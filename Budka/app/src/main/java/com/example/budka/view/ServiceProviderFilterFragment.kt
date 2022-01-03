package com.example.budka.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.budka.R
import com.example.budka.data.model.CountryData
import com.example.budka.data.model.Pet
import com.example.budka.data.model.PetType
import com.example.budka.databinding.MainPageFragmentBinding
import com.example.budka.databinding.ServiceProviderFilterFragmentBinding
import com.example.budka.viewModel.CountriesListViewModel
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel


class ServiceProviderFilterFragment:Fragment() {
    private lateinit var viewBinding: ServiceProviderFilterFragmentBinding
    private val countriesListViewModel: CountriesListViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ServiceProviderFilterFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countriesListViewModel.fetchCountryList().observe(viewLifecycleOwner, Observer {
            setCountries(it)
        })
        setPetTypes()
    }


    private fun setCountries(countries: List<CountryData>){
        val country = mutableListOf<String>()
        for(cc in countries){
            country.add(cc.country)
        }
        val adapter = ArrayAdapter<String>(requireActivity(), R.layout.item_country, R.id.text_view_country_item, country )
        val cityAdapter = ArrayAdapter<String>(requireActivity(), R.layout.item_city, R.id.text_view_city_item)

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
        val petAdapter = ArrayAdapter<String>(requireActivity(),R.layout.item_pet_type_filter, R.id.text_view_pet_type_item, petTypeList)
        viewBinding.petTypeSp.adapter = petAdapter

    }

}