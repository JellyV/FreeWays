package com.ucmobiledevelopment.freeways

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ucmobiledevelopment.freeways.dto.Incident
import com.ucmobiledevelopment.freeways.dto.Photo
import com.ucmobiledevelopment.freeways.dto.User
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import androidx.compose.foundation.lazy.items
import com.ucmobiledevelopment.freeways.ui.theme.Purple500

/**
 * This activity lists all incidents reported by the user.
 */
class MyIncidentDetailsActivity : ComponentActivity() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private var strUri by mutableStateOf("")
    private val viewModel : MainViewModel by viewModel<MainViewModel>()
    private var selectedIncident : Incident = Incident()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToIncidents()
            }
            selectedIncident = intent.getSerializableExtra("EXTRA_INCIDENT") as Incident
            viewModel.fetchPhotos(selectedIncident)
            FreeWaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    IncidentInfo("Android", selectedIncident)
                }
            }
        }
    }

    @Composable
    fun IncidentInfo(name: String, selectedIncident: Incident) {
        var inStateName by remember { mutableStateOf(selectedIncident.stateName) }
        var inCountyName by remember { mutableStateOf(selectedIncident.countyName) }
        var inCityName by remember { mutableStateOf(selectedIncident.cityName) }
        var inLatitude by remember { mutableStateOf(selectedIncident.latitude) }
        var inLongitude by remember { mutableStateOf(selectedIncident.longitude) }
        var inWay1 by remember { mutableStateOf(selectedIncident.way1) }
        var inWay2 by remember { mutableStateOf(selectedIncident.way2) }
        var inVehiclesInvolved by remember { mutableStateOf(selectedIncident.vehiclesInvolved.toString()) }
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
                                    incidentId = selectedIncident.incidentId
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
                        Button(
                            onClick = {
                                takePhoto()
                            }
                        ) {
                            Text(text = "Photo")
                        }
                    }
                }
                PhotoEvents()
            }
        }
    }

    @Composable
    private fun PhotoEvents () {
        val photos by viewModel.eventPhotos.observeAsState(initial = emptyList())
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical=8.dp), modifier = Modifier.fillMaxHeight()) {
            items (
                items = photos,
                itemContent = {
                    EventPhotoListItem(photo = it)
                }
            )
        }
    }

    @Composable
    fun EventPhotoListItem(photo: Photo){
        var inDescription by remember(photo.id) { mutableStateOf(photo.description) }
        Card(
            modifier = Modifier
                .padding(horizontal = 2.dp, vertical = 2.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            backgroundColor = Color.White,
            contentColor = contentColorFor(SnackbarDefaults.backgroundColor),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Purple500)
        )
        {
            Row {
                Column(Modifier.weight(3f)) {
                    AsyncImage(
                        model = photo.localUri,
                        contentDescription = "Event Image",
                        Modifier
                            .width(74.dp)
                            .height(74.dp)
                    )
                }
                Column(Modifier.weight(4f)) {
                    Text(text = photo.id, style = MaterialTheme.typography.caption)
                    Text(
                        text = photo.dateTaken.toString(),
                        style = MaterialTheme.typography.caption
                    )
                    OutlinedTextField(
                        value = inDescription,
                        onValueChange = { inDescription = it },
                        label = { Text(stringResource(R.string.description)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(Modifier.weight(2f)) {

                    Button(
                        onClick = {
                            photo.description = inDescription
                            save(photo)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.SavePhoto),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            delete(photo)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.DeletePhoto),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }

    private fun save(photo: Photo) {
        viewModel.updatePhotoDatabase(photo = photo, incident = selectedIncident)
    }
    private fun delete(photo: Photo) {
        viewModel.delete(photo = photo, incident = selectedIncident)
    }

    private fun takePhoto() {
        if (hasCameraPermission() == PackageManager.PERMISSION_GRANTED && hasExternalStoragePermission() == PackageManager.PERMISSION_GRANTED){
            // User has already granted permission for these activities. Toggle the camera!
            invokeCamera()
        }
        else{
            // User has not granted permissions, so we must request.
            requestMultiplePermissionsLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
            resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value == true) {
                permissionGranted = it.value
            }
            else {
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted) {
            invokeCamera()
        }
        else {
            Toast.makeText(this, getString(R.string.cameraPermissionDenied), Toast.LENGTH_LONG).show()
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try{
            uri = FileProvider.getUriForFile(this, "com.ucmobiledevelopment.freeways.fileprovider", file)
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error: ${e.message}")
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile() : File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Incident_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success) {
            Log.i(ContentValues.TAG, "Image Location: $uri")
            strUri = uri.toString()
            val photo = Photo(localUri = uri.toString())
            viewModel.photos.add(photo)
        }
        else {
            Log.e(ContentValues.TAG, "Image not saved. $uri")
        }
    }
    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    private fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}