package com.example.smartvipparking.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartvipparking.R
import com.example.smartvipparking.ui.auth.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Admin Dashboard
 * - Accessible only by ADMIN role
 * - Backend validated
 * - Includes QR Access Scanner
 */
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var btnScanQr: MaterialButton
    private lateinit var btnManageUsers: MaterialButton
    private lateinit var btnManageVehicles: MaterialButton
    private lateinit var btnManageParking: MaterialButton
    private lateinit var btnReports: MaterialButton

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val DB_URL = "https://smartvipparking-f3e3c-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val db: FirebaseDatabase by lazy { FirebaseDatabase.getInstance(DB_URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Verify admin before loading UI
        verifyAdminAccess()

        // Initialize buttons
        btnScanQr = findViewById(R.id.btn_scan_qr)
        btnManageUsers = findViewById(R.id.btn_manage_users)
        btnManageVehicles = findViewById(R.id.btn_manage_vehicles)
        btnManageParking = findViewById(R.id.btn_manage_parking)
        btnReports = findViewById(R.id.btn_reports)

        setupClickListeners()
    }

    private fun verifyAdminAccess() {
        val user = auth.currentUser
        if (user == null) {
            redirectToLogin()
            return
        }

        // Check if user exists in Admins node
        db.getReference("Admins")
            .child(user.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    Toast.makeText(this, "Access denied: Admins only", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    redirectToLogin()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to verify admin access", Toast.LENGTH_SHORT).show()
                redirectToLogin()
            }
    }

    private fun setupClickListeners() {
        btnScanQr.setOnClickListener {
            startActivity(Intent(this, QrScannerActivity::class.java))
        }

        btnManageUsers.setOnClickListener {
            showFeaturePending("Manage Users")
        }

        btnManageVehicles.setOnClickListener {
            showFeaturePending("Manage Vehicles")
        }

        btnManageParking.setOnClickListener {
            showFeaturePending("Manage Parking Lots")
        }

        btnReports.setOnClickListener {
            showFeaturePending("Reports & Invoices")
        }
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun showFeaturePending(feature: String) {
        Toast.makeText(this, "$feature coming soon", Toast.LENGTH_SHORT).show()
    }
}
