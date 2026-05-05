package com.example.apptea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp  // ★ ESTA ES LA LÍNEA QUE FALTABA ★
import com.example.apptea.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TeaCommunicationApp()
                }
            }
        }
    }
}

@Composable
fun TeaCommunicationApp() {
    var currentScreen by remember { mutableStateOf("welcome") }

    when (currentScreen) {
        "welcome" -> {
            WelcomeScreen(
                onTutorClick = { currentScreen = "pin" },
                onNinoClick = { currentScreen = "nino" }
            )
        }
        "pin" -> {
            PinScreen(
                onPinSuccess = { currentScreen = "tutor" },
                onBack = { currentScreen = "welcome" }
            )
        }
        "tutor" -> {
            TutorPanelScreen(
                onBack = { currentScreen = "welcome" }
            )
        }
        "nino" -> {
            // Pantalla del niño (temporal)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Pantalla del Niño - Próximamente")
                    Spacer(modifier = Modifier.height(16.dp))  // ← Aquí también estaba mal: decía "fp" en lugar de "dp"
                    Button(onClick = { currentScreen = "welcome" }) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}