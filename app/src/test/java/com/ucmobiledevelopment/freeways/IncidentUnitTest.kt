package com.ucmobiledevelopment.freeways

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.service.IncidentService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class IncidentUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mvm : MainViewModel

    lateinit var incidentService : IncidentService

    var allIncidents: List<Incident>? = ArrayList<Incident>()

    @MockK
    lateinit var mockIncidentService: IncidentService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    @Test
    fun `given a view model with live data when populated with incidents between 2019 and 2020, in state 40, county 1, results show incident with caseId 400132`(){
        givenViewModelIsInitializedWithMockData()
        whenIncidentServiceFetchIncidentsInvoked()
        thenResultsShouldContainIncidentWithCaseId400132()
    }


    private fun givenViewModelIsInitializedWithMockData() {
        val incidents = ArrayList<Incident>()
        incidents.add(Incident(stateId = 40, stateName = "Oklahoma", caseId = 400132, countyId = 1, countyName = "Adair", latitude = "35.64110000", longitude = "-94.678300000"))
        incidents.add(Incident(stateId = 40, stateName = "Oklahoma", caseId = 400429, countyId = 1, countyName = "Adair", latitude = "35.98840000", longitude = "-94.655000000"))
        incidents.add(Incident(stateId = 40, stateName = "Oklahoma", caseId = 400434, countyId = 1, countyName = "Adair", latitude = "35.82660000", longitude = "-94.613000000"))

        coEvery { mockIncidentService.fetchIncidents(2019, 2020, 40, 1) } returns incidents

        mvm = MainViewModel()
        mvm.incidentService = mockIncidentService
    }

    private fun whenIncidentServiceFetchIncidentsInvoked() {
        mvm.fetchIncidents(2019, 2020, 40, 1)
    }

    private fun thenResultsShouldContainIncidentWithCaseId400132() {
        var allIncidents : List<Incident>? = ArrayList<Incident>()
        val latch = CountDownLatch(1)
        val observer = object : Observer<List<Incident>> {
            override fun onChanged(receivedIncidents: List<Incident>?) {
                allIncidents = receivedIncidents
                latch.countDown()
                mvm.incidents.removeObserver(this)
            }
        }
        mvm.incidents.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allIncidents)
        assertTrue(allIncidents!!.isNotEmpty())
        var containsIncidentWithCaseId400132 = false
        allIncidents!!.forEach {
            if(it.caseId.equals(400132)){
                containsIncidentWithCaseId400132 = true
            }
        }
        assertTrue(containsIncidentWithCaseId400132)
    }

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