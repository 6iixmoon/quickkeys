package com.sam.quickkeys.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val type: String,
    val name: String,
    val model: String,
    val pricePerDay: Double,  // Ensure this field is named 'pricePerDay' and used correctly in queries
    val isAvailable: Boolean,
    val description: String,
    val imageUrl: String
)
