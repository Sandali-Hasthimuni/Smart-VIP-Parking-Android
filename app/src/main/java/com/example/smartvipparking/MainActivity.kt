package com.example.smartvipparking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.ui.auth.LoginActivity
import com.example.smartvipparking.ui.dashboard.AdminDashboardActivity
import com.example.smartvipparking.ui.dashboard.OwnerDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            checkUserRole(currentUser.uid)
        }
    }

    private fun checkUserRole(userId: String) {
        val dbRef = FirebaseDatabase.getInstance().reference

        dbRef.child("Admins").child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            } else {
                dbRef.child("VehicleOwners").child(userId).get().addOnSuccessListener { ownerSnapshot ->
                    if (ownerSnapshot.exists()) {
                        startActivity(Intent(this, OwnerDashboardActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }.addOnFailureListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
