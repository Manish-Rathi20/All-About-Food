package com.example.allaboutfood_pricetalkdonate.Post

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.allaboutfood_pricetalkdonate.Model.postss
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import java.util.*

open class CreatePostActivity : PostActivity(){
    //private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var binding: ActivityCreatePostBinding
   // private var signInUser: User? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        findViewById<Button>(R.id.btnPost).setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun postUserDetails(cityName: String = "") {
        val title = binding.etTitle.text.toString()
        val desc = binding.etDesc.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                    val posts = postss(System.currentTimeMillis(), title, desc, cityName,signInUser)
                    firestoreDb.collection("posts").document()
                        .set(posts)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this@CreatePostActivity, "Posted", Toast.LENGTH_LONG)
                                    .show()
                                startActivity(Intent(this@CreatePostActivity, PostActivity::class.java))
                                finishAffinity()

                            } else {
                                Toast.makeText(
                                    this@CreatePostActivity,
                                    "Not Posted",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            } }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CreatePostActivity, "${e?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    //location
    private fun getCurrentLocation() {
        if(checkPermissions()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task->
                    val location : Location? = task.result
                    if(location == null){
                        Toast.makeText(this,"Location is not specified",Toast.LENGTH_LONG).show()
                    }else{
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val cityName = addresses!![0].locality
                        postUserDetails(cityName)
                    }

                }
            }else{
                Toast.makeText(this,"turn on location",Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermission()
        }


    }

    private fun isLocationEnabled(): Boolean {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Granted",Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(this,"Not Granted",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }
}