package com.example.admin_app.Organization

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admin_app.databinding.ActivityOrgranizationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class Orgranization : AppCompatActivity() {
    lateinit var binding: ActivityOrgranizationBinding
    lateinit var firebasestore: FirebaseFirestore
    lateinit var storageRef: StorageReference
    lateinit var uniqueId: UUID
    var photoUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrgranizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebasestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("UserImages")
        binding.btnOragPost.setOnClickListener {
            postOrganizationData()
        }
        binding.imOraganization.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                launcher.launch(it)
            }
        }

    }

    private fun postOrganizationData() {
        val organizationAdd = binding.etOrganizationAdd.text.toString()
        val organizationCity = binding.etOraganizationCity.text.toString().uppercase()
        val mobile = binding.etOragMobile.text.toString()
        val imPhoto = binding.imOraganization
        uniqueId = UUID.randomUUID()

        if (organizationAdd.isNotBlank() && organizationCity.isNotBlank() && mobile.isNotBlank() && binding.imOraganization.drawable != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val orgDatas = orgData(organizationAdd, organizationCity, mobile)
                firebasestore.collection("Org").document(uniqueId.toString())
                    .set(orgDatas)
                    .addOnCompleteListener {
                        uploadPhoto(uniqueId)
                        if (it.isSuccessful) {
                            Toast.makeText(this@Orgranization, "Added", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@Orgranization, "Not Added", Toast.LENGTH_LONG)
                                .show()

                        }
                    }
            }
        } else {
            Toast.makeText(this@Orgranization, "Field can't be empty", Toast.LENGTH_LONG).show()

        }
    }

    private fun uploadPhoto(uniqueId: UUID?) {
        var imgUrl = photoUrl as Uri
        storageRef = storageRef.child("Org/${System.currentTimeMillis().toString()}-photo.jpg")
        storageRef.putFile(imgUrl).continueWithTask {
            storageRef.downloadUrl
        }.continueWithTask {
            firebasestore.collection("Org").document(uniqueId.toString())
                .update("image_Url", it.result.toString())
        }.addOnCompleteListener { usercreation ->
            if (!usercreation.isSuccessful)
                Toast.makeText(
                    this@Orgranization,
                    "Note : {${usercreation.exception}}",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        photoUrl = it.data?.data
        binding.imOraganization.setImageURI(photoUrl)
    }
}