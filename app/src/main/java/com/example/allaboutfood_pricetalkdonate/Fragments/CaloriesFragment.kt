package com.example.allaboutfood_pricetalkdonate.Fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.allaboutfood_pricetalkdonate.Model.CalorieModel.caoriesServices
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentCaloriesBinding
import com.example.learnokhttp.caloriesDataX
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import retrofit2.Call
import retrofit2.Response


class CaloriesFragment : Fragment() {
    private lateinit var binding : FragmentCaloriesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaloriesBinding.inflate(layoutInflater)
        setCircular(binding.circularProgressBar)
        setCircular(binding.circularProgressBar1)
        setCircular(binding.circularProgressBar2)

        binding.button.setOnClickListener{
            val foodItem = binding.etEmailCalorie.text.toString()
            if(foodItem.isNotBlank()){
                getData(foodItem)
                binding.circularProgressBar.visibility = VISIBLE
                binding.circularProgressBar1.visibility = VISIBLE
                binding.circularProgressBar2.visibility = VISIBLE
            }else{
                Toast.makeText(requireContext(),"Field Can't be Empty",Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun getData(foodItem: String) {
        val calories = caoriesServices.calories.getCategoryRecipe(foodItem)
        calories.enqueue(object  : retrofit2.Callback<caloriesDataX>{
            override fun onResponse(call: Call<caloriesDataX>, response: Response<caloriesDataX>) {
                val res = response.body()
                if(res != null){
                    try {
                        var  label = res.hits[0].recipe.totalNutrients.SUGAR.label.toString()
                        var quantity = res.hits[0].recipe.totalNutrients.SUGAR.quantity.toFloat()
                        var  unit = res.hits[0].recipe.totalNutrients.SUGAR.unit.toString()
                        binding.tvSugar.text = "$label \n $quantity $unit"
                        val label2 = res.hits[0].recipe.totalNutrients.FAT.label.toString()
                        val quantity1 = res.hits[0].recipe.totalNutrients.FAT.quantity.toFloat()
                        val unit2 = res.hits[0].recipe.totalNutrients.FAT.unit.toString()
                        binding.tvFat.text = "$label2 \n $quantity1 $unit2"
                        val label3 = res.hits[0].recipe.totalNutrients.ENERC_KCAL.label.toString()
                        val quantity2 = res.hits[0].recipe.totalNutrients.ENERC_KCAL.quantity.toFloat()
                        val unit3 = res.hits[0].recipe.totalNutrients.ENERC_KCAL.unit.toString()
                        binding.tvEcl.text = "$label3 \n $quantity2 $unit3"
                        //binding.textView2.text = res.hits[0].recipe.totalNutrients.FAT.quantity.toString()
                    }catch (e : Exception){
                        Toast.makeText(requireContext(),"No Data Available", Toast.LENGTH_LONG).show()
                    }

                }else{
                    Log.d("MainActivity","$res")
                }
            }

            override fun onFailure(call: Call<caloriesDataX>, t: Throwable) {
                Log.d("MainActivity","${t.message}")
            }

        })
    }

    private fun setCircular(circularProgressBar2: CircularProgressBar) {
        circularProgressBar2.apply {
            // Set Progress
            progress = 65f
            // or with animation
            setProgressWithAnimation(65f, 1000) // =1s

            // Set Progress Max
            progressMax = 200f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.RED
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.RED
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 20f // in DP
            backgroundProgressBarWidth = 30f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}