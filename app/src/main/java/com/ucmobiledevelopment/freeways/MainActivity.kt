package com.ucmobiledevelopment.freeways

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.User
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import com.ucmobiledevelopment.freeways.ui.theme.Purple500
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var selectedIncident: Incident? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToIncidents()
            }

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

                val context = LocalContext.current

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = "Report Incident") },
                            onClick = {
                                val intent = Intent(context, ReportIncidentActivity::class.java)
                                context.startActivity(intent)
                            },
                            icon = { Icon(Icons.Filled.Add, "@drawable/plus_icon") },
                            backgroundColor = Purple500
                        )
                    }
                }

            }
        }
    }


    @Composable
    fun IncidentInfo(name: String, incidents: List<Incident> = ArrayList<Incident>()) {
        var inStateName by remember { mutableStateOf("") }
        var inCountyName by remember { mutableStateOf("") }
        var inCityName by remember { mutableStateOf("") }
        var inLatitude by remember { mutableStateOf("") }
        var inLongitude by remember { mutableStateOf("") }
        var inWay1 by remember { mutableStateOf("") }
        var inWay2 by remember { mutableStateOf("") }
        var inVehiclesInvolved by remember { mutableStateOf("") }
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
                Button(
                    onClick = {
                        val incidentInfo = Incident("1").apply {

                            stateId = 0
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
                        viewModel.saveIncident(incidentInfo)
                        Toast.makeText(
                            context,
                            "$inCityName $inCountyName $inStateName $inLatitude $inLongitude $inWay1 $inWay2 $inVehiclesInvolved",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {
                    Text(text = "Save")
                }

                Button(
                    onClick = {
                        signIn()
                    }
                ) {
                    Text(text = "Logon")
                }
            }
        }


    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FreeWaysTheme {
            Greeting("Android")
        }
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()

        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)

    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }

    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
                viewModel.listenToIncidents()

            }

        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }
}

