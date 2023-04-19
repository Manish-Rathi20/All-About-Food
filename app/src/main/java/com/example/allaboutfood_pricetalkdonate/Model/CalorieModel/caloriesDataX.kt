package com.example.learnokhttp

data class caloriesDataX(
    val count: Int,
    val from: Int,
    val hits: List<Hit>,
    val more: Boolean,
    val q: String,
    val to: Int
)