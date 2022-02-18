package com.ucmobiledevelopment.freeways.dto

data class Incident(
    var incidentId: Int,
    var caseId: Int,
    var stateId: Int,
    var stateName: String,
    var countyId: Int,
    var countyName: String,
    var cityName: String,
    var latitude: String,
    var longitude: String,
    var way1: String,
    var way2: String,
    var vehiclesInvolved: Int,
)
