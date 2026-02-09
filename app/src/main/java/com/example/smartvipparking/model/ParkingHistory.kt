package com.example.smartvipparking.model

data class ParkingHistory(
    val id: String = "",
    val userId: String = "",
    val vehicleNumber: String = "",
    val entryTime: Long = 0L,
    val exitTime: Long? = null,
    val parkingSpot: String = "",
    val status: String = "Active", // Active, Completed
    val amountPaid: Double = 0.0
)
