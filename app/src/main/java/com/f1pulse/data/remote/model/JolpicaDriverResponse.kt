package com.f1pulse.data.remote.model

import com.google.gson.annotations.SerializedName

data class JolpicaDriverResponse(
    @SerializedName("MRData")
    val mrData: MRData
)

data class MRData(
    @SerializedName("xmlns")
    val xmlns: String,
    @SerializedName("series")
    val series: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("limit")
    val limit: String,
    @SerializedName("offset")
    val offset: String,
    @SerializedName("total")
    val total: String,
    @SerializedName("DriverTable")
    val driverTable: DriverTable?
)

data class DriverTable(
    @SerializedName("Drivers")
    val drivers: List<Driver>?
)

data class Driver(
    @SerializedName("driverId")
    val driverId: String,
    @SerializedName("permanentNumber")
    val permanentNumber: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("givenName")
    val givenName: String?,
    @SerializedName("familyName")
    val familyName: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?,
    @SerializedName("nationality")
    val nationality: String?
)