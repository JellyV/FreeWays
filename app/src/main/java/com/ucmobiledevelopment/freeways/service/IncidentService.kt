package com.ucmobiledevelopment.freeways.service

import com.ucmobiledevelopment.freeways.RetrofitClientInstance
import com.ucmobiledevelopment.freeways.dao.IIncidentDAO
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.IncidentListDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.lang.Exception


interface IIncidentService {
    suspend fun fetchIncidents(fromCaseYear: Int, toCaseYear: Int, state: Int, county: Int) : List<Incident>?
}

class IncidentService : IIncidentService {



    override suspend fun fetchIncidents(fromCaseYear: Int, toCaseYear: Int, state: Int, county: Int) : List<Incident>? {

        return withContext(Dispatchers.IO) {
            var incidentList: MutableList<Incident> = mutableListOf()

            val service = RetrofitClientInstance.retrofitInstance?.create(IIncidentDAO::class.java)
            val incidentListDTO =
                async { service?.getAllIncidents(fromCaseYear, toCaseYear, state, county) }
            var result = incidentListDTO.await()?.awaitResponse()?.body()

            if (result != null) {
                if(result.Results[0] != null){
                    result.Results[0].forEach{
                        var newIncident: Incident = Incident()

                        newIncident.cityName = it.CITYNAME ?: ""
                        newIncident.countyId = if(it.COUNTY != null) it.COUNTY.toInt() else 0
                        newIncident.countyName = it.COUNTYNAME ?: ""
                        newIncident.stateId = if(it.STATE != null) it.STATE.toInt() else 0
                        newIncident.stateName = it.STATENAME ?: ""
                        newIncident.latitude = it.LATITUDE ?: ""
                        newIncident.longitude = it.LONGITUD ?: ""
                        newIncident.caseId =  if(it.ST_CASE != null) it.ST_CASE.toInt() else 0
                        newIncident.way1 = it.TWAY_ID ?: ""
                        newIncident.way2 = it.TWAY_ID2 ?: ""
                        newIncident.vehiclesInvolved =  if(it.TOTALVEHICLES != null) it.TOTALVEHICLES.toInt() else 0

                        incidentList.add(newIncident)

                    }
                }
            }

            return@withContext incidentList
        }




    }

}