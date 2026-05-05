package com.example.apptea.data

data class Module(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)

data class Category(
    val id: String,
    val name: String,
    val icon: String
)