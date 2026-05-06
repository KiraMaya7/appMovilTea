package com.example.apptea.data.mock

import androidx.compose.ui.graphics.Color
import com.example.apptea.data.models.Category

object CategoryMock {
    fun getCategories(): List<Category> = listOf(
        Category("1", "Alimentos", "🍴", listOf(Color(0xFF00C853), Color(0xFF00E676))),
        Category("2", "Bebidas", "🥤", listOf(Color(0xFF448AFF), Color(0xFF2962FF))),
        Category("3", "Baño", "🛁", listOf(Color(0xFFB388FF), Color(0xFF7C4DFF))),
        Category("4", "Dormir", "🌙", listOf(Color(0xFF7C4DFF), Color(0xFF536DFE))),
        Category("5", "Dolores", "💓", listOf(Color(0xFFFF5252), Color(0xFFFF1744))),
        Category("6", "Emociones", "😊", listOf(Color(0xFFFF4081), Color(0xFFF50057))),
        Category("7", "Malestares", "⚠️", listOf(Color(0xFFFF6D00), Color(0xFFFF9100))),
        Category("8", "Juegos y Actividades", "🎮", listOf(Color(0xFFFFAB00), Color(0xFFFFD600)))
    )
}
