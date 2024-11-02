package com.example.climatic.model.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class NominatimResult(
    val display_name: String,
    val lat: String,
    val lon: String
)

interface NominatimApiService {
    @GET("search")
    fun searchCity(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 5
    ): Call<List<NominatimResult>>
}
