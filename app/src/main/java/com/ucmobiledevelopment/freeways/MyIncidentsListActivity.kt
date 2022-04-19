package com.ucmobiledevelopment.freeways

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.User
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.ucmobiledevelopment.freeways.ui.theme.Purple500

class MyIncidentsListActivity : ComponentActivity() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToIncidents()
            }

            viewModel.fetchMyIncidents()

            FreeWaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    IncidentsList()
                }



            }

        }

    }

    @Composable
    fun IncidentsList(){
        val context = LocalContext.current
        Column {
            Events()
        }
    }

    @Composable
    private fun Events() {
        val myIncidents by viewModel.eventIncidents.observeAsState(initial = emptyList())
        LazyColumn(contentPadding = PaddingValues(horizontal = 4.dp, vertical = 5.dp)) {
            items (
                items = myIncidents,
                itemContent = {
                    EventListItem(incident = it)
                }
            )
        }

    }

    @Composable
    fun EventListItem(incident: Incident){
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 10.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            backgroundColor = Color.White,
            contentColor = contentColorFor(backgroundColor),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Purple500)
        ){
            Row{

                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .weight(8f)
                ) {
                    Text(text = "${incident.cityName}, ${incident.stateName}", style = typography.h6)
                    Text(text = "${incident.dateReported}", style = typography.h6)
                    Text(text = "County: ${incident.countyName}", style = typography.body1)
                    Text(text = "Vehicles Involved: ${incident.vehiclesInvolved}", style = typography.body1)
                    Text(text = "Way 1: ${incident.way1}", style = typography.body1)
                    Text(text = "Way 2: ${incident.way2}", style = typography.body1)
                    Text(text = "Location: (${incident.latitude}, ${incident.longitude})", style = typography.body1)
                    Text(text = "# ${incident.incidentId}", style = typography.caption)
                }


                Column(
                    Modifier.weight(2f)
                ){
                    Button (
                        modifier = Modifier.padding(top = 5.dp),
                        onClick = {
//                            val intent = Intent(context, MyIncidentDetailsActivity::class.java)
//                            intent.putExtra("selectedIncidentId", incident.incidentId)

                            val intent = Intent(context, MyIncidentDetailsActivity::class.java)
                            intent.putExtra("EXTRA_INCIDENT", incident)
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Details",
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }

                    Button (
                        modifier = Modifier.padding(top = 10.dp),
                        onClick = {
                            delete(incident)
                            Toast.makeText(
                                context,
                                "incident deleted",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",

                        )
                    }
                }
            }
        }


    }

    private fun delete(incident: Incident) {
        viewModel.deleteIncident(incident)
    }

}