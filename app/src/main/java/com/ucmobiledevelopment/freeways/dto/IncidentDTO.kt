package com.ucmobiledevelopment.freeways.dto

/**
 * Only used for JSON parsing.
 */
data class IncidentDTO(
    val CITY: String? = "",
    val CITYNAME: String? = "",
    val COUNTY: String? = "",
    val COUNTYNAME: String? = "",
    val CaseYear: String? = "",
    val FATALS: String? = "",
    val LATITUDE: String? = "",
    val LONGITUD: String? = "",
    val STATE: String? = "",
    val STATENAME: String? = "",
    val ST_CASE: String? = "",
    val TOTALVEHICLES: String? = "",
    val TWAY_ID: String? = "",
    val TWAY_ID2: String? = "",
    val VE_FORMS: String? = ""
)