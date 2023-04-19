package com.example.allaboutfood_pricetalkdonate

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.allaboutfood_pricetalkdonate.Authentication.LoginActivity
import com.example.allaboutfood_pricetalkdonate.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.motionLayout.startLayoutAnimation()

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (isNetworkRequest()) {
                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                    finishAffinity()
                } else {
                    AlertDialog.Builder(this@SplashScreen).setTitle("No Internet Connection")
                        .setMessage("Please check your internet connection and try again")
                        .setPositiveButton(android.R.string.ok) { dialog,which ->
                            this@SplashScreen.recreate()
                        }
                        .setIcon(android.R.drawable.ic_dialog_alert).show()
                }

//
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkRequest():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        var networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return (networkCapabilities != null) && networkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )


    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun registerNetworkCallback() {
//        try {
//            val connectivityManager =
//                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            connectivityManager.registerDefaultNetworkCallback(object :
//                ConnectivityManager.NetworkCallback() {
//
//                override fun onAvailable(network: Network) {
//                    super.onAvailable(network)
//                    isConnected = true
//                }
//
//                override fun onLost(network: Network) {
//                    super.onLost(network)
//                    isConnected = false
//                }
//            })
//
//
//        } catch (e: Exception) {
//            isConnected = false
//        }
//    }
//
//    fun unregisterNetworkCallback() {
//        connectivityManager.unregisterNetworkCallback(object :
//            ConnectivityManager.NetworkCallback() {})
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun onResume() {
//        super.onResume()
//        registerNetworkCallback()
//    }
//
//
//    override fun onPause() {
//        super.onPause()
//        unregisterNetworkCallback()
//    }
//
}