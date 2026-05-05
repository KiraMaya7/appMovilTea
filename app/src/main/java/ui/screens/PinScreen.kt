package com.example.apptea.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinScreen(
    onPinSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val correctPin = "1234"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón volver
        Text(
            text = "←",
            fontSize = 32.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Título
        Text(
            text = "Crea tu PIN",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3366CC)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingresa un PIN de 4 dígitos",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = "Este PIN protegerá el acceso al panel de tutor",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Indicadores del PIN (4 círculos)
        Row(
            modifier = Modifier.padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (index < pin.length) Color(0xFF3366CC)
                            else Color(0xFFE0E0E0)
                        )
                )
            }
        }

        // Teclado numérico
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                PinKey("1") { if (pin.length < 4) pin += it }
                PinKey("2") { if (pin.length < 4) pin += it }
                PinKey("3") { if (pin.length < 4) pin += it }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                PinKey("4") { if (pin.length < 4) pin += it }
                PinKey("5") { if (pin.length < 4) pin += it }
                PinKey("6") { if (pin.length < 4) pin += it }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                PinKey("7") { if (pin.length < 4) pin += it }
                PinKey("8") { if (pin.length < 4) pin += it }
                PinKey("9") { if (pin.length < 4) pin += it }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Box(modifier = Modifier.size(72.dp))  // Espacio vacío
                PinKey("0") { if (pin.length < 4) pin += it }
                PinKey("⌫") { if (pin.isNotEmpty()) pin = pin.dropLast(1) }
            }
        }

        // Verificar PIN automáticamente
        LaunchedEffect(pin) {
            if (pin.length == 4) {
                if (pin == correctPin) {
                    onPinSuccess()
                } else {
                    pin = ""
                }
            }
        }
    }
}

@Composable
fun PinKey(number: String, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick(number) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (number == "⌫") Color(0xFFFF6B6B) else Color(0xFF333333)
        )
    }
}