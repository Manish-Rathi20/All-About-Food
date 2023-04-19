package com.example.allaboutfood_pricetalkdonate.Model

data class Result(
    val current_price: Int,
    val discounted: Boolean,
    val link: String,
    val name: String,
    val original_price: Int,
    val query_url: String,
    val thumbnail: Any
)