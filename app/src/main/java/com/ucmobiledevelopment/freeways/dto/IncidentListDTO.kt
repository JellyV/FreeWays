package com.ucmobiledevelopment.freeways.dto

data class IncidentListDTO(
    val Count: Int = 0,
    val Message: String = "",
    val Results: List<List<IncidentDTO>>,
    val SearchCriteria: String = "",
)