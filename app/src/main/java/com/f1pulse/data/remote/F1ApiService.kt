package com.f1pulse.data.remote

import com.f1pulse.data.remote.model.CircuitResponse
import com.f1pulse.data.remote.model.JolpicaDriverResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * F1 API Service interface for accessing the Jolpi.ca F1 API
 * Documentation available at: https://github.com/jolpica/jolpica-f1/tree/main/docs
 */
interface F1ApiService {
    @GET("f1/{year}/drivers.json")
    suspend fun getDrivers(@Path("year") year: String = "2023"): JolpicaDriverResponse

    @GET("f1/drivers/{driverId}.json")
    suspend fun getDriverById(@Path("driverId") driverId: String): JolpicaDriverResponse

    @GET("f1/{year}/circuits.json")
    suspend fun getCircuits(@Path("year") year: String = "2023"): CircuitResponse

    @GET("f1/circuits/{circuitId}.json")
    suspend fun getCircuitById(@Path("circuitId") circuitId: String): CircuitResponse
}
