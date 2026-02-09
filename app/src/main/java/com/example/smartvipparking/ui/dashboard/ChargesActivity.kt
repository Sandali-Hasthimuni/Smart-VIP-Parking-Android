package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.databinding.ActivityChargesBinding

class ChargesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChargesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}
