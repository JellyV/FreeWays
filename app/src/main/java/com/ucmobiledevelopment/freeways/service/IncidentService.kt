package com.ucmobiledevelopment.freeways.service

import com.ucmobiledevelopment.freeways.RetrofitClientInstance
import com.ucmobiledevelopment.freeways.dao.IIncidentDAO
import com.ucmobiledevelopment.freeways.dto.Incident
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class IncidentService {

    suspend fun fetchIncidents() : List<Incident>? {
        withContext(Dispatchers.IO){
            val service = RetrofitClientInstance.retrofitInstance?.create(IIncidentDAO::class.java)
            val incidents = async {service?.getAllIncidents()}
            var result = incidents.await()?.awaitResponse()?.body()
            return@withContext result
        }


    }

}