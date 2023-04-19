package com.example.allaboutfood_pricetalkdonate.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allaboutfood_pricetalkdonate.Adapter.OrgAdapter
import com.example.allaboutfood_pricetalkdonate.Model.organizationData
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentDonationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class DonationFragment : DonateFragment() {
    private lateinit var binding : FragmentDonationBinding
    private lateinit var firebaseDb : FirebaseFirestore
    private lateinit var adapter : OrgAdapter

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationBinding.inflate(layoutInflater)
        firebaseDb = FirebaseFirestore.getInstance()
        getOrgData()
        return binding.root
    }

    private fun getOrgData() {
        firebaseDb.collection("Org")
            .addSnapshotListener{ snapshot , exception ->
                if(snapshot == null || exception != null){
                    Toast.makeText(activity,"${exception?.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                val OrgData = snapshot.toObjects(organizationData::class.java)
                adapter = OrgAdapter(OrgData)
                binding.rvOrganizationsList.adapter = adapter
                binding.rvOrganizationsList.layoutManager = LinearLayoutManager(activity)
                adapter.notifyDataSetChanged()


            }
    }


}

