package com.example.apptea.ui.screens.tutor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.apptea.data.models.ImageItem
import com.example.apptea.data.room.AppDatabase
import com.example.apptea.data.room.MediaEntity
import com.example.apptea.ui.components.ImageItemCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun CategoryDetailScreen(
    categoryId: String,
    categoryName: String,
    userRole: String = "tutor",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.getDatabase(context) }
    val mediaDao = db.mediaDao()

    // Cargar imágenes desde Room
    val mediaItems by mediaDao.getMediaByCategory(categoryId).collectAsState(initial = emptyList())
    
    // Mapear de MediaEntity a ImageItem para la UI
    val images = mediaItems.map { entity ->
        ImageItem(
            id = entity.id,
            name = entity.name,
            imageEmoji = entity.emoji,
            imagePath = entity.imagePath,
            hasAudio = entity.hasAudio,
            audioUrl = entity.audioPath,
            isSystem = entity.isSystem
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<ImageItem?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    fun playAudio(audioPath: String?) {
        if (audioPath.isNullOrEmpty()) return
        try {
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(audioPath)
                prepare()
                start()
            }
            mediaPlayer.setOnCompletionListener { it.release() }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al reproducir audio", Toast.LENGTH_SHORT).show()
        }
    }

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
            
            if (userRole == "tutor") {
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
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }
        }

        // Contador
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (userRole == "tutor") "Gestionar Imágenes" else "Selecciona una imagen",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(text = "${images.size} items", fontSize = 14.sp, color = Color(0xFF666666))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(images) { image ->
                ImageItemCard(
                    image = image,
                    userRole = userRole,
                    onEdit = {
                        selectedImage = image
                        showEditDialog = true
                    },
                    onDelete = {
                        scope.launch { mediaDao.deleteById(image.id) }
                    },
                    onClick = {
                        if (userRole == "infante") {
                            playAudio(image.audioUrl)
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AddImageDialog(
            categoryId = categoryId,
            onDismiss = { showAddDialog = false },
            onImageAdded = { newImage ->
                scope.launch {
                    mediaDao.insertMedia(
                        MediaEntity(
                            id = newImage.id,
                            categoryId = categoryId,
                            name = newImage.name,
                            imagePath = newImage.imagePath,
                            emoji = newImage.imageEmoji,
                            audioPath = newImage.audioUrl,
                            hasAudio = newImage.hasAudio,
                            isSystem = false
                        )
                    )
                }
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedImage != null) {
        EditImageDialog(
            image = selectedImage!!,
            onDismiss = {
                showEditDialog = false
                selectedImage = null
            },
            onSave = { updatedImage ->
                scope.launch {
                    mediaDao.updateMedia(
                        MediaEntity(
                            id = updatedImage.id,
                            categoryId = categoryId,
                            name = updatedImage.name,
                            imagePath = updatedImage.imagePath,
                            emoji = updatedImage.imageEmoji,
                            audioPath = updatedImage.audioUrl,
                            hasAudio = updatedImage.hasAudio,
                            isSystem = updatedImage.isSystem
                        )
                    )
                }
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
    val context = LocalContext.current
    var imageName by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📷") }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var audioPath by remember { mutableStateOf<String?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = File(context.getExternalFilesDir(null), "img_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(it)?.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            imagePath = file.absolutePath
            Toast.makeText(context, "Imagen guardada", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            val file = File(context.getExternalFilesDir(null), "cam_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            imagePath = file.absolutePath
            Toast.makeText(context, "Foto capturada", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { !it }) {
            Toast.makeText(context, "Permisos necesarios denegados", Toast.LENGTH_SHORT).show()
        }
    }

    val emojiOptions = listOf("🍕", "🍔", "🍎", "🍌", "💧", "🧃", "🥛", "☕")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Imagen", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Imagen", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { galleryLauncher.launch("image/*") }, modifier = Modifier.weight(1f)) {
                        Text("📁 Galería")
                    }
                    OutlinedButton(onClick = { 
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch()
                        } else {
                            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("📷 Cámara")
                    }
                }
                if (imagePath != null) Text("✅ Imagen cargada", color = Color(0xFF4CAF50), fontSize = 12.sp)

                Text("Nombre", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                OutlinedTextField(value = imageName, onValueChange = { imageName = it }, modifier = Modifier.fillMaxWidth())

                Text("Grabación de Voz", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(arrayOf(Manifest.permission.RECORD_AUDIO))
                            return@Button
                        }
                        if (!isRecording) {
                            val file = File(context.getExternalFilesDir(null), "audio_${System.currentTimeMillis()}.mp3")
                            audioPath = file.absolutePath
                            recorder = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context) else MediaRecorder()).apply {
                                setAudioSource(MediaRecorder.AudioSource.MIC)
                                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                setOutputFile(audioPath)
                                prepare()
                                start()
                            }
                            isRecording = true
                        } else {
                            recorder?.stop()
                            recorder?.release()
                            recorder = null
                            isRecording = false
                            Toast.makeText(context, "Audio grabado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isRecording) Color.Red else Color(0xFF9C27B0))
                ) {
                    Text(if (isRecording) "🛑 Detener Grabación" else "🎙️ Grabar voz del niño", color = Color.White)
                }

                Text("Icono (si no hay foto):", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    emojiOptions.take(4).forEach { emoji ->
                        Surface(
                            modifier = Modifier.size(45.dp).clickable { selectedEmoji = emoji },
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedEmoji == emoji) Color(0xFF9C27B0) else Color(0xFFEEEEEE)
                        ) {
                            Box(contentAlignment = Alignment.Center) { Text(emoji, fontSize = 24.sp) }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onImageAdded(ImageItem(
                        id = System.currentTimeMillis().toString(),
                        name = imageName,
                        imageEmoji = selectedEmoji,
                        imagePath = imagePath,
                        hasAudio = audioPath != null,
                        audioUrl = audioPath,
                        isSystem = false
                    ))
                },
                enabled = imageName.isNotBlank()
            ) { Text("Agregar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun EditImageDialog(image: ImageItem, onDismiss: () -> Unit, onSave: (ImageItem) -> Unit) {
    var imageName by remember { mutableStateOf(image.name) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Imagen") },
        text = {
            OutlinedTextField(value = imageName, onValueChange = { imageName = it }, label = { Text("Nombre") })
        },
        confirmButton = {
            Button(onClick = { onSave(image.copy(name = imageName)) }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
