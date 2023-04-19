package com.example.allaboutfood_pricetalkdonate.Model.CalorieModel

import com.example.learnokhttp.caloriesDataX
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CaloriesInterface  {
    @Headers(
        "X-RapidAPI-Key: bc3e979897msh48f4ae62a20d922p1a21e5jsn411abd8c9fa1",
        "X-RapidAPI-Host: edamam-recipe-search.p.rapidapi.com",
    )
    @GET("/search")
    fun getCategoryRecipe(@Query("q")q:String): retrofit2.Call<caloriesDataX>

}

object caoriesServices{
    var calories : CaloriesInterface
    init {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl("https://edamam-recipe-search.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        calories = retrofitInstance.create(CaloriesInterface::class.java)
    }
}