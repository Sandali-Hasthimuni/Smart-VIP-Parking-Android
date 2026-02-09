package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.databinding.ActivityGenericPlaceholderBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenericPlaceholderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericPlaceholderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.toolbar.title = "Parking History"
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.tvPlaceholder.text = "Your parking history will appear here."
    }
}
