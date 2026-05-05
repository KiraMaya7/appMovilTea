package com.example.apptea.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CategoryModel(
    val id: String,
    val name: String,
    val icon: String
)

@Composable
fun TutorPanelScreen(onBack: () -> Unit) {
    val categories = listOf(
        CategoryModel("1", "Alimentos", "🍎"),
        CategoryModel("2", "Bebidas", "🥤"),
        CategoryModel("3", "Baño", "🚿"),
        CategoryModel("4", "Dormir", "😴"),
        CategoryModel("5", "Dolores", "🤕"),
        CategoryModel("6", "Emociones", "😊"),
        CategoryModel("7", "Malestares", "🤢"),
        CategoryModel("8", "Juegos y Actividades", "🎮")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header azul
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3366CC))
                .padding(16.dp),
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
                text = "Panel Tutor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Box(modifier = Modifier.size(28.dp))  // Espacio para balancear
        }

        // Título sección
        Text(
            text = "Gestión de Imágenes",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3366CC),
            modifier = Modifier.padding(20.dp)
        )

        Text(
            text = "Selecciona una categoría para administrar",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grid de categorías (2 columnas)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.chunked(2)) { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowCategories.forEach { category ->
                        CategoryCard(category, Modifier.weight(1f))
                    }
                    // Si la fila tiene solo 1 elemento, agregar espacio vacío
                    if (rowCategories.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: CategoryModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable {
                // Aquí irá la navegación a la gestión de la categoría
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = category.icon, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                maxLines = 2
            )
        }
    }
}