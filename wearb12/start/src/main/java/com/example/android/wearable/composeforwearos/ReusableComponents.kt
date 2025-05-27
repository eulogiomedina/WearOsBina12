package com.example.android.wearable.composeforwearos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.PendingActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.example.android.wearable.composeforwearos.theme.WearAppTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CodeEntryScreen(
    modifier: Modifier = Modifier,
    onAccept: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BIENVENIDO",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ingresa el código generado en la aplicación para continuar",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(5) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAccept,
            modifier = Modifier.defaultMinSize(minWidth = 80.dp)
        ) {
            Text("Aceptar")
        }
    }
}

@WearPreviewDevices
@Composable
fun CodeEntryScreenPreview() {
    WearAppTheme {
        CodeEntryScreen()
    }
}

@Composable
fun EstadoPagadoChip() {
    Chip(
        label = { Text("Pagado", fontWeight = FontWeight.Bold) },
        secondaryLabel = { Text("Pago completado con éxito") },
        icon = {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "Pagado",
                tint = Color.Green
            )
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color(0xFF4CAF50),
            contentColor = Color.White
        ),
        onClick = { }
    )
}

@Composable
fun EstadoDebesChip() {
    Chip(
        label = { Text("Debes", fontWeight = FontWeight.Bold) },
        secondaryLabel = { Text("Tienes pendiente realizar el pago") },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = "Debes",
                tint = Color.Red
            )
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color(0xFFD32F2F),
            contentColor = Color.White
        ),
        onClick = { }
    )
}

@Composable
fun EstadoPendientesChip() {
    Chip(
        label = { Text("Pagos pendientes", fontWeight = FontWeight.Bold) },
        secondaryLabel = { Text("Tienes varios pagos pendientes") },
        icon = {
            Icon(
                imageVector = Icons.Rounded.PendingActions,
                contentDescription = "Pendientes",
                tint = Color(0xFFFFEB3B)
            )
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color(0xFFFF9800),
            contentColor = Color.Black
        ),
        onClick = { }
    )
}

@Composable
fun EstadoPagadoScreen(onBack: () -> Unit) {
    val proximaFecha = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 7)
    }
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Chip(
            label = { Text("Pagado", fontWeight = FontWeight.Bold) },
            secondaryLabel = { Text("Pago completado con éxito") },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Pagado",
                    tint = Color.White
                )
            },
            colors = ChipDefaults.chipColors(
                backgroundColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ),
            onClick = onBack // Este chip también sirve como botón de regreso
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Próxima fecha de pago:",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = formato.format(proximaFecha.time),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}

@WearPreviewDevices
@Composable
fun EstadoPagadoChipPreview() {
    WearAppTheme {
        EstadoPagadoChip()
    }
}

@WearPreviewDevices
@Composable
fun EstadoDebesChipPreview() {
    WearAppTheme {
        EstadoDebesChip()
    }
}

@WearPreviewDevices
@Composable
fun EstadoPendientesChipPreview() {
    WearAppTheme {
        EstadoPendientesChip()
    }
}



