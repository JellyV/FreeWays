package com.ucmobiledevelopment.freeways

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import com.ucmobiledevelopment.freeways.ui.theme.Purple200
import com.ucmobiledevelopment.freeways.ui.theme.Purple500
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

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
                    color = MaterialTheme.colors.background
                ) {

                }
                val context = LocalContext.current
                ExtendedFloatingActionButton (
                    text = {  Text(text = "Report Incident") },
                    onClick = { val intent = Intent(context, ReportIncidentActivity::class.java)
                                context.startActivity(intent)
                              },
                    icon = { Icon(Icons.Filled.Add,"@drawable/plus_icon") },
                    backgroundColor = Purple500
                )
                //TO DO: var foo = incidents (mock data to test in debugger)
                //TO DO: var i = 1 + 1
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