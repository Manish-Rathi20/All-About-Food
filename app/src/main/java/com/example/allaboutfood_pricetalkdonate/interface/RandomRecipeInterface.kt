package com.google.android.scrape

import com.google.android.allaboutfood.recipeProduct
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://randomuser.me/api/?seed=df&page=2@results=3
//www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
//https://www.themealdb.com/api/json/v1/1/random.php
interface RandomRecipeInterface {

    @GET("/api/json/v1/1/random.php")
    fun getRandomRecipe(): Call<recipeProduct>

}
//https://randomuser.me/api/&
object randomeRecipeServices{
    var randomRecipe : RandomRecipeInterface
    init {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        randomRecipe = retrofitInstance.create(RandomRecipeInterface::class.java)
    }
}