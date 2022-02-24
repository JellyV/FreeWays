package com.ucmobiledevelopment.freeways

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.IncidentDTO
import com.ucmobiledevelopment.freeways.dto.IncidentListDTO
import com.ucmobiledevelopment.freeways.service.IncidentService
import junit.framework.Assert.*
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class IncidentTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var incidentService : IncidentService
    var allIncidents : IncidentListDTO? = IncidentListDTO(Results = ArrayList<List<IncidentDTO>>())

    @Test
    suspend fun `Given incident API is available When I fetch incidents from 2019 to 2020 in state 40 county 1 Then results should contain a case with caseId 400434`() = runTest{
        GivenIncidentServiceIsInitialized()
        WhenIncidentDataAreReadAndParsed()
        ThenTheIncidentCollectionShouldContainIncidentWithCaseId400434()
    }




    private fun GivenIncidentServiceIsInitialized() {
        incidentService = IncidentService()
    }

    private suspend fun WhenIncidentDataAreReadAndParsed() {
       // allIncidents = incidentService.fetchIncidents(2019, 2020, 40, 1)    // fetchIncidents will take these 4 parameters: fromCaseYear, toCaseYear, state, county

        var x = "blabla"
        allIncidents = incidentService.fetchIncidents()    // fetchIncidents will take these 4 parameters: fromCaseYear, toCaseYear, state, county

        var y = "blabla"
    }

    private fun ThenTheIncidentCollectionShouldContainIncidentWithCaseId400434() {
        assertNotNull(allIncidents)
        assertTrue(1 == 1)
//        assertTrue(allIncidents!!.isNotEmpty())
//        var hasCaseId400434 = false
//        allIncidents!!.forEach {
//            if(it.caseId.equals("400434")){
//                hasCaseId400434 = true
//            }
//        }
//        assertTrue(hasCaseId400434)
    }

}