/*
 * *
 *  * Created by Ali Ashkeyev on 03.01.2022, 22:55
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 03.01.2022, 22:54
 *
 */

package com.example.budka.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.example.budka.R
import com.example.budka.data.model.CountryData
import com.example.budka.databinding.ConfirmServiceApplicationFragmentBinding
import com.example.budka.viewModel.CountriesListViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ConfirmServiceApplicationFragment: Fragment() {
    private lateinit var viewBinding: ConfirmServiceApplicationFragmentBinding
    val myCalendarStart = Calendar.getInstance()
    val myClockStart = Calendar.getInstance()
    val myCalendarEnd = Calendar.getInstance()
    val myClockEnd = Calendar.getInstance()
    private val countriesListViewModel: CountriesListViewModel by viewModel()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ConfirmServiceApplicationFragmentBinding.inflate(inflater, container, false)


        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countriesListViewModel.fetchCountryList().observe(viewLifecycleOwner, {
            setCountries(it)
        })
        setListeners()
        updateLabel()

    }

    private fun updateLabel() {
        val myDateFormat = "dd.MMMM.yyyy"
        val myTimeFormat = "HH:mm"
        val dateFormat = SimpleDateFormat(myDateFormat, Locale.getDefault())
        val timeFormat = SimpleDateFormat(myTimeFormat, Locale.getDefault())
        viewBinding.startDateEt.setText(dateFormat.format(myCalendarStart.time))
        viewBinding.startTimeEt.setText(timeFormat.format(myClockStart.time))
        viewBinding.endDateEt.setText(dateFormat.format(myCalendarEnd.time))
        viewBinding.endTimeEt.setText(timeFormat.format(myClockEnd.time))

    }
    private fun setListeners(){
        val date = DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, month: Int, day: Int ->
            myCalendarStart.set(Calendar.YEAR, year)
            myCalendarStart.set(Calendar.MONTH, month)
            myCalendarStart.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }

        val time = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            myClockStart.set(Calendar.HOUR_OF_DAY, hour)
            myClockStart.set(Calendar.MINUTE, minute)
            updateLabel()
        }

        val dateEnd = DatePickerDialog.OnDateSetListener() { _: DatePicker, year: Int, month: Int, day: Int ->
            myCalendarEnd.set(Calendar.YEAR, year)
            myCalendarEnd.set(Calendar.MONTH, month)
            myCalendarEnd.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }

        val timeEnd = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            myClockEnd.set(Calendar.HOUR_OF_DAY, hour)
            myClockEnd.set(Calendar.MINUTE, minute)
            updateLabel()
        }


        viewBinding.startDateBlock.setOnClickListener {
            DatePickerDialog(requireActivity(), date, myCalendarStart.get(Calendar.YEAR),
                myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.startDateEt.setOnClickListener {
            DatePickerDialog(requireActivity(), date, myCalendarStart.get(Calendar.YEAR),
                myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.startTimeBlock.setOnClickListener {
            TimePickerDialog(requireActivity(), time, myClockStart.get(Calendar.HOUR_OF_DAY),
                myClockStart.get(Calendar.MINUTE), true).show()
        }

        viewBinding.startTimeEt.setOnClickListener {
            TimePickerDialog(requireActivity(), time, myClockStart.get(Calendar.HOUR_OF_DAY),
                myClockStart.get(Calendar.MINUTE), true).show()
        }


        viewBinding.endDateBlock.setOnClickListener {
            DatePickerDialog(requireActivity(), dateEnd, myCalendarEnd.get(Calendar.YEAR),
                myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.endDateEt.setOnClickListener {
            DatePickerDialog(requireActivity(), dateEnd, myCalendarEnd.get(Calendar.YEAR),
                myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.endTimeBlock.setOnClickListener {
            TimePickerDialog(requireActivity(), timeEnd, myClockEnd.get(Calendar.HOUR_OF_DAY),
                myClockEnd.get(Calendar.MINUTE), true).show()
        }

        viewBinding.endTimeEt.setOnClickListener {
            TimePickerDialog(requireActivity(), timeEnd, myClockEnd.get(Calendar.HOUR_OF_DAY),
                myClockEnd.get(Calendar.MINUTE), true).show()
        }
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
}