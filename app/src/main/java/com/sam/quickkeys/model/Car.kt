package com.sam.quickkeys.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    val id: Int,
    val name: String,
    val model: String,
    val pricePerDay: Double,  // Ensure this is Double
    val isAvailable: Boolean,
    val description: String, // Ensure this field exists
    val imageUrl: String
)
