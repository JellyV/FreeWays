package com.ucmobiledevelopment.freeways.dao

import retrofit2.Call
import retrofit2.http.GET
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.IncidentDTO
import com.ucmobiledevelopment.freeways.dto.IncidentListDTO
import retrofit2.http.Query

interface IIncidentDAO {

    @GET("CrashAPI/crashes/GetCrashesByLocation?format=json")
    fun getAllIncidents(
        @Query("fromCaseYear") fromCaseYear: Int, @Query("toCaseYear") toCaseYear: Int,
        @Query("state") state: Int, @Query("county") county: Int)
        : Call<IncidentListDTO>

}