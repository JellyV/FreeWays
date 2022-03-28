package com.ucmobiledevelopment.freeways

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.User
import com.ucmobiledevelopment.freeways.service.IIncidentService
import com.ucmobiledevelopment.freeways.service.IncidentService
import kotlinx.coroutines.launch

class MainViewModel(var incidentService : IIncidentService = IncidentService()) : ViewModel(){
    internal val NEW_INCIDENT = "New Incident"
    var incidents : MutableLiveData<List<Incident>> = MutableLiveData<List<Incident>>()
    var user : User? = null

    private lateinit var firestore: FirebaseFirestore

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun listenToIncidents() {
        user?.let {
                user ->
            firestore.collection("users").document(user.uid).collection("incidents").addSnapshotListener {
                    snapshot, e ->
                // handle the error if there is one, and then return
                if (e != null) {
                    Log.w("Listen failed", e)
                    return@addSnapshotListener
                }
                // if we reached this point , there was not an error
                snapshot?.let {
                    val allIncidents = ArrayList<Incident>()
                    allIncidents.add(Incident(caseId = NEW_INCIDENT))
                    val documents = snapshot.documents
                    documents.forEach {
                        docSnap ->
                        val incident = docSnap.toObject(Incident::class.java)
                        incident?.let {
                            incident ->
                            allIncidents.add(incident)
                        }
                    }
                    incidents.value = allIncidents
                }
            }
        }

    }

    fun fetchIncidents(fromCaseYear: Int, toCaseYear: Int, state: Int, county: Int){
        viewModelScope.launch {
           var innerIncidents = incidentService.fetchIncidents(fromCaseYear, toCaseYear, state, county)
            incidents.postValue(innerIncidents)
        }
    }

    fun saveIncident(incident: Incident) {
        user?.let {
            user ->
            val document = if (incident.incidentId == null || incident.incidentId.isEmpty()) {
                // Create a new incident
                firestore.collection("users").document(user.uid).collection("incidents").document()
            } else {
                // Update an existing incident
                firestore.collection("users").document(user.uid).collection("incidents").document(incident.incidentId)
            }
            incident.incidentId = document.id
            val handle = document.set(incident)
            handle.addOnSuccessListener { Log.d("Firebase", "Document saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }
        }

    }

    fun saveUser() {
        user?.let {
            user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "User document saved") }
            handle.addOnFailureListener { Log.e("Firebase", "User save failed $it") }
        }

    }
}