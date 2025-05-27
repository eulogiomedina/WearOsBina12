package com.example.android.wearable.composeforwearos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.wearable.composeforwearos.theme.WearAppTheme
import androidx.wear.compose.material3.*
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import com.google.android.horologist.compose.layout.ColumnItemType
import com.google.android.horologist.compose.layout.rememberResponsiveColumnPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    WearAppTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "code_entry") {
            composable("code_entry") {
                AppScaffold {
                    val listState = rememberTransformingLazyColumnState()
                    val transformationSpec = rememberTransformationSpec()

                    ScreenScaffold(
                        scrollState = listState,
                        contentPadding = rememberResponsiveColumnPadding(
                            first = ColumnItemType.BodyText,
                            last = ColumnItemType.Button
                        )
                    ) { contentPadding ->
                        TransformingLazyColumn(
                            state = listState,
                            contentPadding = contentPadding
                        ) {
                            item {
                                CodeEntryScreen(onAccept = {
                                    navController.navigate("estado_pagado")
                                })
                            }
                            item { EstadoPagadoChip() }
                            item { EstadoDebesChip() }
                            item { EstadoPendientesChip() }
                        }
                    }
                }
            }

            composable("estado_pagado") {
                EstadoPagadoScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
