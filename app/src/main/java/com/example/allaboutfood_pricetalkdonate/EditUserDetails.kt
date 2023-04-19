package com.example.allaboutfood_pricetalkdonate

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.Model.User
import com.example.allaboutfood_pricetalkdonate.Post.PostActivity
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityEditUserDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditUserDetails : AppCompatActivity() {
    private lateinit var binding : ActivityEditUserDetailsBinding
    lateinit var auth: FirebaseAuth
    var signInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    lateinit var storageRef: StorageReference
    var photoUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("UserImages")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestoreDb.collection("users").document(auth.currentUser?.uid as String)
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null || snapshot == null) {
                            Log.d("EditUserDetails", "exception is there", exception)
                            return@addSnapshotListener
                        }
                        signInUser = snapshot.toObject(User::class.java)
                        binding.EditetUserName.setText(signInUser?.username)
                        binding.EditetEmailReg.setText(signInUser?.email)

                    }
            } catch (e: Exception) {
                Log.d("PostActivity", e.message.toString())
            }
        }
        binding.EditbtnSignUp.setOnClickListener{
            if(binding.EditProfileImage.drawable != null && binding.EditetUserName.text.toString().isNotBlank() && binding.EditetEmailReg.text.toString().isNotBlank()){
                updateData(binding.EditProfileImage.drawable,binding.EditetUserName.text.toString(),binding.EditetEmailReg.text.toString())
            }else{
                Toast.makeText(this@EditUserDetails, "Field Can't be Empty", Toast.LENGTH_LONG).show()
            }
        }
        binding.EditProfileImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                launcher.launch(it)
            }
        }
    }

    private fun updateData(drawable: Drawable, toString: String, toString1: String) {
        firestoreDb.collection("users").document(auth.currentUser?.uid as String)
            .update("email",toString1,"username",toString)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    uploadPhoto()
                    Toast.makeText(this@EditUserDetails, "Updated", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@EditUserDetails,PostActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@EditUserDetails, "${it.exception}", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun uploadPhoto() {
        var photoUploadUrl = photoUrl as Uri
        storageRef = storageRef.child("images/${System.currentTimeMillis().toString()}-photo.jpg")
        storageRef.putFile(photoUploadUrl).continueWithTask {
            storageRef.downloadUrl
        }.continueWithTask {
            firestoreDb.collection("users").document(auth.currentUser?.uid.toString())
                .update("picUrl", it.result.toString())
        }.addOnCompleteListener { userCreationTask ->
            if (!userCreationTask.isSuccessful) {
                Toast.makeText(this@EditUserDetails, "Exception is there + ${userCreationTask.exception}", Toast.LENGTH_LONG)
                    .show()
                Log.d("Edit","${userCreationTask.exception}")
            }
        }

    }
    var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        photoUrl = it.data?.data
        binding.EditProfileImage.setImageURI(photoUrl)
    }
}