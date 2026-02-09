package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartvipparking.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener { finish() }

        loadProfile()
    }

    private fun loadProfile() {
        val userId = auth.currentUser?.uid ?: return
        val role = intent.getStringExtra("ROLE") ?: "OWNER"

        val node = when (role) {
            "ADMIN" -> "Admins"
            else -> "VehicleOwners"
        }

        db.getReference(node).child(userId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    binding.tvName.text =
                        snapshot.child("fullname").getValue(String::class.java) ?: "N/A"

                    binding.tvEmail.text =
                        snapshot.child("email").getValue(String::class.java) ?: "N/A"

                    binding.tvPhone.text =
                        snapshot.child("phone").getValue(String::class.java) ?: "N/A"
                } else {
                    Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }
}
