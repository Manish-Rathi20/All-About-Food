package com.example.allaboutfood_pricetalkdonate.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.Adapter.recipeAdapter
import com.example.allaboutfood_pricetalkdonate.Adapter.recipeCategoryAdapter
import com.example.allaboutfood_pricetalkdonate.Model.Category
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.RandomActivity
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentRecipeBinding
import com.example.allaboutfood_pricetalkdonate.network
import com.google.android.allaboutfood.Meal
import com.google.android.allaboutfood.recipeProduct
import com.google.android.scrape.randomePersonServices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import org.checkerframework.checker.index.qual.GTENegativeOne
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue


class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private  var adapter: recipeAdapter? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var adapters: recipeCategoryAdapter
    val meals: List<Meal> =  mutableListOf(
        Meal("No Recipe Available","No Recipe Available","No Recipe Available","No Recipe Available","R.drawable.logonew","No Recipe Available")
    )
//    var list : List<Meal> = mutableListOf(
//        Meal("h","h","h","h","h","h")
//    )

    val items = mutableListOf<Category>(Category("1","Indian",""),
        Category("2","Italian",""),
        Category("3","Canadian",""),
        Category("4","French",""),
        Category("5","American",""),
        Category("6","American",""),
        Category("7","British",""),
        Category("8","Thai",""))

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(layoutInflater)
        val root: View = binding.root
        val networkTalk = this.activity?.let { network(it.applicationContext) }
        auth = FirebaseAuth.getInstance()
        adapters = recipeCategoryAdapter(requireContext() , items)
        binding.recipeCategory.adapter = adapters
        binding.recipeCategory.layoutManager = LinearLayoutManager(requireContext())
        if(networkTalk?.isNetworkRequest() == true) {
            val Ingredient = binding.etIngredient.text.toString().replace(" ","")
            binding.btnGet.setOnClickListener {
                if (binding.etIngredient.text.isNotBlank()) {
                    getPerson(binding.etIngredient.text.toString())
                    binding.textView6.visibility = GONE
                    binding.recipeCategory.visibility = GONE
                } else {
                    Toast.makeText(activity, "Please Enter Ingredient", Toast.LENGTH_SHORT).show()
                }

            }

            binding.btnRandomRecipe.setOnClickListener {
                startActivity(Intent(activity, RandomActivity::class.java))
            }

        }else{
            Toast.makeText(activity, "Turn on Internet", Toast.LENGTH_LONG).show()
        }
        return root
    }

    private fun getPerson(ingredient : String) {

    CoroutineScope(Dispatchers.IO).launch {
        try{
            val random = randomePersonServices.randomPerson.getRandomPerson(ingredient)
            random.enqueue(object : Callback<recipeProduct> {
                override fun onResponse(call: Call<recipeProduct>, response: Response<recipeProduct>) {
                    val random = response.body()
                    if (random != null) {
                        val staggeredGridLayoutManager = StaggeredGridLayoutManager(1,LinearLayoutManager.HORIZONTAL)
                        try {
                            adapter = activity?.applicationContext?.let { recipeAdapter(it,random.meals) }
                        }catch (e : Exception) {
                            adapter = activity?.applicationContext?.let { recipeAdapter(it,meals) }
                        }

                        binding.rvRecipe.adapter = adapter
                        binding.rvRecipe.layoutManager = staggeredGridLayoutManager
                    } else {
                        Toast.makeText(activity,"null",Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<recipeProduct>, t: Throwable) {
                    Toast.makeText(activity,"null",Toast.LENGTH_LONG).show()
                }

            })
        }catch (e : Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(activity,"${e.message}",Toast.LENGTH_LONG).show()
            }

        }

    }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }



}