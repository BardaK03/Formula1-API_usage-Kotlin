package com.f1pulse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "circuits")
data class CircuitEntity(
    @PrimaryKey val circuitId: String,
    val circuitName: String?,
    val locality: String?,
    val country: String?,
    val lat: String?,
    val longitude: String?, // Changed from "long" to "longitude"
    val url: String?
)
