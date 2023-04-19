package com.example.admin_app

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREFERENCE_NAME = "my_pref"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}