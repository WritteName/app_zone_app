package com.example.app_s10

class Game (
    var id: String? = null,
    var title: String? = null,
    var genre: String? = null,
    val platform: String = "",
    var rating: Float = 0f,
    val description: String = "",
    val releaseYear: Int = 0,
    val completed: Boolean = false,
    val userId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)