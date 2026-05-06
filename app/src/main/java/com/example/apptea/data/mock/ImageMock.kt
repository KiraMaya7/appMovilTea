package com.example.apptea.data.mock

import com.example.apptea.data.models.ImageItem

object ImageMock {

    fun getImagesForCategory(categoryId: String): List<ImageItem> {
        return when (categoryId) {
            "1" -> getAlimentosImages()
            "2" -> getBebidasImages()
            else -> emptyList()
        }
    }

    private fun getAlimentosImages(): List<ImageItem> = listOf(
        ImageItem("101", "Pizza", "🍕", hasAudio = false, isSystem = true),
        ImageItem("102", "Hamburguesa", "🍔", hasAudio = false, isSystem = true),
        ImageItem("103", "Manzana", "🍎", hasAudio = false, isSystem = true),
        ImageItem("104", "Banana", "🍌", hasAudio = false, isSystem = true),
        ImageItem("105", "Pan", "🍞", hasAudio = false, isSystem = true),
        ImageItem("106", "Galletas", "🍪", hasAudio = false, isSystem = true)
    )

    private fun getBebidasImages(): List<ImageItem> = listOf(
        ImageItem("201", "Agua", "💧", hasAudio = false, isSystem = true),
        ImageItem("202", "Jugo", "🧃", hasAudio = false, isSystem = true),
        ImageItem("203", "Leche", "🥛", hasAudio = false, isSystem = true),
        ImageItem("204", "Chocolate", "🍫", hasAudio = false, isSystem = true)
    )
}
