package com.example.admin_app.Report

data class postss(
    var creation_time : Long = 0,
    var title : String ="",
    var desc : String = "",
    var user : User? = null

)