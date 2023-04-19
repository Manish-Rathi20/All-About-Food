package com.example.admin_app.AdminAuth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.admin_app.Report.UserActivity
import com.example.admin_app.SharedPreferencesManager
import com.example.admin_app.databinding.ActivityAdminLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var firebaseDb: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        SharedPreferencesManager.init(this)
        firebaseDb = FirebaseFirestore.getInstance()
        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val editor = sharedPref.edit()
        binding.adminLogbtn.setOnClickListener {
            val username = binding.adminLog.text?.trim().toString()
            val pass = binding.adminLogPass.text?.trim().toString()
            val adminLog = admins(username, pass)
            if (username.isNotBlank() && pass.isNotBlank()) {
                firebaseDb.collection("admin").whereEqualTo("username", username)
                    .get().addOnSuccessListener {
                        if (!it.isEmpty) {
                            editor.apply {
                                putBoolean("isLog", true)
                                putString("username",username)
                                apply()
                            }
                            val intent = Intent(this@AdminLoginActivity, UserActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this,
                                "Credential Are Wrong Or Not Registered",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }
            } else {
                Toast.makeText(
                    this,
                    "Field Can't be Empty",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.goToSignIn.setOnClickListener {
            startActivity(Intent(this@AdminLoginActivity, AdminSignUpActivity::class.java))
            finishAffinity()
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val isLog = sharedPref.getBoolean("isLog", false)
        if (isLog) {
            startActivity(Intent(this@AdminLoginActivity, UserActivity::class.java))
            finishAffinity()
        }
    }
}