package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.databinding.ActivityAddVehicleBinding
import com.example.smartvipparking.model.Vehicle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddVehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVehicleBinding
    private val auth = FirebaseAuth.getInstance()
    private val dbUrl = "https://smartvipparking-f3e3c-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val db = FirebaseDatabase.getInstance(dbUrl)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnSaveVehicle.setOnClickListener {
            saveVehicle()
        }
    }

    private fun saveVehicle() {
        val number = binding.etVehicleNumber.text.toString().trim()
        val model = binding.etVehicleModel.text.toString().trim()
        val type = if (binding.rbCar.isChecked) "Car" else "Bike"
        val userId = auth.currentUser?.uid ?: return

        if (number.isEmpty() || model.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val vehicleId = db.getReference("Vehicles").push().key ?: return
        val vehicle = Vehicle(vehicleId, userId, number, model, type)

        db.getReference("Vehicles").child(vehicleId).setValue(vehicle)
            .addOnSuccessListener {
                Toast.makeText(this, "Vehicle added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add vehicle", Toast.LENGTH_SHORT).show()
            }
    }
}
