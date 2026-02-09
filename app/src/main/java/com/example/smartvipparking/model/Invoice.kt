package com.example.smartvipparking.model

data class Invoice(
    val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val date: Long = 0L,
    val status: String = "Paid", // Paid, Pending
    val description: String = ""
)
