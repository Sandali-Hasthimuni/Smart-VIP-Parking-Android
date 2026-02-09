package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartvipparking.databinding.ActivityGenericPlaceholderBinding

class InvoicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenericPlaceholderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenericPlaceholderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.toolbar.title = "Invoices"
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.tvPlaceholder.text = "Your invoices will be listed here."
    }
}
