package com.example.composepermissions
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.composepermissions.ui.theme.ComposePermissionsTheme


class MainActivity : ComponentActivity() {
    lateinit var requestPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
    lateinit var requestLocalPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ComposePermissionsTheme {
                PermissionExampleScreen()
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
            Text(
                text = "Ações que precisam de permissão",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

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
                //ligarFunc(context,"+5588999999999")
                //ligarFunc_Perm(context, "+558596122709", statusText)
             //   ligarWhen(context, "+558596122709",
             //       statusText, requestPermissionLauncher,
              //      showPermissionDialog)
            }

            ActionItem("Tirar uma foto") {

            }

            ActionItem("Obter a localização") {

            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = statusText.value, fontSize = 16.sp)
        }
        //Inicializando a parte de requisições
        requestPermissionLauncher= rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                statusText.value = "Permissão concedida"
                ligarFunc(context, "+558596122709")
            } else {
                statusText.value = "Permissão negada"
            }
        }
//Essa Dialog Abaixo só vai ser chamado se a variável showPermissionDialog mudar para true
//        val showPermissionDialog = remember { mutableStateOf(false) }
//        if (showPermissionDialog.value) {
//            AlertDialog(
//                onDismissRequest = { showPermissionDialog.value = false },
//                title = { Text("Permissão Necessária") },
//                text = { Text("Precisamos de permissão para realizar chamadas. Sem isso, não será possível ligar para o número desejado.") },
//                confirmButton = {
//                    TextButton(onClick = {
//                        showPermissionDialog.value = false
//                        requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
//                    }) {
//                        Text("Conceder")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = {
//                        showPermissionDialog.value = false
//                        statusText.value = "Permissão não concedida."
//                    }) {
//                        Text("Cancelar")
//                    }
//                }
//            )
//        }
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

    @SuppressLint("MissingPermission")
    fun ligarFunc(context: Context, numeroCall: String) {
        val uri = Uri.parse("tel:$numeroCall")
        val itLigar = Intent(Intent.ACTION_CALL, uri)
        context.startActivity(itLigar)
    }

    @SuppressLint("MissingPermission")
    fun ligarFunc_Perm(context: Context, numeroCall: String, statusText: MutableState<String>) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ligarFunc(context, numeroCall)
        } else {
            statusText.value = "Sem permissão para ligar"
        }
    }


    @SuppressLint("MissingPermission")

    fun ligarWhen(
        context: Context,
        numeroCall: String,
        statusText: MutableState<String>,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        showDialogState: MutableState<Boolean>
    ) {

        when {
            //Primeiro Caso do When - A permissão já foi concedida
            ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED -> {
                ligarFunc(context, numeroCall)
            }
            // Permissão foi negada, mas não para sempre
            shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                // Exibe um AlertDialog explicando o motivo da permissão
                showDialogState.value=true
            }
            // Permissão negada ou não foi pedida
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

}// Fim da Classe

