package com.ucmobiledevelopment.freeways.dao

import android.telecom.Call
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.IncidentDTO
import com.ucmobiledevelopment.freeways.dto.IncidentListDTO

import retrofit2.http.GET

interface IIncidentDAO {

    @GET("CrashAPI/crashes/GetCrashesByLocation?fromCaseYear=2019&toCaseYear=2020&state=40&county=1&format=json")
    fun getAllIncidents() : Call<ArrayList<IncidentListDTO<IncidentDTO<Incident>>>

}