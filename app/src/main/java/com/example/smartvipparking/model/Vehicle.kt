package com.example.smartvipparking.model

data class Vehicle(
    val id: String = "",
    val ownerId: String = "",
    val vehicleNumber: String = "",
    val model: String = "",
    val type: String = "" // Car, Bike, etc.
)
