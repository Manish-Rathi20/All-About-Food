package com.example.allaboutfood_pricetalkdonate.Model

data class ItemData(
    val fetch_from: String,
    val query: String,
    val result: List<Result>,
    val total_result: Int
)