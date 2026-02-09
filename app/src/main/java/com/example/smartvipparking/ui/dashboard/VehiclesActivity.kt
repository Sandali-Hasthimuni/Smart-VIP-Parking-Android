package com.example.smartvipparking.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartvipparking.databinding.ActivityVehiclesBinding
import com.example.smartvipparking.model.Vehicle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VehiclesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehiclesBinding
    private val auth = FirebaseAuth.getInstance()
    private val dbUrl = "https://smartvipparking-f3e3c-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val db = FirebaseDatabase.getInstance(dbUrl)
    private val vehicleList = mutableListOf<Vehicle>()
    private lateinit var adapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehiclesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.rvVehicles.layoutManager = LinearLayoutManager(this)
        adapter = VehicleAdapter(vehicleList)
        binding.rvVehicles.adapter = adapter

        binding.fabAddVehicle.setOnClickListener {
            startActivity(Intent(this, AddVehicleActivity::class.java))
        }

        loadVehicles()
    }

    private fun loadVehicles() {
        val userId = auth.currentUser?.uid ?: return
        db.getReference("Vehicles").orderByChild("ownerId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    vehicleList.clear()
                    for (child in snapshot.children) {
                        val vehicle = child.getValue(Vehicle::class.java)
                        if (vehicle != null) {
                            vehicleList.add(vehicle)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@VehiclesActivity, "Failed to load vehicles", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
