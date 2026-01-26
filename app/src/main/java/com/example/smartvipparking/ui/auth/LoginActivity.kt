package com.example.smartvipparking.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        checkUserRole(userId)
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserRole(userId: String) {
        val dbRef = FirebaseDatabase.getInstance().reference

        // Check Admin
        dbRef.child("Admins").child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            } else {
                // Check Vehicle Owner
                dbRef.child("VehicleOwners").child(userId).get().addOnSuccessListener { ownerSnapshot ->
                    if (ownerSnapshot.exists()) {
                        startActivity(Intent(this, OwnerDashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
