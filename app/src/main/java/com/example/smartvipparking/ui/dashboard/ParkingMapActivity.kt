package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.R
import com.example.smartvipparking.databinding.ActivityParkingMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ParkingMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityParkingMapBinding
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Sample Parking Lots
        val lotA = LatLng(6.9271, 79.8612) // Colombo
        val lotB = LatLng(6.9319, 79.8478)

        mMap.addMarker(MarkerOptions().position(lotA).title("VIP Parking Lot A"))
        mMap.addMarker(MarkerOptions().position(lotB).title("VIP Parking Lot B"))
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lotA, 14f))

        mMap.setOnMarkerClickListener { marker ->
            binding.cardLotDetails.visibility = View.VISIBLE
            binding.tvLotName.text = marker.title
            binding.tvLotStatus.text = "Available Slots: ${ (5..20).random() }"
            true
        }
    }
}
