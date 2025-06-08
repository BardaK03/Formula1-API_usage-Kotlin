package com.f1pulse.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CircuitResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: List<CircuitDto>,
    @SerialName("message")
    val message: String
)

@Serializable
data class CircuitDto(
    @SerialName("circuitId")
    val circuitId: String,
    @SerialName("circuitRef")
    val circuitRef: String?,
    @SerialName("name")
    val circuitName: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("Location")
    val location: LocationDto?
)

@Serializable
data class LocationDto(
    @SerialName("lat")
    val lat: String?,
    @SerialName("long")
    val long: String?,
    @SerialName("locality")
    val locality: String?,
    @SerialName("country")
    val country: String?
)
