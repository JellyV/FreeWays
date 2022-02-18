package com.ucmobiledevelopment.freeways.dto

data class IncidentListDTO(
    val Count: Int,
    val Message: String,
    val Results: List<List<Incident>>,
    val SearchCriteria: String
)