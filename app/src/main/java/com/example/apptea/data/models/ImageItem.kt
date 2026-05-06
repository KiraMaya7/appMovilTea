package com.example.apptea.data.models

data class ImageItem(
    val id: String,
    val name: String,
    val imageEmoji: String,
    val hasAudio: Boolean = false,
    val audioUrl: String? = null,
    val isSystem: Boolean = true
)
