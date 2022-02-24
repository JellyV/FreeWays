package com.ucmobiledevelopment.freeways.dao

import retrofit2.Call
import retrofit2.http.GET
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.IncidentDTO
import com.ucmobiledevelopment.freeways.dto.IncidentListDTO

interface IIncidentDAO {

    @GET("CrashAPI/crashes/GetCrashesByLocation?fromCaseYear=2019&toCaseYear=2020&state=40&county=1&format=json")
    fun getAllIncidents() : Call<IncidentListDTO>

}