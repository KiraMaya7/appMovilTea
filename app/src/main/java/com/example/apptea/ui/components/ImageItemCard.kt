package com.example.apptea.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apptea.data.models.ImageItem
import java.io.File

@Composable
fun ImageItemCard(
    image: ImageItem,
    userRole: String = "tutor",
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono/Imagen
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF9C27B0), Color(0xFFE040FB))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (image.imagePath != null) {
                    AsyncImage(
                        model = File(image.imagePath),
                        contentDescription = image.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = image.imageEmoji, fontSize = 40.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = image.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (image.hasAudio) {
                    Text(
                        text = "🔊 Con voz",
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50)
                    )
                } else {
                    Text(
                        text = "🔇 Sin voz",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B6B)
                    )
                }
            }

            // Botones de acción (solo si es tutor)
            if (userRole == "tutor") {
                IconButton(onClick = onEdit) {
                    Text("✏", fontSize = 24.sp)
                }
                IconButton(onClick = onDelete) {
                    Text("🗑", fontSize = 24.sp)
                }
            } else {
                Text("▶", fontSize = 24.sp, color = Color(0xFF9C27B0))
            }
        }
    }
}
