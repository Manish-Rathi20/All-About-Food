package com.example.allaboutfood_pricetalkdonate.`interface`

import android.content.ClipData.Item
import com.example.allaboutfood_pricetalkdonate.Model.ItemData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://flipkart.dvishal485.workers.dev/search/maggi

interface webScrapeData {
    @GET("/search/{product_name}")
    fun getItem(@Path("product_name") product_name : String) : Call<ItemData>
    //

}

object getItemServices{
    val items : webScrapeData
    init {
        val retrofitInstance = Retrofit.Builder()
            .baseUrl("https://flipkart.dvishal485.workers.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        items = retrofitInstance.create(webScrapeData::class.java)
    }

}