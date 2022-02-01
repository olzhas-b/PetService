/*
 * *
 *  * Created by Ali Ashkeyev on 20.01.22 14:31
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 20.01.22 13:53
 *
 */

package com.example.budka.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.budka.R
import com.example.budka.databinding.FragmentMapsBinding
import com.example.budka.utils.Constants.Companion.YANDEX_MAPS_API_KEY
import com.example.budka.view.adapter.viewHolder.UploadNewImageListener

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import org.koin.ext.getOrCreateScope

class MapsFragment : Fragment() {
    private var _viewBinding: FragmentMapsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var mapView: MapView
    val arg: MapsFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireContext())
        _viewBinding = FragmentMapsBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = viewBinding.yandexMap
        if(arg.setLocation){
            viewBinding.setLocationBlock.visibility = View.VISIBLE
            setProvidableServiceLocation()
        }
        else{
            loadServiceProviderLocation(43.227028, 76.944611)

        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadServiceProviderLocation(latitude: Double, longitude: Double) {
        mapView.map.move(
            CameraPosition(Point( latitude, longitude), 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 2F),
            null
        )

        val locationCircle = Circle(Point(43.227028, 76.944611),
            1000F)
        mapView.map.mapObjects.addCircle(locationCircle, ResourcesCompat
            .getColor(resources,
                R.color.stroke_red,
                null),
            2F, ResourcesCompat
                .getColor(resources,
                    R.color.stroke_trasparent_red,
                    null))

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setProvidableServiceLocation() {

        val geoCoder = Geocoder(context)
        val addresses = geoCoder.getFromLocationName("Казахстан, Алматы",1)
        if(addresses.size > 0){
            mapView.map.move(
                CameraPosition(Point( addresses[0].latitude, addresses[0].longitude), 14.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2F),
                null
            )
        }
        val locationIcon = View(requireContext()).apply {
            background = requireContext().getDrawable(R.drawable.ic_baseline_location_on_24)
        }
        val k = mapView.map.mapObjects.addPlacemark(
            Point(addresses[0].latitude, addresses[0].longitude),
            ViewProvider(locationIcon)
        )
        k.isDraggable = true

        k.setDragListener(object : MapObjectDragListener{
            override fun onMapObjectDragStart(p0: MapObject) {
            }

            @SuppressLint("LogNotTimber")
            override fun onMapObjectDrag(p0: MapObject, p1: Point) {
                viewBinding.setLocationBtn.setOnClickListener {
//                    setLocationInterface?.sendLocation(p1.longitude, p1.latitude)
                }
            }

            override fun onMapObjectDragEnd(p0: MapObject) {

            }
        })

    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    interface SendLocationInterface {
        fun sendLocation(longitude: Double, latitude: Double)
    }
}