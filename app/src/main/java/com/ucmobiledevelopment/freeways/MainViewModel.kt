package com.ucmobiledevelopment.freeways

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.Photo
import com.ucmobiledevelopment.freeways.dto.User
import com.ucmobiledevelopment.freeways.service.IIncidentService
import com.ucmobiledevelopment.freeways.service.IncidentService
import kotlinx.coroutines.launch

class MainViewModel(var incidentService : IIncidentService = IncidentService()) : ViewModel(){
    val photos: ArrayList<Photo> = ArrayList<Photo>()
    internal val NEW_INCIDENT = "New Incident"
    var incidents : MutableLiveData<List<Incident>> = MutableLiveData<List<Incident>>()
    var user : User? = null
    val eventIncidents : MutableLiveData<List<Incident>> = MutableLiveData<List<Incident>>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = FirebaseStorage.getInstance().getReference()

    val mySelectedIncident : MutableLiveData<Incident> = MutableLiveData<Incident>()

    init{
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
                        val incident = it.toObject(Incident::class.java)
                        incident?.let {
                            allIncidents.add(it)
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
            val document =
                if (incident.incidentId == null || incident.incidentId.isEmpty()) {
                // Create a new incident
                firestore.collection("users").document(user.uid).collection("incidents").document()
            } else {
                // Update an existing incident
                firestore.collection("users").document(user.uid).collection("incidents").document(incident.incidentId)
            }
            incident.incidentId = document.id
            val handle = document.set(incident)
            handle.addOnSuccessListener { Log.d("Firebase", "Document saved")
            if (photos.isNotEmpty()) {
                uploadPhotos(incident)
            }
            }
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

    fun uploadPhotos(incident: Incident) {
        photos.forEach {
            photo ->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image Uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener { 
                    remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo, incident)
                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No message")
            }
        }
    }

    // TO DO: We will add this after the incident update functionality is created
    fun updatePhotoDatabase(photo: Photo, incident: Incident) {
        user?.let {
            user ->
            var photoCollection = firestore.collection("users").document(user.uid).collection("incidents").document(incident.incidentId).collection("photos")
            var handle = photoCollection.add(photo)
            handle.addOnSuccessListener {
                Log.i(TAG, "Successfully updated photo metadata")
                photo.id = it.id
                firestore.collection("users").document(user.uid).collection("incidents").document(incident.incidentId).collection("photos").document(photo.id).set(photo)
            }
            handle.addOnFailureListener {
                Log.e(TAG, "Error updating photo data: ${it.message}")
            }
        }
    }

    fun fetchMyIncidents() {
        user?.let{
            user ->
            var myIncidentsCollection = firestore.collection("users").document(user.uid).collection("incidents")
            var myIncidentsListener = myIncidentsCollection.addSnapshotListener {
                querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    querySnapshot ->
                    var documents = querySnapshot.documents
                    var inIncidents = ArrayList<Incident>()
                    documents?.forEach {
                        var incident = it.toObject(Incident::class.java)
                        incident?.let{
                            incident ->
                            inIncidents.add(incident)
                        }
                    }
                    eventIncidents.value = inIncidents
                }
            }
        }

    }

    fun fetchMyIncident(incidentId: String) {
        user?.let { user ->

            var myIncidentfromFirebase =
                firestore.collection("users").document(user.uid).collection("incidents").document(incidentId)
            var myIncidentListener = myIncidentfromFirebase.addSnapshotListener {
                    querySnapshot, firebaseFirestoreException ->
                    querySnapshot?.let { querySnapshot ->
                        myIncidentfromFirebase.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                                    mySelectedIncident.value = document.toObject(Incident::class.java)
                                } else {
                                    Log.d(TAG, "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "get failed with ", exception)
                            }
                    }
                }
        }
    }

    fun deleteIncident(incident: Incident) {
        user?.let {
            user ->
            var myIncidentsCollection = firestore.collection("users").document(user.uid).collection("incidents")
            myIncidentsCollection.document(incident.incidentId).delete()
        }
    }

    fun deleteAllGovernmentIncidents() {

        var myIncidentsCollection = firestore.collection("users").document("government").collection("incidents")
        var myIncidentsListener = myIncidentsCollection.addSnapshotListener {
                querySnapshot, firebaseFirestoreException ->
            querySnapshot?.let {
                    querySnapshot ->
                var documents = querySnapshot.documents
                documents?.forEach {
                    it.reference.delete()
                }
            }
        }
    }

    fun populateDatabaseWithGovernmentIncidents(fromCaseYear: Int, toCaseYear: Int, state: Int, county: Int) {

        viewModelScope.launch {
            var allAPIIncidents = incidentService.fetchIncidents(fromCaseYear, toCaseYear, state, county)
            var x = "fs"
            allAPIIncidents?.forEach{

                val document = firestore.collection("users").document("government").collection("incidents").document()
                it.incidentId = document.id
                val handle = document.set(it)
                handle.addOnSuccessListener {
                    Log.d("Firebase", "Document saved") }
                handle.addOnFailureListener {
                    Log.e("Firebase", "Save failed $it") }

            }
        }

    }




}