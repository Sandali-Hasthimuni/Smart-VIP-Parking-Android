package com.example.smartvipparking.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartvipparking.databinding.ActivityHistoryBinding
import com.example.smartvipparking.model.ParkingHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://smartvipparking-f3e3c-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val historyList = mutableListOf<ParkingHistory>()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(historyList)
        binding.rvHistory.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        val userId = auth.currentUser?.uid ?: return
        db.getReference("ParkingHistory").orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    historyList.clear()
                    for (child in snapshot.children) {
                        val item = child.getValue(ParkingHistory::class.java)
                        if (item != null) historyList.add(item)
                    }
                    historyList.sortByDescending { it.entryTime }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HistoryActivity, "Failed to load history", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
