package com.example.admin_app.AdminAuth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_app.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class AdminSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    lateinit var firebaseDb: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        firebaseDb = FirebaseFirestore.getInstance()
        binding.adminSignUp.setOnClickListener {
            val username = binding.adminReg.text?.trim().toString()
            val pass = binding.adminRegPass.text?.trim().toString()

            val admin = admins(username, pass)
            if(username.isNotBlank() && pass.isNotBlank()) {
                if(pass.length >= 6) {
                    firebaseDb.collection("admin").whereEqualTo("username", username)
                        .get().addOnSuccessListener {
                            if (it.isEmpty) {
                                firebaseDb.collection("admin")
                                    .document(UUID.randomUUID().toString())
                                    .set(admin).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            startActivity(
                                                Intent(
                                                    this@AdminSignUpActivity,
                                                    AdminLoginActivity::class.java
                                                )
                                            )
                                            finishAffinity()
                                        } else {
                                            Toast.makeText(this, "Not Added", Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Username has been Already taken",
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        }
                }else{
                    Toast.makeText(
                        this,
                        "Password Should Contain 6 or More Letter",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }else{
                Toast.makeText(
                    this,
                    "Field Can't be Empty",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        binding.goToLogIn.setOnClickListener{
            startActivity(Intent(this@AdminSignUpActivity, AdminLoginActivity::class.java))
            finishAffinity()
        }


    }
}