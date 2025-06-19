package com.example.android.wearable.composeforwearos

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONArray
import java.io.Serializable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// 🔸 Función que retorna color de fondo según tipo
fun getNotificacionColor(tipo: String): Color {
    return when (tipo) {
        "EstadoPago" -> Color(0xFFE53935)        // Rojo
        "RecordatorioPago" -> Color(0xFF43A047)  // Verde
        "AtrasoPago" -> Color(0xFFFFA000)        // Naranja
        else -> Color(0xFF90CAF9)                // Azul por defecto
    }
}

// 🔸 Función que retorna ícono según tipo
fun getNotificacionIcon(tipo: String): ImageVector {
    return when (tipo) {
        "EstadoPago" -> Icons.Default.Close       // ❌
        "RecordatorioPago" -> Icons.Default.CheckCircle // ✅
        "AtrasoPago" -> Icons.Default.Warning     // ⚠️
        else -> Icons.Default.Notifications
    }
}

@Composable
fun NotificacionesScreen(userId: String, onVerDetalle: (Notificacion) -> Unit) {
    val notificaciones = remember { mutableStateListOf<Notificacion>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://backendgias.onrender.com/api/wearos/notificaciones/$userId")
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                val body = response.body?.string()

                if (response.isSuccessful && body != null) {
                    val jsonArray = JSONArray(body)
                    val nuevasNotificaciones = mutableListOf<Notificacion>()

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        nuevasNotificaciones.add(
                            Notificacion(
                                id = obj.getString("_id"),
                                titulo = obj.getString("titulo"),
                                mensaje = obj.getString("mensaje"),
                                tipo = obj.getString("tipo")
                            )
                        )
                    }

                    withContext(Dispatchers.Main) {
                        notificaciones.clear()
                        notificaciones.addAll(nuevasNotificaciones)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (notificaciones.isEmpty()) {
            item {
                Text("No tienes notificaciones", textAlign = TextAlign.Center)
            }
        } else {
            items(notificaciones) { noti ->
                val backgroundColor = getNotificacionColor(noti.tipo)
                val icon = getNotificacionIcon(noti.tipo)

                Chip(
                    label = {
                        Text(
                            noti.titulo,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    },
                    secondaryLabel = {
                        Text(noti.tipo, fontSize = 12.sp, maxLines = 1)
                    },
                    icon = {
                        Icon(imageVector = icon, contentDescription = null)
                    },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = backgroundColor,
                        contentColor = Color.White
                    ),
                    onClick = {
                        scope.launch {
                            // 🔄 Marcar como leída antes de navegar
                            withContext(Dispatchers.IO) {
                                try {
                                    val client = OkHttpClient()
                                    val request = Request.Builder()
                                        .url("https://backendgias.onrender.com/api/wearos/notificaciones/${noti.id}/leido")
                                        .put(RequestBody.create(null, ByteArray(0)))
                                        .build()
                                    client.newCall(request).execute().close()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            onVerDetalle(noti)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DetalleNotificacionScreen(noti: Notificacion, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = noti.titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = noti.mensaje, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Chip(label = { Text("Volver") }, onClick = onBack)
    }
}

// Modelo para una notificación
data class Notificacion(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val tipo: String
) : Serializable
