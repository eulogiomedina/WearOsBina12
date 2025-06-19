package com.example.android.wearable.composeforwearos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.wearable.composeforwearos.theme.WearAppTheme
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import com.google.android.horologist.compose.layout.ColumnItemType
import com.google.android.horologist.compose.layout.rememberResponsiveColumnPadding
import androidx.wear.compose.material3.*
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import android.net.Uri


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

        NavHost(navController = navController, startDestination = "simulated_watch_face") {

            // 🕒 Pantalla inicial con el reloj
            composable("simulated_watch_face") {
                SimulatedWatchFaceScreen(onAccept = {
                    navController.navigate("code_entry")
                })
            }

            // 🔐 Pantalla para ingresar token
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
                                CodeEntryScreen(onAccept = { userId ->
                                    navController.navigate("notificaciones/$userId")
                                })

                            }
                        }
                    }
                }
            }

            // 🔔 Pantalla de notificaciones (lista)
            composable(
                "notificaciones/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                NotificacionesScreen(
                    userId = userId,
                    onVerDetalle = { noti ->
                        navController.navigate("detalle/${Uri.encode(noti.titulo)}/${Uri.encode(noti.mensaje)}")
                    }
                )
            }

            // 📄 Pantalla de detalle de notificación
            composable(
                "detalle/{titulo}/{mensaje}",
                arguments = listOf(
                    navArgument("titulo") { type = NavType.StringType },
                    navArgument("mensaje") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val titulo = backStackEntry.arguments?.getString("titulo") ?: ""
                val mensaje = backStackEntry.arguments?.getString("mensaje") ?: ""

                DetalleNotificacionScreen(
                    noti = Notificacion(id = "", titulo = titulo, mensaje = mensaje, tipo = ""), // tipo lo puedes mostrar si gustas
                    onBack = { navController.popBackStack() }
                )
            }


        }
    }
}
