package com.ucmobiledevelopment.freeways

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ucmobiledevelopment.freeways.ui.theme.FreeWaysTheme
import com.ucmobiledevelopment.freeways.ui.theme.Purple500


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeWaysTheme(){
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



