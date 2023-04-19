package com.example.admin_app.NeedyPost

data class Needy(
    val address : String = "",
    val desc : String = "",
    val created_by : String = "",
    val mobile : Long = 0,
    val image_url : String =  "",
    val verified : Int = 0

)
