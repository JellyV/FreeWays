package com.ucmobiledevelopment.freeways

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.service.IIncidentService
import com.ucmobiledevelopment.freeways.service.IncidentService
import kotlinx.coroutines.launch

class MainViewModel(var incidentService : IIncidentService = IncidentService()) : ViewModel(){
    var incidents : MutableLiveData<List<Incident>> = MutableLiveData<List<Incident>>()

    private lateinit var firestore: FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchIncidents(fromCaseYear: Int, toCaseYear: Int, state: Int, county: Int){
        viewModelScope.launch {
           var innerIncidents = incidentService.fetchIncidents(fromCaseYear, toCaseYear, state, county)
            incidents.postValue(innerIncidents)
        }
    }

    fun save(incident: Incident) {
        val document = if (incident.incidentId == null || incident.incidentId.isEmpty()) {
            // Create a new incident
            firestore.collection("incidents").document()
        } else {
            // Update an existing incident
            firestore.collection("incidents").document(incident.incidentId)
        }
        incident.incidentId = document.id
        val handle = document.set(incident)
        handle.addOnSuccessListener { Log.d("Firebase", "Document saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }
    }
}