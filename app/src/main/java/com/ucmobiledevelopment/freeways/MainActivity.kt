package com.ucmobiledevelopment.freeways

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import com.ucmobiledevelopment.freeways.ui.theme.Purple500
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val applicationViewModel : ApplicationViewModel by viewModel<ApplicationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeWaysTheme(){
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    IncidentMap()
                }
                val context = LocalContext.current
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                    Column{
                        Row(Modifier
                            .padding(5.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExtendedFloatingActionButton (
                                text = {  Text(text = "Report Incident") },
                                onClick = { val intent = Intent(context, ReportIncidentActivity::class.java)
                                    context.startActivity(intent)
                                },
                                icon = { Icon(Icons.Filled.Add,"@drawable/plus_icon") },
                                backgroundColor = Purple500,
                                contentColor = Color.White
                            )
                        }

                        Row(Modifier
                            .padding(5.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExtendedFloatingActionButton (
                                text = {  Text(text = "My Incidents") },
                                onClick = { val intent = Intent(context, MyIncidentsListActivity::class.java)
                                    context.startActivity(intent)
                                },
                                icon = { Icon(Icons.Filled.Add,"@drawable/plus_icon") },
                                backgroundColor = Purple500,
                                contentColor = Color.White
                            )
                        }
                    }

                }
            }
            prepLocationUpdates()
        }
    }

    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestSinglePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if (isGranted) {
            requestLocationUpdates()
        } else {
            Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestLocationUpdates() {
        applicationViewModel.startLocationUpdates()
    }

    @Composable
    private fun IncidentMap() {
        val cincinnati = LatLng(39.74, -84.51)
        val cameraPosition = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(cincinnati, 10f)
        }

        GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPosition) {
            Marker (position = cincinnati, title = "Cincinnati", snippet = "Marker in Cincinnati")
        }
    }
}