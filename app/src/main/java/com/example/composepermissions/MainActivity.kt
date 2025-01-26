package com.example.composepermissions
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import com.example.composepermissions.ui.theme.ComposePermissionsTheme
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePermissionsTheme {
                    PermissionExampleScreen()
            }
        }
    }
}
@SuppressLint("MissingPermission")
@Composable
fun PermissionExampleScreen() {
    val context = LocalContext.current //acesso ao contetxo para pedir permissões
    val statusText = remember { mutableStateOf("Status...") } //Caixa de texto de status


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ações que precisam de permissão", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        ActionItem("Vibrate Device") {
            val buzzer = context.getSystemService<Vibrator>()
            val pattern = longArrayOf(0, 100, 50, 600)
            buzzer?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                      buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))

                } else {
                    //deprecated in API 26
                      buzzer.vibrate(pattern, -1)

                }
            }

            statusText.value = "Vibrando dispositivo"
        }

        ActionItem("Fazer uma ligação") {

        }

        ActionItem("Tirar uma foto") {

        }

        ActionItem("Obter a localização") {

        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = statusText.value, fontSize = 16.sp)
    }
}

@Composable
fun ActionItem(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 20.sp)
    }
}

