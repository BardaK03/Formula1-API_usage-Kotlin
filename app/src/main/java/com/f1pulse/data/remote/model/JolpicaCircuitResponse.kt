package com.f1pulse.data.remote.model

import com.google.gson.annotations.SerializedName

data class JolpicaCircuitResponse(
    @SerializedName("MRData")
    val mrData: CircuitMRData
)

data class CircuitMRData(
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
    @SerializedName("CircuitTable")
    val circuitTable: CircuitTable?
)

data class CircuitTable(
    @SerializedName("Circuits")
    val circuits: List<Circuit>?
)

data class Circuit(
    @SerializedName("circuitId")
    val circuitId: String,
    @SerializedName("url")
    val url: String?,
    @SerializedName("circuitName")
    val circuitName: String?,
    @SerializedName("Location")
    val location: Location?
)

data class Location(
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("long")
    val long: String?,
    @SerializedName("locality")
    val locality: String?,
    @SerializedName("country")
    val country: String?
)
