package com.example.allaboutfood_pricetalkdonate.Post

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.allaboutfood_pricetalkdonate.Model.postss
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

open class CreatePostActivity : PostActivity(){
    //private lateinit var auth: FirebaseAuth
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var binding: ActivityCreatePostBinding
   // private var signInUser: User? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        findViewById<Button>(R.id.btnPost).setOnClickListener {

            postUserDetails()

        }
    }

    private fun postUserDetails() {
        val title = binding.etTitle.text.toString()
        val desc = binding.etDesc.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                    val posts = postss(System.currentTimeMillis(), title, desc, signInUser)
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
}