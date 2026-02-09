package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.databinding.ActivityGenericPlaceholderBinding

class ChargesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenericPlaceholderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericPlaceholderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.toolbar.title = "Parking Charges"
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.tvPlaceholder.text = "Current parking rates and charges will be displayed here."
    }
}
