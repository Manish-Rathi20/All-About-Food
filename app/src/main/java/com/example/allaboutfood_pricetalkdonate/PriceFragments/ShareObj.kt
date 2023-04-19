package com.example.allaboutfood_pricetalkdonate

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


object SharedPreferencesManager {
    private const val PREFERENCE_NAME = "my_preference_name"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}