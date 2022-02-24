package com.ucmobiledevelopment.freeways.service

import com.ucmobiledevelopment.freeways.RetrofitClientInstance
import com.ucmobiledevelopment.freeways.dao.IIncidentDAO
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.IncidentListDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class IncidentService {

    suspend fun fetchIncidents() : IncidentListDTO? {
       return withContext(Dispatchers.IO){
            val service = RetrofitClientInstance.retrofitInstance?.create(IIncidentDAO::class.java)
            val incidentListDTO = async {service?.getAllIncidents()}
            var result = incidentListDTO.await()?.awaitResponse()?.body()
            return@withContext result
        }


    }

}