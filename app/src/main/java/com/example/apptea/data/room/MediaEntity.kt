package com.example.apptea.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val name: String,
    val imagePath: String?, // Ruta local de la imagen
    val emoji: String,      // Por si no tiene imagen local
    val audioPath: String?, // Ruta local del audio
    val hasAudio: Boolean,
    val isSystem: Boolean = false
)
