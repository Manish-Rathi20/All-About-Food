package com.example.allaboutfood_pricetalkdonate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityRecipeDetailsBinding


open class recipeDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRecipeDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.textView2.text = intent.getStringExtra("strMeal")

        Glide
            .with(this@recipeDetailsActivity)
            .load(intent.getStringExtra("strThumb"))
            .into(binding.imMealPhoto)
        binding.tvRecipeMeal.text = intent.getStringExtra("strIns")
    }

}