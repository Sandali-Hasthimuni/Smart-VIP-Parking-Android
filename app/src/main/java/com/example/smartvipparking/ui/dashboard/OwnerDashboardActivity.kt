package com.example.smartvipparking.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartvipparking.databinding.ActivityOwnerDashboardBinding
import com.example.smartvipparking.ui.auth.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

/**
 * Owner Dashboard
 * - Fully functional for all modules
 */
class OwnerDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOwnerDashboardBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val DB_URL = "https://smartvipparking-f3e3c-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val db: FirebaseDatabase by lazy { FirebaseDatabase.getInstance(DB_URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOwnerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (auth.currentUser == null) {
            redirectToLogin()
            return
        }

        setupUI()
        loadOwnerProfile()
    }

    private fun setupUI() = with(binding) {
        cardQrToken.setOnClickListener { showQrToken() }
        
        cardProfile.setOnClickListener {
            startActivity(Intent(this@OwnerDashboardActivity, ProfileActivity::class.java))
        }
        
        cardVehicles.setOnClickListener {
            startActivity(Intent(this@OwnerDashboardActivity, VehiclesActivity::class.java))
        }
        
        cardHistory.setOnClickListener {
            startActivity(Intent(this@OwnerDashboardActivity, HistoryActivity::class.java))
        }
        
        cardInvoices.setOnClickListener {
            startActivity(Intent(this@OwnerDashboardActivity, InvoicesActivity::class.java))
        }
        
        cardCharges.setOnClickListener {
            startActivity(Intent(this@OwnerDashboardActivity, ChargesActivity::class.java))
        }

        cardMap.setOnClickListener {
            startActivity(Intent(this@OwnerDashboardActivity, ParkingMapActivity::class.java))
        }
        
        cardOverdue.setOnClickListener {
            startActivity(Intent(this@OwnerDashboardActivity, OverdueActivity::class.java))
        }
        
        cardLogout.setOnClickListener { confirmLogout() }
    }

    private fun loadOwnerProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.getReference("VehicleOwners").child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val name = snapshot.child("fullname").getValue(String::class.java)
                binding.tvWelcome.text = if (!name.isNullOrBlank()) "Welcome, $name" else "Welcome to Smart VIP Parking"
            }
        }
    }

    private fun showQrToken() {
        val userId = auth.currentUser?.uid ?: return
        try {
            val bitmap = BarcodeEncoder().encodeBitmap(userId, BarcodeFormat.QR_CODE, 600, 600)
            val imageView = ImageView(this).apply {
                setImageBitmap(bitmap)
                setPadding(48, 48, 48, 48)
            }
            MaterialAlertDialogBuilder(this)
                .setTitle("Your Access Pass")
                .setMessage("Present this QR at entry/exit points.")
                .setView(imageView)
                .setPositiveButton("Close", null)
                .show()
        } catch (e: Exception) {
            Toast.makeText(this, "QR generation failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Logout") { _, _ ->
                auth.signOut()
                redirectToLogin()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}
