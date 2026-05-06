package com.example.apptea.ui.screens.tutor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptea.data.mock.ImageMock
import com.example.apptea.data.models.ImageItem
import com.example.apptea.ui.components.ImageItemCard

@Composable
fun CategoryDetailScreen(
    categoryId: String,
    categoryName: String,
    onBack: () -> Unit
) {
    var images by remember { mutableStateOf(ImageMock.getImagesForCategory(categoryId)) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<ImageItem?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF9C27B0), Color(0xFFE040FB), Color(0xFFFF4081))
                )
            )
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                text = categoryName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF9C27B0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ Agregar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Contador de imágenes
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Imágenes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "${images.size} imágenes",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de imágenes
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(images) { image ->
                ImageItemCard(
                    image = image,
                    onEdit = {
                        selectedImage = image
                        showEditDialog = true
                    },
                    onDelete = {
                        images = images.filter { it.id != image.id }
                    }
                )
            }
        }
    }

    // Diálogo para agregar imagen
    if (showAddDialog) {
        AddImageDialog(
            categoryId = categoryId,
            onDismiss = { showAddDialog = false },
            onImageAdded = { newImage ->
                images = images + newImage
                showAddDialog = false
            }
        )
    }

    // Diálogo para editar imagen
    if (showEditDialog && selectedImage != null) {
        EditImageDialog(
            image = selectedImage!!,
            onDismiss = {
                showEditDialog = false
                selectedImage = null
            },
            onSave = { updatedImage ->
                images = images.map { if (it.id == updatedImage.id) updatedImage else it }
                showEditDialog = false
                selectedImage = null
            }
        )
    }
}

@Composable
fun AddImageDialog(
    categoryId: String,
    onDismiss: () -> Unit,
    onImageAdded: (ImageItem) -> Unit
) {
    var imageName by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📷") }

    val emojiOptions = if (categoryId == "1") {
        listOf("🍕", "🍔", "🍎", "🍌", "🍞", "🍪", "🥗", "🍜")
    } else {
        listOf("💧", "🧃", "🥛", "☕", "🧋", "🥤", "🍵")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar nueva imagen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = imageName,
                    onValueChange = { imageName = it },
                    label = { Text("Nombre de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text("Selecciona un ícono:", fontSize = 14.sp)
                LazyColumn(modifier = Modifier.height(120.dp)) {
                    items(emojiOptions.chunked(5)) { rowEmojis ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowEmojis.forEach { emoji ->
                                Surface(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable { selectedEmoji = emoji },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selectedEmoji == emoji) Color(0xFF9C27B0) else Color(0xFFEEEEEE)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(emoji, fontSize = 28.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (imageName.isNotBlank()) {
                        val newImage = ImageItem(
                            id = System.currentTimeMillis().toString(),
                            name = imageName,
                            imageEmoji = selectedEmoji,
                            hasAudio = false,
                            isSystem = false
                        )
                        onImageAdded(newImage)
                    }
                },
                enabled = imageName.isNotBlank()
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun EditImageDialog(
    image: ImageItem,
    onDismiss: () -> Unit,
    onSave: (ImageItem) -> Unit
) {
    var imageName by remember { mutableStateOf(image.name) }
    var hasAudio by remember { mutableStateOf(image.hasAudio) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar imagen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = imageName,
                    onValueChange = { imageName = it },
                    label = { Text("Nombre de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = { /* Aquí irá grabación de audio */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasAudio) Color(0xFF4CAF50) else Color(0xFF9C27B0)
                    )
                ) {
                    Text(if (hasAudio) " Audio grabado" else "🎙️ Grabar audio")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedImage = image.copy(name = imageName, hasAudio = hasAudio)
                onSave(updatedImage)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}