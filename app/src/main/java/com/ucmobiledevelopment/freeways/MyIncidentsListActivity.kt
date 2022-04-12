package com.ucmobiledevelopment.freeways

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext

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
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
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
        Row{

            Column {
                Text(text = incident.cityName, style = typography.h6)
                Text(text = incident.countyName, style = typography.caption)
                Text(text = incident.latitude, style = typography.caption)
                Text(text = incident.longitude, style = typography.caption)
            }

        }
    }

}