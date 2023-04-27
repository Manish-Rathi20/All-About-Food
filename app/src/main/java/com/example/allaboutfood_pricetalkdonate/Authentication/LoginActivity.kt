package com.example.allaboutfood_pricetalkdonate.Authentication

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.allaboutfood_pricetalkdonate.Post.PostActivity
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener {
            signInUser()
        }
        binding.model.setOnClickListener{
            startActivity(Intent(this@LoginActivity,signUpActivity::class.java))
            finish()
        }

        binding.tvForgot.setOnClickListener{
            createDialog()
        }

    }

    private fun createDialog() {
        val builder = AlertDialog.Builder(this, R.style.MyDialog)
        builder.setTitle("Recover Password")
        val linearLayout = LinearLayout(this)
        val emailet = EditText(this)
        emailet.hint = "Enter your Email"
        emailet.minEms = 16
        emailet.gravity = Gravity.CENTER
        emailet.setBackgroundColor(Color.parseColor("#FFFFFF"))
        emailet.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        linearLayout.addView(emailet)
        linearLayout.setPadding(10, 30, 10, 30)
        builder.setView(linearLayout)
        builder.setPositiveButton("Recover"){_,_->
            val email = emailet.text.toString().trim()
            resetEmail(email)
        }

        builder.setNegativeButton("Cancel"){dialog,_->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun resetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(
                        this@LoginActivity,
                        "Check Your Email For Recovery...",
                        Toast.LENGTH_LONG
                    ).show()
                }else{
                    Toast.makeText(
                        this@LoginActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("Login","${it.exception}")
                }
            }
            .addOnFailureListener{
                Toast.makeText(
                    this@LoginActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("Login","${it.message}")
            }
    }

    //onStart() if user is logged then automatically goes to next activity
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null && auth.currentUser!!.isEmailVerified) {
            switchToPost()
        }
    }

    // for sign In ( Log In ) user
    private fun signInUser() {
        val email = binding.etEmail.text.toString().replace(" ","")
        val password = binding.etPass.text.toString().replace(" ","")
        if (email.isNotBlank() || password.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        if(auth.currentUser?.isEmailVerified == true){
                            checkLoggedState()
                        }else{
                            Toast.makeText(
                                this@LoginActivity,
                                "Verify Your Email First",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(
                this@LoginActivity,
                "Email or Password can't be empty",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    // if user is logged then next activity ( first time)
    private fun checkLoggedState() {
        if (auth.currentUser != null) {
            switchToPost()
        } else {
            Toast.makeText(this@LoginActivity, "Authentication Failed", Toast.LENGTH_LONG).show()
        }
    }

    // next activity intent code
    private fun switchToPost() {
        startActivity(Intent(this@LoginActivity, PostActivity::class.java))
        finish()
    }




}