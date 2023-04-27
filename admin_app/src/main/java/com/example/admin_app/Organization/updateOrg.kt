package com.example.admin_app.Organization

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admin_app.databinding.ActivityUpdateOrgBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class updateOrg : AppCompatActivity() {
    var photoUrl: Uri? = null
    private lateinit var db: FirebaseFirestore
    private lateinit var binding : ActivityUpdateOrgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        val image = intent.getStringExtra("image")
        val add = intent.getStringExtra("add")
        val city = intent.getStringExtra("city")
        val mobile = intent.getStringExtra("mobile")
        val documentId = intent.getStringExtra("documentId")

        binding.etUpdateOragMobile.setText(mobile)
        binding.etUpdateOrganizationAdd.setText(add)
        binding.etUpdateOraganizationCity.setText(city)

        binding.btnUpdateOragPost.setOnClickListener{
            if(binding.etUpdateOragMobile.text.toString().isNotBlank() &&
                binding.etUpdateOraganizationCity.text.toString().isNotBlank() &&
                binding.etUpdateOrganizationAdd.text.toString().isNotBlank() &&
                binding.imUpdateOraganization.drawable != null){

                updateData( binding.etUpdateOrganizationAdd.text.toString(), binding.etUpdateOragMobile.text.toString(), binding.etUpdateOraganizationCity.text.toString() , binding.imUpdateOraganization.drawable , documentId)

            }else{
                Toast.makeText(this@updateOrg,"Field Can't be Empty",Toast.LENGTH_LONG).show()
            }
        }

        binding.imUpdateOraganization.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                launcher.launch(it)
            }
        }
    }

    private fun updateData(
        add: String,
        mobile: String,
        city: String,
        drawable: Drawable,
        documentId: String?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                            db.collection("Org").document(documentId!!)
                                .update("organizationAdd",add,"organizationCity",city,"organizationMobile",mobile)
                                .addOnCompleteListener{
                                    if(it.isSuccessful){
                                       //uploadPhoto()
                                        Toast.makeText(this@updateOrg, "Updated", Toast.LENGTH_LONG).show()
                                        //startActivity(Intent(this@EditUserDetails,PostActivity::class.java))
                                        //finish()
                                    }else{
                                        Toast.makeText(this@updateOrg, "${it.exception}", Toast.LENGTH_LONG).show()
                                    }
                                }

            }catch (e : Exception){
                Toast.makeText(this@updateOrg, "${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
         photoUrl = it.data?.data
         binding.imUpdateOraganization.setImageURI(photoUrl)
    }
}