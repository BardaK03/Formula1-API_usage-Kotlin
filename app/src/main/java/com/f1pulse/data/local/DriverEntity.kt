package com.f1pulse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val driverId: String,
    val code: String?,
    val dateOfBirth: String?,
    val familyName: String?,
    val givenName: String?,
    val nationality: String?,
    val permanentNumber: String?,
    val url: String?,
    val team: String?,
    val isBookmarked: Boolean = false,
    val userId: String // New field to associate bookmark with a user
)
