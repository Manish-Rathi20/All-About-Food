package com.example.allaboutfood_pricetalkdonate

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi


class network(val context: Context){
    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkRequest():Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        var networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return (networkCapabilities != null) && networkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )


    }
}





