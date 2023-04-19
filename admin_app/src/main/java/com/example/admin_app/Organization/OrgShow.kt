package com.example.admin_app.Organization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_app.databinding.ActivityOrgShowBinding
import com.google.firebase.firestore.FirebaseFirestore

class OrgShow : AppCompatActivity() {
    private lateinit var binding : ActivityOrgShowBinding
    private lateinit var firebaseDb : FirebaseFirestore
    private lateinit var adapter : OrganizationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrgShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDb = FirebaseFirestore.getInstance()
        getOrgData()
    }

    private fun getOrgData() {
        firebaseDb.collection("Org")
            .addSnapshotListener{ snapshot , exception ->
                if(snapshot == null || exception != null){
                    Toast.makeText(this@OrgShow,"${exception?.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                val OrgData = snapshot.toObjects(orgData::class.java)
                adapter = OrganizationAdapter(OrgData)
                binding.rvOrganizationData.adapter = adapter
                binding.rvOrganizationData.layoutManager = LinearLayoutManager(this@OrgShow)
                adapter.notifyDataSetChanged()


            }
    }
}