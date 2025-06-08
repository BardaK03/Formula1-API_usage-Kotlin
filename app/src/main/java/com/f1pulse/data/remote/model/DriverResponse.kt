package com.f1pulse.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DriverResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: List<DriverDto>,
    @SerialName("message")
    val message: String
)

@Serializable
data class DriverDto(
    @SerialName("driverId")
    val driverId: String,
    @SerialName("driverRef")
    val driverRef: String,
    @SerialName("number")
    val permanentNumber: String?,
    @SerialName("code")
    val code: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("givenName")
    val givenName: String?,
    @SerialName("familyName")
    val familyName: String?,
    @SerialName("dateOfBirth")
    val dateOfBirth: String?,
    @SerialName("nationality")
    val nationality: String?,
    @SerialName("constructor")
    val constructor: ConstructorDto?
)

@Serializable
data class ConstructorDto(
    @SerialName("constructorId")
    val constructorId: String?,
    @SerialName("name")
    val name: String?
)
