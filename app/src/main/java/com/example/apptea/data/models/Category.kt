package com.example.apptea.data.models

import androidx.compose.ui.graphics.Color

data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val gradientColors: List<Color>
)