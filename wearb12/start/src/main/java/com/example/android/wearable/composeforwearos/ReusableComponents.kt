package com.example.android.wearable.composeforwearos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.example.android.wearable.composeforwearos.theme.WearAppTheme
import kotlinx.coroutines.delay
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image

@Composable
fun CodeEntryScreen(
    modifier: Modifier = Modifier,
    onAccept: (String) -> Unit
) {
    val tokenInput = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val isLoading = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("BIENVENIDO", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ingresa el código generado en la aplicación para continuar",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))

        BasicTextField(
            value = tokenInput.value,
            onValueChange = { tokenInput.value = it },
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading.value = true
                val tokenClean = tokenInput.value.trim()
                validarTokenEnServidor(tokenClean) { userId ->
                    isLoading.value = false
                    if (userId != null) {
                        errorMessage.value = null
                        onAccept(userId)
                    } else {
                        errorMessage.value = "Token inválido"
                    }
                }
            },
            modifier = Modifier.defaultMinSize(minWidth = 80.dp),
            enabled = tokenInput.value.trim().length == 5 && !isLoading.value
        ) {
            Text(if (isLoading.value) "Validando..." else "Aceptar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        errorMessage.value?.let { msg ->
            Text(
                text = msg,
                color = Color.Red,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@WearPreviewDevices
@Composable
fun CodeEntryScreenPreview() {
    WearAppTheme {
        CodeEntryScreen(onAccept = {})
    }
}

@Composable
fun SimulatedWatchFaceScreen(onAccept: () -> Unit = {}) {
    val time = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            time.value = currentTime
            delay(1000)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.logogias),
            contentDescription = "Logo GIAS",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
                .padding(16.dp)
        ) {
            Text(text = time.value, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(24.dp))
            CompactChip(
                label = { Text("Ingresar", fontWeight = FontWeight.Bold) },
                onClick = onAccept
            )
        }
    }
}

fun validarTokenEnServidor(token: String, onResult: (String?) -> Unit) {
    val url = "https://backendgias.onrender.com/api/wearos/validar-token"
    val client = OkHttpClient()

    val json = JSONObject()
    json.put("token", token)

    val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder().url(url).post(body).build()
    val handler = Handler(Looper.getMainLooper())

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            handler.post { onResult(null) }
        }

        override fun onResponse(call: Call, response: Response) {
            val bodyString = response.body?.string()
            if (response.isSuccessful && bodyString != null) {
                val jsonObj = JSONObject(bodyString)
                val userId = jsonObj.getString("userId")
                handler.post { onResult(userId) }
            } else {
                handler.post { onResult(null) }
            }
        }
    })
}
