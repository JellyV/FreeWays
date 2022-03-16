package com.ucmobiledevelopment.freeways

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import com.ucmobiledevelopment.freeways.ui.theme.Purple200
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private  var selectedIncident: Incident? = null
    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchIncidents(2019, 2020, 40, 1)
            val incidents by viewModel.incidents.observeAsState(initial = emptyList())
            FreeWaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    IncidentInfo("Android", incidents)
                }
                //TO DO: var foo = incidents (mock data to test in debugger)
                //TO DO: var i = 1 + 1
            }
        }
    }
        //TO DO: Get id to be passed from Incident and saved by using let corountine
        //TO DO: ids needing to be passed [stateId and countyId]
        //TO DO: vehicles involved needs to be passed or converted from a string based on the users input.
        //TO DO: How do we get the dataIn without using the drop downs.

    @Composable
    fun IncidentInfo(name: String, incidents : List<Incident> = ArrayList<Incident>()) {
        var inStateName by remember { mutableStateOf("") }
        var inCountyName by remember { mutableStateOf("") }
        var inCityName by remember { mutableStateOf("") }
        var inLatitude by remember { mutableStateOf("") }
        var inLongitude by remember { mutableStateOf("") }
        var inWay1 by remember { mutableStateOf("") }
        var inWay2 by remember { mutableStateOf("") }
        var inVehiclesInvolved by remember { mutableStateOf("") }
        val context = LocalContext.current
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
            Button(
                onClick = {
                    var incidentInfo = Incident().apply {

                        stateId =  0
                        stateName = inStateName
                        countyId = 0
                        countyName = inCountyName
                        cityName = inCityName
                        latitude = inLatitude
                        longitude = inLongitude
                        way1 = inWay1
                        way2 = inWay2
                        vehiclesInvolved = 0

                    }
                    viewModel.save(incidentInfo)
                    Toast.makeText(
                        context,
                        "$inCityName $inCountyName $inStateName $inLatitude $inLongitude $inWay1 $inWay2 $inVehiclesInvolved",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                Text(text = "Save")
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FreeWaysTheme {

        }
    }
}

