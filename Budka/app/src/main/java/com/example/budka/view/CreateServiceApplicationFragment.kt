package com.example.budka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.budka.R
import com.example.budka.data.model.PetSize
import com.example.budka.data.model.PetType
import com.example.budka.data.model.ServiceType
import com.example.budka.databinding.CreateServiceApplicationFragmentBinding

class CreateServiceApplicationFragment : Fragment() {
    private lateinit var viewBinding: CreateServiceApplicationFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = CreateServiceApplicationFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPetTypes()
        setPetSize()
        setServiceType()
    }

    private fun setPetTypes() {
        val petTypeList = mutableListOf<String>()
        for (pet in PetType.values()) {
            petTypeList.add(pet.value)
        }
        val petAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_pet_type_filter, R.id.text_view_pet_type_item, petTypeList
        )
        viewBinding.petTypeSp.adapter = petAdapter

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
        for (size in PetSize.values()) {
            petSizeList.add(size.value)
        }
        val sizeAdapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.item_pet_type_filter, R.id.text_view_pet_type_item, petSizeList
        )
        viewBinding.petSizeSp.adapter = sizeAdapter
    }
}

