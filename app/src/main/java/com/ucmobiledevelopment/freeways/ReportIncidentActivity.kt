package com.ucmobiledevelopment.freeways

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import com.ucmobiledevelopment.freeways.ui.theme.Purple200

class ReportIncidentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeWaysTheme {
                Surface(
                    color = Purple200,
                    modifier = Modifier.fillMaxWidth()) {
                        reportDataFields()
                }
            }
        }
    }
}

@Composable
fun reportDataFields(){
    var stateId by remember { mutableStateOf("")}
    var StateName by remember { mutableStateOf("")}
    var countyId by remember { mutableStateOf("")}
    var countyName by remember { mutableStateOf("")}
    var cityName by remember { mutableStateOf("")}
    var latitude by remember { mutableStateOf("")}
    var longitude by remember { mutableStateOf("")}
    var way1 by remember { mutableStateOf("")}
    var way2 by remember { mutableStateOf("")}
    var vehiclesInvolved by remember { mutableStateOf("")}
    val context = LocalContext.current

    Column {
        OutlinedTextField(
            value = stateId,
            onValueChange = { stateId = it },
            label = { Text(stringResource(R.string.stateID)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = StateName,
            onValueChange = { StateName = it },
            label = { Text(stringResource(R.string.StateName)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = countyId,
            onValueChange = { countyId = it },
            label = { Text(stringResource(R.string.CountyID)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = countyName,
            onValueChange = { countyName = it },
            label = { Text(stringResource(R.string.CountyName)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text(stringResource(R.string.CityName)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text(stringResource(R.string.Latitude)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text(stringResource(R.string.Longitude)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = way1,
            onValueChange = { way1 = it },
            label = { Text(stringResource(R.string.Way1)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = way2,
            onValueChange = { way2 = it },
            label = { Text(stringResource(R.string.Way2)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = vehiclesInvolved,
            onValueChange = { vehiclesInvolved = it },
            label = { Text(stringResource(R.string.VehiclesInvolved)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                Toast.makeText(context, "$stateId $StateName $countyId $countyName $cityName $latitude $longitude $way1 $way2 $vehiclesInvolved",
                Toast.LENGTH_LONG).show()
            }
        ) {
            Text(text = "Save")
        }
    }
}

@Preview
@Composable
fun DefaultPreview2() {
    FreeWaysTheme {
        reportDataFields()
    }
}