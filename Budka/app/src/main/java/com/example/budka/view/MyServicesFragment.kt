/*
 * *
 *  * Created by Ali Ashkeyev on 12.01.22 21:00
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12.01.22 19:41
 *
 */

package com.example.budka.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budka.R
import com.example.budka.data.model.*
import com.example.budka.databinding.FragmentMyServicesBinding
import com.example.budka.view.adapter.ServiceProvidersAdapter
import com.example.budka.view.adapter.UserPetsAdapter
import com.example.budka.view.adapter.viewHolder.NavigationListener
import com.example.budka.view.adapter.viewHolder.PetEditListener
import com.example.budka.viewModel.PetsListViewModel
import com.example.budka.viewModel.ServicesViewModel
import kotlinx.android.synthetic.main.fragment_my_services.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyServicesFragment : Fragment(), NavigationListener, PetEditListener {

    private var _viewBinding: FragmentMyServicesBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val args: MyServicesFragmentArgs by navArgs()
    private lateinit var serviceProvidersAdapter: ServiceProvidersAdapter
    private lateinit var myPetsAdapter: UserPetsAdapter
    private val petsListViewModel: PetsListViewModel by viewModel()
    private val myservicesViewModel: ServicesViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentMyServicesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(args.toPage){
            "service" -> {
                    myservicesViewModel.fetchUserServicesList(args.userId)

                setMyServicesObserver()
                setServiceListeners()
                setupServiceAdapter()
            }
            "pets" -> {
                    petsListViewModel.fetchUserPetsList(args.userId)
                setMyPetsObserver()
                setupPetsAdapter()
                setPetsListeners()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    private fun setMyPetsObserver(){
        petsListViewModel.getUserPetsList().observe(viewLifecycleOwner) { result ->
            result.doIfSuccess { pets ->
                viewBinding.errorTextTv.visibility =
                    if(pets.isNullOrEmpty())
                        View.GONE
                    else
                        View.VISIBLE
                viewBinding.errorTextTv.text = "У вас нет питомцев"
                pets?.let { myPetsAdapter.updatePetList(pets) }
            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }

            }
        }
    }

    private fun setMyServicesObserver(){
        myservicesViewModel.getUserServicesList().observe(viewLifecycleOwner
        ) { result ->
            result.doIfSuccess { services ->
                viewBinding.errorTextTv.visibility =
                    if(services.isNullOrEmpty())
                        View.GONE
                    else
                        View.VISIBLE
                viewBinding.errorTextTv.text = "У вас нет текущих объявлений"

                services?.let { serviceProvidersAdapter.updateEmployeeList(services) }
            }
            result.doIfFailure { error, data ->
                error?.let { (activity as MainActivity).showAlert(it) }

            }
        }
    }

    private fun setServiceListeners(){
        viewBinding.fab.setOnClickListener {

            it.findNavController()
                .navigate(MyServicesFragmentDirections.actionMyServicesToCreateServiceRequiredFragment())

        }

    }

    private fun setPetsListeners(){
        viewBinding.fab.setOnClickListener {
            it.findNavController().navigate(
                MyServicesFragmentDirections.actionMyServicesToCreatePetFragment()
            )
        }
        }


    private fun setupServiceAdapter(){
        serviceProvidersAdapter = ServiceProvidersAdapter(isMyServicesPage = true, navigationListener = this)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        myServicesRv.layoutManager = layoutManager
        myServicesRv.adapter = serviceProvidersAdapter
        myServicesRv.setHasFixedSize(true)
        myServicesRv.setItemViewCacheSize(20)
    }

    private fun setupPetsAdapter(){
        myPetsAdapter = UserPetsAdapter(0, navigationListener = this, showDeleteBtn = true, listener = null)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        myServicesRv.layoutManager = layoutManager
        myServicesRv.adapter = myPetsAdapter
        myServicesRv.setHasFixedSize(true)
        myServicesRv.setItemViewCacheSize(20)
    }

    override fun navigate(serviceProviderData: ServiceProvider) {
        findNavController().navigate(MyServicesFragmentDirections.actionMyServicesToCreateServiceRequiredFragment(user = serviceProviderData, operationType = "update"))
    }

    override fun navigate(petsData: Pet) {
        findNavController().navigate(MyServicesFragmentDirections.actionMyServicesToCreatePetFragment(pet = petsData, operationType = "update"))
    }

    override fun delete(petId: Int) {
        petsListViewModel.deletePet(petId).observe(viewLifecycleOwner, { result ->
            result.doIfSuccess {
                (activity as MainActivity).showProgressBar()
                petsListViewModel.fetchUserPetsList(args.userId)
                setMyPetsObserver()

            }
            result.doIfFailure{ error, data ->
                (activity as MainActivity).showProgressBar()
                (activity as MainActivity).showProgressBar()
                error?.let{(activity as MainActivity).showAlert(it)}
            }

            if(result is NetworkResult.Loading){
                (activity as MainActivity).showProgressBar(true)

            }

        })
    }

    override fun deleteService(serviceId: Int) {
        myservicesViewModel.deleteService(serviceId).observe(viewLifecycleOwner, { result ->
            result.doIfSuccess {
                (activity as MainActivity).showProgressBar()
                myservicesViewModel.fetchUserServicesList(args.userId)
                setMyServicesObserver()

            }
            result.doIfFailure{ error, data ->
                (activity as MainActivity).showProgressBar()
                (activity as MainActivity).showProgressBar()
                error?.let{(activity as MainActivity).showAlert(it)}
            }

            if(result is NetworkResult.Loading){
                (activity as MainActivity).showProgressBar(true)

            }

        })
    }
}