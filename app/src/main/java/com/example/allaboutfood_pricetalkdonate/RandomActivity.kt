package com.example.allaboutfood_pricetalkdonate


import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.allaboutfood_pricetalkdonate.Adapter.recipeAdapter
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityRandomBinding
import com.google.android.allaboutfood.Meal

import com.google.android.allaboutfood.recipeProduct
import com.google.android.scrape.randomeRecipeServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.sqrt

class RandomActivity : AppCompatActivity() {

    // Declaring sensorManager
    // and acceleration constants
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private lateinit var adapter : recipeAdapter
    private lateinit var binding : ActivityRandomBinding
    var i = 1
    val meals: List<Meal> =  mutableListOf(
        Meal("No Recipe Available","No Recipe Available","No Recipe Available","No Recipe Available","R.drawable.logonew","No Recipe Available")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRandomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Getting the Sensor Manager instance
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)
            ?.registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Display a Toast message if
            // acceleration value is over 12
            if (acceleration > 12) {
                Toast.makeText(applicationContext, "Here You Go", Toast.LENGTH_SHORT).show()
                getPerson()

            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    private fun getPerson() {

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val random = randomeRecipeServices.randomRecipe.getRandomRecipe()
                random.enqueue(object : Callback<recipeProduct> {
                    override fun onResponse(call: Call<recipeProduct>, response: Response<recipeProduct>) {
                        val random = response.body()
                        if (random != null) {
                            Log.d("MainActivity",random.meals.toString())
                            val staggeredGridLayoutManager = StaggeredGridLayoutManager(1,
                                LinearLayoutManager.HORIZONTAL)
                            try {
                                adapter = recipeAdapter(this@RandomActivity,random.meals)
                            }catch (e : Exception) {
                                adapter = recipeAdapter(this@RandomActivity,meals)
                            }

                            binding.rvRandomRecipe.adapter = adapter
                            binding.rvRandomRecipe.layoutManager = staggeredGridLayoutManager
                            //binding.randombg.setBackgroundColor(Color.parseColor(getRandomColor()))
                        } else {
                            Toast.makeText(this@RandomActivity,"null",Toast.LENGTH_LONG).show()
                            Log.d("MainActivity","There is err")

                        }
                    }

                    override fun onFailure(call: Call<recipeProduct>, t: Throwable) {
                        Toast.makeText(this@RandomActivity,"null",Toast.LENGTH_LONG).show()
                        Log.d("MainActivity","There is errors + ${t.message}")
                    }

                })
            }catch (e : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RandomActivity,"${e.message}",Toast.LENGTH_LONG).show()
                    Log.d("MainActivity","There is errorss")
                }

            }

        }


    }

    fun getRandomColor(): String {

        val colorArr = arrayOf("#a29cf4","#a29cf4","#a6a184","#FF000000","#FFFFFF")
        return colorArr[i++ % colorArr.size]
    }

}
