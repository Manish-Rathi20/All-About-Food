package com.example.allaboutfood_pricetalkdonate

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.allaboutfood_pricetalkdonate.Fragments.NeedyFragment
import com.example.allaboutfood_pricetalkdonate.Model.Needy
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityNeedyDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class NeedyDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNeedyDataBinding
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDb: FirebaseFirestore
    lateinit var storageRef: StorageReference
    var photoUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeedyDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        firebaseDb = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("UserImages")

        binding.btnNeedyPost.setOnClickListener {
            if (binding.etAddressNeedy.text.toString()
                    .isNotBlank() && binding.etDescNeedy.text.toString().isNotBlank() && binding.etMobile.text.toString()
                    .isNotBlank() && binding.imPhotoNeedys.drawable != null
            ) {
                sendPost()

            } else {
                Toast.makeText(this@NeedyDataActivity, "Field Can't be Empty", Toast.LENGTH_LONG)
                    .show()
            }


        }
        binding.imPhotoNeedys.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                launcher.launch(it)
            }
        }

    }



    private fun sendPost() {
        val address = binding.etAddressNeedy.text.toString()
        val desc = binding.etDescNeedy.text.toString()
        val mobile = binding.etMobile.text.toString().toLong()
        var UUID = UUID.randomUUID()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                var needyReference = firebaseDb.collection("needy").document(UUID.toString())
                var needyData = Needy(address, desc, auth.currentUser?.email.toString(), mobile,"",0)
                needyReference.set(needyData).addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        uploadPhoto(UUID)
                        Toast.makeText(this@NeedyDataActivity, "Added", Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@NeedyDataActivity, "Not Added", Toast.LENGTH_LONG).show()
                    }
                }.await()

            }catch (e : Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@NeedyDataActivity,
                        "Something went wrong ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private  fun uploadPhoto(UUID: UUID) {
        var imgUrl = photoUrl as Uri
        storageRef = storageRef.child("needy/${System.currentTimeMillis()}-photo.jpg")
        storageRef.putFile(imgUrl).continueWithTask{
            storageRef.downloadUrl
        }.continueWithTask {
            firebaseDb.collection("needy").document(UUID.toString())
                .update("image_url", it.result.toString())
        }.addOnCompleteListener{ usercreation->
            if(!usercreation.isSuccessful)
               Toast.makeText(this@NeedyDataActivity,"Note : {${usercreation.exception}}",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this@NeedyDataActivity,"Note : {${it.message}}",Toast.LENGTH_SHORT).show()

        }

    }

    var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        photoUrl = it.data?.data
        binding.imPhotoNeedys.setImageURI(photoUrl)
    }

}