package com.example.allaboutfood_pricetalkdonate.Authentication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.allaboutfood_pricetalkdonate.Model.User
import com.example.allaboutfood_pricetalkdonate.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class signUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDb: FirebaseFirestore
    lateinit var storageRef: StorageReference
    var photoUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        firebaseDb = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("UserImages")

        binding.btnSignUp.setOnClickListener {
            if (binding.etUserName.text.toString().isNotBlank() && binding.etEmailReg.text.toString().isNotBlank() && binding.etPassReg.text.toString().isNotBlank() && binding.etAge.text.toString().isNotBlank() && binding.profileImageReg.drawable != null){
                registerUser()
            }else{
                Toast.makeText(this@signUpActivity, "Field Can't be Empty", Toast.LENGTH_LONG).show()
            }


        }
        binding.profileImageReg.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                launcher.launch(it)
            }
        }

    }

    private fun registerUser() {

        val username = binding.etUserName.text.toString()
        val email = binding.etEmailReg.text.toString()
        val Password = binding.etPassReg.text.toString()
        val age = binding.etAge.text.toString().toInt()

        if (username.isNotBlank() || email.isNotBlank() || Password.isNotBlank() || binding.etAge.text.toString().isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, Password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener{ tas->
                                        if(tas.isSuccessful){
                                            var documentReference = firebaseDb.collection("users")
                                                .document(auth.currentUser?.uid.toString())
                                            val user = User(username, email, age)
                                            documentReference.set(user).addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                        uploadPhoto()
                                                    Toast.makeText(
                                                        this@signUpActivity,
                                                        "Successfully Registered, Verify your email",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    startActivity(Intent(this@signUpActivity, LoginActivity::class.java))
                                                    finish()
                                                } else {
                                                    Toast.makeText(
                                                        this@signUpActivity,
                                                        "Not Successfully Registered",
                                                        Toast.LENGTH_LONG
                                                    ).show()

                                                }
                                            }
                                        }else{
                                            Toast.makeText(this@signUpActivity,"${tas.exception}",Toast.LENGTH_LONG).show()
                                        }
                                    }

                            }
                        }.await()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@signUpActivity,
                            "Authentication Failed ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(this@signUpActivity, "Field Can't be Empty", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadPhoto() {
        var photoUploadUrl = photoUrl as Uri
        storageRef = storageRef.child("images/${System.currentTimeMillis().toString()}-photo.jpg")
        storageRef.putFile(photoUploadUrl).continueWithTask {
                storageRef.downloadUrl
            }.continueWithTask {
                firebaseDb.collection("users").document(auth.currentUser?.uid.toString())
                    .update("picUrl", it.result.toString())
            }.addOnCompleteListener { userCreationTask ->
                if (!userCreationTask.isSuccessful) {
                    Toast.makeText(this@signUpActivity, "Exception is there + ${userCreationTask.exception}", Toast.LENGTH_LONG)
                        .show()
                    Log.d("SignIn","${userCreationTask.exception}")
                }
            }

    }

    var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        photoUrl = it.data?.data
        binding.profileImageReg.setImageURI(photoUrl)
    }
}

