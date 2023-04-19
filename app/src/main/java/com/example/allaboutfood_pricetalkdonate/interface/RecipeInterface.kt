package com.google.android.scrape

import com.example.allaboutfood_pricetalkdonate.Model.categoryRecipeModel
import com.google.android.allaboutfood.recipeProduct
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//https://randomuser.me/api/?seed=df&page=2@results=3
//www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
//www.themealdb.com
interface RandomInterface  {

    @GET("/api/json/v1/1/search.php")
    fun getRandomPerson(@Query("s")s:String): Call<recipeProduct>

}

//https://randomuser.me/api/&
object randomePersonServices{
    var randomPerson : RandomInterface
    init {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        randomPerson = retrofitInstance.create(RandomInterface::class.java)
    }
}
//category
interface CategoryInterface  {
    @GET("/api/json/v1/1/filter.php")
    fun getCategoryRecipe(@Query("a")a:String): Call<categoryRecipeModel>

}

object categoryServices{
    var categoryRecipe : CategoryInterface
    init {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        categoryRecipe = retrofitInstance.create(CategoryInterface::class.java)
    }
}
// recipe through id
//https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772
interface recipeByIdInterface{
    @GET("/api/json/v1/1/lookup.php")
    fun getRecipeById(@Query("i")i:String): Call<recipeProduct>
}

object recipeByIdServices{
    var recipId : recipeByIdInterface

    init {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        recipId = retrofitInstance.create(recipeByIdInterface::class.java)
    }


}