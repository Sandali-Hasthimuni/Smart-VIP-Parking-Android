package com.example.smartvipparking.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartvipparking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var fullnameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var roleGroup: RadioGroup
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    private val DB_URL = "https://smartvipparking-f3e3c-default-rtdb.asia-southeast1.firebasedatabase.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        fullnameInput = findViewById(R.id.fullname_input)
        emailInput = findViewById(R.id.email_input)
        phoneInput = findViewById(R.id.phone_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
        registerButton = findViewById(R.id.register_button)
        roleGroup = findViewById(R.id.role_group)
        progressBar = findViewById(R.id.progress_bar)

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val fullname = fullnameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        if (fullname.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill all fields")
            return
        }

        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return
        }

        if (password.length < 6) {
            showToast("Password must be at least 6 characters")
            return
        }

        val selectedRoleId = roleGroup.checkedRadioButtonId
        val roleNode = if (selectedRoleId == R.id.radio_admin) "Admins" else "VehicleOwners"

        progressBar.visibility = View.VISIBLE
        registerButton.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        saveUserToDatabase(userId, fullname, email, phone, roleNode)
                    }
                } else {
                    progressBar.visibility = View.GONE
                    registerButton.isEnabled = true
                    val errorMsg = task.exception?.message ?: "Registration Failed"
                    Log.e("RegisterActivity", "Auth Error: $errorMsg")
                    showToast("Registration Failed: $errorMsg")
                }
            }
    }

    private fun saveUserToDatabase(userId: String, fullname: String, email: String, phone: String, roleNode: String) {
        val database = FirebaseDatabase.getInstance(DB_URL)
        val dbRef = database.getReference(roleNode)
        
        val userMap = hashMapOf(
            "uid" to userId,
            "fullname" to fullname,
            "email" to email,
            "phone" to phone,
            "role" to if (roleNode == "Admins") "admin" else "user"
        )

        dbRef.child(userId).setValue(userMap)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                showToast("Registration Successful")
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                registerButton.isEnabled = true
                Log.e("RegisterActivity", "Database Error: ${e.message}")
                showToast("Failed to save data: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
