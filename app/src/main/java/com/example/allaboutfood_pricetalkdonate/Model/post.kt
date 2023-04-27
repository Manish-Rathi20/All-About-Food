package com.example.allaboutfood_pricetalkdonate.Model

data class postss(
    var creation_time : Long = 0,
    var title : String ="",
    var desc : String = "",
    var location : String = "",
    var user : User? = null

)