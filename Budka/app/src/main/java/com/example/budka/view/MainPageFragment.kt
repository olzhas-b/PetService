package com.example.budka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.databinding.MainPageFragmentBinding
import com.example.budka.view.adapter.PetSittersListHorizontalAdapter
import com.example.budka.view.adapter.PetsListHorizontalAdapter
import com.example.budka.viewModel.PetSittersListViewModel
import com.example.budka.viewModel.PetsListViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_page_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainPageFragment: Fragment() {
    private lateinit var viewBinding: MainPageFragmentBinding
    private lateinit var petsListHorizontalAdapter: PetsListHorizontalAdapter
    private lateinit var petSittersListHorizontalAdapter: PetSittersListHorizontalAdapter
    private val petsListViewModel: PetsListViewModel by viewModel()
    private val petSittersListViewModel: PetSittersListViewModel by viewModel()
//    private val petsListHorizontalAdapter by lazy {
//        PetsListHorizontalAdapter(petsListViewModel)
//    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = MainPageFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(R.drawable.banner).fit().centerCrop().placeholder(R.drawable.banner).into(viewBinding.bannerIv)
        setupAdapter()
        setObservers()
    }

    private fun setObservers(){
        petsListViewModel.getPetsList().observe(viewLifecycleOwner, Observer {
            petsListHorizontalAdapter.updatePetList(it)
        })

        petSittersListViewModel.getPetSittersList().observe(viewLifecycleOwner, {
            petSittersListHorizontalAdapter.updatePetSittersList(it)
        })


    }

    private fun setupAdapter(){
        petsListHorizontalAdapter = PetsListHorizontalAdapter()
        petSittersListHorizontalAdapter = PetSittersListHorizontalAdapter()
        val petsLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false )
        mainPagePetsRv.layoutManager = petsLayoutManager
        mainPagePetsRv.addItemDecoration(DividerItemDecoration(activity, petsLayoutManager.orientation))
        mainPagePetsRv.adapter = petsListHorizontalAdapter
        mainPagePetsRv.setHasFixedSize(true)
        mainPagePetsRv.setItemViewCacheSize(20)
        mainPagePetsRv.isNestedScrollingEnabled = false

        val petSittersLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false )
        nearPetSitterRv.layoutManager = petSittersLayoutManager
        nearPetSitterRv.adapter = petSittersListHorizontalAdapter
        nearPetSitterRv.setHasFixedSize(true)
        nearPetSitterRv.setItemViewCacheSize(20)
        nearPetSitterRv.isNestedScrollingEnabled =  false


    }

    private fun setOnClickListener(view: View){

    }


}