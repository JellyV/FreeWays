package com.ucmobiledevelopment.freeways


import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.User
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import java.util.*
import kotlin.collections.ArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyIncidentDetailsActivity : ComponentActivity() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val viewModel : MainViewModel by viewModel<MainViewModel>()
    //private var selectedIncident by mutableStateOf(incidents())
    private var myExampleIncident = Incident()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToIncidents()
            }




            val selectedIncidentId = intent.getStringExtra("selectedIncidentId")


            val myTestIncident = intent.getSerializableExtra("EXTRA_INCIDENT") as Incident


            selectedIncidentId?.let {
                viewModel.fetchMyIncident(incidentId = selectedIncidentId)
            }

            //viewModel.fetchMyIncidents()

            val incidents by viewModel.incidents.observeAsState(initial = emptyList())
            myExampleIncident.apply {
                incidentId = "stuff"
                stateName = "Nebraska"
                cityName = "Omaha"

            }
            FreeWaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    IncidentInfo("Android", myTestIncident)
                }



            }

        }

    }

    @Composable
    fun IncidentInfo(name: String, myTestIncident: Incident) {

        val myIncidents11 by viewModel.eventIncidents.observeAsState(initial = emptyList())
        val mySelectedIncident by viewModel.mySelectedIncident.observeAsState(initial = Incident())

        var inStateName by remember { mutableStateOf(myTestIncident.stateName) }
        var inCountyName by remember { mutableStateOf(myTestIncident.countyName) }
        var inCityName by remember { mutableStateOf(myTestIncident.cityName) }
        var inLatitude by remember { mutableStateOf("RandomDAta") }
        var inLongitude by remember { mutableStateOf("Randomument DAta") }
        var inWay1 by remember { mutableStateOf("Somethingelse") }
        var inWay2 by remember { mutableStateOf("RandomDAta") }
        var inVehiclesInvolved by remember { mutableStateOf("10") }
        val context = LocalContext.current

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column {
                OutlinedTextField(
                    value = inStateName,
                    onValueChange = { inStateName = it },
                    label = { Text(stringResource(R.string.stateName)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inCountyName,
                    onValueChange = { inCountyName = it },
                    label = { Text(stringResource(R.string.countyName)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inCityName,
                    onValueChange = { inCityName = it },
                    label = { Text(stringResource(R.string.cityName)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inLatitude,
                    onValueChange = { inLatitude = it },
                    label = { Text(stringResource(R.string.latitude)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inLongitude,
                    onValueChange = { inLongitude = it },
                    label = { Text(stringResource(R.string.longitude)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inWay1,
                    onValueChange = { inWay1 = it },
                    label = { Text(stringResource(R.string.way1)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inWay2,
                    onValueChange = { inWay2 = it },
                    label = { Text(stringResource(R.string.way2)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = inVehiclesInvolved,
                    onValueChange = { inVehiclesInvolved = it },
                    label = { Text(stringResource(R.string.vehiclesInvolved)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                {
                    Row {
                        Button(
                            onClick = {

                                val sdf = SimpleDateFormat("MM-dd-yyyy' 'HH:mm:ss")
                                var incidentInfo = Incident().apply {
                                    incidentId = myTestIncident.incidentId
                                    stateId =  0
                                    stateName = inStateName
                                    countyId = 0
                                    countyName = inCountyName
                                    cityName = inCityName
                                    latitude = inLatitude
                                    longitude = inLongitude
                                    way1 = inWay1
                                    way2 = inWay2
                                    vehiclesInvolved = if(inVehiclesInvolved.toIntOrNull() != null){
                                        inVehiclesInvolved.toInt()
                                    }else{
                                        0
                                    }
                                    dateReported = sdf.format(Calendar.getInstance().time)

                                }

                                viewModel.saveIncident(incidentInfo)
                                Toast.makeText(
                                    context,
                                    "$inCityName $inCountyName $inStateName $inLatitude $inLongitude $inWay1 $inWay2 $inVehiclesInvolved",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )

                        {
                            Text(text = "Save")
                        }

                    }
                }

            }

        }
    }
}