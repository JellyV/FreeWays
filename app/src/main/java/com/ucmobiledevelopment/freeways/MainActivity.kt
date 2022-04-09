package com.ucmobiledevelopment.freeways

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.nfc.Tag
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.Photo
import com.ucmobiledevelopment.freeways.dto.User
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import com.ucmobiledevelopment.freeways.ui.theme.Purple500
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ComponentActivity() {

    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var selectedIncident: Incident? = null
    private val viewModel : MainViewModel by viewModel<MainViewModel>()
    private var strUri by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToIncidents()
            }
            FreeWaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {

                }

                val context = LocalContext.current
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
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
                            backgroundColor = Purple500
                        )
                    }
                }

            }
        }
    }



}



