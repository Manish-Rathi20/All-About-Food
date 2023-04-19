package com.example.allaboutfood_pricetalkdonate.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.recipeDetailsActivity
import com.google.android.allaboutfood.Meal

class recipeAdapter(val context : Context, val Meals : List<Meal> ) : RecyclerView.Adapter<recipeAdapter.randomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): randomViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return randomViewHolder(view)
    }

    override fun onBindViewHolder(holder: randomViewHolder, position: Int) {
        Glide
            .with(holder.context)
            .load(Meals[position].strMealThumb)
            .placeholder(R.drawable.logonew)
            .into(holder.recipePhoto)

        holder.recipename.text = Meals[position].strMeal
        holder.itemView.setOnClickListener{
            var intent = Intent(context,recipeDetailsActivity::class.java)
            intent.putExtra("strMeal",Meals[position].strMeal)
            intent.putExtra("strIns",Meals[position].strInstructions)
            intent.putExtra("strThumb",Meals[position].strMealThumb)
            intent.putExtra("strArea",Meals[position].strArea)
            intent.putExtra("strCat",Meals[position].strCategory)
            intent.putExtra("strYou",Meals[position].strYoutube)
            it.context.startActivity(intent)

        }
    }

    override fun getItemCount() : Int = Meals.size


    class randomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val recipename = itemView.findViewById<TextView>(R.id.tvRecipeName)
        val recipePhoto = itemView.findViewById<ImageView>(R.id.RecipePhoto)
        val context = itemView.context

    }


}