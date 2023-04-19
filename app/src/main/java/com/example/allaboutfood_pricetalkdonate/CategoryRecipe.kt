package com.example.allaboutfood_pricetalkdonate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allaboutfood_pricetalkdonate.Adapter.recipeCategoryAdapter
import com.example.allaboutfood_pricetalkdonate.Model.categoryRecipeModel
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityCategoryRecipeBinding
import com.google.android.scrape.categoryServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRecipe : AppCompatActivity() {
    private lateinit var adapter: recipeCategoryAdapter
    private lateinit var binding : ActivityCategoryRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val category = intent.getStringExtra("category")
        getCategoryData(category)
    }

    private fun getCategoryData(category: String?) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val category = categoryServices.categoryRecipe.getCategoryRecipe(category!!)
                category.enqueue(object : Callback<categoryRecipeModel>{
                    override fun onResponse(
                        call: Call<categoryRecipeModel>,
                        response: Response<categoryRecipeModel>
                    ) {
                        val categoryData = response.body()
                        if(categoryData != null){
                            adapter = recipeCategoryAdapter(this@CategoryRecipe,categoryData.meals)
                            binding.categoryRecipeRv.adapter = adapter
                            binding.categoryRecipeRv.layoutManager = LinearLayoutManager(this@CategoryRecipe)

                        }else{
                            Log.d("CategoryRecipe","Something went wrong")
                        }
                    }

                    override fun onFailure(call: Call<categoryRecipeModel>, t: Throwable) {
                        Log.d("CategoryRecipe","${t.message}")
                    }

                })
            }catch (e : Exception){
                Log.d("CategoryRecipe","${e.message}")
            }
        }

    }
}