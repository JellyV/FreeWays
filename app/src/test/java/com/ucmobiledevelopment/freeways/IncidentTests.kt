package com.ucmobiledevelopment.freeways

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.IncidentDTO
import com.ucmobiledevelopment.freeways.dto.IncidentListDTO
import com.ucmobiledevelopment.freeways.service.IncidentService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class IncidentTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var incidentService : IncidentService
    var allIncidents: List<Incident>? = ArrayList<Incident>()






    @Test
    fun `Given incident API is available When I fetch incidents from 2019 to 2020 in state 40 county 1 Then results should contain a case with caseId 400434`() = runTest{
        givenIncidentServiceIsInitialized()
        whenIncidentDataAreReadAndParsed()
        thenTheIncidentCollectionShouldContainIncidentWithCaseId400434()
    }

    private fun givenIncidentServiceIsInitialized() {
        incidentService = IncidentService()
    }

    private suspend fun whenIncidentDataAreReadAndParsed() {
        allIncidents = incidentService.fetchIncidents(2019, 2020, 40, 1)
    }

    private fun thenTheIncidentCollectionShouldContainIncidentWithCaseId400434() {
        assertNotNull(allIncidents)
        assertTrue(allIncidents!!.isNotEmpty())
       var hasCaseId400434 = false
       allIncidents!!.forEach {
             if(it.caseId.equals(400434)){
               hasCaseId400434 = true
           }
       }
       assertTrue(hasCaseId400434)
    }








}