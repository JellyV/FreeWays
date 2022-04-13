package com.ucmobiledevelopment.freeways.dto

data class Incident(
    var incidentId: String = "",
    var caseId: String = "",
    var stateId: Int = 0,
    var stateName: String = "",
    var countyId: Int = 0,
    var countyName: String = "",
    var cityName: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var way1: String = "",
    var way2: String = "",
    var vehiclesInvolved: Int = 0,
    var dateReported: String = ""
)
