package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartvipparking.databinding.ActivityInvoicesBinding
import com.example.smartvipparking.model.Invoice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InvoicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoicesBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://smartvipparking-f3e3c-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val invoiceList = mutableListOf<Invoice>()
    private lateinit var adapter: InvoiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.rvInvoices.layoutManager = LinearLayoutManager(this)
        adapter = InvoiceAdapter(invoiceList)
        binding.rvInvoices.adapter = adapter

        loadInvoices()
    }

    private fun loadInvoices() {
        val userId = auth.currentUser?.uid ?: return
        db.getReference("Invoices").orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    invoiceList.clear()
                    for (child in snapshot.children) {
                        val item = child.getValue(Invoice::class.java)
                        if (item != null) invoiceList.add(item)
                    }
                    invoiceList.sortByDescending { it.date }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@InvoicesActivity, "Failed to load invoices", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
