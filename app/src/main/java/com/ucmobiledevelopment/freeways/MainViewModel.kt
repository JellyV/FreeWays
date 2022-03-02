package com.ucmobiledevelopment.freeways

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.service.IncidentService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){
    var incidents : MutableLiveData<List<Incident>> = MutableLiveData<List<Incident>>()
    var incidentService : IncidentService = IncidentService()


    fun fetchIncidents(){
        viewModelScope.launch {
           var innerIncidents = incidentService.fetchIncidents(2019, 2020, 33, 1)
            incidents.postValue(innerIncidents)
        }
    }
}