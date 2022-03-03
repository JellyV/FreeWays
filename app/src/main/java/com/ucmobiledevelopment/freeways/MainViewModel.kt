package com.ucmobiledevelopment.freeways

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.service.IIncidentService
import com.ucmobiledevelopment.freeways.service.IncidentService
import kotlinx.coroutines.launch

class MainViewModel(var incidentService : IIncidentService = IncidentService()) : ViewModel(){
    var incidents : MutableLiveData<List<Incident>> = MutableLiveData<List<Incident>>()

    fun fetchIncidents(fromCaseYear: Int, toCaseYear: Int, state: Int, county: Int){
        viewModelScope.launch {
           var innerIncidents = incidentService.fetchIncidents(fromCaseYear, toCaseYear, state, county)
            incidents.postValue(innerIncidents)
        }
    }
}