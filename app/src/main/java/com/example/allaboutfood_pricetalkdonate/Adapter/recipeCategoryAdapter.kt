package com.example.allaboutfood_pricetalkdonate.Adapter



import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.CategoryRecipe
import com.example.allaboutfood_pricetalkdonate.Model.Category
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.recipeDetailsActivity
import com.google.android.allaboutfood.recipeProduct
import com.google.android.scrape.recipeByIdServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class recipeCategoryAdapter(val context : Context, val categorys : List<Category>) : RecyclerView.Adapter<recipeCategoryAdapter.CategoryHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_recipe, parent, false)
        return CategoryHolder(view)
    }

    override fun getItemCount(): Int {
      return categorys.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
       holder.recipeCategoryName.text = categorys[position].strMeal
        Glide
            .with(holder.context)
            .load(categorys[position].strMealThumb)
            .placeholder(R.drawable.logonew)
            .into(holder.recipeCategoryImage)

        if( position % 2 == 0){
            holder.recipeBg.setBackgroundColor((Color.parseColor("#73c5d6")))
        }else{
            holder.recipeBg.setBackgroundColor((Color.parseColor("#ffb496")))
        }
     if(context !is CategoryRecipe) {

         holder.itemView.setOnClickListener {
             val intent = Intent(holder.context, CategoryRecipe::class.java)
             intent.putExtra("category", categorys[position].strMeal)
             holder.context.startActivity(intent)
         }
     }
        if(context is CategoryRecipe) {
            holder.itemView.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val recipeId = recipeByIdServices.recipId.getRecipeById(categorys[position].idMeal)
                    recipeId.enqueue(object : retrofit2.Callback<recipeProduct> {
                        override fun onResponse(
                            call: Call<recipeProduct>,
                            response: Response<recipeProduct>
                        ) {
                            val recipeResponse = response.body()
                            if (recipeResponse != null) {
                                Toast.makeText(
                                    holder.context,
                                    recipeResponse.meals[0].strMeal,
                                    Toast.LENGTH_LONG
                                ).show()
                                var intent = Intent(context, recipeDetailsActivity::class.java)
                                intent.putExtra("strMeal",recipeResponse.meals[0].strMeal)
                                intent.putExtra("strIns",recipeResponse.meals[0].strInstructions)
                                intent.putExtra("strThumb",recipeResponse.meals[0].strMealThumb)
                                intent.putExtra("strArea",recipeResponse.meals[0].strArea)
                                intent.putExtra("strCat",recipeResponse.meals[0].strCategory)
                                intent.putExtra("strYou",recipeResponse.meals[0].strYoutube)
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(holder.context, "Not get Data", Toast.LENGTH_LONG)
                                    .show()

                            }
                        }

                        override fun onFailure(call: Call<recipeProduct>, t: Throwable) {
                            Toast.makeText(holder.context, t.message, Toast.LENGTH_LONG).show()

                        }

                    })

                } catch (e: Exception) {

                }
            }
        }
        }
    }

    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val recipeCategoryImage = itemView.findViewById<ImageView>(R.id.recipeCategoryImage)
       val recipeCategoryName = itemView.findViewById<TextView>(R.id.recipeCategoryName)
        val recipeBg = itemView.findViewById<LinearLayout>(R.id.bgPopularRecipe)
        val context = itemView.context
    }

}
